package com.bondarenko.leetcode.n2000.n100;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.bondarenko.leetcode.ds.Ints.p1D;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class SortTheJumbledNumbers2191Test {
	private static Stream<Arguments> args() {
		return Stream.of(
				of(p1D("[8,9,4,0,2,1,3,5,7,6]"), p1D("[991,338,38]"), p1D("[338,38,991]")),
				of(p1D("[0,1,2,3,4,5,6,7,8,9]"), p1D("[789,456,123]"), p1D("[123,456,789]")),
				of(p1D("[9,8,7,6,5,4,3,2,1,0]"), p1D("[0,1,2,3,4,5,6,7,8,9]"), p1D("[9,8,7,6,5,4,3,2,1,0]")),
				of(p1D("[5,6,8,7,4,0,3,1,9,2]"), p1D("[7686,97012948,84234023,2212638,99]"), p1D("[99,7686,2212638,97012948,84234023]"))
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int[] mapping, int[] nums, int[]ans) {
		var algo = new SortTheJumbledNumbers2191();
		assertThat(algo.sortJumbled(mapping, nums)).isEqualTo(ans);
	}
}
