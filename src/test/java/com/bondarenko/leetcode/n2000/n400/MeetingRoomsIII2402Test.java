package com.bondarenko.leetcode.n2000.n400;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Ints;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MeetingRoomsIII2402Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(2, Ints.p2D("[[0,10],[1,5],[2,7],[3,4]]"), 0),
				of(3, Ints.p2D("[[1,20],[2,10],[3,5],[4,9],[6,8]]"), 1)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(int n, int[][] meetings, int ans) {
		var algo = new MeetingRoomsIII2402();
		assertThat(algo.mostBooked(n, meetings)).isEqualTo(ans);
	}

}
