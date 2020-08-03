package com.bondarenko.algo.princeton.c1.sort;

public class SortQuick implements Sort {

	@Override
	public void sort(int[] arr) {
		sort(arr, 0, arr.length);
	}

	private void sort(int[] arr, int lo, int hi) {
		if (hi - lo < 2) {
			return;
		}

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
}
