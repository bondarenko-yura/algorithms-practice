package com.bondarenko.leetcode.n0000.n100;

import com.bondarenko.leetcode.ds.TreeNode;

/*
 https://leetcode.com/problems/binary-tree-maximum-path-sum
 Time: N
 Space: N
 */
public class BinaryTreeMaximumPathSum124 {

	private int ans = Integer.MIN_VALUE;

	// Kadane's algorithm
	public int maxPathSum(TreeNode root) {
		dfs(root);
		return ans;
	}

	private int dfs(TreeNode root) {
		if (root == null)
			return 0;
		var left = dfs(root.left);
		var right = dfs(root.right);
		var oneBranchMax = root.val + Math.max(0, Math.max(left, right));
		var twoBranchMax = root.val + left + right;
		ans = Math.max(ans, Math.max(oneBranchMax, twoBranchMax));
		return oneBranchMax;
	}

}
