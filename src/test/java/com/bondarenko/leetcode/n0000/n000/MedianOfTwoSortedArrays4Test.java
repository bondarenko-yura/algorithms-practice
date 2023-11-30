package com.bondarenko.leetcode.n0000.n000;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MedianOfTwoSortedArrays4Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{1, 3}, new int[]{2}, 2),
				of(new int[]{1, 2}, new int[]{3, 4}, 2.5),
				of(new int[]{}, new int[]{1}, 1)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] a, int[] b, double ans) {
		var algo = new MedianOfTwoSortedArrays4();
		assertThat(algo.findMedianSortedArrays(a, b)).isEqualTo(ans);
	}

}
