package com.bondarenko.leetcode.n2000.n300;

import java.util.stream.Stream;

import com.bondarenko.leetcode.n1000.n900.UniqueLength3PalindromicSubsequences1930;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MinimumAmountOfTimeToCollectGarbage2391Test {
	private final MinimumAmountOfTimeToCollectGarbage2391 algo = new MinimumAmountOfTimeToCollectGarbage2391();

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new String[] {"G","P","GP","GG"}, new int[]{2,4,3}, 21),
				of(new String[] {"MMM","PGM","GP"}, new int[]{3,10}, 37)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String[] garbage, int[] travel, int ans) {
		assertThat(algo.garbageCollection(garbage, travel)).isEqualTo(ans);
	}
}
