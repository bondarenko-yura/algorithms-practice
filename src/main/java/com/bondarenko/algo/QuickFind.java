package com.bondarenko.algo;

public class QuickFind {

	private final int[] data;

	public QuickFind(int size) {
		this.data = new int[size];
		for (int i = 0; i < size; i++) {
			data[i] = i;
		}
	}

	public void connect(int a, int b) {
		if (!isConnected(a, b)) {
			int aVal = data[a];
			int bVal = data[b];
			for (int i = 0; i < data.length; i++) {
				if (data[i] == bVal) {
					data[i] = aVal;
				}
			}
		}
	}

	public boolean isConnected(int a, int b) {
		return data[a] == data[b];
	}
}
