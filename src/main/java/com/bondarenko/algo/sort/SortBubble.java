package com.bondarenko.algo.sort;

/**
 * O(n) best = N
 * O(n) worst = N^2
 */
public class SortBubble implements Sort {

	@Override
	public void sort(int[] arr) {
		boolean sorted = false;
		while (!sorted) {
			sorted = true;
			for (int i = 1; i < arr.length; i++) {
				if (arr[i - 1] > arr[i]) {
					swap(arr, i - 1, i);
					sorted = false;
				}
			}
		}
	}

}
