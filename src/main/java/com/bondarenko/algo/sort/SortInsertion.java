package com.bondarenko.algo.sort;

/**
 * O(n) = N^2
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
