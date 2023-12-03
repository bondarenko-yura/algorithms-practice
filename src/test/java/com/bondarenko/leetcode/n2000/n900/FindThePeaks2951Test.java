package com.bondarenko.leetcode.n2000.n900;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class FindThePeaks2951Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{2, 4, 4}, List.of()),
				of(new int[]{1, 4, 3, 8, 5}, List.of(1, 3))
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] peaks, List<Integer> ans) {
		var algo = new FindThePeaks2951();
		assertThat(algo.findPeaks(peaks)).isEqualTo(ans);
	}

}
