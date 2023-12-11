package com.bondarenko.leetcode.n0000.n000;

import java.util.ArrayList;
import java.util.List;

/*
 URL: https://leetcode.com/problems/combination-sum
 N - number of candidates
 T - target value
 M - minimal value among the candidates
 candidates.
 Time: N ^ ( T / M - 1)
 Space: T / M
 */
public class CombinationSum39 {

	private static void backtrack(
			int c, int[] candidates, int target, List<Integer> buf, List<List<Integer>> ans) {
		if (target == 0) {
			ans.add(new ArrayList<>(buf));
			return;
		}
		if (target < 0 || c == candidates.length)
			return;
		backtrack(c + 1, candidates, target, buf, ans);
		buf.add(candidates[c]);
		backtrack(c, candidates, target - candidates[c], buf, ans);
		buf.remove(buf.size() - 1);
	}

	// backtracking
	public List<List<Integer>> combinationSum(int[] candidates, int target) {
		var ans = new ArrayList<List<Integer>>();
		backtrack(0, candidates, target, new ArrayList<>(), ans);
		return ans;
	}

}
