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
 * CacheV3 uses Spanner Graph to model similarity clusters.
 * - SIMILAR nodes are connected by a path of `Similar` edges.
 * - DIFFERENT clusters are connected by a `Different` edge between any of their members.
 *
 * SCHEMA: src/main/java/com/bondarenko/algo/cache/spanner_schema_graph.sql
 */
public class CacheDbV3 {

    private final DatabaseClient db;

    public CacheDbV3(DatabaseClient db) {
        this.db = db;
    }

    public void saveInvestigation(Investigation investigation) {
        db.readWriteTransaction().run(tx -> {
            String a = investigation.sourceID;
            String b = investigation.targetID;

            // 1. Ensure both nodes exist
            tx.buffer(Mutation.newInsertOrUpdateBuilder("Items")
                    .set("id").to(a)
                    .set("created_at").to(Value.COMMIT_TIMESTAMP)
                    .build());
            tx.buffer(Mutation.newInsertOrUpdateBuilder("Items")
                    .set("id").to(b)
                    .set("created_at").to(Value.COMMIT_TIMESTAMP)
                    .build());

            if (investigation.relation == RelationType.SIMILAR) {
                // Check contradiction: are they already known to be DIFFERENT?
                // Use the optimized targeted query to check if 'b' is in the DIFFERENT cluster of 'a'
                Map<String, String> differentNodes = getDifferentNodesForClusterWithReasons(transaction, a, List.of(b));
                if (differentNodes.containsKey(b)) {
                    throw new IllegalStateException(
                            a + " and " + b + " are already known to be DIFFERENT — cannot mark as SIMILAR");
                }

                // Insert bidirectional SIMILAR edges
                insertEdge(tx, "SimilarEdges", a, b, investigation.reason);
                insertEdge(tx, "SimilarEdges", b, a, investigation.reason);

            } else if (investigation.relation == RelationType.DIFFERENT) {
                // Check contradiction: are they already known to be SIMILAR?
                // Use the optimized targeted query to check if 'b' is in the SIMILAR cluster of 'a'
                Set<String> similarNodes = getReachableSimilarNodes(tx, a, List.of(b));
                if (similarNodes.contains(b)) {
                    throw new IllegalStateException(
                            a + " and " + b + " are already known to be SIMILAR — cannot mark as DIFFERENT");
                }

                // Insert bidirectional DIFFERENT edges
                insertEdge(tx, "DifferentEdges", a, b, investigation.reason);
                insertEdge(tx, "DifferentEdges", b, a, investigation.reason);
            }
            return null;
        });
    }

    public List<Investigation> findRelationship(String sourceId, List<String> targetIds) {
        if (targetIds.isEmpty()) return List.of();

        List<Investigation> results = new ArrayList<>();

        try (ReadOnlyTransaction tx = db.readOnlyTransaction()) {
            // If sourceId itself doesn't exist, we just return UNKNOWN for all
            if (!nodeExists(tx, sourceId)) {
                for (String t : targetIds) {
                    results.add(new Investigation(sourceId, t, RelationType.UNKNOWN));
                }
                return results;
            }

            // Find ONLY the targetIds that are reachable via SIMILAR edges
            Map<String, String> similarNodes = getReachableSimilarNodesWithReasons(tx, sourceId, targetIds);

            // Find ONLY the targetIds that are reachable via DIFFERENT paths
            Map<String, String> differentNodes = getDifferentNodesForClusterWithReasons(tx, sourceId, targetIds);

            for (String targetId : targetIds) {
                RelationType rel;
                String reason = null;

                if (sourceId.equals(targetId)) {
                    rel = RelationType.SIMILAR;
                    reason = "Self";
                } else if (similarNodes.containsKey(targetId)) {
                    rel = RelationType.SIMILAR;
                    reason = similarNodes.get(targetId);
                } else if (differentNodes.containsKey(targetId)) {
                    rel = RelationType.DIFFERENT;
                    reason = differentNodes.get(targetId);
                } else {
                    rel = RelationType.UNKNOWN;
                }
                results.add(new Investigation(sourceId, targetId, rel, reason));
            }
        }
        return results;
    }

    public List<String> findAllInCluster(String sourceId, List<String> queryIds) {
        if (queryIds.isEmpty()) return List.of();

        List<String> result = new ArrayList<>();
        try (ReadOnlyTransaction tx = db.readOnlyTransaction()) {
            // Optimized query that only checks presence, filtering strictly by queryIds
            Set<String> similarNodes = getReachableSimilarNodes(tx, sourceId, queryIds);

            for (String targetId : queryIds) {
                if (similarNodes.contains(targetId)) {
                    result.add(targetId);
                }
            }
        }
        return result;
    }

    // --- Private Graph Queries ---

    private boolean nodeExists(ReadContext tx, String id) {
        String gql = """
            GRAPH CacheGraph
            MATCH (n:Items {id: @id})
            RETURN n.id LIMIT 1
            """;
        Statement statement = Statement.newBuilder(gql)
                .bind("id").to(id)
                .build();
        try (ResultSet rs = tx.executeQuery(statement)) {
            return rs.next();
        }
    }

