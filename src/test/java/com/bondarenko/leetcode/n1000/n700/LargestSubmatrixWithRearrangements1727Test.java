package com.bondarenko.leetcode.n1000.n700;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class LargestSubmatrixWithRearrangements1727Test {

	private final LargestSubmatrixWithRearrangements1727 algo = new LargestSubmatrixWithRearrangements1727();

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[][]{{0, 0, 1}, {1, 1, 1}, {1, 0, 1}}, 4),
				of(new int[][]{{1, 1, 0}, {1, 0, 1}}, 2),
				of(new int[][]{{1, 0, 1, 0, 1}}, 3)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[][] arr, int ans) {
		assertThat(algo.largestSubmatrix(arr)).isEqualTo(ans);
	}

}
