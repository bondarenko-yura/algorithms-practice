package com.bondarenko.leetcode.n1000.n400;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MaximumProductOfTwoElementsInAnArray1464Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{3, 4, 5, 2}, 12),
				of(new int[]{1, 5, 4, 5}, 16),
				of(new int[]{3, 7}, 12)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] nums, int ans) {
		var algo = new MaximumProductOfTwoElementsInAnArray1464();
		assertThat(algo.maxProduct(nums)).isEqualTo(ans);
	}

}
