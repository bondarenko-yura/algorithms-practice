package com.bondarenko.leetcode.n1000.n200;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Array;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class BestMeetingPoint296Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(Array.parse2D("[[1,0,0,0,1],[0,0,0,0,0],[0,0,1,0,0]]"), 6),
				of(Array.parse2D("[[1,1]]"), 1)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[][] grid, int ans) {
		var algo = new BestMeetingPoint296();
		assertThat(algo.minTotalDistance(grid)).isEqualTo(ans);
	}

}
