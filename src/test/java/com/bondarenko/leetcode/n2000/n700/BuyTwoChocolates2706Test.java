package com.bondarenko.leetcode.n2000.n700;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class BuyTwoChocolates2706Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{1, 2, 2}, 3, 0),
				of(new int[]{3, 2, 3}, 3, 3)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] prices, int money, int ans) {
		var algo = new BuyTwoChocolates2706();
		assertThat(algo.buyChoco(prices, money)).isEqualTo(ans);
	}

}
