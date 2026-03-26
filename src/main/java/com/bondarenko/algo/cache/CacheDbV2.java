package com.bondarenko.algo.cache;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Alternative to {@link CacheDB} that replaces Union-Find with direct cluster IDs.
 *
 * Key difference: every node stores its canonical cluster_id directly. When two clusters
 * merge, a single bulk UPDATE reassigns all members of the smaller cluster to the larger one.
 * This eliminates the two-pass root resolution that CacheDB needs in findRelationship and
 * findAllInCluster (getRoots → find each stored root), reducing those calls from O(2+2U) to O(3).
 *
 * Trade-off: merge costs 2 extra DB calls (getClusterSize×2 for union-by-size) vs CacheDB's
 * lazy updateRoot. But each subsequent read saves 2×U calls, so V2 wins as soon as cluster
 * sizes exceed trivial.
 *
 * Layers:
 *   CacheDbV2 — cluster ID business logic only, no SQL.
 *   Db         — private inner class; all SQL lives here, no business logic.
 */
public class CacheDbV2 {

    private static final AtomicInteger DB_SEQ = new AtomicInteger();

    /**
     * An unordered pair of cluster IDs stored in canonical (sorted) order.
     * Canonical ordering is enforced in the compact constructor so callers never need to sort.
     * Value-based equals/hashCode (record) makes ClusterPair usable as a Set/Map key directly.
     */
    private record ClusterPair(String clusterId1, String clusterId2) {
        ClusterPair {
            if (clusterId1.compareTo(clusterId2) > 0) { String t = clusterId1; clusterId1 = clusterId2; clusterId2 = t; }
        }
    }

    private final Db db;

    /** Returns the total number of SQL statements executed against the DB since construction. */
    public int dbCallCount() { return db.callCount.get(); }

