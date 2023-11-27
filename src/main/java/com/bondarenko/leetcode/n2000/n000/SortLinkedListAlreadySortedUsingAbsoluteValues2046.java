package com.bondarenko.leetcode.n2000.n000;

import com.bondarenko.leetcode.ds.ListNode;

/*
 URL: https://leetcode.com/problems/sort-linked-list-already-sorted-using-absolute-values/
 Time: N
 Space: 1
 */
public class SortLinkedListAlreadySortedUsingAbsoluteValues2046 {

	// iteration
	public ListNode sortLinkedList(ListNode head) {
		var negHead = new ListNode();
		var negTail = negHead;
		var posHead = new ListNode();
		var posCur = posHead;
		while (head != null) {
			var node = head;
			head = node.next;
			node.next = null;
			if (node.val >= 0) {
				posCur.next = node;
				posCur = node;
			} else {
				node.next = negHead.next;
				negHead.next = node;
				if (negTail == negHead)
					negTail = node;
			}
		}
		negTail.next = posHead.next;
		return negHead.next;
	}

}
