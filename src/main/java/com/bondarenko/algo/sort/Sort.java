package com.bondarenko.algo.sort;

public interface Sort {

  void sort(int[] arr);

  default void swap(int[] arr, int i, int j) {
    var tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  default boolean isSorted(int[] arr) {
    for (int i = 1; i < arr.length; i++) {
      if (arr[i - 1] > arr[i]) {
        return false;
      }
    }
    return true;
  }
}
