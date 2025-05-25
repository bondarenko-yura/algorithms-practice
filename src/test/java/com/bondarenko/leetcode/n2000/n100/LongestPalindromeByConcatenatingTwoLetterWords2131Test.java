package com.bondarenko.leetcode.n2000.n100;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class LongestPalindromeByConcatenatingTwoLetterWords2131Test {
	private static Stream<Arguments> args() {
		return Stream.of(
				of(new String[]{"qo","fo","fq","qf","fo","ff","qq","qf","of","of","oo","of","of","qf","qf","of"}, 14),
				of(new String[]{"lc","cl","gg"}, 6),
				of(new String[]{"ab","ty","yt","lc","cl","ab"}, 8),
				of(new String[]{"cc","ll","xx"}, 2)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String[] words, int ans) {
		var algo = new LongestPalindromeByConcatenatingTwoLetterWords2131();
		assertThat(algo.longestPalindrome(words)).isEqualTo(ans);
	}
}
