package com.bondarenko.leetcode.n2000.n000;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.ListNode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class SortLinkedListAlreadySortedUsingAbsoluteValues2046Test {

	private static Stream<Arguments> args() {
		return Stream.of(
				of(ListNode.of(0, 2, -5, 5, 10, -10), ListNode.of(-10, -5, 0, 2, 5, 10)),
				of(ListNode.of(0, 1, 2), ListNode.of(0, 1, 2)),
				of(ListNode.of(1), ListNode.of(1))
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(ListNode head, ListNode ans) {
		var algo = new SortLinkedListAlreadySortedUsingAbsoluteValues2046();
		assertThat(algo.sortLinkedList(head)).isEqualTo(ans);
	}

}
