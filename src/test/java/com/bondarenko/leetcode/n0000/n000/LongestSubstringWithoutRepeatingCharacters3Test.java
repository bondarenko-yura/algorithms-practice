package com.bondarenko.leetcode.n0000.n000;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class LongestSubstringWithoutRepeatingCharacters3Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of("abcabcbb", 3),
				of("bbbbb", 1),
				of("pwwkew", 3)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String s, int ans) {
		var algo = new LongestSubstringWithoutRepeatingCharacters3();
		assertThat(algo.lengthOfLongestSubstring(s)).isEqualTo(ans);
	}

}
