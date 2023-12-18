package com.bondarenko.leetcode.n1000.n900;

/*
 URL: https://leetcode.com/problems/maximum-product-difference-between-two-pairs/
 Time: N
 Space: 1
 */
public class MaximumProductDifferenceBetweenTwoPairs1913 {

	// find two largest and smallest
	public int maxProductDifference(int[] nums) {
		int max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE;
		int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
		for (int n : nums) {
			if (n > max1) {
				max2 = max1;
				max1 = n;
			} else if (n > max2) {
				max2 = n;
			}
			if (n < min1) {
				min2 = min1;
				min1 = n;
			} else if (n < min2) {
				min2 = n;
			}
		}
		return max1 * max2 - min1 * min2;
	}

}
