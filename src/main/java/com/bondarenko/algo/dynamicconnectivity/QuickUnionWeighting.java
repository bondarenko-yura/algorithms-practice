package com.bondarenko.algo.dynamicconnectivity;

/**
 * Build the tree. We keep track of tree size (number of objects).
 * We link the root of a smaller tree to larger tree.
 * if both values have the same root - they are connected
 *
 * init - n
 * union - lgN
 * find - lgN
 *
 * max tree depth - lgN
 */
public class QuickUnionWeighting {
	private final int[] treeSize;
	private final int[] tree;

	public QuickUnionWeighting(int size) {
		this.tree = new int[size];
		this.treeSize = new int[size];
		for (int i = 0; i < size; i++) {
			tree[i] = i;
			treeSize[i] = 1;
		}
	}

	public void union(int p, int q) {
		if (!isConnected(p, q)) {
			var i = rootValue(p);
			var j = rootValue(q);

			if (treeSize[i] > treeSize[j]) {
				tree[j] = i;
				treeSize[i] += treeSize[j];
			} else {
				tree[i] = j;
				treeSize[j] += treeSize[i];
			}
		}
	}

	public boolean isConnected(int a, int b) {
		return rootValue(a) == rootValue(b);
	}

	private int rootValue(int i) {
		while (i != tree[i]) {
			i = tree[i];
		}
		return i;
	}
}
