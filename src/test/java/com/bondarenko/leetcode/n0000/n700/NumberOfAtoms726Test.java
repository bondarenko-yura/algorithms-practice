package com.bondarenko.leetcode.n0000.n700;

import java.util.List;
import java.util.stream.Stream;

import com.bondarenko.leetcode.n0000.n600.MaximumDistanceInArrays624;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class NumberOfAtoms726Test {
	private static Stream<Arguments> args() {
		return Stream.of(
				of("H2O", "H2O"),
				of("Mg(OH)2", "H2MgO2"),
				of("K4(ON(SO3)2)2", "K4N2O14S4")
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(String formula, String ans) {
		var algo = new NumberOfAtoms726();
		assertThat(algo.countOfAtoms(formula)).isEqualTo(ans);
	}
}
