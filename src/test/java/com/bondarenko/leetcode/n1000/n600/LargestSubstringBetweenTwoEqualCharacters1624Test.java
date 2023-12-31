package com.bondarenko.leetcode.n1000.n600;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class LargestSubstringBetweenTwoEqualCharacters1624Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of("aa", 0),
				of("abca", 2),
				of("cbzxy", -1)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String s, int ans) {
		var algo = new LargestSubstringBetweenTwoEqualCharacters1624();
		assertThat(algo.maxLengthBetweenEqualCharacters(s)).isEqualTo(ans);
	}

}
