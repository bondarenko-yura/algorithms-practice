package com.bondarenko.algo.cache;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * DB-backed equivalent of {@link Cache}.
 * Uses an H2 in-memory relational database to store the Union-Find state:
 *
 *   parent(id PK, root)             — each node mapped to its cluster root (path-compressed)
 *   different_clusters(root1, root2) — canonical pairs of roots known to be in different clusters
 *
 * Optimizations over the naive per-row approach:
 *  1. Path compression skips no-op updates: only nodes that don't yet point directly to the root
 *     are written (saves the spurious UPDATE for the penultimate node in the chain).
 *  2. findRelationship issues one SELECT IN for all targets and one batched different_clusters
 *     check, reducing O(6N) → O(1) DB calls regardless of the number of targets.
 *  3. findAllInCluster issues one SELECT IN for all query ids → O(5M) → O(1).
 */
public class CacheDB {

    private static final AtomicInteger DB_SEQ = new AtomicInteger();

    private final Connection conn;

    public CacheDB() {
        try {
            String url = "jdbc:h2:mem:cachedb_" + DB_SEQ.getAndIncrement() + ";DB_CLOSE_DELAY=-1";
            conn = DriverManager.getConnection(url, "sa", "");
            try (Statement st = conn.createStatement()) {
                st.execute("CREATE TABLE parent (id VARCHAR PRIMARY KEY, root VARCHAR NOT NULL)");
                st.execute("""
                        CREATE TABLE different_clusters (
                            root1 VARCHAR NOT NULL,
                            root2 VARCHAR NOT NULL,
                            PRIMARY KEY (root1, root2)
                        )""");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ── Union-Find ────────────────────────────────────────────────────────────

    /**
     * Returns the cluster root of {@code id}, inserting it if absent.
     *
     * DB calls: 1 INSERT (no-op if exists) + chain-length SELECTs
     *           + at most (chain-length − 2) batch UPDATEs.
     *
     * Optimization: the last node in the path IS the root (no update), and the
     * second-to-last already points to the root (it was the stored value that led us
     * there — also no update).  Only nodes with stale intermediate pointers are written.
     */
    private String find(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO parent (id, root) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM parent WHERE id = ?)")) {
            ps.setString(1, id);
            ps.setString(2, id);
            ps.setString(3, id);
            ps.executeUpdate();
        }

        // Walk parent chain to root
        List<String> path = new ArrayList<>();
        String cur = id;
        while (true) {
            path.add(cur);
            String parent = queryRoot(cur);
            if (parent.equals(cur)) break;
            cur = parent;
        }
        String root = cur;

        // Optimization: skip the last two nodes in the path —
        //   path[size-1] = root itself (root = root, no-op)
        //   path[size-2] already stores root as its parent (that's how we reached it)
        // Only path[0..size-3] have stale intermediate pointers and need updating.
        int needsUpdate = path.size() - 2;   // number of nodes to update
        if (needsUpdate > 0) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE parent SET root = ? WHERE id = ?")) {
                for (int i = 0; i < needsUpdate; i++) {
                    ps.setString(1, root);
                    ps.setString(2, path.get(i));
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
        return root;
    }

    private String queryRoot(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT root FROM parent WHERE id = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : id;
            }
        }
    }

