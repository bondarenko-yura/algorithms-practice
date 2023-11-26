package com.bondarenko.leetcode.n0000.n000;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.bondarenko.leetcode.ds.ListNode;

/*
 URL: https://leetcode.com/problems/merge-k-sorted-lists
 K - list count
 N - total nodes count
 Time: N * KLogK
 Space: 1
 */
public class MergeKSortedLists23 {

	// heap
	public ListNode mergeKLists(ListNode[] lists) {
		var heap = new PriorityQueue<ListNode>(Comparator.comparingInt(a -> a.val));
		for (ListNode ln : lists)
			if (ln != null)
				heap.add(ln);
		var head = new ListNode();
		var cur = head;
		while (!heap.isEmpty()) {
			var ln = heap.poll();
			if (ln.next != null) {
				heap.add(ln.next);
				ln.next = null;
			}
			cur.next = ln;
			cur = cur.next;
		}
		return head.next;
	}

}
