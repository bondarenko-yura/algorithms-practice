package com.bondarenko.algo.sort;

/**
 * O(n) = N^2
 * <p>
 * not stable
 */
public class SortSelection implements Sort {

	@Override
	public void sort(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			int minIdx = i;
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[j] < arr[minIdx]) {
					minIdx = j;
				}
			}
			swap(arr, i, minIdx);
		}
	}
}
