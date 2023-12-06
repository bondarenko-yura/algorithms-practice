package com.bondarenko.leetcode.n0000.n000;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class ValidParentheses20Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of("()", true),
				of("()[]{}", true),
				of("(]", false)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String s, boolean ans) {
		var algo = new ValidParentheses20();
		assertThat(algo.isValid(s)).isEqualTo(ans);
	}

}
