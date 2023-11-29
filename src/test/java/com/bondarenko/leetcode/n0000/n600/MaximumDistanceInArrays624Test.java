package com.bondarenko.leetcode.n0000.n600;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MaximumDistanceInArrays624Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(List.of(List.of(1, 2, 3), List.of(4, 5), List.of(1, 2, 3)), 4),
				of(List.of(List.of(1), List.of(1)), 0)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(List<List<Integer>> arr, int ans) {
		var algo = new MaximumDistanceInArrays624();
		assertThat(algo.maxDistance(arr)).isEqualTo(ans);
	}

}
