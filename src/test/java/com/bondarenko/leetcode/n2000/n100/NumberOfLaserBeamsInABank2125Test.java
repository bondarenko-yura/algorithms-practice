package com.bondarenko.leetcode.n2000.n100;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class NumberOfLaserBeamsInABank2125Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new String[]{"011001", "000000", "010100", "001000"}, 8),
				of(new String[]{"000", "111", "000"}, 0)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String[] bank, int ans) {
		var algo = new NumberOfLaserBeamsInABank2125();
		assertThat(algo.numberOfBeams(bank)).isEqualTo(ans);
	}

}
