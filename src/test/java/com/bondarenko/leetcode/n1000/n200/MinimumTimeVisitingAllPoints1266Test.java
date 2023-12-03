package com.bondarenko.leetcode.n1000.n200;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MinimumTimeVisitingAllPoints1266Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[][]{{1, 1}, {3, 4}, {-1, 0}}, 7),
				of(new int[][]{{3, 2}, {-2, 2}}, 5)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[][] points, int ans) {
		var algo = new MinimumTimeVisitingAllPoints1266();
		assertThat(algo.minTimeToVisitAllPoints(points)).isEqualTo(ans);
	}

}
