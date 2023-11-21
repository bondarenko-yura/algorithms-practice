package com.bondarenko.leetcode.n1000.n800;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CountNicePairsInAnArray1814Test {

	private final CountNicePairsInAnArray1814 algo = new CountNicePairsInAnArray1814();

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{42, 11, 1, 97}, 2),
				of(new int[]{13, 10, 35, 24, 76}, 4)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] arr, int ans) {
		assertThat(algo.countNicePairs(arr)).isEqualTo(ans);
	}

}
