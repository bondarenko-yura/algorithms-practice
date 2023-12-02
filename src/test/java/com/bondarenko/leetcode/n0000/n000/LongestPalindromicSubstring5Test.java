package com.bondarenko.leetcode.n0000.n000;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class LongestPalindromicSubstring5Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of("babad", "bab"),
				of("cbbd", "bb"),
				of("c", "c")
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String s, String ans) {
		var algo = new LongestPalindromicSubstring5();
		assertThat(algo.longestPalindrome(s)).isEqualTo(ans);
	}

	@ParameterizedTest
	@MethodSource("args")
	void testManachersAlgorithm(String s, String ans) {
		var algo = new LongestPalindromicSubstring5();
		assertThat(algo.longestPalindromeManachersAlgorithm(s)).isEqualTo(ans);
	}
}
