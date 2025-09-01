package com.bondarenko.leetcode.n1000.n700;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Ints;

import org.assertj.core.data.Offset;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MaximumAveragePassRatio1792Test {
	private static Stream<Arguments> args() {
		return Stream.of(
				of(Ints.p2D("[[1,2],[3,5],[2,2]]"), 2, 0.78333),
				of(Ints.p2D("[[2,4],[3,9],[4,5],[2,10]]"), 4, 0.53485)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[][] classes, int extraStudents, double ans) {
		var algo = new MaximumAveragePassRatio1792();
		assertThat(algo.maxAverageRatio(classes, extraStudents))
				.isEqualTo(ans, Offset.offset(Math.pow(10, -5)));
	}
}
