package com.bondarenko.algo.sort;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("heavy")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SortTest {

	private static final boolean RUN_FAST = true;

	private static final int PROBLEM_SIZE_SUPER_LOW_TEAR = problemSize(200_000);
	private static final int PROBLEM_SIZE_LOW_TEAR = problemSize(500_000);
	private static final int PROBLEM_SIZE_MID_TEAR = problemSize(200_000_000);

	private Random random;

	private static int problemSize(int targetSize) {
		return RUN_FAST ? 1 : targetSize;
	}

	@BeforeEach
	void setUp() {
		this.random = new Random(55);
	}

	@Order(1)
	@Test
	void sortBubble() {
		testSort(new SortBubble(), PROBLEM_SIZE_SUPER_LOW_TEAR);
	}

	@Order(2)
	@Test
	void sortSelection() {
		testSort(new SortSelection(), PROBLEM_SIZE_LOW_TEAR);
	}

	@Order(3)
	@Test
	void sortInsertion() {
		testSort(new SortInsertion(), PROBLEM_SIZE_LOW_TEAR);
	}

	@Order(4)
	@Test
	void sortShell() {
		testSort(new SortShell(), PROBLEM_SIZE_MID_TEAR);
	}

	@Order(5)
	@Test
	void sortHeap() {
		testSort(new SortHeap(), PROBLEM_SIZE_MID_TEAR);
	}

	@Order(6)
	@Test
	void sortMergeUpBottom() {
		testSort(new SortMergeUpBottom(), PROBLEM_SIZE_MID_TEAR);
	}

	@Order(7)
	@Test
	void sortMergeUbBottomOptimized() {
		testSort(new SortMergeUpBottomOptimized(), PROBLEM_SIZE_MID_TEAR);
	}

	@Order(8)
	@Test
	void sortMergeBottomUp() {
		testSort(new SortMergeBottomUp(), PROBLEM_SIZE_MID_TEAR);
	}

	@Order(9)
	@Test
	void sortQuick() {
		testSort(new SortQuick(), PROBLEM_SIZE_MID_TEAR);
	}

	@Order(10)
	@Test
	void sortQuickCutoff() {
		testSort(new SortQuickCutoff(), PROBLEM_SIZE_MID_TEAR);
	}

	@Order(11)
	@Test
	void sortQuick3Tear() {
		testSort(new SortQuick3WayPartitioning(), PROBLEM_SIZE_MID_TEAR);
	}

	private void testSort(Sort sort, int problemSize) {
		tryGs();

		Stopwatch stp = new Stopwatch();

		int[] arr = generateArray(problemSize);
		sort.sort(arr);

		double elapsedTime = stp.elapsedTime();
		StdOut.printf("'%s', problem size = '%d', time '%d' sec",
				sort.getClass().getSimpleName(), problemSize, Math.round(elapsedTime));

		assertThat(sort.isSorted(arr)).isTrue();
	}

	private int[] generateArray(int problemSize) {
		int[] arr = new int[problemSize];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = random.nextInt(problemSize);
		}
		return arr;
	}

	private void tryGs() {
		System.gc();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

}
