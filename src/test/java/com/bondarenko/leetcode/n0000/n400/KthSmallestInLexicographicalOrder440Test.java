package com.bondarenko.leetcode.n0000.n400;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class KthSmallestInLexicographicalOrder440Test {
	private static Stream<Arguments> args() {
		return Stream.of(
				of(130, 2, 10),
				of(13, 2, 10),
				of(1, 1, 1)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int n, int k, int ans) {
		var algo = new KthSmallestInLexicographicalOrder440();
		assertThat(algo.findKthNumber(n, k)).isEqualTo(ans);
	}
}
