package com.bondarenko.algo.sort;

/**
 * O(n) = 2N logN
 * heap construction is linear
 *
 * fastest algorithms in theory, does in-place sorting in guarantee time
 *
 * but not used, because:
 * - inner loop is longer than quick sorts
 * - makes a poor use of cache memory (because it touches elements far away from each other)
 * - not stable
 */
public class SortHeap implements Sort {

  public static final int PARENT_IDX = 0;

  @Override
  public void sort(int[] arr) {
    for (int i = arr.length / 2; i >= PARENT_IDX; i--) {
      sink(arr, i, arr.length);
    }

    int i = arr.length;
    while (i > 1) {
      swap(arr, PARENT_IDX, --i);
      sink(arr, PARENT_IDX, i);
    }
  }

  private void sink(int[] arr, int parentIdx, int limit) {
    int childIdx = getFirstChildIdx(parentIdx);
    while (childIdx < limit) {
      if (childIdx + 1 < limit && arr[childIdx] < arr[childIdx + 1]) {
        childIdx++;
      }
      if (arr[parentIdx] >= arr[childIdx]) {
        break;
      }
      swap(arr, parentIdx, childIdx);
      parentIdx = childIdx;
      childIdx = getFirstChildIdx(parentIdx);
    }
  }

  private int getFirstChildIdx(int parentIdx) {
    return (parentIdx + 1) * 2 - 1;
  }
}
