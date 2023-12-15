package com.bondarenko.leetcode.n1000.n400;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class DestinationCity1436Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(List.of(List.of("London", "New York"), List.of("New York", "Lima"), List.of("Lima", "Sao Paulo")),
						"Sao Paulo"),
				of(List.of(List.of("B", "C"), List.of("D", "B"), List.of("C", "A")), "A"),
				of(List.of(List.of("A", "Z")), "Z")
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(List<List<String>> paths, String ans) {
		var algo = new DestinationCity1436();
		assertThat(algo.destCity(paths)).isEqualTo(ans);
	}

}
