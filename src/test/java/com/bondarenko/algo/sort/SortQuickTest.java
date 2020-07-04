package com.bondarenko.algo.sort;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SortQuickTest {

  @Test
  void sort() {
    Sort sort = new SortQuick();
    int[] arr = {3, 1, 2, 8, 2, 3, 0, 10, 4, 5, 4};
    sort.sort(arr);
    assertThat(sort.isSorted(arr)).isTrue();
  }
}