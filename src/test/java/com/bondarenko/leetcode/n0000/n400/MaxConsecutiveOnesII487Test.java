package com.bondarenko.leetcode.n0000.n400;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MaxConsecutiveOnesII487Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{1, 0, 1, 1, 0}, 4),
				of(new int[]{1, 0, 1, 1, 0, 1}, 4)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] nums, int ans) {
		var algo = new MaxConsecutiveOnesII487();
		assertThat(algo.findMaxConsecutiveOnes(nums)).isEqualTo(ans);
	}

}
