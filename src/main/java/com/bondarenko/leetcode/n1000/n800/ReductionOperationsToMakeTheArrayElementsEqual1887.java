package com.bondarenko.leetcode.n1000.n800;

import java.util.Arrays;

/*
 URL: https://leetcode.com/problems/reduction-operations-to-make-the-array-elements-equal
 Time: NLogN
 Space: 1
 */
public class ReductionOperationsToMakeTheArrayElementsEqual1887 {

	// sort, sliding window
	public int reductionOperations(int[] nums) {
		Arrays.sort(nums);
		var ans = 0;
		for (int i = 1; i < nums.length; i++) {
			if (nums[i - 1] == nums[i])
				continue;
			ans += nums.length - i;
		}
		return ans;
	}
}
