package com.bondarenko.leetcode.n2000.n100;

import java.util.Arrays;
import java.util.Comparator;

/*
 URL: https://leetcode.com/problems/sort-the-jumbled-numbers/
 Time: NLogN
 Space: N
*/
public class SortTheJumbledNumbers2191 {
	public int[] sortJumbled(int[] mapping, int[] nums) {
		var jumbled = new int[nums.length][];
		for (int i = 0; i < nums.length; i++)
			jumbled[i] = new int[] {convert(nums[i], mapping), i, nums[i]};
		Arrays.sort(jumbled, Comparator.comparingInt((int[] a) -> a[0]).thenComparingInt(a -> a[1]));
		var ans = new int[nums.length];
		for (int i = 0; i < jumbled.length; i++)
			ans[i] = jumbled[i][2];
		return ans;
	}

	private static int convert(int n, int[] mapping) {
		var mult = 1;
		var v = 0;
		do {
			v += mapping[n % 10] * mult;
			n /= 10;
			mult *= 10;
		} while (n > 0);
		return v;
	}
}
