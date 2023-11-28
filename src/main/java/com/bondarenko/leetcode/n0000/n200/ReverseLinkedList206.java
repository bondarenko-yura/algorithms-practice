package com.bondarenko.leetcode.n0000.n200;

import com.bondarenko.leetcode.ds.ListNode;

/*
 URL: https://leetcode.com/problems/reverse-linked-list
 Time: N
 Space: 1
*/
public class ReverseLinkedList206 {

	// iteration
	public ListNode reverseList(ListNode head) {
		var tmp = new ListNode();
		while (head != null) {
			var node = head;
			head = head.next;
			node.next = tmp.next;
			tmp.next = node;
		}
		return tmp.next;
	}

}
