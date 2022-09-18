package com.bondarenko.algo.misc;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HashingUtilTest {

	private static int dfs(int l, int r, int k, int[] w, int[][][] memo) {
		if (r == w.length)
			return 0;
		if (memo[l][r][k] > 0)
			return memo[l][r][k];
		var min = Integer.MAX_VALUE;
		var max = Integer.MIN_VALUE;
		if (k == 1) {
			min = w[l] + w[w.length - 1];
			max = min;
		} else {
			while (r <= w.length - k) {
				var sum = dfs(r + 1, r + 1, k - 1, w, memo);
				min = Math.min(w[l] + w[r] + sum, min);
				max = Math.max(w[l] + w[r] + sum, max);
				r++;
			}
		}
		return memo[l][r][k] = max - min;
	}

	@Test
	void hash() {
		assertThat(putMarbles(new int[]{1, 3, 5, 1}, 2)).isEqualTo(4);
		assertThat(putMarbles(new int[]{1, 3}, 2)).isEqualTo(0);
	}

	public long putMarbles(int[] w, int k) {
		return dfs(0, 0, k, w, new int[w.length][w.length][k + 1]);
	}

	/**
	 * Definition for singly-linked list.
	 */
	public class ListNode {

		int val;
		ListNode next;

		ListNode() {
		}

		ListNode(int val) {
			this.val = val;
		}

		ListNode(int val, ListNode next) {
			this.val = val;
			this.next = next;
		}

		@Override
		public String toString() {
			return "ListNode{" +
					"val=" + val +
					'}';
		}

	}

}
