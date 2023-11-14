package com.bondarenko.leetcode.n1000.n900;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class UniqueLength3PalindromicSubsequences1930Test {

	private final UniqueLength3PalindromicSubsequences1930 algo = new UniqueLength3PalindromicSubsequences1930();

	private static Stream<Arguments> args() {
		return Stream.of(
				of("aabca", 3),
				of("adc", 0),
				of("bbcbaba", 4)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String s, int ans) {
		assertThat(algo.countPalindromicSubsequence(s)).isEqualTo(ans);
	}

}
