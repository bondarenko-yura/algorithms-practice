package com.bondarenko.algo.cache;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.ReadContext;
import com.google.cloud.spanner.ReadOnlyTransaction;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.TransactionContext;
import com.google.cloud.spanner.Value;

import java.util.*;

/**
 * Graph-backed cache that stores item similarity knowledge in Spanner.
 *
 * <p>Data model:
 * <ul>
 *   <li>Each item is a node in the {@code Items} table.</li>
 *   <li>SIMILAR items are connected by directed {@code SimilarEdges}. Edges are stored in
 *       both directions (A→B and B→A), so graph traversal can follow either direction.</li>
 *   <li>Two items are in the same <em>cluster</em> when there is any path of Similar edges
 *       between them (transitive closure).</li>
 *   <li>DIFFERENT clusters are represented by a single {@code DifferentEdges} edge between
 *       any one member from each cluster. The edge is also stored bidirectionally.</li>
 *   <li>Every edge carries an optional {@code reason} string. When a relationship is derived
 *       via a multi-hop path, the reasons along that path are chained with {@code " -> "}.</li>
 * </ul>
 *
 * <p>All three public methods are safe to call concurrently. Writes use Spanner read-write
 * transactions; reads use snapshot reads (single-use or read-only transaction).
 *
 * <p>Schema: {@code src/main/java/com/bondarenko/algo/cache/spanner_schema_graph.sql}
 */
public class CacheDbV3 {

    private final DatabaseClient db;

