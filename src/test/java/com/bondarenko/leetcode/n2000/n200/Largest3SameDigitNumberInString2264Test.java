package com.bondarenko.leetcode.n2000.n200;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class Largest3SameDigitNumberInString2264Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of("6777133339", "777"),
				of("2300019", "000"),
				of("42352338", "")
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String num, String ans) {
		var algo = new Largest3SameDigitNumberInString2264();
		assertThat(algo.largestGoodInteger(num)).isEqualTo(ans);
	}

}
