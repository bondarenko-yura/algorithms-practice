package com.bondarenko.leetcode.ds;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class ArrayTest {

	private static Stream<Arguments> args1D() {
		return Stream.of(
				of("[1,2,3]", new int[]{1, 2, 3}),
				of(" [ 1 , 2 , 3 ] ", new int[]{1, 2, 3}),
				of("[1]", new int[]{1}),
				of("[]", new int[0]),
				of(" ", null),
				of("", null)
		);
	}

	private static Stream<Arguments> args2D() {
		return Stream.of(
				of("[[1,2,3],[4,5,6],[7,8,9]]", new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
				of("[[1],[2,3],[4,5,6]]", new int[][]{{1}, {2, 3}, {4, 5, 6}}),
				of("[[],[],[]]", new int[][]{{}, {}, {}}),
				of("[[]]", new int[][]{{}}),
				of("[]", new int[][]{})
		);
	}

	@ParameterizedTest
	@MethodSource("args1D")
	void test1D(String in, int[] ans) {
		assertThat(Array.parse1D(in)).isEqualTo(ans);
	}

	@ParameterizedTest
	@MethodSource("args2D")
	void test2D(String in, int[][] ans) {
		assertThat(Array.parse2D(in)).isDeepEqualTo(ans);
	}

}
