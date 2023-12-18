package com.bondarenko.leetcode.n0000.n800;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Ints;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class TransposeMatrix867Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(Ints.p2D("[[1,2,3],[4,5,6],[7,8,9]]"), Ints.p2D("[[1,4,7],[2,5,8],[3,6,9]]")),
				of(Ints.p2D("[[1,2,3],[4,5,6]]"), Ints.p2D("[[1,4],[2,5],[3,6]]"))
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[][] m, int[][] ans) {
		var algo = new TransposeMatrix867();
		assertThat(algo.transpose(m)).isEqualTo(ans);
	}

}
