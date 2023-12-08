package com.bondarenko.leetcode.ds;

import java.util.ArrayList;
import java.util.List;

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
		var cur = new ArrayList<TreeNode>();
		var pre = List.of(root);
		var lvlSize = 2;
		var li = 0;
		var vi = 1;
		while (vi < vals.length) {
			if (vals[vi] == null) {
				cur.add(null);
			} else {
				var node = new TreeNode(vals[vi]);
				var idx = cur.size() / 2;
				if (cur.size() % 2 == 0)
					pre.get(idx).left = node;
				else
					pre.get(idx).right = node;
				cur.add(node);
			}
			li++;
			vi++;
			if (li == lvlSize) {
				li = 0;
				lvlSize *= 2;
				pre = cur;
				cur = new ArrayList<>();
			}
		}
		return root;
	}

}
