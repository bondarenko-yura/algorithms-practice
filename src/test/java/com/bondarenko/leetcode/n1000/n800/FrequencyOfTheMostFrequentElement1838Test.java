package com.bondarenko.leetcode.n1000.n800;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class FrequencyOfTheMostFrequentElement1838Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{1,2,4}, 5, 3),
				of(new int[]{1,4,8,13}, 5, 2),
				of(new int[]{3,9,6}, 2, 1)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] arr, int k, int ans) {
		var algo = new FrequencyOfTheMostFrequentElement1838();
		assertThat(algo.maxFrequency(arr, k)).isEqualTo(ans);
	}
}
