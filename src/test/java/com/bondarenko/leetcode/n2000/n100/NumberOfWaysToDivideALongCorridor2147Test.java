package com.bondarenko.leetcode.n2000.n100;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class NumberOfWaysToDivideALongCorridor2147Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of("SSPPSPS", 3),
				of("PPSPSP", 1),
				of("S", 0)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String corridor, int ans) {
		var algo = new NumberOfWaysToDivideALongCorridor2147();
		assertThat(algo.numberOfWays(corridor)).isEqualTo(ans);
	}

}
