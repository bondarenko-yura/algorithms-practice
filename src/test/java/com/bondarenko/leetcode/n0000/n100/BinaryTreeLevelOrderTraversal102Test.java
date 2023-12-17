package com.bondarenko.leetcode.n0000.n100;

import java.util.List;
import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.Array;
import com.bondarenko.leetcode.ds.TreeNode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class BinaryTreeLevelOrderTraversal102Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(TreeNode.of(3, 9, 20, null, null, 15, 7), Array.p2DList("[[3],[9,20],[15,7]]")),
				of(TreeNode.of(1), Array.p2DList("[[1]]")),
				of(TreeNode.of(), Array.p2DList("[]"))
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(TreeNode root, List<List<Integer>> ans) {
		var algo = new BinaryTreeLevelOrderTraversal102();
		assertThat(algo.levelOrder(root)).isEqualTo(ans);
	}

}
