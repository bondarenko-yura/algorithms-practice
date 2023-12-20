package com.bondarenko.leetcode.n0000.n600;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class TopKFrequentWords692Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 2,
						List.of("i", "love")),
				of(new String[]{"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"}, 4,
						List.of("the", "is", "sunny", "day"))
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String[] words, int k, List<String> ans) {
		var algo = new TopKFrequentWords692();
		assertThat(algo.topKFrequent(words, k)).isEqualTo(ans);
	}

}
