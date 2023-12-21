package com.bondarenko.leetcode.n1000.n600;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Ints;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class WidestVerticalAreaBetweenTwoPointsContainingNoPoints1637Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(Ints.p2D("[[8,7],[9,9],[7,4],[9,7]]"), 1),
				of(Ints.p2D("[[3,1],[9,0],[1,0],[1,4],[5,3],[8,8]]"), 3)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[][] points, int ans) {
		var algo = new WidestVerticalAreaBetweenTwoPointsContainingNoPoints1637();
		assertThat(algo.maxWidthOfVerticalArea(points)).isEqualTo(ans);
	}

}
