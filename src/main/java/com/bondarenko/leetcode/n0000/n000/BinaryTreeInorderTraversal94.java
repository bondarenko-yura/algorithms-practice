package com.bondarenko.leetcode.n0000.n000;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.bondarenko.leetcode.ds.TreeNode;

/*
 URL: https://leetcode.com/problems/binary-tree-inorder-traversal
 Time: N
 Space: N
 */
public class BinaryTreeInorderTraversal94 {

	private static void inorder(TreeNode root, List<Integer> ans) {
		if (root == null)
			return;
		inorder(root.left, ans);
		ans.add(root.val);
		inorder(root.right, ans);
	}

	// dfs
	public List<Integer> inorderTraversal(TreeNode root) {
		var ans = new ArrayList<Integer>();
		inorder(root, ans);
		return ans;
	}

	public List<Integer> inorderTraversalStack(TreeNode root) {
		var res = new ArrayList<Integer>();
		var stack = new ArrayDeque<TreeNode>();
		TreeNode curr = root;
		while (curr != null || !stack.isEmpty()) {
			while (curr != null) {
				stack.push(curr);
				curr = curr.left;
			}
			curr = stack.pop();
			res.add(curr.val);
			curr = curr.right;
		}
		return res;
	}

}
