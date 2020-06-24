package com.bondarenko.algo.sort;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class SortTest {

  private static final int PROBLEM_SIZE = 250_000;

  private Random random;

  @BeforeEach
  void setUp() {
    this.random = new Random(55);
  }

  @Test
  void sortSelection() {
    testSort(new SortSelection());
  }

  @Test
  void sortInsertion() {
    testSort(new SortInsertion());
  }

  @Test
  void sortShell() {
    testSort(new SortShell());
  }

  private void testSort(Sort sort) {
    Stopwatch stp = new Stopwatch();

    int[] arr = generateArray();
    sort.sort(arr);

    double elapsedTime = stp.elapsedTime();
    StdOut.printf("'%s' time '%d' sec", sort.getClass().getSimpleName(), Math.round(elapsedTime));

    assertThat(sort.isSorted(arr)).isTrue();
  }

  private int[] generateArray() {
    int[] arr = new int[PROBLEM_SIZE];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = random.nextInt(PROBLEM_SIZE);
    }
    return arr;
  }
}