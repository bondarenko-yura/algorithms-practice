package com.bondarenko.leetcode.ds;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class TreeNodeTest {

	private static Stream<Arguments> toStringArgs() {
		return Stream.of(
				of(new TreeNode(1), "1"),
				of(new TreeNode(1,
								new TreeNode(2),
								new TreeNode(3)
						),
						"1(2)(3)"
				),
				of(new TreeNode(1,
								new TreeNode(2),
								new TreeNode(3,
										null,
										new TreeNode(5,
												new TreeNode(6),
												null
										)
								)
						),
						"1(2)(3()(5(6)))"
				)
		);
	}

	private static Stream<Arguments> factoryArgs() {
		return Stream.of(
				of(new Integer[]{1}, new TreeNode(1)),
				of(new Integer[]{1, 2, 3},
						new TreeNode(1,
								new TreeNode(2),
								new TreeNode(3)
						)
				),
				of(new Integer[]{1, null, null, 4}, new TreeNode(1)),
				of(new Integer[]{1, null, 3, 4},
						new TreeNode(1,
								null,
								new TreeNode(3,
										new TreeNode(4),
										null
								)
						)
				),
				of(new Integer[]{1, 2, 3, null, null, null, 5, 6},
						new TreeNode(1,
								new TreeNode(2),
								new TreeNode(3,
										null,
										new TreeNode(5,
												new TreeNode(6),
												null
										)
								)
						)
				)
		);
	}

	@ParameterizedTest
	@MethodSource("toStringArgs")
	void toString(TreeNode root, String expected) {
		assertThat(root.toString()).isEqualTo(expected);
	}

	@ParameterizedTest
	@MethodSource("factoryArgs")
	void factory(Integer[] args, TreeNode expected) {
		assertThat(TreeNode.of(args)).isEqualTo(expected);
	}

}
