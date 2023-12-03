package com.bondarenko.leetcode.contest;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class ContestTemplateP3Test {

	private static Stream<Arguments> problemArgs() {
		return Stream.of(
				of(0, 0),
				of(1, 0)
		);
	}

	@ParameterizedTest
	@MethodSource("problemArgs")
	void testProblem(int in, int ans) {
		var algo = new ContestTemplateP3();
		assertThat(algo.problem(in)).isEqualTo(ans);
	}

}
