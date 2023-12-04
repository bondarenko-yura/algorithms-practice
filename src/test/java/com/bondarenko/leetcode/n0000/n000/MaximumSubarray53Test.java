package com.bondarenko.leetcode.n0000.n000;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MaximumSubarray53Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}, 6),
				of(new int[]{1}, 1),
				of(new int[]{5, 4, -1, 7, 8}, 23)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] nums, int ans) {
		var algo = new MaximumSubarray53();
		assertThat(algo.maxSubArray(nums)).isEqualTo(ans);
	}

}
