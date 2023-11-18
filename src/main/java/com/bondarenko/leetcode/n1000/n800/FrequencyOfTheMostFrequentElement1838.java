package com.bondarenko.leetcode.n1000.n800;

import java.util.Arrays;

/*
 URL: https://leetcode.com/problems/frequency-of-the-most-frequent-element
 Time: NLogN
 Space: 1
 */
public class FrequencyOfTheMostFrequentElement1838 {

	// sliding window
	public int maxFrequency(int[] nums, int k) {
		Arrays.sort(nums);
		var ans = 1;
		for (int l = 0, r = 1; r < nums.length; r++) {
			k -= (nums[r] - nums[r - 1]) * (r - l);
			while (k < 0)
				k += nums[r] - nums[l++];
			ans = Math.max(ans, r - l + 1);
		}
		return ans;
	}

}
