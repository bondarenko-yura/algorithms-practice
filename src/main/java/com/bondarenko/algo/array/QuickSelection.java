package com.bondarenko.algo.array;

public class QuickSelection {

	public int[] findGreatestValues(int[] arr, int n) {
		assert arr.length >= n;

		int lo = 0;
		int hi = arr.length;

		while (lo < hi) {
			int j = partition(arr, lo, hi);

			int elemCount = arr.length - j;

			if (elemCount < n) {
				hi = j;
			} else if (elemCount > n) {
				lo = j + 1;
			} else {
				break;
			}
		}

		int[] answer = new int[n];
		System.arraycopy(arr, arr.length - n, answer, 0, n);
		return answer;
	}

	private int partition(int[] arr, int lo, int hi) {
		int curLo = lo;
		int curHi = hi;

		int k = arr[lo];

		while (curLo < curHi) {
			do {
				curLo++;
			} while (curLo < hi && arr[curLo] < k);

			do {
				curHi--;
			} while (curHi >= lo && arr[curHi] > k);

			if (curLo < curHi) {
				swap(arr, curLo, curHi);
			}
		}

		swap(arr, lo, curHi);

		return curHi;
	}

	private void swap(int[] arr, int i, int j) {
		var tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

}
