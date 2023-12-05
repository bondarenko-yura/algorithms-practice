package com.bondarenko.leetcode.n1000.n600;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CountOfMatchesInTournament1688Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(7, 6),
				of(14, 13)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int n, int ans) {
		var algo = new CountOfMatchesInTournament1688();
		assertThat(algo.numberOfMatches(n)).isEqualTo(ans);
	}

}
