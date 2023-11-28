package com.bondarenko.leetcode.n0000.n200;

import java.util.stream.Stream;

import com.bondarenko.leetcode.ds.ListNode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class ReverseLinkedList206Test {

	private final ReverseLinkedList206 algo = new ReverseLinkedList206();

	private static Stream<Arguments> args() {
		return Stream.of(
				of(ListNode.of(1, 2, 3, 4, 5), ListNode.of(5, 4, 3, 2, 1)),
				of(ListNode.of(1, 2), ListNode.of(2, 1)),
				of(ListNode.of(1), ListNode.of(1)),
				of(ListNode.of(), ListNode.of())
		);
	}

	@ParameterizedTest
	@MethodSource("args")
	void test(ListNode list, ListNode ans) {
		assertThat(algo.reverseList(list)).isEqualTo(ans);
	}

}
