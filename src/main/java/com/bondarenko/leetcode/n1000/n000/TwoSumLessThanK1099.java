package com.bondarenko.leetcode.n1000.n000;

import java.util.TreeSet;

/*
 URL: https://leetcode.com/problems/two-sum-less-than-k
 Time: NLogN
 Space: N
 */
public class TwoSumLessThanK1099 {

	// tree
	public int twoSumLessThanK(int[] nums, int k) {
		var set = new TreeSet<Integer>();
		var ans = -1;
		for (int n : nums) {
			var dif = k - n;
			var v = set.lower(dif);
			if (v != null)
				ans = Math.max(ans, v + n);
			set.add(n);
		}
		return ans;
	}

}
