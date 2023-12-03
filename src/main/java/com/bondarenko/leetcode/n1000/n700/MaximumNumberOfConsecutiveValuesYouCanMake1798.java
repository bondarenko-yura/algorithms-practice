package com.bondarenko.leetcode.n1000.n700;

import java.util.Arrays;

/*
 URL: https://leetcode.com/problems/maximum-number-of-consecutive-values-you-can-make
 Time: NLogN
 Space: 1
 */
public class MaximumNumberOfConsecutiveValuesYouCanMake1798 {

	// sort
	public int getMaximumConsecutive(int[] coins) {
		Arrays.sort(coins);
		var reachable = 1;
		for (int c : coins) {
			if (c > reachable)
				break;
			reachable += c;
		}
		return reachable;
	}

}
