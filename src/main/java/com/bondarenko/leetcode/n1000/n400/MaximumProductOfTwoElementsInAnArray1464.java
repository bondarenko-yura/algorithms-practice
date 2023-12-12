package com.bondarenko.leetcode.n1000.n400;

/*
 URL: https://leetcode.com/problems/maximum-product-of-two-elements-in-an-array
 Time: x
 Space: x
 */
public class MaximumProductOfTwoElementsInAnArray1464 {

	// iteration
	public int maxProduct(int[] nums) {
		var n1 = 0;
		var n2 = 0;
		for (int n : nums) {
			if (n1 < n) {
				n2 = n1;
				n1 = n;
			} else if (n2 < n) {
				n2 = n;
			}
		}
		return (n1 - 1) * (n2 - 1);
	}

}
