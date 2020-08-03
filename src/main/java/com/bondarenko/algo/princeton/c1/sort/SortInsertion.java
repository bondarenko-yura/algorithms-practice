package com.bondarenko.algo.princeton.c1.sort;

/**
 * O(n) best = N
 * O(n) worst = N^2
 * <p>
 * stable
 */
public class SortInsertion implements Sort {
	@Override
	public void sort(int[] arr) {
		for (int i = 1; i < arr.length; i++) {
			int j = i;
			while (j > 0 && arr[j] < arr[j - 1]) {
				swap(arr, j, j - 1);
				j--;
			}
		}
	}
}
