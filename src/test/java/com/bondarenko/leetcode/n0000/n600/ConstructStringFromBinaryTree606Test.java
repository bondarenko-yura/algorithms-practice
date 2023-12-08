package com.bondarenko.leetcode.n0000.n600;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.TreeNode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class ConstructStringFromBinaryTree606Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(TreeNode.of(1, 2, 3, 4), "1(2(4))(3)"),
				of(TreeNode.of(1, 2, 3, null, 4), "1(2()(4))(3)")
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(TreeNode root, String ans) {
		var algo = new ConstructStringFromBinaryTree606();
		assertThat(algo.tree2str(root)).isEqualTo(ans);
	}

}
