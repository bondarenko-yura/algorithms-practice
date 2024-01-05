package com.bondarenko.leetcode.n2000.n800;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MinimumNumberOfOperationsToMakeArrayEmpty2870Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{2, 3, 3, 2, 2, 4, 2, 3, 4}, 4),
				of(new int[]{2, 1, 2, 2, 3, 3}, -1)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] nums, int ans) {
		var algo = new MinimumNumberOfOperationsToMakeArrayEmpty2870();
		assertThat(algo.minOperations(nums)).isEqualTo(ans);
	}

}
