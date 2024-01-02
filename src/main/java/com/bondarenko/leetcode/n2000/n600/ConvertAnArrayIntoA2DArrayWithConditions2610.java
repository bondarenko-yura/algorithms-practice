package com.bondarenko.leetcode.n2000.n600;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 URL: https://leetcode.com/problems/convert-an-array-into-a-2d-array-with-conditions
 Time: N
 Space: N
 */
public class ConvertAnArrayIntoA2DArrayWithConditions2610 {

	// hash map with last row
	public List<List<Integer>> findMatrix(int[] nums) {
		var lastRow = new HashMap<Integer, Integer>();
		var ans = new ArrayList<List<Integer>>();
		for (int n : nums) {
			var r = lastRow.merge(n, 1, Integer::sum) - 1;
			if (ans.size() == r)
				ans.add(new ArrayList<>());
			ans.get(r).add(n);
		}
		return ans;
	}

}