    public CacheDbV3(DatabaseClient db) {
        this.db = db;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Persists a SIMILAR or DIFFERENT relationship and the human-readable reason behind it.
     *
     * <p>Both items are registered (idempotently) in the same transaction.
     *
     * <ul>
     *   <li><b>SIMILAR</b> — inserts bidirectional {@code SimilarEdges}. Throws if the two items
     *       are already known to be DIFFERENT (directly or transitively through their clusters).</li>
     *   <li><b>DIFFERENT</b> — inserts bidirectional {@code DifferentEdges}. Throws if the two
     *       items are already in the same SIMILAR cluster.</li>
     *   <li><b>UNKNOWN</b> — ignored; no edges are written.</li>
     * </ul>
     *
     * <p>Re-saving the same pair with a new reason overwrites the previous reason on the edge.
     *
     * @throws IllegalStateException if the new relation contradicts existing knowledge
     */
    public void saveInvestigation(Investigation investigation) {
        db.readWriteTransaction().run(tx -> {
            String a = investigation.sourceID;
            String b = investigation.targetID;

            // Register both items. insertOrUpdate is idempotent; created_at is reset on each
            // call, which refreshes the ROW DELETION POLICY clock (items expire after 365 days
            // of inactivity).
            tx.buffer(Mutation.newInsertOrUpdateBuilder("Items")
                    .set("id").to(a)
                    .set("created_at").to(Value.COMMIT_TIMESTAMP)
                    .build());
            tx.buffer(Mutation.newInsertOrUpdateBuilder("Items")
                    .set("id").to(b)
                    .set("created_at").to(Value.COMMIT_TIMESTAMP)
                    .build());

            if (investigation.relation == RelationType.SIMILAR) {
                // Guard: b must not be in a DIFFERENT cluster of a (directly or transitively).
                Map<String, String> differentNodes = getDifferentNodesForClusterWithReasons(tx, a, List.of(b));
                if (differentNodes.containsKey(b)) {
                    throw new IllegalStateException(
                            a + " and " + b + " are already known to be DIFFERENT — cannot mark as SIMILAR");
                }
                insertEdge(tx, "SimilarEdges", a, b, investigation.reason);
                insertEdge(tx, "SimilarEdges", b, a, investigation.reason);

            } else if (investigation.relation == RelationType.DIFFERENT) {
                // Guard: b must not be reachable from a via Similar edges (same cluster).
                Set<String> similarNodes = getReachableSimilarNodes(tx, a, List.of(b));
                if (similarNodes.contains(b)) {
                    throw new IllegalStateException(
                            a + " and " + b + " are already known to be SIMILAR — cannot mark as DIFFERENT");
                }
                insertEdge(tx, "DifferentEdges", a, b, investigation.reason);
                insertEdge(tx, "DifferentEdges", b, a, investigation.reason);
            }
            return null;
        });
    }

    /**
     * Returns the known relationship between {@code sourceId} and each of {@code targetIds},
     * together with a reason string that explains how the relationship was derived.
     *
     * <p>Relationship resolution order (first match wins):
     * <ol>
     *   <li>Self — {@code sourceId.equals(targetId)}: always SIMILAR, reason {@code "Self"}.</li>
     *   <li>SIMILAR — target is reachable from source via a path of Similar edges.
     *       Reason is the edge reasons along the shortest such path, joined by {@code " -> "}.</li>
     *   <li>DIFFERENT — target is reachable via (Similar*)→(Different)→(Similar*) from source.
     *       Reason is the full edge-reason chain across that path.</li>
     *   <li>UNKNOWN — no path found, or source/target has never been seen.</li>
     * </ol>
     *
     * <p>Both queries share a single read-only snapshot so the result is consistent even if a
     * concurrent write commits between the two graph traversals.
     *
     * @param sourceId  item whose perspective is used for classification
     * @param targetIds items to classify relative to {@code sourceId}
     * @return one {@link Investigation} per target, in the same order as {@code targetIds}
     */
    public List<Investigation> findRelationship(String sourceId, List<String> targetIds) {
        if (targetIds.isEmpty()) return List.of();

        // A single ReadOnlyTransaction snapshot covers both graph traversals, ensuring a consistent
        // view. If sourceId is unknown, both traversals return empty maps and every target becomes
        // UNKNOWN — no separate existence check is needed.
        List<Investigation> results = new ArrayList<>();
        try (ReadOnlyTransaction tx = db.readOnlyTransaction()) {
            Map<String, String> similarNodes   = getReachableSimilarNodesWithReasons(tx, sourceId, targetIds);
            Map<String, String> differentNodes = getDifferentNodesForClusterWithReasons(tx, sourceId, targetIds);

            for (String targetId : targetIds) {
                RelationType rel;
                String reason = null;

                if (sourceId.equals(targetId)) {
                    rel    = RelationType.SIMILAR;
                    reason = "Self";
                } else if (similarNodes.containsKey(targetId)) {
                    rel    = RelationType.SIMILAR;
                    reason = similarNodes.get(targetId);
                } else if (differentNodes.containsKey(targetId)) {
                    rel    = RelationType.DIFFERENT;
                    reason = differentNodes.get(targetId);
                } else {
                    rel = RelationType.UNKNOWN;
                }
                results.add(new Investigation(sourceId, targetId, rel, reason));
            }
        }
        return results;
    }

    /**
     * Returns the subset of {@code queryIds} that belong to the same SIMILAR cluster as
     * {@code sourceId} (i.e., are reachable from it via any path of Similar edges).
     *
     * <p>Order of results matches the order of {@code queryIds}. Items not yet registered
     * in the graph are silently excluded.
     *
     * @param sourceId  reference item defining the cluster
     * @param queryIds  candidate items to test for cluster membership
     * @return members of {@code queryIds} that are in the same cluster as {@code sourceId}
     */
    public List<String> findAllInCluster(String sourceId, List<String> queryIds) {
        if (queryIds.isEmpty()) return List.of();

        // singleUse() is lighter than a ReadOnlyTransaction for a single graph query.
        Set<String> similarNodes = getReachableSimilarNodes(db.singleUse(), sourceId, queryIds);
        List<String> result = new ArrayList<>();
        for (String id : queryIds) {
            if (similarNodes.contains(id)) result.add(id);
        }
        return result;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Buffers an edge mutation into {@code tx}. Both directions must be inserted by the caller
     * to make the graph traversable in either direction.
     *
     * <p>{@code reason} is always written explicitly — even when {@code null} — so that a
     * re-investigation overwrites any stale reason left from a previous write. Omitting the
     * column on {@code insertOrUpdate} would silently preserve the old value for existing rows.
     */
    private void insertEdge(TransactionContext tx, String table, String src, String tgt, String reason) {
        tx.buffer(Mutation.newInsertOrUpdateBuilder(table)
                .set("source_id").to(src)
                .set("target_id").to(tgt)
                .set("reason").to(reason)
                .build());
    }

    /**
     * Returns the IDs from {@code targetIds} that are reachable from {@code sourceId} via
     * zero or more Similar edges (i.e., members of the same cluster as source).
     *
     * <p>{@code Similar*0..} includes a 0-hop match, so {@code sourceId} itself is returned
     * if it appears in {@code targetIds}.
     *
     * <p>{@code ANY SHORTEST} ensures one result row per reachable target, regardless of how
     * many paths connect source to that target.
     */
    private Set<String> getReachableSimilarNodes(ReadContext tx, String sourceId, List<String> targetIds) {
        String gql = """
            GRAPH CacheGraph
            MATCH ANY SHORTEST (n:Items {id: @source})-[e:Similar*0..]->(m:Items)
            WHERE m.id IN UNNEST(@targets)
            RETURN m.id AS id
            """;
        Statement statement = Statement.newBuilder(gql)
                .bind("source").to(sourceId)
                .bind("targets").toStringArray(targetIds)
                .build();

        Set<String> cluster = new HashSet<>();
        try (ResultSet rs = tx.executeQuery(statement)) {
            while (rs.next()) cluster.add(rs.getString("id"));
        }
        return cluster;
    }

    /**
     * Same as {@link #getReachableSimilarNodes} but also returns the chained reason for each
     * reachable target. The reason is built by joining the {@code reason} properties of all
     * edges along the shortest path with {@code " -> "}, skipping edges whose reason is null.
     *
     * <p>Returns {@code null} for the reason when no edge along the path carried a reason.
     *
     * @return map of targetId → chained reason (null if no reasons on the path)
     */
    private Map<String, String> getReachableSimilarNodesWithReasons(ReadContext tx, String sourceId, List<String> targetIds) {
        String gql = """
            GRAPH CacheGraph
            MATCH ANY SHORTEST p = (n:Items {id: @source})-[e:Similar*0..]->(m:Items)
            WHERE m.id IN UNNEST(@targets)
            LET reasons = ARRAY(SELECT element.reason FROM UNNEST(EDGES(p)) AS element WHERE element.reason IS NOT NULL)
            RETURN m.id AS id, ARRAY_TO_STRING(reasons, ' -> ') AS aggregated_reason
            """;
        Statement statement = Statement.newBuilder(gql)
                .bind("source").to(sourceId)
                .bind("targets").toStringArray(targetIds)
                .build();

        Map<String, String> cluster = new HashMap<>();
        try (ResultSet rs = tx.executeQuery(statement)) {
            while (rs.next()) {
                String reason = rs.isNull("aggregated_reason") ? null : rs.getString("aggregated_reason");
                cluster.put(rs.getString("id"), nullIfEmpty(reason));
            }
        }
        return cluster;
    }

    /**
     * Returns the IDs from {@code targetIds} that are known to be in a DIFFERENT cluster from
     * {@code sourceId}, together with the chained reason explaining why.
     *
     * <p>A target is "known different" when there exists a path of the form:
     * <pre>
     *   source -[Similar*]-> clusterMember -[Different]-> otherClusterMember -[Similar*]-> target
     * </pre>
     * The {@code Similar*0..} segments include the 0-hop case, so a direct DIFFERENT edge from
     * source to a member of target's cluster is found even when neither has other cluster members.
     *
     * <p>The reason is built by joining all edge reasons along the shortest such path with
     * {@code " -> "}, skipping edges whose reason is null. Returns {@code null} when no edge
     * on the path carried a reason.
     *
     * <p>{@code ANY SHORTEST} collapses the many possible path combinations (Similar cluster of
     * source × Different crossing × Similar cluster of target) into one row per target, making
     * the result deterministic.
     *
     * @return map of targetId → chained reason (null if no reasons on the path)
     */
    private Map<String, String> getDifferentNodesForClusterWithReasons(ReadContext tx, String sourceId, List<String> targetIds) {
        String gql = """
            GRAPH CacheGraph
            MATCH ANY SHORTEST p = (n:Items {id: @source})-[e1:Similar*0..]->(x:Items)-[d:Different]->(y:Items)-[e2:Similar*0..]->(target:Items)
            WHERE target.id IN UNNEST(@targets)
            LET reasons = ARRAY(SELECT element.reason FROM UNNEST(EDGES(p)) AS element WHERE element.reason IS NOT NULL)
            RETURN target.id AS id, ARRAY_TO_STRING(reasons, ' -> ') AS aggregated_reason
            """;
        Statement statement = Statement.newBuilder(gql)
                .bind("source").to(sourceId)
                .bind("targets").toStringArray(targetIds)
                .build();

        Map<String, String> different = new HashMap<>();
        try (ResultSet rs = tx.executeQuery(statement)) {
            while (rs.next()) {
                String reason = rs.isNull("aggregated_reason") ? null : rs.getString("aggregated_reason");
                different.put(rs.getString("id"), nullIfEmpty(reason));
            }
        }
        return different;
    }

    /**
     * Converts an empty string to {@code null}. Needed because {@code ARRAY_TO_STRING} returns
     * {@code ""} (not {@code null}) when the input array is empty — i.e., when no edge on the
     * path carried a reason. Storing {@code ""} as a reason would be misleading, so both cases
     * are collapsed to {@code null}.
     */
    private static String nullIfEmpty(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}