    private void insertEdge(TransactionContext tx, String table, String src, String tgt, String reason) {
        Mutation.WriteBuilder builder = Mutation.newInsertOrUpdateBuilder(table)
                .set("source_id").to(src)
                .set("target_id").to(tgt);

        if (reason != null) {
            builder.set("reason").to(reason);
        }
        tx.buffer(builder.build());
    }

    private boolean areSimilar(TransactionContext tx, String a, String b) {
        String gql = """
            GRAPH CacheGraph
            MATCH (a:Items {id: @a})-[e1:Similar*0..]-(x:Items)
            MATCH (x)-[d:Different]-(y:Items)
            MATCH (y)-[e2:Similar*0..]-(b:Items {id: @b})
            RETURN b.id LIMIT 1
            """;
        Statement statement = Statement.newBuilder(gql)
                .bind("a").to(a)
                .bind("b").to(b)
                .build();
        try (ResultSet rs = tx.executeQuery(statement)) {
            return rs.next();
        }
    }

    private boolean areDifferent(TransactionContext tx, String a, String b) {
        // GQL: Find if there's any Different edge connecting the SIMILAR cluster of A to the SIMILAR cluster of B
        String gql = """
            GRAPH CacheGraph
            MATCH (a:Items {id: @a})-[e1:Similar*0..]-(x:Items)
            MATCH (x)-[d:Different]-(y:Items)
            MATCH (y)-[e2:Similar*0..]-(b:Items {id: @b})
            RETURN b.id LIMIT 1
            """;
        Statement statement = Statement.newBuilder(gql)
                .bind("a").to(a)
                .bind("b").to(b)
                .build();
        try (ResultSet rs = tx.executeQuery(statement)) {
            return rs.next();
        }
    }

    private Set<String> getReachableSimilarNodes(ReadContext tx, String sourceId, List<String> targetIds) {
        String gql = """
            GRAPH CacheGraph
            MATCH (n:Items {id: @source})-[e:Similar*0..]->(m:Items)
            WHERE m.id IN UNNEST(@targets)
            RETURN m.id
            """;
        Statement statement = Statement.newBuilder(gql)
                .bind("source").to(sourceId)
                .bind("targets").toStringArray(targetIds)
                .build();

        Set<String> cluster = new HashSet<>();
        try (ResultSet rs = tx.executeQuery(statement)) {
            while (rs.next()) {
                cluster.add(rs.getString(0));
            }
        }
        return cluster;
    }
    private Map<String, String> getReachableSimilarNodesWithReasons(ReadContext tx, String sourceId, List<String> targetIds) {
        String gql = """
            GRAPH CacheGraph
            MATCH p = (n:Items {id: @source})-[e:Similar*0..]->(m:Items)
            WHERE m.id IN UNNEST(@targets)
            LET reasons = ARRAY(SELECT element.reason FROM UNNEST(EDGES(p)) AS element WHERE element.reason IS NOT NULL)
            RETURN m.id, ARRAY_TO_STRING(reasons, ' -> ') AS aggregated_reason
            """;
        Statement statement = Statement.newBuilder(gql)
                .bind("source").to(sourceId)
                .bind("targets").toStringArray(targetIds)
                .build();

        Map<String, String> cluster = new HashMap<>();
        try (ResultSet rs = tx.executeQuery(statement)) {
            while (rs.next()) {
                String targetId = rs.getString(0);
                String reason = rs.isNull(1) ? null : rs.getString(1);
                cluster.put(targetId, (reason == null || reason.isEmpty()) ? null : reason);
            }
        }
        return cluster;
    }

    private Map<String, String> getDifferentNodesForClusterWithReasons(ReadContext tx, String sourceId, List<String> targetIds) {
        String gql = """
            GRAPH CacheGraph
            MATCH p1 = (n:Items {id: @source})-[e1:Similar*0..]-(x:Items)
            MATCH p2 = (x)-[d:Different]-(y:Items)
            MATCH p3 = (y)-[e2:Similar*0..]-(target:Items)
            WHERE target.id IN UNNEST(@targets)
            LET all_edges = ARRAY_CONCAT(EDGES(p1), EDGES(p2), EDGES(p3))
            LET reasons = ARRAY(SELECT element.reason FROM UNNEST(all_edges) AS element WHERE element.reason IS NOT NULL)
            RETURN target.id, ARRAY_TO_STRING(reasons, ' -> ') AS aggregated_reason
            """;
        Statement statement = Statement.newBuilder(gql)
                .bind("source").to(sourceId)
                .bind("targets").toStringArray(targetIds)
                .build();

        Map<String, String> different = new HashMap<>();
        try (ResultSet rs = tx.executeQuery(statement)) {
            while (rs.next()) {
                String targetId = rs.getString(0);
                String reason = rs.isNull(1) ? null : rs.getString(1);
                different.put(targetId, (reason == null || reason.isEmpty()) ? null : reason);
            }
        }
        return different;
    }
}