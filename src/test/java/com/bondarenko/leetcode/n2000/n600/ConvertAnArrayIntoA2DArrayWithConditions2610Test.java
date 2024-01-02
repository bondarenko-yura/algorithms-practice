package com.bondarenko.leetcode.n2000.n600;

import java.util.List;
import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Ints;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class ConvertAnArrayIntoA2DArrayWithConditions2610Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{1, 3, 4, 1, 2, 3, 1}, Ints.p2DList("[[1, 3, 4, 2], [1, 3], [1]]")),
				of(new int[]{1, 2, 3, 4}, Ints.p2DList("[[1, 2, 3, 4]]"))
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] nums, List<List<Integer>> ans) {
		var algo = new ConvertAnArrayIntoA2DArrayWithConditions2610();
		assertThat(algo.findMatrix(nums)).isEqualTo(ans);
	}

}
