package com.bondarenko.algo.cache;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CacheDBTest {

    @Test
    public void testMinimizeComputationCalls() {
        var clusters = List.of(List.of("A", "B", "C"), List.of("D"), List.of("E", "F"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDB());
        var expectCalls = 0;

        expectCalls += 1;
        var call1 = finder.findKnownSimilar("A", List.of("B", "C", "D", "E", "F"));
        assertThat(call1).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(15);

        expectCalls += 0;
        var call2 = finder.findKnownSimilar("B", List.of("A", "C", "D", "E", "F"));
        assertThat(call2).containsExactlyInAnyOrder(new Investigation("B", "A", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(21);

        expectCalls += 1;
        var call3 = finder.findKnownSimilar("C", List.of("A", "B", "D", "E", "F"));
        assertThat(call3).containsExactlyInAnyOrder(
                new Investigation("C", "A", RelationType.SIMILAR),
                new Investigation("C", "B", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(39);

        expectCalls += 3;
        var call4 = finder.findKnownSimilar("D", List.of("A", "B", "C", "E", "F"));
        assertThat(call4).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(95);

        expectCalls += 2;
        var call5 = finder.findKnownSimilar("E", List.of("A", "B", "C", "D", "F"));
        assertThat(call5).containsExactlyInAnyOrder(new Investigation("E", "F", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(148);

        expectCalls += 0;
        var call6 = finder.findKnownSimilar("F", List.of("A", "B", "C", "D", "E"));
        assertThat(call6).containsExactlyInAnyOrder(new Investigation("F", "E", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(162);

        expectCalls += 0;
        var call7 = finder.findKnownSimilar("A", List.of("E"));
        assertThat(call7).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(169);
    }

    @Test
    public void testClusterSkipReducesCallsForLargeKnownCluster() {
        var clusters = List.of(List.of("A"), List.of("B", "C", "D", "E"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDB());
        var expectCalls = 0;

        expectCalls += 1;
        finder.findKnownSimilar("B", List.of("C"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(15);

        expectCalls += 1;
        finder.findKnownSimilar("B", List.of("D"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(30);

        expectCalls += 1;
        finder.findKnownSimilar("B", List.of("E"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(45);

        expectCalls += 1;
        var r1 = finder.findKnownSimilar("A", List.of("B", "C", "D", "E"));
        assertThat(r1).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(61);

        expectCalls += 0;
        var r2 = finder.findKnownSimilar("A", List.of("B", "C", "D", "E"));
        assertThat(r2).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(67);
    }

    @Test
    public void testMergeCorrectlyPreservesDifferentRelationships() {
        var clusters = List.of(List.of("A", "B"), List.of("X"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDB());
        var expectCalls = 0;

        expectCalls += 1;
        var r1 = finder.findKnownSimilar("A", List.of("X"));
        assertThat(r1).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(13);

        expectCalls += 1;
        var r2 = finder.findKnownSimilar("A", List.of("B"));
        assertThat(r2).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(28);

        expectCalls += 0;
        var r3 = finder.findKnownSimilar("B", List.of("X"));
        assertThat(r3).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(35);

        expectCalls += 0;
        var r4 = finder.findKnownSimilar("A", List.of("X"));
        assertThat(r4).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(41);
    }

    @Test
    public void testTransitiveClusterMembershipReducesCalls() {
        var clusters = List.of(List.of("A", "B", "C"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDB());
        var expectCalls = 0;

        expectCalls += 1;
        var r1 = finder.findKnownSimilar("A", List.of("B", "C"));
        assertThat(r1).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(15);

        expectCalls += 1;
        var r2 = finder.findKnownSimilar("C", List.of("A", "B"));
        assertThat(r2).containsExactlyInAnyOrder(
                new Investigation("C", "A", RelationType.SIMILAR),
                new Investigation("C", "B", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(33);

        expectCalls += 0;
        var r3 = finder.findKnownSimilar("B", List.of("A", "C"));
        assertThat(r3).containsExactlyInAnyOrder(
                new Investigation("B", "A", RelationType.SIMILAR),
                new Investigation("B", "C", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(41);
    }

    @Test
    public void testEarlyReturnWhenSimilarAlreadyKnown() {
        var clusters = List.of(List.of("A", "B"), List.of("C"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDB());
        var expectCalls = 0;

        expectCalls += 1;
        finder.findKnownSimilar("A", List.of("B"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(15);

        expectCalls += 0;
        var r = finder.findKnownSimilar("A", List.of("B", "C"));
        assertThat(r).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(20);
    }

    @Test
    public void testDifferentKnowledgeTransfersThroughChainedMerges() {
        var clusters = List.of(List.of("A", "B", "C"), List.of("X", "Y"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new CacheDB());
        var expectCalls = 0;

        expectCalls += 1;
        finder.findKnownSimilar("A", List.of("X"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(13);

        expectCalls += 1;
        finder.findKnownSimilar("X", List.of("Y"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(28);

        expectCalls += 1;
        finder.findKnownSimilar("A", List.of("B"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(43);

        expectCalls += 1;
        var r4 = finder.findKnownSimilar("C", List.of("A", "B"));
        assertThat(r4).containsExactlyInAnyOrder(
                new Investigation("C", "A", RelationType.SIMILAR),
                new Investigation("C", "B", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(63);

        expectCalls += 0;
        var r5 = finder.findKnownSimilar("B", List.of("X"));
        assertThat(r5).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(72);

        expectCalls += 0;
        var r6 = finder.findKnownSimilar("C", List.of("Y"));
        assertThat(r6).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
        assertThat(finder.cache.dbCallCount()).isEqualTo(78);
    }

    // ── Test helpers ──────────────────────────────────────────────────────────

    @Test
    public void testContradictionThrows() {
        var cache = new CacheDB();
        cache.saveInvestigation(new Investigation("A", "B", RelationType.SIMILAR));
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "B", RelationType.DIFFERENT)))
                .isInstanceOf(IllegalStateException.class);
        // transitively similar: A==B, B==C → A and C are in the same cluster
        cache.saveInvestigation(new Investigation("B", "C", RelationType.SIMILAR));
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "C", RelationType.DIFFERENT)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testReverseContradictionThrows() {
        var cache = new CacheDB();
        cache.saveInvestigation(new Investigation("A", "B", RelationType.DIFFERENT));
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "B", RelationType.SIMILAR)))
                .isInstanceOf(IllegalStateException.class);
        // transitively different: A!=B, B==C → A!=C (via cluster membership)
        cache.saveInvestigation(new Investigation("B", "C", RelationType.SIMILAR));
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "C", RelationType.SIMILAR)))
                .isInstanceOf(IllegalStateException.class);
    }

    static class DuplicateFinder {
        final VideoCompareService videoService;
        final CacheDB cache;
        DuplicateFinder(VideoCompareService videoService, CacheDB cache) {
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