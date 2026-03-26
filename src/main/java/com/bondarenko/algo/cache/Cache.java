package com.bondarenko.algo.cache;

import java.util.*;

public class Cache {

    // Union-Find: maps each id to its cluster representative (root)
    private final Map<String, String> parent = new HashMap<>();

    // Canonical pairs of cluster roots known to be DIFFERENT, stored as "root1|root2"
    private final Set<String> differentClusters = new HashSet<>();

    // --- Union-Find helpers ---

    private String find(String id) {
        parent.putIfAbsent(id, id);
        if (!parent.get(id).equals(id)) {
            parent.put(id, find(parent.get(id))); // path compression
        }
        return parent.get(id);
    }

    private void union(String a, String b) {
        String rootA = find(a);
        String rootB = find(b);
        if (rootA.equals(rootB)) return;

        // Before merging, rewrite any "different" pairs that referenced rootB → rootA
        Set<String> updated = new HashSet<>();
        for (String key : differentClusters) {
            String[] parts = key.split("\\|", 2);
            String r1 = parts[0].equals(rootB) ? rootA : parts[0];
            String r2 = parts[1].equals(rootB) ? rootA : parts[1];
            if (!r1.equals(r2)) {        // drop pairs that became self-pairs after merge
                updated.add(clusterKey(r1, r2));
            }
        }
        differentClusters.clear();
        differentClusters.addAll(updated);

        parent.put(rootB, rootA);        // merge rootB into rootA
    }

    /** Canonical, order-independent key for a pair of cluster roots. */
    private String clusterKey(String r1, String r2) {
        return r1.compareTo(r2) <= 0 ? r1 + "|" + r2 : r2 + "|" + r1;
    }

    // --- Public API ---

    public void saveInvestigation(Investigation investigation) {
        if (investigation.relation == RelationType.SIMILAR) {
            union(investigation.sourceID, investigation.targetID);
        } else if (investigation.relation == RelationType.DIFFERENT) {
            String rootA = find(investigation.sourceID);
            String rootB = find(investigation.targetID);
            differentClusters.add(clusterKey(rootA, rootB));
        }
    }

    public List<Investigation> findRelationship(String sourceId, List<String> targetIds) {
        List<Investigation> result = new ArrayList<>();
        String sourceRoot = find(sourceId);

        for (String targetId : targetIds) {
            if (!parent.containsKey(targetId)) {
                // Never seen before — completely unknown
                result.add(new Investigation(sourceId, targetId, RelationType.UNKNOWN));
                continue;
            }
            String targetRoot = find(targetId);
            if (sourceRoot.equals(targetRoot)) {
                result.add(new Investigation(sourceId, targetId, RelationType.SIMILAR));
            } else if (differentClusters.contains(clusterKey(sourceRoot, targetRoot))) {
                result.add(new Investigation(sourceId, targetId, RelationType.DIFFERENT));
            } else {
                result.add(new Investigation(sourceId, targetId, RelationType.UNKNOWN));
            }
        }
        return result;
    }

    public List<String> findAllInCluster(String sourceId, List<String> queryIds) {
        String sourceRoot = find(sourceId);
        List<String> result = new ArrayList<>();
        for (String id : queryIds) {
            if (parent.containsKey(id) && find(id).equals(sourceRoot)) {
                result.add(id);
            }
        }
        return result;
    }


    public static class Investigation {
        public String sourceID;
        public String targetID;
        public RelationType relation;

        public Investigation(String sourceID, String targetID, RelationType relation) {
            this.sourceID = sourceID;
            this.targetID = targetID;
            this.relation = relation;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Investigation that = (Investigation) o;
            return Objects.equals(sourceID, that.sourceID) && Objects.equals(targetID, that.targetID) && relation == that.relation;
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceID, targetID, relation);
        }

        @Override
        public String toString() {
            return "Investigation{" +
                    "sourceID='" + sourceID + '\'' +
                    ", targetID='" + targetID + '\'' +
                    ", relation=" + relation +
                    '}';
        }
    }

    public enum RelationType {
        UNKNOWN,
        SIMILAR,
        DIFFERENT
    }
}