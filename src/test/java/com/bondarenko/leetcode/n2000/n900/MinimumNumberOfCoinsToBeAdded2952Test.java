package com.bondarenko.leetcode.n2000.n900;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MinimumNumberOfCoinsToBeAdded2952Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{1, 4, 10}, 19, 2),
				of(new int[]{1, 4, 10, 5, 7, 19}, 19, 1),
				of(new int[]{1, 1, 1}, 20, 3),
				of(new int[]{10, 1, 10}, 10, 3)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] coins, int target, int ans) {
		var algo = new MinimumNumberOfCoinsToBeAdded2952();
		assertThat(algo.minimumAddedCoins(coins, target)).isEqualTo(ans);
	}

}
