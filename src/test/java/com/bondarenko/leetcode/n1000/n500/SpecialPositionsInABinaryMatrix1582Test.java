package com.bondarenko.leetcode.n1000.n500;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Array;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class SpecialPositionsInABinaryMatrix1582Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(Array.parse2D("[[0,0,1,0],[0,0,0,0],[0,0,0,0],[0,1,0,0]]"), 2),
				of(Array.parse2D("[[1,0,0],[0,0,1],[1,0,0]]"), 1),
				of(Array.parse2D("[[1,0,0],[0,1,0],[0,0,1]]"), 3)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[][] arr, int ans) {
		var algo = new SpecialPositionsInABinaryMatrix1582();
		assertThat(algo.numSpecial(arr)).isEqualTo(ans);
	}

}
