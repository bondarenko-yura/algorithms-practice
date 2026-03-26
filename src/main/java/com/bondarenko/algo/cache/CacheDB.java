package com.bondarenko.algo.cache;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DB-backed equivalent of {@link Cache}.
 * Uses an H2 in-memory relational database to store the Union-Find state:
 *
 *   parent(id PK, root)          — each node mapped to its cluster root (path-compressed)
 *   different_clusters(root1, root2) — canonical pairs of roots known to be in different clusters
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

    /** Returns the cluster root of {@code id}, inserting it if absent. Applies path compression. */
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

        // Path compression: point every node in the path directly to root
        if (path.size() > 1) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE parent SET root = ? WHERE id = ?")) {
                for (String node : path) {
                    ps.setString(1, root);
                    ps.setString(2, node);
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

    public List<Investigation> findRelationship(String sourceId, List<String> targetIds) {
        try {
            List<Investigation> result = new ArrayList<>();
            String sourceRoot = find(sourceId);
            for (String targetId : targetIds) {
                if (!existsInParent(targetId)) {
                    result.add(new Investigation(sourceId, targetId, RelationType.UNKNOWN));
                    continue;
                }
                String targetRoot = find(targetId);
                if (sourceRoot.equals(targetRoot)) {
                    result.add(new Investigation(sourceId, targetId, RelationType.SIMILAR));
                } else {
                    String key = clusterKey(sourceRoot, targetRoot);
                    String[] parts = key.split("\\|", 2);
                    RelationType rel = isDifferentCluster(parts[0], parts[1])
                            ? RelationType.DIFFERENT
                            : RelationType.UNKNOWN;
                    result.add(new Investigation(sourceId, targetId, rel));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> findAllInCluster(String sourceId, List<String> queryIds) {
        try {
            String sourceRoot = find(sourceId);
            List<String> result = new ArrayList<>();
            for (String id : queryIds) {
                if (existsInParent(id) && find(id).equals(sourceRoot)) {
                    result.add(id);
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean existsInParent(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM parent WHERE id = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean isDifferentCluster(String r1, String r2) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM different_clusters WHERE root1 = ? AND root2 = ?")) {
            ps.setString(1, r1);
            ps.setString(2, r2);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}