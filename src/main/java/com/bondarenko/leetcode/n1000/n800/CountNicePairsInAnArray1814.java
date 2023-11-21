package com.bondarenko.leetcode.n1000.n800;

import java.util.HashMap;

/*
 URL: https://leetcode.com/problems/count-nice-pairs-in-an-array
 Time: N
 Space: N
 */
public class CountNicePairsInAnArray1814 {

	private static final int MOD = 1000000007;

	// array
	public int countNicePairs(int[] nums) {
		var ans = 0;
		var cnt = new HashMap<Integer, Integer>();
		for (int n : nums)
			ans = (ans + cnt.merge(n - rev(n), 1, Integer::sum) - 1) % MOD;
		return ans;
	}

	private static int rev(int k) {
		var r = 0;
		while (k > 0) {
			r = r * 10 + k % 10;
			k /= 10;
		}
		return r;
	}

}
