package com.bondarenko.leetcode.n1000.n800;

/*
 URL: https://leetcode.com/problems/maximum-element-after-decreasing-and-rearranging
 Time: N
 Space: N
 */
public class MaximumElementAfterDecreasingAndRearranging1846 {

	// count, greedy
	public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
		var n = arr.length;
		var seq = new int[n];
		for (int v : arr)
			seq[Math.min(v - 1, n - 1)]++;
		var ans = n;
		for (int i = n - 1; i > 0; i--) {
			seq[i-1] += Math.max(0, seq[i] - 1);
			if (seq[i] == 0)
				ans--;
		}
		return ans;
	}

}
