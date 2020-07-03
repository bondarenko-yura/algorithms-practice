package com.bondarenko.algo.sort;

public class MergeSortBottomUp implements Sort {

  @Override
  public void sort(int[] arr) {
    int[] aux = new int[arr.length];

    for (int halfLen = 1; halfLen < arr.length; halfLen *= 2) {
      int len = halfLen * 2;
      for (int lo = 0; lo + halfLen < arr.length; lo += len) {
        merge(arr, aux, lo, lo + halfLen, Math.min(lo + len, arr.length));
      }
    }
  }

  void merge(int[] arr, int[] aux, int lo, int mid, int hi) {
    if (arr[mid - 1] <= arr[mid]) {
      return;
    }

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
