package com.bondarenko.leetcode.n0000.n000;

import java.util.List;
import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.TreeNode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class BinaryTreeInorderTraversal94Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(TreeNode.of(1, null, 2, 3), List.of(1, 3, 2)),
				of(TreeNode.of(), List.of()),
				of(TreeNode.of(1), List.of(1))
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(TreeNode root, List<Integer> ans) {
		var algo = new BinaryTreeInorderTraversal94();
		assertThat(algo.inorderTraversal(root)).isEqualTo(ans);
	}

	@ParameterizedTest
	@MethodSource("args")
	void testStack(TreeNode root, List<Integer> ans) {
		var algo = new BinaryTreeInorderTraversal94();
		assertThat(algo.inorderTraversalStack(root)).isEqualTo(ans);
	}

}
