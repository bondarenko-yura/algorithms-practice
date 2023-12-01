package com.bondarenko.leetcode.n0000.n000;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class TrappingRainWater42Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}, 6),
				of(new int[]{4, 2, 0, 3, 2, 5}, 9)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] height, int ans) {
		var algo = new TrappingRainWater42();
		assertThat(algo.trap(height)).isEqualTo(ans);
	}

}
