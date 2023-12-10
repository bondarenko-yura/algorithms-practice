package com.bondarenko.leetcode.n2000.n900;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CountTestedDevicesAfterTestOperations2960Test {

	private static Stream<Arguments> problemArgs() {
		return Stream.of(
				of(new int[]{1, 1, 2, 1, 3}, 3),
				of(new int[]{0, 1, 2}, 2)
		);
	}

	@ParameterizedTest
	@MethodSource("problemArgs")
	void testProblem(int[] in, int ans) {
		var algo = new CountTestedDevicesAfterTestOperations2960();
		assertThat(algo.countTestedDevices(in)).isEqualTo(ans);
	}

}
