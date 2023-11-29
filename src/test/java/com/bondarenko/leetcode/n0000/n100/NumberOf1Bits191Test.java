package com.bondarenko.leetcode.n0000.n100;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.lang.Integer.parseUnsignedInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class NumberOf1Bits191Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(parseUnsignedInt("00000000000000000000000000001011", 2), 3),
				of(parseUnsignedInt("00000000000000000000000010000000", 2), 1),
				of(parseUnsignedInt("11111111111111111111111111111101", 2), 31)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int n, int ans) {
		var algo = new NumberOf1Bits191();
		assertThat(algo.hammingWeight(n)).isEqualTo(ans);
	}

	@ParameterizedTest
	@MethodSource("args")
	void testJavaAPI(int n, int ans) {
		var algo = new NumberOf1Bits191();
		assertThat(algo.hammingWeightJavaAPI(n)).isEqualTo(ans);
	}

}
