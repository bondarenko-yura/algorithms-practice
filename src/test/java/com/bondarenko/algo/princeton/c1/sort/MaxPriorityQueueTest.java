package com.bondarenko.algo.princeton.c1.sort;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class MaxPriorityQueueTest {

	public static final int PROBLEM_SIZE = 1_000_000;

	@Test
	void queueTest() {
		MaxPriorityQueue<Integer> queue = new MaxPriorityQueue<>();
		int[] input = new int[PROBLEM_SIZE];
		Random random = new Random(PROBLEM_SIZE);
		for (int i = 0; i < input.length; i++) {
			input[i] = random.nextInt(PROBLEM_SIZE * 10);
			queue.insert(input[i]);
		}
		new SortQuick().sort(input);
		for (int i = input.length - 1; i >= 0; i--) {
			assertThat(queue.size() == i);
			assertThat(queue.delMax()).isEqualTo(input[i]);
		}

		assertThat(queue.isEmpty()).isTrue();
	}

}