package com.bondarenko.leetcode.n1000.n700;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CalculateMoneyInLeetcodeBank1716Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(175, 2800),
				of(4, 10),
				of(10, 37),
				of(20, 96)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int n, int ans) {
		var algo = new CalculateMoneyInLeetcodeBank1716();
		assertThat(algo.totalMoney(n)).isEqualTo(ans);
	}

}
