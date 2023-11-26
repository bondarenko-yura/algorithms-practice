package com.bondarenko.leetcode.ds;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class ListNodeTest {

	private static Stream<Arguments> equalsAndHashArgs() {
		return Stream.of(
				of(generate(1), generate(1), true),
				of(generate(2), generate(2), true),
				of(generate(3), generate(3), true),
				of(generate(4), generate(4), true),
				of(generate(5), generate(5), true),
				of(generate(10_000), generate(10_000), true),

				of(generate(1), generate(2), false),
				of(generate(2), generate(3), false),
				of(generate(3), generate(4), false),
				of(generate(4), generate(5), false),
				of(generate(5), generate(6), false),
				of(generate(10_000), generate(10_001), false)
		);
	}

	private static Stream<Arguments> toStringArgs() {
		return Stream.of(
				of(generate(1), "{1}"),
				of(generate(2), "{1->2}"),
				of(generate(3), "{1->2->3}")
		);
	}

	private static Stream<Arguments> toStringArgsLoop() {
		return Stream.of(
				of(makeLoop(generate(1)), "{1->(loop)}"),
				of(makeLoop(generate(2)), "{1->2->(loop)}"),
				of(makeLoop(generate(3)), "{1->2->3->(loop)}")
		);
	}

	private static ListNode generate(int len) {
		var head = new ListNode();
		var cur = head;
		for (int i = 1; i <= len; i++) {
			cur.next = new ListNode(i);
			cur = cur.next;
		}
		return head.next;
	}

	private static ListNode makeLoop(ListNode head) {
		if (head == null)
			return null;
		var tail = head;
		while (tail.next != null)
			tail = tail.next;
		tail.next = head;
		return head;
	}

	@Test
	void factoryMethod() {
		var node = ListNode.of(0, 1, 2, 3);
		for (int i = 0; i < 4; i++) {
			assertThat(node.val).isEqualTo(i);
			node = node.next;
		}
		assertThat(node).isNull();
	}

	@Test
	void factoryMethod_noParameters() {
		assertThat(ListNode.of()).isNull();
	}

	@ParameterizedTest
	@MethodSource("equalsAndHashArgs")
	void equals(ListNode a, ListNode b, boolean expectEqual) {
		assertThat(a.equals(b)).isEqualTo(expectEqual);
	}

	@ParameterizedTest
	@MethodSource("equalsAndHashArgs")
	void equals_loop(ListNode a, ListNode b, boolean expectEqual) {
		assertThat(makeLoop(a).equals(makeLoop(b))).isEqualTo(expectEqual);
	}

	@Test
	void equals_loopAndNonLoop() {
		var list = generate(10);
		var loop = makeLoop(generate(10));
		assertThat(list).isNotEqualTo(loop);
		assertThat(loop).isNotEqualTo(list);
	}

	@Test
	void equals_null() {
		assertThat(new ListNode(25).equals(generate(0))).isFalse();
		assertThat(makeLoop(new ListNode(25)).equals(generate(0))).isFalse();
	}

	@ParameterizedTest
	@MethodSource("equalsAndHashArgs")
	void hashCode(ListNode a, ListNode b, boolean expectEqual) {
		assertThat(a.hashCode() == b.hashCode()).isEqualTo(expectEqual);
	}

	@ParameterizedTest
	@MethodSource("equalsAndHashArgs")
	void hashCode_loop(ListNode a, ListNode b, boolean expectEqual) {
		assertThat(makeLoop(a).hashCode() == makeLoop(b).hashCode()).isEqualTo(expectEqual);
	}

	@Test
	void hashCode_loopAndNonLoop() {
		assertThat(generate(10).hashCode()).isEqualTo(makeLoop(generate(10)).hashCode());
		assertThat(makeLoop(generate(10)).hashCode()).isEqualTo(generate(10).hashCode());
	}

	@ParameterizedTest
	@MethodSource("toStringArgs")
	void toString(ListNode head, String expected) {
		assertThat(head.toString()).isEqualTo(expected);
	}

	@ParameterizedTest
	@MethodSource("toStringArgsLoop")
	void toString_loop(ListNode head, String expected) {
		assertThat(head.toString()).isEqualTo(expected);
	}

}
