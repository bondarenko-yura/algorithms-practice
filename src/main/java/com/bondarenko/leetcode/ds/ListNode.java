package com.bondarenko.leetcode.ds;

import java.util.Objects;

public class ListNode {

	public int val;
	public ListNode next;

	public ListNode() {
	}

	public ListNode(int val) {
		this.val = val;
	}

	public ListNode(int val, ListNode next) {
		this.val = val;
		this.next = next;
	}

	public static ListNode of(int... vals) {
		var head = new ListNode();
		var cur = head;
		for (int v : vals) {
			cur.next = new ListNode(v);
			cur = cur.next;
		}
		return head.next;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ListNode listNode = (ListNode) o;
		return val == listNode.val && Objects.equals(next, listNode.next);
	}

	@Override
	public int hashCode() {
		return Objects.hash(val, next);
	}

}
