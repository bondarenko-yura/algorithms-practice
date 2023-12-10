package com.bondarenko.leetcode.n2000.n900;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class DoubleModularExponentiation2961Test {

	private static Stream<Arguments> problemArgs() {
		return Stream.of(
				of(new int[][]{{2, 3, 3, 10}, {3, 3, 3, 1}, {6, 1, 1, 4}}, 2, List.of(0, 2)),
				of(new int[][]{{39, 3, 1000, 1000}}, 17, List.of())
		);
	}

	@ParameterizedTest
	@MethodSource("problemArgs")
	void testProblem(int[][] variables, int target, List<Integer> ans) {
		var algo = new DoubleModularExponentiation2961();
		assertThat(algo.getGoodIndices(variables, target)).isEqualTo(ans);
	}

}
