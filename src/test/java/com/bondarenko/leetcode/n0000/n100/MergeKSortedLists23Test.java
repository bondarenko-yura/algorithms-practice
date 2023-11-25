package com.bondarenko.leetcode.n0000.n100;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.ListNode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MergeKSortedLists23Test {

	private final MergeKSortedLists23 algo = new MergeKSortedLists23();

	private static Stream<Arguments> args() {
		return Stream.of(
				of(new ListNode[]{ListNode.of(1, 4, 5), ListNode.of(1, 3, 4), ListNode.of(2, 6)}, ListNode.of(1, 1, 2, 3, 4, 4, 5, 6)),
				of(new ListNode[]{}, ListNode.of()),
				of(new ListNode[]{null}, ListNode.of())
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(ListNode[] lists, ListNode ans) {
		assertThat(algo.mergeKLists(lists)).isEqualTo(ans);
	}

}
