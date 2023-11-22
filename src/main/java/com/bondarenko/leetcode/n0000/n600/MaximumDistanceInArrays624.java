package com.bondarenko.leetcode.n0000.n600;

import java.util.List;

/*
 URL: https://leetcode.com/problems/maximum-distance-in-arrays
 Time: R
 Space: 1
 */
public class MaximumDistanceInArrays624 {

	// array
	public int maxDistance(List<List<Integer>> arr) {
		var ans = 0;
		var min = arr.get(0).get(0);
		var max = arr.get(0).get(arr.get(0).size() - 1);
		for (int i = 1; i < arr.size(); i++) {
			var a = arr.get(i);
			var d1 = Math.abs(max - a.get(0));
			var d2 = Math.abs(a.get(a.size() - 1) - min);
			ans = Math.max(ans, Math.max(d1, d2));
			min = Math.min(min, a.get(0));
			max = Math.max(max, a.get(a.size() - 1));
		}
		return ans;
	}

}
