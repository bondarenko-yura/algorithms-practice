package com.bondarenko.leetcode.n0000.n800;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class BusRoutes815Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[][]{{1,2,7},{3,6,7}}, 1, 6, 2),
				of(new int[][]{{7,12},{4,5,15},{6},{15,19},{9,12,13}}, 15, 12, -1)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[][] routes, int source, int target, int ans) {
		var algo = new BusRoutes815();
		assertThat(algo.numBusesToDestination(routes, source, target)).isEqualTo(ans);
	}

}
