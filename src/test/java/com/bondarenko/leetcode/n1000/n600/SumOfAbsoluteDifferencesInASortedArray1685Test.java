package com.bondarenko.leetcode.n1000.n600;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class SumOfAbsoluteDifferencesInASortedArray1685Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{2, 3, 5}, new int[]{4, 3, 5}),
				of(new int[]{1, 4, 6, 8, 10}, new int[]{24, 15, 13, 15, 21})
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] arr, int[] ans) {
		var algo = new SumOfAbsoluteDifferencesInASortedArray1685();
		assertThat(algo.getSumAbsoluteDifferences(arr)).isEqualTo(ans);
	}

}
