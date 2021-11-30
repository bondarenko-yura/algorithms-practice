package com.bondarenko.algo.sort;

public class SortQuick3WayPartitioning implements Sort {

	@Override
	public void sort(int[] arr) {
		sort(arr, 0, arr.length);
	}

	private void sort(int[] arr, int lo, int hi) {
		if (hi - lo < 2) {
			return;
		}

		int lt = lo;
		int gt = hi;
		int cursor = lo + 1;
		int k = arr[lo];

		while (cursor < gt) {
			if (arr[cursor] < k) {
				swap(arr, lt++, cursor++);
			} else if (arr[cursor] > k) {
				swap(arr, cursor, --gt);
			} else {
				cursor++;
			}
		}

		sort(arr, lo, lt);
		sort(arr, gt, hi);
	}
}
