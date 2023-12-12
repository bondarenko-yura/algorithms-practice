package com.bondarenko.leetcode.n0000.n200;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class IntegerToEnglishWords273Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(123, "One Hundred Twenty Three"),
				of(12345, "Twelve Thousand Three Hundred Forty Five"),
				of(1234567, "One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven"),
				of(0, "Zero")
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int rem, String ans) {
		var algo = new IntegerToEnglishWords273();
		assertThat(algo.numberToWords(rem)).isEqualTo(ans);
	}

}
