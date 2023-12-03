package com.bondarenko.leetcode.n1000.n700;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MaximumNumberOfConsecutiveValuesYouCanMake1798Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{1, 3}, 2),
				of(new int[]{1, 1, 1, 4}, 8),
				of(new int[]{1, 4, 10, 3, 1}, 20)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] coins, int ans) {
		var algo = new MaximumNumberOfConsecutiveValuesYouCanMake1798();
		assertThat(algo.getMaximumConsecutive(coins)).isEqualTo(ans);
	}

}
