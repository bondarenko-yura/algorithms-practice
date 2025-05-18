package com.bondarenko.leetcode.n1000.n900;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class PaintingAGridWithThreeDifferentColors1931Test {
	private static Stream<Arguments> args() {
		return Stream.of(
				of(1, 1, 3),
				of(1, 2, 6),
				of(2, 2, 18),
				of(3, 3, 246),
				of(5, 5, 580986)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int m, int n, int ans) {
		var algo = new PaintingAGridWithThreeDifferentColors1931();
		assertThat(algo.colorTheGrid(m, n)).isEqualTo(ans);
	}
}
