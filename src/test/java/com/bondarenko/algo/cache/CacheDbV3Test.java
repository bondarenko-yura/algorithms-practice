package com.bondarenko.algo.cache;

import com.google.cloud.spanner.DatabaseClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Functional tests for CacheDbV3 (Spanner-backed).
 *
 * To enable: wire a real DatabaseClient pointing at a Spanner instance with
 * spanner_schema_graph.sql applied, assign it to the {@code db} field, and
 * remove @Disabled.
 *
 * Key V3 behaviour not present in V2:
 *   Transitive SIMILAR relationships carry a *chained* reason built from
 *   all edge reasons along the shortest path, joined by " -> ".
 *   e.g. A-B stored with reason "A-B", C-A stored with reason "C-A" →
 *        findRelationship("C", ["B"]) returns reason "C-A -> A-B".
 */
@Disabled("Requires a live Spanner instance — remove @Disabled to run")
class CacheDbV3Test {

    // Provide a real DatabaseClient before enabling the tests.
    DatabaseClient db = null;

    CacheDbV3 cache;

    @BeforeEach
    void setUp() {
        cache = new CacheDbV3(db);
    }

    // ── Functional parity with V2 ─────────────────────────────────────────────

    @Test
    void testDirectSimilarRelationshipIsFound() {
        var clusters = List.of(List.of("A", "B"), List.of("C"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), cache);

        var result = finder.findKnownSimilar("A", List.of("B", "C"));

        assertThat(result).containsExactlyInAnyOrder(
                new Investigation("A", "B", RelationType.SIMILAR, "A-B")
        );
        assertThat(finder.videoService.count).isEqualTo(1);
    }

    @Test
    void testCachedResultAvoidesRecomputation() {
        var clusters = List.of(List.of("A", "B"), List.of("C"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), cache);

        finder.findKnownSimilar("A", List.of("B"));                        // 1 compute call
        var result = finder.findKnownSimilar("B", List.of("A", "C"));      // 0 compute calls

        assertThat(result).containsExactlyInAnyOrder(
                new Investigation("B", "A", RelationType.SIMILAR, "A-B")
        );
        assertThat(finder.videoService.count).isEqualTo(1);
    }

    @Test
    void testTransitiveChainedReasonIsBuilt() {
        // V3 hallmark: chained reasons are built from edge reasons along the shortest path.
        cache.saveInvestigation(new Investigation("A", "B", RelationType.SIMILAR, "A-B"));
        cache.saveInvestigation(new Investigation("C", "A", RelationType.SIMILAR, "C-A"));

        // C→B: path C→A→B, reasons chained as "C-A -> A-B"
        var result = cache.findRelationship("C", List.of("B"));
        assertThat(result).containsExactlyInAnyOrder(
                new Investigation("C", "B", RelationType.SIMILAR, "C-A -> A-B")
        );
    }

    @Test
    void testFullClusterDiscoveryWithChainedReasons() {
        var clusters = List.of(List.of("A", "B", "C"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), cache);

        finder.findKnownSimilar("A", List.of("B", "C"));   // stores A-B, reason "A-B"
        finder.findKnownSimilar("C", List.of("A", "B"));   // stores C-A, reason "C-A"
        var result = finder.findKnownSimilar("B", List.of("A", "C"));

        // B→A: direct stored edge "A-B" (same reason, bidirectional)
        // B→C: path B→A→C, chained "A-B -> C-A"
        assertThat(result).containsExactlyInAnyOrder(
                new Investigation("B", "A", RelationType.SIMILAR, "A-B"),
                new Investigation("B", "C", RelationType.SIMILAR, "A-B -> C-A")
        );
    }

    @Test
    void testDifferentRelationshipSkipsWholeCluster() {
        var clusters = List.of(List.of("A"), List.of("B", "C", "D", "E"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), cache);

        finder.findKnownSimilar("B", List.of("C"));
        finder.findKnownSimilar("B", List.of("D"));
        finder.findKnownSimilar("B", List.of("E"));

        // A is different from the B-C-D-E cluster; only one compute call needed
        var result = finder.findKnownSimilar("A", List.of("B", "C", "D", "E"));
        assertThat(result).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(4); // 3 B-* + 1 A-B
    }

    @Test
    void testDifferentRelationshipTransfersAcrossMerges() {
        // A-X: different. X-Y: similar. A-B: similar. C queries B and X.
        var clusters = List.of(List.of("A", "B", "C"), List.of("X", "Y"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), cache);

        finder.findKnownSimilar("A", List.of("X"));   // different: A ≠ X
        finder.findKnownSimilar("X", List.of("Y"));   // similar: X = Y
        finder.findKnownSimilar("A", List.of("B"));   // similar: A = B
        finder.findKnownSimilar("C", List.of("A", "B"));  // similar: C = A, transitive C ~ B

        // B is in A's cluster; X is in a different cluster from A.
        // Transitively B is also different from X and Y.
        assertThat(finder.findKnownSimilar("B", List.of("X"))).isEmpty();
        assertThat(finder.findKnownSimilar("C", List.of("Y"))).isEmpty();
    }

    @Test
    void testReasonIsPreservedOnReverseQuery() {
        cache.saveInvestigation(new Investigation("A", "B", RelationType.SIMILAR, "A-B"));

        // Reverse query: B asking about A — should return the same stored reason.
        var result = cache.findRelationship("B", List.of("A"));
        assertThat(result).containsExactlyInAnyOrder(
                new Investigation("B", "A", RelationType.SIMILAR, "A-B")
        );
    }

