package com.bondarenko.leetcode.n2000.n900;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CountSubarraysWhereMaxElementAppearsAtLeastKTimes2962Test {

	private static Stream<Arguments> problemArgs() {
		return Stream.of(
				of(new int[]{1, 3, 2, 3, 3}, 2, 6),
				of(new int[]{1, 3, 2, 3, 3, 1, 1}, 2, 14),
				of(new int[]{1, 4, 2, 1}, 3, 0),
				of(new int[]{61, 23, 38, 23, 56, 40, 82, 56, 82, 82, 82, 70, 8, 69, 8, 7, 19, 14, 58, 42, 82, 10, 82, 78, 15, 82}, 2, 224)
		);
	}

	@ParameterizedTest
	@MethodSource("problemArgs")
	void testProblem(int[] nums, int k, int ans) {
		var algo = new CountSubarraysWhereMaxElementAppearsAtLeastKTimes2962();
		assertThat(algo.countSubarrays(nums, k)).isEqualTo(ans);
	}

}
