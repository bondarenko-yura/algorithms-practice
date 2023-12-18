package com.bondarenko.leetcode.n1000.n200;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Ints;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class ShortestPathInAGridWithObstaclesElimination1293Test {
	private static Stream<Arguments> args() {
		return Stream.of(
				of(Ints.p2D("[[0,0,0],[1,1,0],[0,0,0],[0,1,1],[0,0,0]]"), 1, 6),
				of(Ints.p2D("[[0,1,1],[1,1,1],[1,0,0]]"), 1, -1),
				of(Ints.p2D("[[0]]"), 1, 0)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[][] grid, int k, int ans) {
		var algo = new ShortestPathInAGridWithObstaclesElimination1293();
		assertThat(algo.shortestPath(grid, k)).isEqualTo(ans);
	}
}
