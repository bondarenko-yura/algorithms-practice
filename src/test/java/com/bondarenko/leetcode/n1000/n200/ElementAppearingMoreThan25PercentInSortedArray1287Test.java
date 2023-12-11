package com.bondarenko.leetcode.n1000.n200;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class ElementAppearingMoreThan25PercentInSortedArray1287Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{1}, 1),
				of(new int[]{15, 15, 21, 21, 32, 32, 33, 33, 33, 34, 35}, 33),
				of(new int[]{1, 2, 2, 6, 6, 6, 6, 7, 10}, 6),
				of(new int[]{1, 1}, 1)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] arr, int ans) {
		var algo = new ElementAppearingMoreThan25PercentInSortedArray1287();
		assertThat(algo.findSpecialInteger(arr)).isEqualTo(ans);
	}

}
