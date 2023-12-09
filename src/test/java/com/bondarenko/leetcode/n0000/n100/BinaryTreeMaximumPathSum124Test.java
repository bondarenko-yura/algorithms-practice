package com.bondarenko.leetcode.n0000.n100;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.TreeNode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class BinaryTreeMaximumPathSum124Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(TreeNode.of(1, 2, 3), 6),
				of(TreeNode.of(-10, 9, 20, null, null, 15, 7), 42)
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(TreeNode root, int ans) {
		var algo = new BinaryTreeMaximumPathSum124();
		assertThat(algo.maxPathSum(root)).isEqualTo(ans);
	}

}
