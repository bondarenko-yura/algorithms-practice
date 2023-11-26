package com.bondarenko.leetcode.ds;

import java.util.Collections;
import java.util.IdentityHashMap;
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
		if (o == null || getClass() != o.getClass())
			return false;
		var thisSlow = this;
		var thisFast = thisSlow;
		var thatSlow = (ListNode) o;
		var thatFast = thatSlow;
		while (thisSlow != null && thatSlow != null) {
			if (thisSlow.val != thatSlow.val)
				return false;
			thisSlow = thisSlow.next;
			thatSlow = thatSlow.next;
			var loopCnt = 0;
			if (thisFast != null && thisFast.next != null) {
				thisFast = thisFast.next.next;
				if (thisFast != null && thisFast == thisSlow)
					loopCnt++;
			}
			if (thatFast != null && thatFast.next != null) {
				thatFast = thatFast.next.next;
				if (thatFast != null && thatFast == thatSlow)
					loopCnt++;
			}
			if (loopCnt > 0)
				return loopCnt == 2;
		}
		return thisSlow == null && thatSlow == null;
	}

	@Override
	public int hashCode() {
		var slow = this;
		var fast = this;
		var hash = 31;
		while (slow != null) {
			hash = Objects.hash(hash, slow.val);
			slow = slow.next;
			if (fast != null && fast.next != null) {
				fast = fast.next.next;
				if (slow == fast)
					break;
			}
		}
		return hash;
	}

	@Override
	public String toString() {
		var sb = new StringBuilder("{");
		var map = Collections.newSetFromMap(new IdentityHashMap<ListNode, Boolean>());
		var head = this;
		while (head != null) {
			if (!map.add(head)) {
				sb.append("(loop)");
				break;
			}
			sb.append(head.val);
			head = head.next;
			if (head != null)
				sb.append("->");
		}
		sb.append("}");
		return sb.toString();
	}
}
