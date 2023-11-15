package com.bondarenko.leetcode.n1000.n800;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MaximumElementAfterDecreasingAndRearranging1846Test {

	private final MaximumElementAfterDecreasingAndRearranging1846 algo = new MaximumElementAfterDecreasingAndRearranging1846();

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{2, 2, 1, 2, 1}, 2),
				of(new int[]{100, 1, 1000}, 3),
				of(new int[]{1, 2, 3, 4, 5}, 5)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] arr, int ans) {
		assertThat(algo.maximumElementAfterDecrementingAndRearranging(arr)).isEqualTo(ans);
	}

}
