package com.bondarenko.algo.cache;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * DB-backed equivalent of {@link Cache}.
 *
 * Layers:
 *   CacheDB  — Union-Find business logic only, no SQL.
 *   Db       — private inner class; all SQL lives here, no business logic.
 */
public class CacheDB {

    private static final AtomicInteger DB_SEQ = new AtomicInteger();

    /**
     * An unordered pair of cluster roots stored in canonical (sorted) order.
     * Canonical ordering is enforced once in the compact constructor, so callers
     * never need to sort arguments themselves. Value-based equals/hashCode (record)
     * makes RootPair directly usable as a Set/Map key without a separate string key.
     */
    private record RootPair(String r1, String r2) {
        RootPair {
            if (r1.compareTo(r2) > 0) { String t = r1; r1 = r2; r2 = t; }
        }
    }

    private final Db db;

    public CacheDB() {
        try {
            String url = "jdbc:h2:mem:cachedb_" + DB_SEQ.getAndIncrement() + ";DB_CLOSE_DELAY=-1";
            db = new Db(DriverManager.getConnection(url, "sa", ""));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ── Union-Find business logic (no SQL) ────────────────────────────────────

    private String find(String id) {
        db.insertIfAbsent(id);

        List<String> path = new ArrayList<>();
        String cur = id;
        while (true) {
            path.add(cur);
            String parent = db.getRoot(cur);
            if (parent.equals(cur)) break;
            cur = parent;
        }
        String root = cur;

        int staleCount = path.size() - 2;
        if (staleCount > 0) {
            db.updateRoots(path.subList(0, staleCount), root);
        }
        return root;
    }

    private void union(String a, String b) {
        String rootA = find(a);
        String rootB = find(b);
        if (rootA.equals(rootB)) return;
        if (db.pairExists(new RootPair(rootA, rootB))) {
            throw new IllegalStateException(
                    a + " and " + b + " are already known to be DIFFERENT — cannot mark as SIMILAR");
        }

        List<RootPair> affected = db.getPairsInvolving(rootB);
        if (!affected.isEmpty()) {
            db.deletePairsInvolving(rootB);
            Set<RootPair> inserted = new HashSet<>();
            for (RootPair pair : affected) {
                String r1 = pair.r1().equals(rootB) ? rootA : pair.r1();
                String r2 = pair.r2().equals(rootB) ? rootA : pair.r2();
                if (!r1.equals(r2)) {
                    RootPair rewritten = new RootPair(r1, r2);
                    if (inserted.add(rewritten)) {
                        db.insertPair(rewritten);
                    }
                }
            }
        }
        db.updateRoot(rootB, rootA);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void saveInvestigation(Investigation investigation) {
        if (investigation.relation == RelationType.SIMILAR) {
            union(investigation.sourceID, investigation.targetID);
        } else if (investigation.relation == RelationType.DIFFERENT) {
            String rootA = find(investigation.sourceID);
            String rootB = find(investigation.targetID);
            if (rootA.equals(rootB)) {
                throw new IllegalStateException(
                        investigation.sourceID + " and " + investigation.targetID +
                        " are already known to be SIMILAR — cannot mark as DIFFERENT");
            }
            db.insertPair(new RootPair(rootA, rootB));
        }
    }

    public List<Investigation> findRelationship(String sourceId, List<String> targetIds) {
        if (targetIds.isEmpty()) return List.of();

        String sourceRoot = find(sourceId);
        Map<String, String> storedRoots = db.getRoots(targetIds);

        Map<String, String> resolvedRoots = new HashMap<>();
        for (String sr : new HashSet<>(storedRoots.values())) {
            resolvedRoots.put(sr, find(sr));
        }

        Map<String, String> trueRoots = new HashMap<>();
        for (Map.Entry<String, String> e : storedRoots.entrySet()) {
            trueRoots.put(e.getKey(), resolvedRoots.get(e.getValue()));
        }

        Set<String> nonSimilarRoots = new HashSet<>(trueRoots.values());
        nonSimilarRoots.remove(sourceRoot);
        Set<RootPair> knownDifferent = nonSimilarRoots.isEmpty()
                ? Set.of()
                : db.getDifferentPairs(sourceRoot, nonSimilarRoots);

        List<Investigation> result = new ArrayList<>();
        for (String targetId : targetIds) {
            if (!trueRoots.containsKey(targetId)) {
                result.add(new Investigation(sourceId, targetId, RelationType.UNKNOWN));
            } else {
                String targetRoot = trueRoots.get(targetId);
                RelationType rel = sourceRoot.equals(targetRoot) ? RelationType.SIMILAR
                        : knownDifferent.contains(new RootPair(sourceRoot, targetRoot)) ? RelationType.DIFFERENT
                        : RelationType.UNKNOWN;
                result.add(new Investigation(sourceId, targetId, rel));
            }
        }
        return result;
    }

    public List<String> findAllInCluster(String sourceId, List<String> queryIds) {
        if (queryIds.isEmpty()) return List.of();

        String sourceRoot = find(sourceId);
        Map<String, String> storedRoots = db.getRoots(queryIds);

        Map<String, String> resolvedRoots = new HashMap<>();
        for (String sr : new HashSet<>(storedRoots.values())) {
            resolvedRoots.put(sr, find(sr));
        }

        List<String> result = new ArrayList<>();
        for (String id : queryIds) {
            String stored = storedRoots.get(id);
            if (stored != null && resolvedRoots.get(stored).equals(sourceRoot)) {
                result.add(id);
            }
        }
        return result;
    }

    // ── DB layer ──────────────────────────────────────────────────────────────

    /**
     * All SQL lives here. No business logic.
     *
     * parent table:
     *   insertIfAbsent  — register a new node pointing to itself
     *   getRoot         — read one node's stored parent pointer
     *   getRoots        — batch-read stored parent pointers for many nodes
     *   updateRoot      — overwrite one node's parent pointer
     *   updateRoots     — batch-overwrite parent pointers (path compression)
     *
     * different_clusters table:
     *   pairExists          — check whether a DIFFERENT pair is recorded
     *   getPairsInvolving   — all pairs that reference a given root
     *   deletePairsInvolving — remove all pairs that reference a given root
     *   insertPair          — record a new DIFFERENT relationship (idempotent)
     *   getDifferentPairs   — batch-check which (source, target) pairs are DIFFERENT
     */
    private static class Db {

        private final Connection conn;

        Db(Connection conn) throws SQLException {
            this.conn = conn;
            try (Statement st = conn.createStatement()) {
                st.execute("CREATE TABLE parent (id VARCHAR PRIMARY KEY, root VARCHAR NOT NULL)");
                st.execute("""
                        CREATE TABLE different_clusters (
                            root1 VARCHAR NOT NULL,
                            root2 VARCHAR NOT NULL,
                            PRIMARY KEY (root1, root2)
                        )""");
            }
        }

        // ── parent ────────────────────────────────────────────────────────────

        void insertIfAbsent(String id) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO parent (id, root) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM parent WHERE id = ?)")) {
                ps.setString(1, id);
                ps.setString(2, id);
                ps.setString(3, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        String getRoot(String id) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT root FROM parent WHERE id = ?")) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? rs.getString(1) : id;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        Map<String, String> getRoots(List<String> ids) {
            String placeholders = ids.stream().map(x -> "?").collect(Collectors.joining(","));
            Map<String, String> result = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, root FROM parent WHERE id IN (" + placeholders + ")")) {
                for (int i = 0; i < ids.size(); i++) ps.setString(i + 1, ids.get(i));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) result.put(rs.getString(1), rs.getString(2));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return result;
        }

        void updateRoot(String id, String root) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE parent SET root = ? WHERE id = ?")) {
                ps.setString(1, root);
                ps.setString(2, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        void updateRoots(List<String> ids, String root) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE parent SET root = ? WHERE id = ?")) {
                for (String id : ids) {
                    ps.setString(1, root);
                    ps.setString(2, id);
                    ps.addBatch();
                }
                ps.executeBatch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // ── different_clusters ────────────────────────────────────────────────

        boolean pairExists(RootPair pair) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT 1 FROM different_clusters WHERE root1 = ? AND root2 = ?")) {
                ps.setString(1, pair.r1());
                ps.setString(2, pair.r2());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        List<RootPair> getPairsInvolving(String root) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT root1, root2 FROM different_clusters WHERE root1 = ? OR root2 = ?")) {
                ps.setString(1, root);
                ps.setString(2, root);
                List<RootPair> result = new ArrayList<>();
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) result.add(new RootPair(rs.getString(1), rs.getString(2)));
                }
                return result;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        void deletePairsInvolving(String root) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM different_clusters WHERE root1 = ? OR root2 = ?")) {
                ps.setString(1, root);
                ps.setString(2, root);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        void insertPair(RootPair pair) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO different_clusters (root1, root2) SELECT ?, ? WHERE NOT EXISTS " +
                    "(SELECT 1 FROM different_clusters WHERE root1 = ? AND root2 = ?)")) {
                ps.setString(1, pair.r1());
                ps.setString(2, pair.r2());
                ps.setString(3, pair.r1());
                ps.setString(4, pair.r2());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        Set<RootPair> getDifferentPairs(String sourceRoot, Set<String> targetRoots) {
            List<RootPair> pairs = targetRoots.stream()
                    .map(tr -> new RootPair(sourceRoot, tr))
                    .collect(Collectors.toList());

            String conditions = pairs.stream()
                    .map(p -> "(root1 = ? AND root2 = ?)")
                    .collect(Collectors.joining(" OR "));

            Set<RootPair> found = new HashSet<>();
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT root1, root2 FROM different_clusters WHERE " + conditions)) {
                int idx = 1;
                for (RootPair pair : pairs) {
                    ps.setString(idx++, pair.r1());
                    ps.setString(idx++, pair.r2());
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) found.add(new RootPair(rs.getString(1), rs.getString(2)));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return found;
        }
    }
}