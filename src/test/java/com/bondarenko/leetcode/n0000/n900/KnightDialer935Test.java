package com.bondarenko.leetcode.n0000.n900;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class KnightDialer935Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(1, 10),
				of(2, 20),
				of(3131, 136006598)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int n, int ans) {
		var algo = new KnightDialer935();
		assertThat(algo.knightDialer(n)).isEqualTo(ans);
	}

}
