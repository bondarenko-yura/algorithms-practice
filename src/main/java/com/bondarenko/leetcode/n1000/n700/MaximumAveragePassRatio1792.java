package com.bondarenko.leetcode.n1000.n700;

import java.util.Comparator;
import java.util.PriorityQueue;

/*
 URL: https://leetcode.com/problems/maximum-average-pass-ratio/description
 Time: KLogN + NLogL
 Space: N + K
 */
public class MaximumAveragePassRatio1792 {
	public double maxAverageRatio(int[][] classes, int extraStudents) {
		var heap = new PriorityQueue<>(Comparator.comparingDouble(this::gain).reversed());
		for (int[] c : classes)
			heap.add(new int[] {c[0], c[1]});
		while (extraStudents-- > 0) {
			var c = heap.poll();
			c[0]++;
			c[1]++;
			heap.add(c);
		}
		double total = 0;
		while (!heap.isEmpty()) {
			var c = heap.poll();
			total += (double) c[0] / c[1];
		}
		return total / classes.length;
	}

	private double gain(int[] v) {
		return ((double) (v[0] + 1) / (v[1] + 1)) - ((double) v[0] / v[1]);
	}
}
