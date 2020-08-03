package com.bondarenko.algo.princeton.c1.sort;

public interface Sort {

	void sort(int[] arr);

	default void swap(int[] arr, int i, int j) {
		var tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	default boolean isSorted(int[] arr) {
		return isSorted(arr, 0, arr.length);
	}

	default boolean isSorted(int[] arr, int lo, int hi) {
		assert lo >= 0;
		assert hi <= arr.length;

		for (int i = lo + 1; i < hi; i++) {
			if (arr[i - 1] > arr[i]) {
				return false;
			}
		}
		return true;
	}
}
