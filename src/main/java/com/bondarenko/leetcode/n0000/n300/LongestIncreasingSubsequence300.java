package com.bondarenko.leetcode.n0000.n300;

import java.util.ArrayList;

/*
 URL: https://leetcode.com/problems/longest-increasing-subsequence
 Time: N^2
 Space: N
*/
public class LongestIncreasingSubsequence300 {

	// build subsequence
	public int lengthOfLIS(int[] nums) {
		var s = new ArrayList<Integer>();
		for (int n : nums) {
			var lt = 0;
			var rt = s.size();
			while (lt < rt) {
				var md = lt + (rt - lt) / 2;
				if (s.get(md) < n)
					lt = md + 1;
				else
					rt = md;
			}
			if (lt == s.size())
				s.add(n);
			else
				s.set(lt, n);
		}
		return s.size();
	}

}
