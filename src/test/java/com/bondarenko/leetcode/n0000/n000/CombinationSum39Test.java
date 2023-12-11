package com.bondarenko.leetcode.n0000.n000;

import java.util.List;
import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Array;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CombinationSum39Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{2, 3, 6, 7}, 7, Array.parse2DList("[[2,2,3],[7]]")),
				of(new int[]{2, 3, 5}, 8, Array.parse2DList("[[2,2,2,2],[2,3,3],[3,5]]")),
				of(new int[]{2}, 1, Array.parse2DList("[]"))
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] candidates, int target, List<List<Integer>> ans) {
		var algo = new CombinationSum39();
		assertThat(algo.combinationSum(candidates, target))
				.containsExactlyInAnyOrderElementsOf(ans);
	}

}
