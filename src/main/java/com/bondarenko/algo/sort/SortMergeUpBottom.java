package com.bondarenko.algo.sort;

/**
 * O(n) = N log(N)
 * <p>
 * stable
 */
public class SortMergeUpBottom implements Sort {

	@Override
	public void sort(int[] arr) {
		sort(arr, new int[arr.length], 0, arr.length);
	}

	void sort(int[] arr, int[] aux, int lo, int hi) {
		int rangeSize = hi - lo;
		if (rangeSize < 2) {
			return;
		}

		int mid = lo + rangeSize / 2;
		sort(arr, aux, lo, mid);
		sort(arr, aux, mid, hi);
		merge(arr, aux, lo, mid, hi);
	}

	void merge(int[] arr, int[] aux, int lo, int mid, int hi) {
		System.arraycopy(arr, lo, aux, lo, hi - lo);

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

}
