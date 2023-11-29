package com.bondarenko.leetcode.n1000.n400;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class DiagonalTraverseII1424Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9)),
						new int[]{1, 4, 2, 7, 5, 3, 8, 6, 9}),
				of(List.of(List.of(1, 2, 3, 4, 5), List.of(6, 7), List.of(8), List.of(9, 10, 11), List.of(12, 13, 14, 15, 16)),
						new int[]{1, 6, 2, 8, 7, 3, 9, 4, 12, 10, 5, 13, 11, 14, 15, 16})
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(List<List<Integer>> nums, int[] ans) {
		var algo = new DiagonalTraverseII1424();
		assertThat(algo.findDiagonalOrder(nums)).isEqualTo(ans);
	}

}
