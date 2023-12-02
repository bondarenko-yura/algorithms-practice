package com.bondarenko.leetcode.n1000.n100;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class FindWordsThatCanBeFormedByCharacters1160Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new String[]{"cat", "bt", "hat", "tree"}, "atach", 6),
				of(new String[]{"hello", "world", "leetcode"}, "welldonehoneyr", 10)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String[] words, String chars, int ans) {
		var algo = new FindWordsThatCanBeFormedByCharacters1160();
		assertThat(algo.countCharacters(words, chars)).isEqualTo(ans);
	}

}
