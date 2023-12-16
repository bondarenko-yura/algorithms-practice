package com.bondarenko.leetcode.n1000.n200;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class ValidAnagram242Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of("anagram", "nagaram", true),
				of("rat", "car", false)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String s, String t, boolean ans) {
		var algo = new ValidAnagram242();
		assertThat(algo.isAnagram(s, t)).isEqualTo(ans);
	}

}
