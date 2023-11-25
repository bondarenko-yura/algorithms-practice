package com.bondarenko.leetcode.n1000.n500;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MaximumNumberOfCoinsYouCanGet1561Test {

	private final MaximumNumberOfCoinsYouCanGet1561 algo = new MaximumNumberOfCoinsYouCanGet1561();

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new int[]{2, 4, 1, 2, 7, 8}, 9),
				of(new int[]{2, 4, 5}, 4),
				of(new int[]{9, 8, 7, 6, 5, 1, 2, 3, 4}, 18)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] arr, int ans) {
		assertThat(algo.maxCoins(arr)).isEqualTo(ans);
	}

}
