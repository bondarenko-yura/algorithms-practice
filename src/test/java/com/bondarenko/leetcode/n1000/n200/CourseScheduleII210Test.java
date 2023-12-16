package com.bondarenko.leetcode.n1000.n200;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.bondarenko.leetcode.ds.Array.p1D;
import static com.bondarenko.leetcode.ds.Array.p2D;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CourseScheduleII210Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(2, p2D("[[1,0]]"), p1D("[0,1]")),
				of(4, p2D("[[1,0],[2,0],[3,1],[3,2]]"), p1D("[0,1,2,3]")),
				of(1, p2D("[]"), p1D("[0]"))
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int numCourses, int[][] prerequisites, int[] ans) {
		var algo = new CourseScheduleII210();
		assertThat(algo.findOrder(numCourses, prerequisites)).isEqualTo(ans);
	}

}
