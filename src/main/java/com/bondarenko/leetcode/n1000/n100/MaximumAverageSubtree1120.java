package com.bondarenko.leetcode.n1000.n100;

import com.bondarenko.leetcode.ds.TreeNode;

/*
 URL: https://leetcode.com/problems/find-words-that-can-be-formed-by-characters
 Time: N
 Space: N
 */
public class MaximumAverageSubtree1120 {

	private static double[] /*sum, cnt, max*/ dfs(TreeNode root) {
		if (root == null)
			return new double[3];
		var left = dfs(root.left);
		var right = dfs(root.right);
		left[0] += root.val + right[0];
		left[1] += 1 + right[1];
		left[2] = Math.max(Math.max(left[2], right[2]), left[0] / left[1]);
		return left;
	}

	// recursion
	public double maximumAverageSubtree(TreeNode root) {
		return dfs(root)[2];
	}

}