    /**
     * Creates a new CacheDbV2 backed by a private H2 in-memory database.
     * Each instance gets its own database URL (via DB_SEQ) so instances are fully isolated,
     * which is required for parallel test execution.
     */
    public CacheDbV2() {
        try {
            String url = "jdbc:h2:mem:cachedbv2_" + DB_SEQ.getAndIncrement() + ";DB_CLOSE_DELAY=-1";
            db = new Db(DriverManager.getConnection(url, "sa", ""));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ── Business logic (no SQL) ───────────────────────────────────────────────

    /**
     * Returns the canonical cluster ID for an already-registered node.
     * Because cluster_id is always kept current by {@link Db#mergeCluster}, no chain traversal
     * is needed — this is always a single SELECT. Callers are responsible for registering the
     * node before calling this method.
     */
    private String resolveCluster(String nodeId) {
        return db.getClusterId(nodeId);
    }

    /**
     * Merges the clusters of two nodes into one, using union-by-size so that the smaller cluster
     * is reassigned to the larger. All DIFFERENT relationships referencing the smaller cluster ID
     * are rewritten to reference the new merged cluster ID before the merge is committed.
     *
     * @throws IllegalStateException if the two nodes are already recorded as DIFFERENT
     */
    private void merge(String nodeId, String otherNodeId) {
        String clusterId      = resolveCluster(nodeId);
        String otherClusterId = resolveCluster(otherNodeId);
        if (clusterId.equals(otherClusterId)) return;
        if (db.pairExists(new ClusterPair(clusterId, otherClusterId))) {
            throw new IllegalStateException(
                    nodeId + " and " + otherNodeId + " are already known to be DIFFERENT — cannot mark as SIMILAR");
        }

        // Union by size: merge smaller cluster into larger to minimize rows updated
        int size      = db.getClusterSize(clusterId);
        int otherSize = db.getClusterSize(otherClusterId);
        String larger  = (size >= otherSize) ? clusterId : otherClusterId;
        String smaller = (size >= otherSize) ? otherClusterId : clusterId;

        // Rewrite any DIFFERENT pairs that reference the disappearing cluster ID
        List<ClusterPair> affected = db.getPairsInvolving(smaller);
        if (!affected.isEmpty()) {
            db.deletePairsInvolving(smaller);
            Set<ClusterPair> inserted = new HashSet<>();
            for (ClusterPair pair : affected) {
                String clusterId1 = pair.clusterId1().equals(smaller) ? larger : pair.clusterId1();
                String clusterId2 = pair.clusterId2().equals(smaller) ? larger : pair.clusterId2();
                if (!clusterId1.equals(clusterId2)) {
                    ClusterPair rewritten = new ClusterPair(clusterId1, clusterId2);
                    if (inserted.add(rewritten)) db.insertPair(rewritten);
                }
            }
        }
        db.mergeCluster(smaller, larger);  // bulk UPDATE: 1 call covers all members
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Records a SIMILAR or DIFFERENT relationship between two nodes.
     * Both nodes are registered (idempotently) before the relationship is applied.
     *
     * <ul>
     *   <li>SIMILAR — merges the two clusters; throws if they are already recorded as DIFFERENT.</li>
     *   <li>DIFFERENT — records the cluster pair; throws if they are already in the same cluster.</li>
     * </ul>
     *
     * @throws IllegalStateException if the new relation contradicts existing knowledge
     */
    public void saveInvestigation(Investigation investigation) {
        db.registerNode(investigation.sourceID);
        db.registerNode(investigation.targetID);
        if (investigation.relation == RelationType.SIMILAR) {
            merge(investigation.sourceID, investigation.targetID);
        } else if (investigation.relation == RelationType.DIFFERENT) {
            String clusterA = resolveCluster(investigation.sourceID);
            String clusterB = resolveCluster(investigation.targetID);
            if (clusterA.equals(clusterB)) {
                throw new IllegalStateException(
                        investigation.sourceID + " and " + investigation.targetID +
                        " are already known to be SIMILAR — cannot mark as DIFFERENT");
            }
            db.insertPair(new ClusterPair(clusterA, clusterB));
        }
    }

    /**
     * Returns the known relationship between {@code sourceId} and each of {@code targetIds}.
     * The source node is registered if not yet seen. Targets that have never been registered
     * are returned as {@link RelationType#UNKNOWN}.
     *
     * <p>Because every node's cluster_id is always canonical, a single batch query resolves
     * all target clusters — no per-root follow-up calls are needed (contrast with CacheDB).
     *
     * @param sourceId  the node whose relationship to each target is queried
     * @param targetIds nodes to classify as SIMILAR, DIFFERENT, or UNKNOWN relative to sourceId
     * @return one {@link Investigation} per target, in the same order as {@code targetIds}
     */
    public List<Investigation> findRelationship(String sourceId, List<String> targetIds) {
        if (targetIds.isEmpty()) return List.of();

        db.registerNode(sourceId);
        String sourceCluster = db.getClusterId(sourceId);

        // Single batch query — returns canonical cluster_id for each known target directly
        Map<String, String> targetClusters = db.getClusterIds(targetIds);

        Set<String> nonSimilarClusters = new HashSet<>(targetClusters.values());
        nonSimilarClusters.remove(sourceCluster);
        Set<ClusterPair> knownDifferent = nonSimilarClusters.isEmpty()
                ? Set.of()
                : db.getDifferentPairs(sourceCluster, nonSimilarClusters);

        List<Investigation> result = new ArrayList<>();
        for (String targetId : targetIds) {
            String targetCluster = targetClusters.get(targetId);
            if (targetCluster == null) {
                result.add(new Investigation(sourceId, targetId, RelationType.UNKNOWN));
            } else if (sourceCluster.equals(targetCluster)) {
                result.add(new Investigation(sourceId, targetId, RelationType.SIMILAR));
            } else if (knownDifferent.contains(new ClusterPair(sourceCluster, targetCluster))) {
                result.add(new Investigation(sourceId, targetId, RelationType.DIFFERENT));
            } else {
                result.add(new Investigation(sourceId, targetId, RelationType.UNKNOWN));
            }
        }
        return result;
    }

    /**
     * Returns all nodes from {@code queryIds} that belong to the same cluster as {@code sourceId}.
     * The source node is registered if not yet seen. Query nodes that have never been registered
     * are silently excluded (they cannot be in any cluster).
     *
     * @param sourceId the reference node whose cluster membership is used as the filter
     * @param queryIds candidate nodes to test for cluster membership
     * @return the subset of {@code queryIds} that share a cluster with {@code sourceId}
     */
    public List<String> findAllInCluster(String sourceId, List<String> queryIds) {
        if (queryIds.isEmpty()) return List.of();

        db.registerNode(sourceId);
        String sourceCluster = db.getClusterId(sourceId);
        Map<String, String> queryClusters = db.getClusterIds(queryIds);

        List<String> result = new ArrayList<>();
        for (String nodeId : queryIds) {
            if (sourceCluster.equals(queryClusters.get(nodeId))) result.add(nodeId);
        }
        return result;
    }

    // ── DB layer ──────────────────────────────────────────────────────────────

    /**
     * All SQL lives here. No business logic.
     *
     * nodes table:
     *   registerNode    — insert node pointing to itself as its own cluster (idempotent)
     *   getClusterId    — read one node's canonical cluster id (always current, never stale)
     *   getClusterIds   — batch-read canonical cluster ids for many nodes
     *   getClusterSize  — count members of a cluster (used for union-by-size)
     *   mergeCluster    — bulk reassign all members of 'from' cluster to 'to' cluster (1 UPDATE)
     *
     * different_clusters table:
     *   pairExists           — check whether a DIFFERENT pair is recorded
     *   getPairsInvolving    — all pairs that reference a given cluster id
     *   deletePairsInvolving — remove all pairs that reference a given cluster id
     *   insertPair           — record a new DIFFERENT relationship (idempotent)
     *   getDifferentPairs    — batch-check which (source, target) pairs are DIFFERENT
     */
    private static class Db {

        private final Connection conn;
        final AtomicInteger callCount = new AtomicInteger();

        /** Creates the schema (nodes + different_clusters tables) on the given connection. */
        Db(Connection conn) throws SQLException {
            this.conn = conn;
            try (Statement st = conn.createStatement()) {
                st.execute("CREATE TABLE nodes (node_id VARCHAR PRIMARY KEY, cluster_id VARCHAR NOT NULL)");
                st.execute("CREATE INDEX idx_nodes_cluster ON nodes (cluster_id)");
                st.execute("""
                        CREATE TABLE different_clusters (
                            c1 VARCHAR NOT NULL,
                            c2 VARCHAR NOT NULL,
                            PRIMARY KEY (c1, c2)
                        )""");
            }
        }

        // ── nodes ─────────────────────────────────────────────────────────────

        /**
         * Inserts a new node with {@code cluster_id = node_id} if the node is not yet present.
         * Safe to call multiple times — a second call for the same nodeId is a no-op.
         */
        void registerNode(String nodeId) {
            callCount.incrementAndGet();
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO nodes (node_id, cluster_id) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM nodes WHERE node_id = ?)")) {
                ps.setString(1, nodeId);
                ps.setString(2, nodeId);
                ps.setString(3, nodeId);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Returns the canonical cluster ID for the given node.
         * Always reflects the current state — cluster_id is kept up-to-date by {@link #mergeCluster}.
         * Falls back to returning nodeId itself if the node is not in the table (should not occur
         * after proper registration at the entry points).
         */
        String getClusterId(String nodeId) {
            callCount.incrementAndGet();
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT cluster_id FROM nodes WHERE node_id = ?")) {
                ps.setString(1, nodeId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? rs.getString(1) : nodeId;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Batch-reads the canonical cluster ID for each node in {@code nodeIds}.
         * Nodes not present in the table are omitted from the result map (caller treats them
         * as UNKNOWN). Result order matches insertion order of the returned LinkedHashMap.
         *
         * @return map of nodeId → clusterId for all nodes found in the table
         */
        Map<String, String> getClusterIds(List<String> nodeIds) {
            callCount.incrementAndGet();
            if (nodeIds.isEmpty()) return Map.of();
            String placeholders = nodeIds.stream().map(ignored -> "?").collect(Collectors.joining(","));
            Map<String, String> result = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT node_id, cluster_id FROM nodes WHERE node_id IN (" + placeholders + ")")) {
                for (int i = 0; i < nodeIds.size(); i++) ps.setString(i + 1, nodeIds.get(i));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) result.put(rs.getString(1), rs.getString(2));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return result;
        }

        /**
         * Returns the number of nodes currently assigned to the given cluster.
         * Used by {@code merge} to decide which cluster is larger (union-by-size).
         */
        int getClusterSize(String clusterId) {
            callCount.incrementAndGet();
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM nodes WHERE cluster_id = ?")) {
                ps.setString(1, clusterId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? rs.getInt(1) : 0;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Reassigns all nodes in the {@code from} cluster to the {@code to} cluster in one
         * bulk UPDATE. This is the core operation that keeps cluster_id always canonical —
         * after this call, no node retains {@code from} as its cluster_id.
         */
        void mergeCluster(String from, String to) {
            callCount.incrementAndGet();
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE nodes SET cluster_id = ? WHERE cluster_id = ?")) {
                ps.setString(1, to);
                ps.setString(2, from);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // ── different_clusters ────────────────────────────────────────────────

        /**
         * Returns true if the given pair of cluster IDs is already recorded as DIFFERENT.
         * Used as a contradiction guard before unioning two clusters.
         */
        boolean pairExists(ClusterPair pair) {
            callCount.incrementAndGet();
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT 1 FROM different_clusters WHERE c1 = ? AND c2 = ?")) {
                ps.setString(1, pair.clusterId1());
                ps.setString(2, pair.clusterId2());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Returns all DIFFERENT pairs that mention the given cluster ID on either side.
         * Called before a merge to find all pairs that must be rewritten when the cluster
         * ID disappears.
         */
        List<ClusterPair> getPairsInvolving(String clusterId) {
            callCount.incrementAndGet();
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT c1, c2 FROM different_clusters WHERE c1 = ? OR c2 = ?")) {
                ps.setString(1, clusterId);
                ps.setString(2, clusterId);
                List<ClusterPair> result = new ArrayList<>();
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) result.add(new ClusterPair(rs.getString(1), rs.getString(2)));
                }
                return result;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Deletes all DIFFERENT pairs that mention the given cluster ID on either side.
         * Called during a merge immediately before reinserting the rewritten pairs, so
         * the table never holds stale references to a cluster ID that no longer exists.
         */
        void deletePairsInvolving(String clusterId) {
            callCount.incrementAndGet();
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM different_clusters WHERE c1 = ? OR c2 = ?")) {
                ps.setString(1, clusterId);
                ps.setString(2, clusterId);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Records the given pair as DIFFERENT. Idempotent — a duplicate insert is silently ignored.
         * The pair is stored in canonical (sorted) order, so the same logical pair always maps
         * to the same row regardless of the argument order.
         */
        void insertPair(ClusterPair pair) {
            callCount.incrementAndGet();
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO different_clusters (c1, c2) SELECT ?, ? WHERE NOT EXISTS " +
                    "(SELECT 1 FROM different_clusters WHERE c1 = ? AND c2 = ?)")) {
                ps.setString(1, pair.clusterId1());
                ps.setString(2, pair.clusterId2());
                ps.setString(3, pair.clusterId1());
                ps.setString(4, pair.clusterId2());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Batch-checks which of the given target cluster IDs are recorded as DIFFERENT from
         * {@code sourceCluster}. Builds a single OR-joined query so all checks are done in
         * one round-trip regardless of how many targets are provided.
         *
         * @param sourceCluster  the cluster ID of the source node
         * @param targetClusters cluster IDs to test against sourceCluster
         * @return the subset of (sourceCluster, targetCluster) pairs found in different_clusters
         */
        Set<ClusterPair> getDifferentPairs(String sourceCluster, Set<String> targetClusters) {
            callCount.incrementAndGet();
            List<ClusterPair> pairs = targetClusters.stream()
                    .map(targetClusterId -> new ClusterPair(sourceCluster, targetClusterId))
                    .toList();

            String conditions = pairs.stream()
                    .map(ignored -> "(c1 = ? AND c2 = ?)")
                    .collect(Collectors.joining(" OR "));

            Set<ClusterPair> found = new HashSet<>();
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT c1, c2 FROM different_clusters WHERE " + conditions)) {
                int idx = 1;
                for (ClusterPair pair : pairs) {
                    ps.setString(idx++, pair.clusterId1());
                    ps.setString(idx++, pair.clusterId2());
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) found.add(new ClusterPair(rs.getString(1), rs.getString(2)));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return found;
        }
    }
}