package com.bondarenko.algo.sort;

/**
 * O(n) best = N log (N)
 * O(n) worst = N^(3/2)
 */
public class SortShell implements Sort {
  @Override
  public void sort(int[] arr) {
    int h = 1;
    while (h < arr.length / 3) {
      h = 3 * h + 1;
    }

    while (h >= 1) {
      for (int i = h; i < arr.length; i++) {
        int j = i;
        while (j >= h && arr[j] < arr[j - h]) {
          swap(arr, j, j - h);
          j -= h;
        }
      }
      h /= 3;
    }
  }
}
