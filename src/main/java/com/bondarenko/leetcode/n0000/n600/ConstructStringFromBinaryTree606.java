package com.bondarenko.leetcode.n0000.n600;

import com.bondarenko.leetcode.ds.TreeNode;

/*
 URL: https://leetcode.com/problems/longest-substring-without-repeating-characters
 Time: N
 Space: N
 */
public class ConstructStringFromBinaryTree606 {

	private static void dfs(TreeNode root, StringBuilder sb) {
		if (root == null)
			return;
		sb.append(root.val);
		if (root.left == null && root.right == null)
			return;
		sb.append('(');
		dfs(root.left, sb);
		sb.append(')');
		if (root.right != null) {
			sb.append('(');
			dfs(root.right, sb);
			sb.append(')');
		}
	}

	// recursion
	public String tree2str(TreeNode root) {
		var sb = new StringBuilder();
		dfs(root, sb);
		return sb.toString();
	}

}
