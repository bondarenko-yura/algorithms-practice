package com.bondarenko.leetcode.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class TreeNode {

	public int val;
	public TreeNode left;
	public TreeNode right;

	public TreeNode() {
	}

	public TreeNode(int val) {
		this.val = val;
	}

	public TreeNode(int val, TreeNode left, TreeNode right) {
		this.val = val;
		this.left = left;
		this.right = right;
	}

	private static void toString(TreeNode root, StringBuilder sb) {
		if (root == null)
			return;
		sb.append(root.val);
		if (root.left == null && root.right == null)
			return;
		sb.append('(');
		toString(root.left, sb);
		sb.append(')');
		if (root.right != null) {
			sb.append('(');
			toString(root.right, sb);
			sb.append(')');
		}
	}

	public static TreeNode of(Integer... vals) {
		if (vals.length == 0 || vals[0] == null)
			return null;
		var root = new TreeNode(vals[0]);
		var parents = List.of(root);
		var children = new ArrayList<TreeNode>();
		for (int i = 1; i < vals.length; i++) {
			while (parents.get(children.size() / 2) == null) {
				if ((children.size() + 1) / 2 == parents.size()) {
					if (hasNodes(children)) {
						parents = children;
						children = new ArrayList<>();
						continue;
					} else {
						return root;
					}
				}
				children.add(null);
			}
			var child = vals[i] == null ? null : new TreeNode(vals[i]);
			var parent = parents.get(children.size() / 2);
			if (children.size() % 2 == 0)
				parent.left = child;
			else
				parent.right = child;
			children.add(child);
			if (parents.size() * 2 == children.size()) {
				parents = children;
				children = new ArrayList<>();
			}
		}
		return root;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TreeNode that)) return false;
		return val == that.val
				&& Objects.equals(left, that.left)
				&& Objects.equals(right, that.right);
	}

	@Override
	public int hashCode() {
		return Objects.hash(val, left, right);
	}

	@Override
	public String toString() {
		var sb = new StringBuilder();
		toString(this, sb);
		return sb.toString();
	}

	private static boolean hasNodes(Collection<TreeNode> row) {
		return row.stream().anyMatch(Objects::nonNull);
	}

}
