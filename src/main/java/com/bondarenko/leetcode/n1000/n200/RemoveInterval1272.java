package com.bondarenko.leetcode.n1000.n200;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 URL: https://leetcode.com/problems/remove-interval
 Time: x
 Space: x
 */
public class RemoveInterval1272 {

	public List<List<Integer>> removeInterval(int[][] intervals, int[] rem) {
		var ans = new ArrayList<List<Integer>>();
		for (int[] t : intervals) {
			if (t[0] < rem[1] && t[1] > rem[0]) {
				if (t[0] < rem[0])
					ans.add(List.of(t[0], rem[0]));
				if (rem[1] < t[1])
					ans.add(List.of(rem[1], t[1]));
			} else {
				ans.add(List.of(t[0], t[1]));
			}
		}
		return ans;
	}

}
