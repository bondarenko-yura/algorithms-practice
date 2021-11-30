package com.bondarenko.algo.sort;

public class SortQuick implements Sort {

	@Override
	public void sort(int[] arr) {
		sort(arr, 0, arr.length - 1);
	}

	private void sort(int[] arr, int lo, int hi) {
		if (hi <= lo) {
			return;
		}

		int mid = partition(arr, lo, hi);
		sort(arr, lo, mid - 1);
		sort(arr, mid + 1, hi);
	}

	private int partition(int[] arr, int lo, int hi) {
		int p = arr[lo];

		int curLo = lo;
		int curHi = hi + 1;

		while (curLo < curHi) {
			while (arr[++curLo] < p) {
				if (curLo == hi) {
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
