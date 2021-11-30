package com.bondarenko.algo.princeton.c1.sort;

/**
 * O(n) = N log(N)
 * <p>
 * stable
 */
public class SortMergeUpBottomOptimized implements Sort {

	private static final int CUTOFF = 7;

	@Override
	public void sort(int[] arr) {
		sort(arr, new int[arr.length], 0, arr.length);
	}

	void sort(int[] arr, int[] aux, int lo, int hi) {
		int rangeSize = hi - lo;

		if (rangeSize <= CUTOFF) {
			sortInsertion(arr, lo, hi);
			return;
		}

		int mid = lo + (rangeSize) / 2;
		sort(arr, aux, lo, mid);
		sort(arr, aux, mid, hi);
		merge(arr, aux, lo, mid, hi);
	}

	void merge(int[] arr, int[] aux, int lo, int mid, int hi) {
		System.arraycopy(arr, lo, aux, lo, hi - lo);

		if (arr[mid - 1] <= arr[mid]) {
			return;
		}

		int midDest = mid;
		for (int i = lo; i < hi; i++) {
			if (lo == midDest) {
				arr[i] = aux[mid++];
			} else if (mid == hi) {
				arr[i] = aux[lo++];
			} else if (aux[lo] < aux[mid]) {
				arr[i] = aux[lo++];
			} else {
				arr[i] = aux[mid++];
			}
		}
	}

	public void sortInsertion(int[] arr, int lo, int hi) {
		for (int i = lo + 1; i < hi; i++) {
			int j = i;
			while (j > lo && arr[j - 1] > arr[j]) {
				swap(arr, j, j - 1);
				j--;
			}
		}
	}
}
