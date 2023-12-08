package com.bondarenko.leetcode.n1000.n100;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.TreeNode;

import org.assertj.core.data.Offset;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MaximumAverageSubtree1120Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(TreeNode.of(5, 6, 1), 6),
				of(TreeNode.of(0, null, 1), 1)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(TreeNode root, double ans) {
		var algo = new MaximumAverageSubtree1120();
		assertThat(algo.maximumAverageSubtree(root)).isEqualTo(ans, Offset.offset(Math.pow(10, -5)));
	}

}
