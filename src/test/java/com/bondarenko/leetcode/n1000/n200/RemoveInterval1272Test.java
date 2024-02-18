package com.bondarenko.leetcode.n1000.n200;

import java.util.List;
import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Ints;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class RemoveInterval1272Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(Ints.p2D("[[0,5]]"), Ints.p1D("[2,3]"), Ints.p2DList("[[0,2],[3,5]]")),
				of(Ints.p2D("[[-5,-4],[-3,-2],[1,2],[3,5],[8,9]]"), Ints.p1D("[-1,4]"), Ints.p2DList("[[-5,-4],[-3,-2],[4,5],[8,9]]"))
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[][] intervals, int[] toBeRemoved, List<List<Integer>> ans) {
		var algo = new RemoveInterval1272();
		assertThat(algo.removeInterval(intervals, toBeRemoved)).isEqualTo(ans);
	}

}
