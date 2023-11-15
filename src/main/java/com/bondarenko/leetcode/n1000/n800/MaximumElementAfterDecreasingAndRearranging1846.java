package com.bondarenko.leetcode.n1000.n800;

/*
 URL: https://leetcode.com/problems/maximum-element-after-decreasing-and-rearranging
 Time: N
 Space: N
 */
public class MaximumElementAfterDecreasingAndRearranging1846 {

	public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
		var n = arr.length;
		var seq = new int[n];
		for (int v : arr)
			seq[Math.min(v - 1, n - 1)]++;
		for (int i = n - 1; i > 0; i--) {
			seq[i-1] += Math.max(0, seq[i] - 1);
			seq[i] = Math.min(1, seq[i]);
		}
		var cnt = 0;
		for (int v : seq)
			if (v > 0)
				cnt++;
		return cnt;
	}

}
