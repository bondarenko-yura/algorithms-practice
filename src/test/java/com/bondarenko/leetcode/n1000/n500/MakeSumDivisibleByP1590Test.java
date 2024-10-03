package com.bondarenko.leetcode.n1000.n500;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MakeSumDivisibleByP1590Test {
	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{3,1,4,2}, 6, 1),
				of(new int[]{6,3,5,2}, 9, 2),
				of(new int[]{1,2,3}, 3, 0),
				of(new int[]{1000000000,1000000000,1000000000}, 3, 0)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] nums, int p, int ans) {
		var algo = new MakeSumDivisibleByP1590();
		assertThat(algo.minSubarray(nums, p)).isEqualTo(ans);
	}

}
