package com.bondarenko.algo.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class SelectionTest {

  private final Random random = new Random(55);
  private final SortQuick sort = new SortQuick();
  private final Selection selection = new Selection();

  @Test
  void findGreatestValues() {
    for (int i = 1; i < 10000; i++) {
      runTest(i, random.nextInt(i) + 1);
    }
  }

  private void runTest(int problemSize, int valCount) {
    int[] arr = new int[problemSize];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = random.nextInt(problemSize * 5);
    }
    int[] inputCopy = Arrays.copyOf(arr, arr.length);

    int[] result = selection.findGreatestValues(arr, valCount);
    assertThat(result.length).isEqualTo(valCount);

    sort.sort(result);
    sort.sort(inputCopy);

    int[] expected = Arrays.copyOfRange(inputCopy, inputCopy.length - valCount, inputCopy.length);
    assertThat(result).isEqualTo(expected);
  }
}