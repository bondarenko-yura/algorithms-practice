package com.bondarenko.algo.cache;

import com.bondarenko.algo.cache.Cache.Investigation;
import com.bondarenko.algo.cache.Cache.RelationType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CacheTest {

    @Test
    public void testMinimizeComputationCalls() {
        var clusters = List.of(List.of("A", "B", "C"), List.of("D"), List.of("E", "F"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new Cache());
        var expectCalls = 0;

        // WE know: nothing
        // No records in DB so we will investigate A + B and immediately return
        expectCalls += 1;
        var call1 = finder.findKnownSimilar("A", List.of("B", "C", "D", "E", "F"));
        assertThat(call1).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // We know: A == B
        // So no calls to expensive service needed
        expectCalls += 0;
        var call2 = finder.findKnownSimilar("B", List.of("A", "C", "D", "E", "F"));
        assertThat(call2).containsExactlyInAnyOrder(new Investigation("B", "A", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // We know: A == B
        // We will compare C to A and find that C == A and also C == B (because A == B)
        expectCalls += 1;
        var call3 = finder.findKnownSimilar("C", List.of("A", "B", "D", "E", "F"));
        assertThat(call3).containsExactlyInAnyOrder(
                new Investigation("C", "A", RelationType.SIMILAR),
                new Investigation("C", "B", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // We know that A, B, C in the same cluster
        // We will compare D to (A or B or C), no need to compare to all 3, save D != (A or B or C)
        // We will compare D to E, save D != E
        // We will compare D to F, save D != F
        expectCalls += 3;
        var call4 = finder.findKnownSimilar("D", List.of("A", "B", "C", "E", "F"));
        assertThat(call4).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // We know that A, B, C in the same cluster, D != E, D != F
        // We will compare E to (A or B or C), no need to compare to all 3, save E != (A or B or C)
        // We will NOT compare E to D, because we know D != E
        // We will compare E to F, save E == F
        expectCalls += 2;
        var call5 = finder.findKnownSimilar("E", List.of("A", "B", "C", "D", "F"));
        assertThat(call5).containsExactlyInAnyOrder(new Investigation("E", "F", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // We know that (A, B, C) and (E, F) in the same cluster, D != E, D != F
        // We will NOT compare F to E, because we know E = F
        expectCalls += 0;
        var call6 = finder.findKnownSimilar("F", List.of("A", "B", "C", "D", "E"));
        assertThat(call6).containsExactlyInAnyOrder(new Investigation("F", "E", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // We know that (A, B, C) and (E, F) in the same cluster, D != E, D != F
        // We know that A belongs to (A, B, C), E belongs to (E, F), and (A, B, C) != (E, F) see call5. No calls needed.
        expectCalls += 0;
        var call7 = finder.findKnownSimilar("A", List.of("E"));
        assertThat(call7).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
    }

    @Test
    public void testClusterSkipReducesCallsForLargeKnownCluster() {
        // Clusters: [A] and [B, C, D, E] — all 4 are duplicates of each other
        var clusters = List.of(List.of("A"), List.of("B", "C", "D", "E"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new Cache());
        var expectCalls = 0;

        // Build up the B-C-D-E cluster incrementally (3 calls)
        expectCalls += 1;
        finder.findKnownSimilar("B", List.of("C"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        expectCalls += 1;
        finder.findKnownSimilar("B", List.of("D"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        expectCalls += 1;
        finder.findKnownSimilar("B", List.of("E"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // A vs [B,C,D,E]: all 4 are in the same known cluster → findAllInCluster returns all 4,
        // so only 1 comparison is needed to rule out the entire cluster
        expectCalls += 1;
        var r1 = finder.findKnownSimilar("A", List.of("B", "C", "D", "E"));
        assertThat(r1).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // Repeat: now A's cluster is known to be DIFFERENT from B's cluster → 0 calls
        expectCalls += 0;
        var r2 = finder.findKnownSimilar("A", List.of("B", "C", "D", "E"));
        assertThat(r2).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
    }

    @Test
    public void testMergeCorrectlyPreservesDifferentRelationships() {
        // Clusters: [A, B] and [X] — A and X are different, then A and B are merged
        var clusters = List.of(List.of("A", "B"), List.of("X"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new Cache());
        var expectCalls = 0;

        // Establish A != X (1 call)
        expectCalls += 1;
        var r1 = finder.findKnownSimilar("A", List.of("X"));
        assertThat(r1).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // Establish A == B (1 call) — this merges clusters; the DIFFERENT(A, X) pair must survive the merge
        expectCalls += 1;
        var r2 = finder.findKnownSimilar("A", List.of("B"));
        assertThat(r2).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // B != X must be deduced from cache (B is now in A's cluster, A != X) → 0 calls
        expectCalls += 0;
        var r3 = finder.findKnownSimilar("B", List.of("X"));
        assertThat(r3).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // A != X still known after the merge → 0 calls
        expectCalls += 0;
        var r4 = finder.findKnownSimilar("A", List.of("X"));
        assertThat(r4).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
    }

    @Test
    public void testTransitiveClusterMembershipReducesCalls() {
        // Clusters: [A, B, C] — demonstrate that after A==B and C is compared to A,
        // the cluster skip covers B as well (only 1 call for C, not 2)
        var clusters = List.of(List.of("A", "B", "C"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new Cache());
        var expectCalls = 0;

        // Establish A == B (1 call). C is unknown, not yet in cache.
        expectCalls += 1;
        var r1 = finder.findKnownSimilar("A", List.of("B", "C"));
        assertThat(r1).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // C vs [A, B]: A and B are in the same known cluster → findAllInCluster("A",…) returns ["A","B"]
        // So only 1 comparison needed; C==A is found, and C==B is derived without an extra call
        expectCalls += 1;
        var r2 = finder.findKnownSimilar("C", List.of("A", "B"));
        assertThat(r2).containsExactlyInAnyOrder(
                new Investigation("C", "A", RelationType.SIMILAR),
                new Investigation("C", "B", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // All three in the same cluster now — B vs [A, C] requires 0 calls
        expectCalls += 0;
        var r3 = finder.findKnownSimilar("B", List.of("A", "C"));
        assertThat(r3).containsExactlyInAnyOrder(
                new Investigation("B", "A", RelationType.SIMILAR),
                new Investigation("B", "C", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
    }

    @Test
    public void testEarlyReturnWhenSimilarAlreadyKnown() {
        // Once a SIMILAR match is found in cache, unknown targets should NOT trigger expensive calls
        var clusters = List.of(List.of("A", "B"), List.of("C"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new Cache());
        var expectCalls = 0;

        // Establish A == B (1 call)
        expectCalls += 1;
        finder.findKnownSimilar("A", List.of("B"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // A vs [B, C]: B is SIMILAR (known), C is UNKNOWN.
        // Because similar list is non-empty after cache lookup, we return early — C is never compared.
        expectCalls += 0;
        var r = finder.findKnownSimilar("A", List.of("B", "C"));
        assertThat(r).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
    }

    @Test
    public void testDifferentKnowledgeTransfersThroughChainedMerges() {
        // Clusters: [A, B, C] and [X, Y]
        // Tests that after chained merges on both sides, cross-cluster DIFFERENT is inferred with 0 calls
        var clusters = List.of(List.of("A", "B", "C"), List.of("X", "Y"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), new Cache());
        var expectCalls = 0;

        // Establish A != X (1 call)
        expectCalls += 1;
        finder.findKnownSimilar("A", List.of("X"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // Establish X == Y (1 call)
        expectCalls += 1;
        finder.findKnownSimilar("X", List.of("Y"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // Establish A == B (1 call) — merges A and B; DIFFERENT(A_root, X_root) must be preserved
        expectCalls += 1;
        finder.findKnownSimilar("A", List.of("B"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // C joins the A-B cluster (1 call)
        expectCalls += 1;
        var r4 = finder.findKnownSimilar("C", List.of("A", "B"));
        assertThat(r4).containsExactlyInAnyOrder(
                new Investigation("C", "A", RelationType.SIMILAR),
                new Investigation("C", "B", RelationType.SIMILAR)
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // B != X — B is in A's cluster, A != X → deduced with 0 calls
        expectCalls += 0;
        var r5 = finder.findKnownSimilar("B", List.of("X"));
        assertThat(r5).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        // C != Y — C is in A's cluster, Y is in X's cluster, A_cluster != X_cluster → 0 calls
        expectCalls += 0;
        var r6 = finder.findKnownSimilar("C", List.of("Y"));
        assertThat(r6).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
    }

    static class DuplicateFinder {
        final VideoCompareService videoService;
        final Cache cache;
        DuplicateFinder(VideoCompareService videoService, Cache cache) {
            this.videoService = videoService;
            this.cache = cache;
        }
        List<Investigation> findKnownSimilar(String sourceId, List<String> targetIds) {
            var similar = new ArrayList<Investigation>();
            var unknown = new ArrayList<Investigation>();
            for (Investigation inv: cache.findRelationship(sourceId, targetIds)) {
                if (inv.relation == RelationType.SIMILAR)
                    similar.add(inv);
                else if (inv.relation == RelationType.UNKNOWN)
                    unknown.add(inv);
            }
            if (!similar.isEmpty())
                return similar;
            var skip = new HashSet<String>();
            for (Investigation inv: unknown) {
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
                    for (String id: cluster)
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