package com.bondarenko.algo.sort;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SortMergeUpBottomTest {

	private final SortMergeUpBottom sort = new SortMergeUpBottom();

	@Test
	void merge() {
		merge(sort, new int[]{ 2, 1 });
		merge(sort, new int[]{ 3, 4, 1, 5 });
		merge(sort, new int[]{ 3, 4, 1, 6, 9 });
	}

	@Test
	void sort() {
		sort(new int[]{ 1 });
		sort(new int[]{ 2, 1 });
		sort(new int[]{ 2, 1, 3 });
		sort(new int[]{ 2, 1, 4, 3 });
		sort(new int[]{ 3, 1, 2, 9, 5 });
	}

	private void merge(SortMergeUpBottom sort, int[] arr) {
		int[] aux = new int[arr.length];
		sort.merge(arr, aux, 0, arr.length / 2, arr.length);
		assertThat(sort.isSorted(arr)).isTrue();
	}

	private void sort(int[] arr) {
		sort.sort(arr);
		assertThat(sort.isSorted(arr)).isTrue();
	}
}
