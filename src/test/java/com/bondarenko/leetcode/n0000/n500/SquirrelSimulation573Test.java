package com.bondarenko.leetcode.n0000.n500;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class SquirrelSimulation573Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(5, 7, new int[]{2, 2}, new int[]{4, 4}, new int[][]{{3, 0}, {2, 5}}, 12),
				of(1, 3, new int[]{0, 1}, new int[]{0, 0}, new int[][]{{0, 2}}, 3),
				of(5, 5, new int[]{3, 2}, new int[]{0, 1}, new int[][]{{2, 0}, {4, 1}, {0, 4}, {1, 3}, {1, 0}, {3, 4}, {3, 0}, {2, 3}, {0, 2}, {0, 0}, {2, 2}, {4, 2}, {3, 3}, {4, 4}, {4, 0}, {4, 3}, {3, 1}, {2, 1}, {1, 4}, {2, 4}}, 100)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int height, int width, int[] tree, int[] squirrel, int[][] nuts, int ans) {
		var algo = new SquirrelSimulation573();
		assertThat(algo.minDistance(height, width, tree, squirrel, nuts)).isEqualTo(ans);
	}

}
