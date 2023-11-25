package com.bondarenko.leetcode.n1000.n600;

/*
 URL: https://leetcode.com/problems/sum-of-absolute-differences-in-a-sorted-array
 Time: N
 Space: 1
 */
public class SumOfAbsoluteDifferencesInASortedArray1685 {

	// prefixSum
	public int[] getSumAbsoluteDifferences(int[] nums) {
		var sum = 0;
		for (int n : nums)
			sum += n;
		var prefSum = 0;
		var ans = new int[nums.length];
		for (int i = 0; i < nums.length; i++) {
			var left = i * nums[i] - prefSum;
			var right = (sum - prefSum) - (nums.length - i) * nums[i];
			ans[i] = left + right;
			prefSum += nums[i];
		}
		return ans;
	}

}
