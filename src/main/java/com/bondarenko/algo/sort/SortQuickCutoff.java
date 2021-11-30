package com.bondarenko.algo.princeton.c1.sort;

public class SortQuickCutoff implements Sort {

	@Override
	public void sort(int[] arr) {
		sort(arr, 0, arr.length);
	}

	private void sort(int[] arr, int lo, int hi) {
		if (hi - lo < 10) {
			sortInsertion(arr, lo, hi);
			return;
		}

		int median = median3(arr, lo, (hi - lo) / 2, hi - 1);
		swap(arr, lo, median);

		int mid = partition(arr, lo, hi);
		sort(arr, lo, mid);
		sort(arr, mid + 1, hi);
	}

	private int partition(int[] arr, int lo, int hi) {
		int p = arr[lo];

		int curLo = lo;
		int curHi = hi;

		while (curLo < curHi) {
			while (arr[++curLo] < p) {
				if (curLo == hi - 1) {
					break;
				}
			}

			while (arr[--curHi] > p) {
				if (curHi == lo) {
					break;
				}
			}

			if (curLo < curHi) {
				swap(arr, curLo, curHi);
			}
		}

		swap(arr, lo, curHi);

		return curHi;
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

	private static int median3(int[] a, int i, int j, int k) {
		return a[i] < a[j]
				? (a[j] < a[k] ? j : a[i] < a[k] ? k : i)
				: (a[k] < a[j] ? j : a[k] < a[i] ? k : i);
	}
}
