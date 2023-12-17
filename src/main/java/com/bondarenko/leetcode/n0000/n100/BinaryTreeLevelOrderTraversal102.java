package com.bondarenko.leetcode.n0000.n100;

import java.util.ArrayList;
import java.util.List;

import com.bondarenko.leetcode.ds.TreeNode;

/*
 https://leetcode.com/problems/binary-tree-level-order-traversal/\
 Time: N
 Space: N
 */
public class BinaryTreeLevelOrderTraversal102 {

	private static void dfs(TreeNode root, List<List<Integer>> ans, int lvl) {
		if (root == null)
			return;
		if (ans.size() == lvl)
			ans.add(new ArrayList<>());
		ans.get(lvl).add(root.val);
		dfs(root.left, ans, lvl + 1);
		dfs(root.right, ans, lvl + 1);
	}

	// dfs
	public List<List<Integer>> levelOrder(TreeNode root) {
		var ans = new ArrayList<List<Integer>>();
		dfs(root, ans, 0);
		return ans;
	}

}