    /** Merges the two clusters, rewriting any DIFFERENT pairs that referenced the absorbed root. */
    private void union(String a, String b) throws SQLException {
        String rootA = find(a);
        String rootB = find(b);
        if (rootA.equals(rootB)) return;

        // Collect DIFFERENT pairs that involve rootB
        List<String[]> affected = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT root1, root2 FROM different_clusters WHERE root1 = ? OR root2 = ?")) {
            ps.setString(1, rootB);
            ps.setString(2, rootB);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) affected.add(new String[]{rs.getString(1), rs.getString(2)});
            }
        }

        if (!affected.isEmpty()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM different_clusters WHERE root1 = ? OR root2 = ?")) {
                ps.setString(1, rootB);
                ps.setString(2, rootB);
                ps.executeUpdate();
            }
            Set<String> inserted = new HashSet<>();
            for (String[] pair : affected) {
                String r1 = pair[0].equals(rootB) ? rootA : pair[0];
                String r2 = pair[1].equals(rootB) ? rootA : pair[1];
                if (!r1.equals(r2)) {
                    String key = clusterKey(r1, r2);
                    if (inserted.add(key)) insertDifferentPair(r1, r2);
                }
            }
        }

        // Point rootB → rootA (lazy path compression handles the rest)
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE parent SET root = ? WHERE id = ?")) {
            ps.setString(1, rootA);
            ps.setString(2, rootB);
            ps.executeUpdate();
        }
    }

    /** Canonical, order-independent key for a pair of roots. */
    private String clusterKey(String r1, String r2) {
        return r1.compareTo(r2) <= 0 ? r1 + "|" + r2 : r2 + "|" + r1;
    }

    private void insertDifferentPair(String r1, String r2) throws SQLException {
        String key = clusterKey(r1, r2);
        String[] parts = key.split("\\|", 2);
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO different_clusters (root1, root2) SELECT ?, ? WHERE NOT EXISTS " +
                "(SELECT 1 FROM different_clusters WHERE root1 = ? AND root2 = ?)")) {
            ps.setString(1, parts[0]);
            ps.setString(2, parts[1]);
            ps.setString(3, parts[0]);
            ps.setString(4, parts[1]);
            ps.executeUpdate();
        }
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void saveInvestigation(Investigation investigation) {
        try {
            if (investigation.relation == RelationType.SIMILAR) {
                union(investigation.sourceID, investigation.targetID);
            } else if (investigation.relation == RelationType.DIFFERENT) {
                String rootA = find(investigation.sourceID);
                String rootB = find(investigation.targetID);
                insertDifferentPair(rootA, rootB);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Optimization: replaces O(6N) per-row calls with:
     *   1 SELECT IN   — fetch all known targets and their stored roots at once
     *   O(K) find()   — resolve any intermediate roots left stale after a union
     *                   (K = distinct stored roots; typically very small after compression)
     *   1 batched SELECT — check all (sourceRoot, targetRoot) pairs against different_clusters
     */
    public List<Investigation> findRelationship(String sourceId, List<String> targetIds) {
        try {
            List<Investigation> result = new ArrayList<>();
            if (targetIds.isEmpty()) return result;

            String sourceRoot = find(sourceId);

            // 1. Fetch stored roots for all targets in one query
            Map<String, String> storedRoots = batchGetStoredRoots(targetIds);

            // 2. Resolve any intermediate stored roots to their true root via find().
            //    After path compression, stored roots are typically already final roots,
            //    so find() on them returns in O(1) with no updates needed.
            Map<String, String> resolvedRoots = new HashMap<>();
            for (String storedRoot : new HashSet<>(storedRoots.values())) {
                resolvedRoots.put(storedRoot, find(storedRoot));
            }

            // 3. Map each known target to its true cluster root
            Map<String, String> targetTrueRoot = new HashMap<>();
            for (Map.Entry<String, String> e : storedRoots.entrySet()) {
                targetTrueRoot.put(e.getKey(), resolvedRoots.get(e.getValue()));
            }

            // 4. Check all (sourceRoot, targetRoot) difference pairs in one query
            Set<String> distinctTargetRoots = new HashSet<>(targetTrueRoot.values());
            distinctTargetRoots.remove(sourceRoot);
            Set<String> knownDifferent = distinctTargetRoots.isEmpty()
                    ? Set.of()
                    : batchCheckDifferent(sourceRoot, distinctTargetRoots);

            // 5. Build result preserving order of targetIds
            for (String targetId : targetIds) {
                if (!targetTrueRoot.containsKey(targetId)) {
                    result.add(new Investigation(sourceId, targetId, RelationType.UNKNOWN));
                } else {
                    String targetRoot = targetTrueRoot.get(targetId);
                    RelationType rel;
                    if (sourceRoot.equals(targetRoot)) {
                        rel = RelationType.SIMILAR;
                    } else {
                        rel = knownDifferent.contains(clusterKey(sourceRoot, targetRoot))
                                ? RelationType.DIFFERENT : RelationType.UNKNOWN;
                    }
                    result.add(new Investigation(sourceId, targetId, rel));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Optimization: replaces O(5M) per-row calls with:
     *   1 SELECT IN — fetch roots for all query ids at once
     *   O(K) find() — resolve intermediate roots (typically O(1))
     */
    public List<String> findAllInCluster(String sourceId, List<String> queryIds) {
        try {
            if (queryIds.isEmpty()) return List.of();

            String sourceRoot = find(sourceId);

            // 1. Fetch stored roots for all query ids in one query
            Map<String, String> storedRoots = batchGetStoredRoots(queryIds);

            // 2. Resolve unique stored roots to their true roots
            Map<String, String> resolvedRoots = new HashMap<>();
            for (String storedRoot : new HashSet<>(storedRoots.values())) {
                resolvedRoots.put(storedRoot, find(storedRoot));
            }

            // 3. Collect ids whose true root matches sourceRoot
            List<String> result = new ArrayList<>();
            for (String id : queryIds) {
                String stored = storedRoots.get(id);
                if (stored != null && resolvedRoots.get(stored).equals(sourceRoot)) {
                    result.add(id);
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ── Batch helpers ─────────────────────────────────────────────────────────

    /**
     * One SELECT IN: returns the stored root for every id that exists in parent.
     * Unknown ids are simply absent from the result map.
     */
    private Map<String, String> batchGetStoredRoots(List<String> ids) throws SQLException {
        String placeholders = ids.stream().map(x -> "?").collect(Collectors.joining(","));
        Map<String, String> result = new LinkedHashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id, root FROM parent WHERE id IN (" + placeholders + ")")) {
            for (int i = 0; i < ids.size(); i++) ps.setString(i + 1, ids.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.put(rs.getString(1), rs.getString(2));
            }
        }
        return result;
    }

    /**
     * One SELECT with OR conditions: returns the canonical keys for all
     * (sourceRoot, targetRoot) pairs that exist in different_clusters.
     */
    private Set<String> batchCheckDifferent(String sourceRoot, Set<String> targetRoots) throws SQLException {
        List<String[]> pairs = targetRoots.stream()
                .map(tr -> clusterKey(sourceRoot, tr).split("\\|", 2))
                .collect(Collectors.toList());

        String conditions = pairs.stream()
                .map(p -> "(root1 = ? AND root2 = ?)")
                .collect(Collectors.joining(" OR "));

        Set<String> found = new HashSet<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT root1, root2 FROM different_clusters WHERE " + conditions)) {
            int idx = 1;
            for (String[] pair : pairs) {
                ps.setString(idx++, pair[0]);
                ps.setString(idx++, pair[1]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) found.add(rs.getString(1) + "|" + rs.getString(2));
            }
        }
        return found;
    }
}