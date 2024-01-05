package com.bondarenko.leetcode.n0000.n300;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class LongestIncreasingSubsequence300Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{10, 9, 2, 5, 3, 7, 101, 18}, 4),
				of(new int[]{0, 1, 0, 3, 2, 3}, 4),
				of(new int[]{7, 7, 7, 7, 7, 7, 7}, 1)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] nums, int ans) {
		var algo = new LongestIncreasingSubsequence300();
		assertThat(algo.lengthOfLIS(nums)).isEqualTo(ans);
	}

}
