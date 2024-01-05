package com.bondarenko.leetcode.n2000.n800;

import java.util.HashMap;

/*
 URL: https://leetcode.com/problems/minimum-number-of-operations-to-make-array-empty
 Time: N
 Space: N
 */
public class MinimumNumberOfOperationsToMakeArrayEmpty2870 {

	// dp
	public int minOperations(int[] nums) {
		var cnt = new HashMap<Integer, Integer>();
		for (int n : nums)
			cnt.merge(n, 1, Integer::sum);
		var ans = 0;
		for (Integer c : cnt.values()) {
			if (c == 1)
				return -1;
			ans += (int) Math.ceil((double) c / 3);
		}
		return ans;
	}

}
