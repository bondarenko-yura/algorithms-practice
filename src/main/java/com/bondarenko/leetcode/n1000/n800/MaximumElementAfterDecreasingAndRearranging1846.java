package com.bondarenko.leetcode.n1000.n800;

import java.util.Arrays;

/*
 URL: https://leetcode.com/problems/maximum-element-after-decreasing-and-rearranging
 Time: NLogN
 Space: 1
 */
public class MaximumElementAfterDecreasingAndRearranging1846 {

	public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
		Arrays.sort(arr);
		arr[0] = 1;
		for (int i = 1; i < arr.length; i++)
			if (arr[i - 1] + 1 < arr[i])
				arr[i] = arr[i - 1] + 1;
		return arr[arr.length - 1];
	}

}
