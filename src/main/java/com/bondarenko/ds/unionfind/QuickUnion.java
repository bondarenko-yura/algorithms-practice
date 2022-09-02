package com.bondarenko.ds.unionfind;

/**
 * Build the tree. if both values have the same root - they are connected
 * init - n
 * union - n (includes a cost of finding roots)
 * find - n
 */
public class QuickUnion {
	private final int[] data;

	public QuickUnion(int size) {
		this.data = new int[size];
		for (int i = 0; i < size; i++) {
			data[i] = i;
		}
	}

	public void union(int p, int q) {
		if (!isConnected(p, q)) {
			var i = rootValue(p);
			var j = rootValue(q);
			data[i] = j;
		}
	}

	public boolean isConnected(int a, int b) {
		return rootValue(a) == rootValue(b);
	}

	private int rootValue(int searchVal) {
		while (searchVal != data[searchVal]) {
			searchVal = data[searchVal];
		}

		return searchVal;
	}
}
