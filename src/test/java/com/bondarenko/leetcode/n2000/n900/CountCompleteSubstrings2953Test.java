package com.bondarenko.leetcode.n2000.n900;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CountCompleteSubstrings2953Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of("igigee", 2, 3),
				of("aaabbbccc", 3, 6),
				of("gvgvvgv", 2, 1),
				of("aaa", 1, 3),
				of("ba", 1, 3),
				of("abb", 1, 4),
				of("baa", 2, 1),
				of("aaabbbccc", 3, 6)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String word, int k, int ans) {
		var algo = new CountCompleteSubstrings2953();
		assertThat(algo.countCompleteSubstrings(word, k)).isEqualTo(ans);
	}

}
