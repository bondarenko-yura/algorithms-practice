package com.bondarenko.ds.heap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MaxPriorityQueueTest {

	@Test
	void queueTest() {
		assertThat(minOperations("1100011000", "0101001010", 2)).isEqualTo(4);
		assertThat(minOperations("10110", "00011", 4)).isEqualTo(-1);
	}

	@Test
	void queueTest2() {
		assertThat(maxSum(Arrays.asList(25, 52, 75, 65), 4)).isEqualTo(24051);
		assertThat(maxSum(Arrays.asList(2, 6, 5, 8), 2)).isEqualTo(261);
		assertThat(maxSum(Arrays.asList(4, 5, 4, 7), 3)).isEqualTo(90);
	}

	public int maxSum(List<Integer> nums, int k) {
		Collections.sort(nums);
		var part = nums.size() - k;
		for (int i = 0; i < nums.size() - 1; i++) {
			for (int j = nums.size() - 1; j >= 0 && nums.get(i) > 0; j--) {
				var red = nums.get(i);
				var cur = nums.get(j);
				var inc = cur | red;
				var nextRed = cur & red;
				if (i < part) {
					if (inc > cur) {
						nums.set(j, inc);
						nums.set(i, nextRed);
					}
				} else {
					if ((long) cur * cur + (long) red * red < (long) inc * inc + (long) nextRed * nextRed) {
						nums.set(j, inc);
						nums.set(i, nextRed);
					}
				}
			}
		}
		var ans = 0L;
		for (int j = nums.size() - 1; j >= part; j--) {
			ans = (ans + (long) nums.get(j) * nums.get(j)) % 1000000007;
		}
		return (int) ans;
	}

	public int minOperations(String s1, String s2, int x) {
		return dp(0, 0, x, s1, s2);
	}

	private int dp(int i, int j, int x, String s1, String s2) {
		if (i == s1.length() && j == s2.length())
			return 0;
		if (s1.charAt(i) != s2.charAt(j))
			return Integer.MAX_VALUE;

		var ans = Integer.MAX_VALUE;
		if (s1.charAt(i) == s2.charAt(j)) {
			ans = Math.min(ans, dp(i + 1, j + 1, x, s1, s2));
		}
		return 0;
	}

}
