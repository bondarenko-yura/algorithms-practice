package com.bondarenko.leetcode.n0000.n200;

import java.util.Comparator;
import java.util.PriorityQueue;

public class FindMedianFromDataStream295 {

	static class MedianFinder {

		private final PriorityQueue<Integer> max = new PriorityQueue<>();
		private final PriorityQueue<Integer> min = new PriorityQueue<>(Comparator.reverseOrder());

		public MedianFinder() {
		}

		public void addNum(int num) {
			if (!min.isEmpty() && num <= min.peek()) {
				min.add(num);
				if (min.size() > max.size() + 1)
					max.add(min.poll());
			} else {
				max.add(num);
				if (max.size() > min.size() + 1)
					min.add(max.poll());
			}
		}

		public double findMedian() {
			var size = min.size() + max.size();
			if (size == 0)
				throw new IllegalStateException();
			if (size % 2 == 0)
				return (min.peek() + max.peek()) / 2d;
			return min.size() > max.size() ? min.peek() : max.peek();
		}

	}

}
