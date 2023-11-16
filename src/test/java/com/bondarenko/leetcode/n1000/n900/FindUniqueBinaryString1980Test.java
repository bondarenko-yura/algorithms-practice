package com.bondarenko.leetcode.n1000.n900;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class FindUniqueBinaryString1980Test {

	private final FindUniqueBinaryString1980 algo = new FindUniqueBinaryString1980();

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new String[]{"01", "10"}, 2),
				of(new String[]{"00", "01"}, 2),
				of(new String[]{"111", "011", "001"}, 3),
				of(new String[]{"0"}, 1),
				of(new String[]{"1"}, 1)

		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String[] nums, int size) {
		var ans = algo.findDifferentBinaryString(nums);
		assertThat(ans).hasSize(size);
		assertThat(ans).matches(s -> Pattern.matches("[01]+", s));
		assertThat(nums).doesNotContain(ans);
	}

}
