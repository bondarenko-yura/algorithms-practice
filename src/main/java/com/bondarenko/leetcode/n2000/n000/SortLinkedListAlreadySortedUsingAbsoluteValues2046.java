package com.bondarenko.leetcode.n2000.n000;

import com.bondarenko.leetcode.ds.ListNode;

/*
 URL: https://leetcode.com/problems/sort-linked-list-already-sorted-using-absolute-values/
 Time: N
 Space: 1
 */
public class SortLinkedListAlreadySortedUsingAbsoluteValues2046 {

	public ListNode sortLinkedList(ListNode head) {
		var neg = new ListNode(Integer.MIN_VALUE);
		var negTail = neg;
		var pos = new ListNode();
		var posCur = pos;
		while (head != null) {
			var node = head;
			head = head.next;
			if (node.val >= 0) {
				posCur.next = node;
				posCur = posCur.next;
				posCur.next = null;
			} else {
				node.next = neg.next;
				neg.next = node;
				if (negTail.val == Integer.MIN_VALUE)
					negTail = node;
			}
		}
		if (negTail.val == Integer.MIN_VALUE) {
			return pos.next;
		}
		negTail.next = pos.next;
		return neg.next;
	}

}
