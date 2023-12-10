package com.bondarenko.leetcode.n0000.n000;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class WordSearch79Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new char[][]{{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}}, "ABCCED", true),
				of(new char[][]{{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}}, "SEE", true),
				of(new char[][]{{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}}, "ABCB", false)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(char[][] board, String word, boolean ans) {
		var algo = new WordSearch79();
		assertThat(algo.exist(board, word)).isEqualTo(ans);
	}

}
