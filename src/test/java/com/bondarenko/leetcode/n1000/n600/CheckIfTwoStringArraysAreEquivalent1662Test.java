package com.bondarenko.leetcode.n1000.n600;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CheckIfTwoStringArraysAreEquivalent1662Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new String[]{"ab", "c"}, new String[]{"a", "bc"}, true),
				of(new String[]{"abc", "d", "defg"}, new String[]{"abcddefg"}, true),
				of(new String[]{"a", "cb"}, new String[]{"ab", "c"}, false)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String[] word1, String[] word2, boolean ans) {
		var algo = new CheckIfTwoStringArraysAreEquivalent1662();
		assertThat(algo.arrayStringsAreEqual(word1, word2)).isEqualTo(ans);
	}

}
