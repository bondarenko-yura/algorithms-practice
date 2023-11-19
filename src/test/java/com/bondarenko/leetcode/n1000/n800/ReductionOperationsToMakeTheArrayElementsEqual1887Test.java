package com.bondarenko.leetcode.n1000.n800;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class ReductionOperationsToMakeTheArrayElementsEqual1887Test {
	private final ReductionOperationsToMakeTheArrayElementsEqual1887 algo = new ReductionOperationsToMakeTheArrayElementsEqual1887();

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{5,1,3}, 3),
				of(new int[]{1,1,1}, 0),
				of(new int[]{1,1,2,2,3}, 4)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] arr, int ans) {
		assertThat(algo.reductionOperations(arr)).isEqualTo(ans);
	}
}