    @Test
    void testDifferentReasonIsReturnedOnQuery() {
        cache.saveInvestigation(new Investigation("A", "B", RelationType.DIFFERENT, "A-B"));

        var result = cache.findRelationship("A", List.of("B"));
        assertThat(result).containsExactlyInAnyOrder(
                new Investigation("A", "B", RelationType.DIFFERENT, "A-B")
        );
    }

    @Test
    void testDifferentReasonChainedAcrossClusterBoundary() {
        // A-B similar, X-Y similar, A-X different.
        // Querying B vs Y: path B→A -[different]→ X→Y, chained reason "A-B -> A-X -> X-Y".
        cache.saveInvestigation(new Investigation("A", "B", RelationType.SIMILAR, "A-B"));
        cache.saveInvestigation(new Investigation("X", "Y", RelationType.SIMILAR, "X-Y"));
        cache.saveInvestigation(new Investigation("A", "X", RelationType.DIFFERENT, "A-X"));

        var result = cache.findRelationship("B", List.of("Y"));
        assertThat(result).containsExactlyInAnyOrder(
                new Investigation("B", "Y", RelationType.DIFFERENT, "A-B -> A-X -> X-Y")
        );
    }

    @Test
    void testMinimizeComputationCalls() {
        // Same clusters and call sequence as CacheDbV2Test.testMinimizeComputationCalls.
        // DB call counts are omitted (Spanner, not H2), but compute call counts must be identical.
        // Unlike V2, transitive cluster members carry chained reasons fetched from the graph.
        var clusters = List.of(List.of("A", "B", "C"), List.of("D"), List.of("E", "F"));
        var finder = new DuplicateFinder(new VideoCompareService(clusters), cache);
        var expectCalls = 0;

        expectCalls += 1;
        var call1 = finder.findKnownSimilar("A", List.of("B", "C", "D", "E", "F"));
        assertThat(call1).containsExactlyInAnyOrder(new Investigation("A", "B", RelationType.SIMILAR, "A-B"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        expectCalls += 0;
        var call2 = finder.findKnownSimilar("B", List.of("A", "C", "D", "E", "F"));
        assertThat(call2).containsExactlyInAnyOrder(new Investigation("B", "A", RelationType.SIMILAR, "A-B"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        expectCalls += 1;
        var call3 = finder.findKnownSimilar("C", List.of("A", "B", "D", "E", "F"));
        assertThat(call3).containsExactlyInAnyOrder(
                new Investigation("C", "A", RelationType.SIMILAR, "C-A"),
                new Investigation("C", "B", RelationType.SIMILAR, "C-A -> A-B")
        );
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        expectCalls += 3;
        var call4 = finder.findKnownSimilar("D", List.of("A", "B", "C", "E", "F"));
        assertThat(call4).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        expectCalls += 2;
        var call5 = finder.findKnownSimilar("E", List.of("A", "B", "C", "D", "F"));
        assertThat(call5).containsExactlyInAnyOrder(new Investigation("E", "F", RelationType.SIMILAR, "E-F"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        expectCalls += 0;
        var call6 = finder.findKnownSimilar("F", List.of("A", "B", "C", "D", "E"));
        assertThat(call6).containsExactlyInAnyOrder(new Investigation("F", "E", RelationType.SIMILAR, "E-F"));
        assertThat(finder.videoService.count).isEqualTo(expectCalls);

        expectCalls += 0;
        var call7 = finder.findKnownSimilar("A", List.of("E"));
        assertThat(call7).isEmpty();
        assertThat(finder.videoService.count).isEqualTo(expectCalls);
    }

    // ── Contradiction guards ──────────────────────────────────────────────────

    @Test
    void testContradictionThrows() {
        cache.saveInvestigation(new Investigation("A", "B", RelationType.SIMILAR));
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "B", RelationType.DIFFERENT)))
                .isInstanceOf(IllegalStateException.class);

        cache.saveInvestigation(new Investigation("B", "C", RelationType.SIMILAR));
        // A and C are transitively similar; marking them different must throw.
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "C", RelationType.DIFFERENT)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void testReverseContradictionThrows() {
        cache.saveInvestigation(new Investigation("A", "B", RelationType.DIFFERENT));
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "B", RelationType.SIMILAR)))
                .isInstanceOf(IllegalStateException.class);

        cache.saveInvestigation(new Investigation("B", "C", RelationType.SIMILAR));
        // A is different from B; B and C are similar → A is different from C.
        // Marking A and C as similar must throw.
        assertThatThrownBy(() -> cache.saveInvestigation(new Investigation("A", "C", RelationType.SIMILAR)))
                .isInstanceOf(IllegalStateException.class);
    }

    // ── Test helpers (mirrors CacheDbV2Test) ─────────────────────────────────

    static class DuplicateFinder {
        final VideoCompareService videoService;
        final CacheDbV3 cache;

        DuplicateFinder(VideoCompareService videoService, CacheDbV3 cache) {
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
                var investigation = new Investigation(sourceId, inv.targetID, relation, sourceId + "-" + inv.targetID);
                this.cache.saveInvestigation(investigation);
                if (same) {
                    similar.add(investigation);
                    var transitiveIds = cluster.stream().filter(id -> !id.equals(inv.targetID)).toList();
                    if (!transitiveIds.isEmpty())
                        similar.addAll(this.cache.findRelationship(sourceId, transitiveIds));
                    break;
                }
            }
            return similar;
        }
    }

    static class VideoCompareService {
        final List<List<String>> stubClusters;
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