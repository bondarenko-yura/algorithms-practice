package com.bondarenko.ds.unionfind;

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

	private final int[] height;
	private final int[] tree;

	public QuickUnionWeightingPathCompression(int size) {
		this.tree = new int[size];
		this.height = new int[size];
		for (int i = 0; i < size; i++) {
			tree[i] = i;
			height[i] = 1;
		}
	}

	public void union(int p, int q) {
		if (!isConnected(p, q)) {
			var pr = find(p);
			var qr = find(q);

			if (height[pr] >= height[qr]) {
				tree[qr] = pr;
				if (height[pr] == height[qr])
					height[pr]++;
			} else {
				tree[pr] = qr;
			}
		}
	}

	public boolean isConnected(int a, int b) {
		return find(a) == find(b);
	}

	private int find(int i) {
		if (i == tree[i])
			return i;

		return tree[i] = find(tree[i]);
	}

}
