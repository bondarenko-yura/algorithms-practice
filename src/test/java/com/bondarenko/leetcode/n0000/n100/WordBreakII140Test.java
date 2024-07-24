package com.bondarenko.leetcode.n0000.n100;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class WordBreakII140Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of("catsanddog", List.of("cat","cats","and","sand","dog"), List.of("cats and dog","cat sand dog")),
				of("pineapplepenapple", List.of("apple","pen","applepen","pine","pineapple"), List.of("pine apple pen apple","pineapple pen apple","pine applepen apple")),
				of("catsandog", List.of("cats","dog","sand","and","cat"), List.of())
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String s, List<String> wordDict, List<String> ans) {
		var algo = new WordBreakII140();
		assertThat(algo.wordBreak(s, wordDict)).isEqualTo(ans);
	}
}
