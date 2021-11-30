package com.bondarenko.algo.princeton.c1.dynamicconnectivity;

/**
 * Build the tree. We keep track of tree size (number of objects).
 * We link the root of a smaller tree to larger tree.
 * Compress path during union and find operations
 * if both values have the same root - they are connected
 * <p>
 * init - n
 * union - ~< 5
 * find - ~< 5
 * <p>
 * max tree depth - ~2
 */
public class QuickUnionWeightingPathCompression {
	private final int[] treeSize;
	private final int[] tree;

	public QuickUnionWeightingPathCompression(int size) {
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
			// make every node point to it's grandparent
			// makes tree almost f
			tree[i] = tree[tree[i]];
			i = tree[i];
		}
		return i;
	}
}
