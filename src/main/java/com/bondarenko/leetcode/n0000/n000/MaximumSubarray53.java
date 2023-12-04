package com.bondarenko.leetcode.n0000.n000;

/*
 URL: https://leetcode.com/problems/maximum-subarray/
 Time: N
 Space: 1
 */
public class MaximumSubarray53 {

	// prefix sum
	public int maxSubArray(int[] nums) {
		var ans = nums[0];
		var cur = nums[0];
		for (int i = 1; i < nums.length; i++) {
			cur = Math.max(nums[i], nums[i] + cur);
			ans = Math.max(ans, cur);
		}
		return ans;
	}

}
