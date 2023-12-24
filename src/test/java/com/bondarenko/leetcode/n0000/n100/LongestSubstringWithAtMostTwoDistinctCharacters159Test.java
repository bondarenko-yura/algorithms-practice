package com.bondarenko.leetcode.n0000.n100;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class LongestSubstringWithAtMostTwoDistinctCharacters159Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of("eceba", 3),
				of("ccaabbb", 5),
				of(
						"abZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZYX", 101)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String s, int ans) {
		var algo = new LongestSubstringWithAtMostTwoDistinctCharacters159();
		assertThat(algo.lengthOfLongestSubstringTwoDistinct(s)).isEqualTo(ans);
	}

}
