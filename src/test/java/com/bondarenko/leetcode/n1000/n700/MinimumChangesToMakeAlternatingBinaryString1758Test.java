package com.bondarenko.leetcode.n1000.n700;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MinimumChangesToMakeAlternatingBinaryString1758Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of("0100", 1),
				of("10", 0),
				of("1111", 2)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String s, int ans) {
		var algo = new MinimumChangesToMakeAlternatingBinaryString1758();
		assertThat(algo.minOperations(s)).isEqualTo(ans);
	}

}
