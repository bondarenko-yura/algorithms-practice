package com.bondarenko.algo.cache;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Same functional tests as CacheDBTest, plus DB call count assertions.
 * V1 (CacheDB) counts are shown in comments for comparison.
 *
 * Why V2 is faster:
 *   CacheDB stores parent pointers that go stale after unions — each findRelationship/
 *   findAllInCluster must call find() on every unique stored root to re-resolve it (2 calls each).
 *   CacheDbV2 stores canonical cluster_id directly; a single getClusterIds batch query returns
 *   up-to-date values for all targets with no second resolution pass.
 */
class CacheDbV2Test {

    @Test
    public void testMinimizeComputationCalls() {
        // V2 total: 96 DB calls vs V1: 169 — 43% fewer
        var clusters = List.of(List.of("A", "B", "C"), List.of("D"), List.of("E", "F"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDbV2());
        var expectCalls = 0;

        expectCalls += 1;
        var call1 = finder.findKnownSimilar("A", List.of("B", "C", "D", "E", "F"));
        assertThat(call1).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(15); // V1: 15

        expectCalls += 0;
        var call2 = finder.findKnownSimilar("B", List.of("A", "C", "D", "E", "F"));
        assertThat(call2).containsExactlyInAnyOrder(new Investigation("B", "A", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(18); // V1: 21

        expectCalls += 1;
        var call3 = finder.findKnownSimilar("C", List.of("A", "B", "D", "E", "F"));
        assertThat(call3).containsExactlyInAnyOrder(
                new Investigation("C", "A", RelationType.SIMILAR),
                new Investigation("C", "B", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(34); // V1: 39

        expectCalls += 3;
        var call4 = finder.findKnownSimilar("D", List.of("A", "B", "C", "E", "F"));
        assertThat(call4).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(62); // V1: 95

        expectCalls += 2;
        var call5 = finder.findKnownSimilar("E", List.of("A", "B", "C", "D", "F"));
        assertThat(call5).containsExactlyInAnyOrder(new Investigation("E", "F", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(88); // V1: 148

        expectCalls += 0;
        var call6 = finder.findKnownSimilar("F", List.of("A", "B", "C", "D", "E"));
        assertThat(call6).containsExactlyInAnyOrder(new Investigation("F", "E", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(92); // V1: 162

        expectCalls += 0;
        var call7 = finder.findKnownSimilar("A", List.of("E"));
        assertThat(call7).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(96); // V1: 169
    }

    @Test
    public void testClusterSkipReducesCallsForLargeKnownCluster() {
        // V2 total: 61 DB calls vs V1: 67
        var clusters = List.of(List.of("A"), List.of("B", "C", "D", "E"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDbV2());
        var expectCalls = 0;

        expectCalls += 1;
        finder.findKnownSimilar("B", List.of("C"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(15); // V1: 15

        expectCalls += 1;
        finder.findKnownSimilar("B", List.of("D"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(30); // V1: 30

        expectCalls += 1;
        finder.findKnownSimilar("B", List.of("E"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(45); // V1: 45

        expectCalls += 1;
        var r1 = finder.findKnownSimilar("A", List.of("B", "C", "D", "E"));
        assertThat(r1).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(57); // V1: 61

        expectCalls += 0;
        var r2 = finder.findKnownSimilar("A", List.of("B", "C", "D", "E"));
        assertThat(r2).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(61); // V1: 67
    }

    @Test
    public void testMergeCorrectlyPreservesDifferentRelationships() {
        // V2 total: 34 DB calls vs V1: 41
        var clusters = List.of(List.of("A", "B"), List.of("X"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDbV2());
        var expectCalls = 0;

        expectCalls += 1;
        var r1 = finder.findKnownSimilar("A", List.of("X"));
        assertThat(r1).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(11); // V1: 13

        expectCalls += 1;
        var r2 = finder.findKnownSimilar("A", List.of("B"));
        assertThat(r2).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(26); // V1: 28

        expectCalls += 0;
        var r3 = finder.findKnownSimilar("B", List.of("X"));
        assertThat(r3).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(30); // V1: 35

        expectCalls += 0;
        var r4 = finder.findKnownSimilar("A", List.of("X"));
        assertThat(r4).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(34); // V1: 41
    }

    @Test
    public void testTransitiveClusterMembershipReducesCalls() {
        // V2 total: 34 DB calls vs V1: 41
        var clusters = List.of(List.of("A", "B", "C"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDbV2());
        var expectCalls = 0;

        expectCalls += 1;
        var r1 = finder.findKnownSimilar("A", List.of("B", "C"));
        assertThat(r1).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(15); // V1: 15

        expectCalls += 1;
        var r2 = finder.findKnownSimilar("C", List.of("A", "B"));
        assertThat(r2).containsExactlyInAnyOrder(
                new Investigation("C", "A", RelationType.SIMILAR),
                new Investigation("C", "B", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(31); // V1: 33

        expectCalls += 0;
        var r3 = finder.findKnownSimilar("B", List.of("A", "C"));
        assertThat(r3).containsExactlyInAnyOrder(
                new Investigation("B", "A", RelationType.SIMILAR),
                new Investigation("B", "C", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(34); // V1: 41
    }

    @Test
    public void testEarlyReturnWhenSimilarAlreadyKnown() {
        // V2 total: 18 DB calls vs V1: 20
        var clusters = List.of(List.of("A", "B"), List.of("C"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDbV2());
        var expectCalls = 0;

        expectCalls += 1;
        finder.findKnownSimilar("A", List.of("B"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(15); // V1: 15

        expectCalls += 0;
        var r = finder.findKnownSimilar("A", List.of("B", "C"));
        assertThat(r).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(18); // V1: 20
    }

    @Test
    public void testDifferentKnowledgeTransfersThroughChainedMerges() {
        // V2 total: 65 DB calls vs V1: 78
        var clusters = List.of(List.of("A", "B", "C"), List.of("X", "Y"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDbV2());
        var expectCalls = 0;

        expectCalls += 1;
        finder.findKnownSimilar("A", List.of("X"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(11); // V1: 13

        expectCalls += 1;
        finder.findKnownSimilar("X", List.of("Y"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(26); // V1: 28

        expectCalls += 1;
        finder.findKnownSimilar("A", List.of("B"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(41); // V1: 43

        expectCalls += 1;
        var r4 = finder.findKnownSimilar("C", List.of("A", "B"));
        assertThat(r4).containsExactlyInAnyOrder(
                new Investigation("C", "A", RelationType.SIMILAR),
                new Investigation("C", "B", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(57); // V1: 63

        expectCalls += 0;
        var r5 = finder.findKnownSimilar("B", List.of("X"));
        assertThat(r5).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(61); // V1: 72

        expectCalls += 0;
        var r6 = finder.findKnownSimilar("C", List.of("Y"));
        assertThat(r6).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(65); // V1: 78
    }

    @Test
    public void testContradictionThrows() {
        var cache = new CacheDbV2();
        cache.saveInvestigation(new Investigation("A", "B", RelationType.SIMILAR));
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "B", RelationType.DIFFERENT)))
                .isInstanceOf(IllegalStateException.class);
        cache.saveInvestigation(new Investigation("B", "C", RelationType.SIMILAR));
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "C", RelationType.DIFFERENT)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testReverseContradictionThrows() {
        var cache = new CacheDbV2();
        cache.saveInvestigation(new Investigation("A", "B", RelationType.DIFFERENT));
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "B", RelationType.SIMILAR)))
                .isInstanceOf(IllegalStateException.class);
        cache.saveInvestigation(new Investigation("B", "C", RelationType.SIMILAR));
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "C", RelationType.SIMILAR)))
                .isInstanceOf(IllegalStateException.class);
    }

    // ── Test helpers ──────────────────────────────────────────────────────────

    static class DuplicateFinder {
        final VideoCompareService videoService;
        final CacheDbV2 cache;
        DuplicateFinder(VideoCompareService videoService, CacheDbV2 cache) {
            this.videoService = videoService;
            this.cache = cache;
        }
        List<Investigation> findKnownSimilar(String sourceId, List<String> targetIds) {
            var similar = new ArrayList<Investigation>();
            var unknown = new ArrayList<Investigation>();
            for (Investigation inv : cache.findRelationship(sourceId, targetIds)) {
                if (inv.relation == RelationType.SIMILAR)
                    similar.add(inv);
                else if (inv.relation == RelationType.UNKNOWN)
                    unknown.add(inv);
            }
            if (!similar.isEmpty())
                return similar;
            var skip = new HashSet<String>();
            for (Investigation inv : unknown) {
                if (skip.contains(inv.targetID))
                    continue;
                var cluster = this.cache.findAllInCluster(inv.targetID, targetIds);
                skip.addAll(cluster);
                var same = this.videoService.isSameVideoExpensiveComputation(sourceId, inv.targetID);
                var relation = same ? RelationType.SIMILAR : RelationType.DIFFERENT;
                var investigation = new Investigation(sourceId, inv.targetID, relation);
                this.cache.saveInvestigation(investigation);
                if (same) {
                    similar.add(investigation);
                    for (String id : cluster)
                        if (!id.equals(inv.targetID))
                            similar.add(new Investigation(sourceId, id, RelationType.SIMILAR));
                    break;
                }
            }
            return similar;
        }
    }

    static class VideoCompareService {
        List<List<String>> stubClusters;
        int count;
        VideoCompareService(List<List<String>> clusters) {
            this.stubClusters = clusters;
        }
        boolean isSameVideoExpensiveComputation(String sourceId, String targetId) {
            this.count++;
            return stubClusters.stream()
                    .filter(s -> s.contains(sourceId))
                    .anyMatch(s -> s.contains(targetId));
        }
    }
}