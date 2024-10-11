package com.bondarenko.leetcode.n1000.n900;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/*
 URL: https://leetcode.com/problems/the-number-of-the-smallest-unoccupied-chair
 Time: NLogN
 Space: N
 */
public class TheNumberOfTheSmallestUnoccupiedChair1942 {

	// sort
	public int smallestChair(int[][] times, int targetFriend) {
		var vis = new int[times.length][/*start, finish, guest*/];
		var leave = new PriorityQueue<int[/*finish, chair*/]>(Comparator.comparingInt(a -> a[0]));
		var chairs = new PriorityQueue<Integer>();
		for (int i = 0; i < times.length; i++) {
			vis[i] = new int[] { times[i][0], times[i][1], i };
			chairs.add(i);
		}
		Arrays.sort(vis, Comparator.comparingInt(a -> a[0]));
		for (int[] v : vis) {
			while (!leave.isEmpty() && leave.peek()[0] <= v[0])
				chairs.add(leave.poll()[1]);
			var c = chairs.poll();
			if (v[2] == targetFriend)
				return c;
			leave.add(new int[] { v[1], c });
		}
		return -1;
	}
}
