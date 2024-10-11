package com.bondarenko.leetcode.n1000.n900;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Ints;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class TheNumberOfTheSmallestUnoccupiedChair1942Test {
	private static Stream<Arguments> args() {
		return Stream.of(
				of(Ints.p2D("[[1,4],[2,3],[4,6]]"), 1, 1),
				of(Ints.p2D("[[3,10],[1,5],[2,6]]"), 0, 2)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[][] times, int targetFriend, int ans) {
		var algo = new TheNumberOfTheSmallestUnoccupiedChair1942();
		assertThat(algo.smallestChair(times, targetFriend)).isEqualTo(ans);
	}
}
