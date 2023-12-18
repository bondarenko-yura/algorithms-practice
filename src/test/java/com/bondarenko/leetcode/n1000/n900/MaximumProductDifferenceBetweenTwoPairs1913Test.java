package com.bondarenko.leetcode.n1000.n900;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MaximumProductDifferenceBetweenTwoPairs1913Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{5, 6, 2, 7, 4}, 34),
				of(new int[]{4, 2, 5, 9, 7, 4, 8}, 64)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] nums, int ans) {
		var algo = new MaximumProductDifferenceBetweenTwoPairs1913();
		assertThat(algo.maxProductDifference(nums)).isEqualTo(ans);
	}

}
