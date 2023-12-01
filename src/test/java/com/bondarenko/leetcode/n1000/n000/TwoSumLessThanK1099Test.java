package com.bondarenko.leetcode.n1000.n000;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class TwoSumLessThanK1099Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{34, 23, 1, 24, 75, 33, 54, 8}, 60, 58),
				of(new int[]{254, 914, 110, 900, 147, 441, 209, 122, 571, 942, 136, 350, 160, 127, 178, 839,
						201, 386, 462, 45, 735, 467, 153, 415, 875, 282, 204, 534, 639, 994, 284, 320, 865, 468, 1, 838, 275, 370,
						295, 574, 309, 268, 415, 385, 786, 62, 359, 78, 854, 944}, 200, 198),
				of(new int[]{0, 20, 30}, 15, -1)

		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] nums, int k, int ans) {
		var algo = new TwoSumLessThanK1099();
		assertThat(algo.twoSumLessThanK(nums, k)).isEqualTo(ans);
	}

}
