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

	public static TreeNode of(Integer... vals) {
		if (vals.length == 0 || vals[0] == null)
			return null;
		var root = new TreeNode(vals[0]);
		var parents = List.of(root);
		var children = new ArrayList<TreeNode>();
		for (Integer v : vals) {
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
			var child = v == null ? null : new TreeNode(v);
			var parent = parents.get(children.size() / 2);
			if (children.size() % 2 == 0)
				parent.left = child;
			else
				parent.right = child;
			children.add(child);
			if (parents.size() * 2 == children.size() && hasNodes(children)) {
				parents = children;
				children = new ArrayList<>();
			}
		}
		return root;
	}

	private static boolean hasNodes(Collection<TreeNode> row) {
		return row.stream().anyMatch(Objects::nonNull);
	}

}
