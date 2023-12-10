package com.bondarenko.leetcode.n2000.n900;

/*
 URL: https://leetcode.com/problems/count-subarrays-where-max-element-appears-at-least-k-times
 Time: N
 Space: 1
 */
public class CountSubarraysWhereMaxElementAppearsAtLeastKTimes2962 {

	// sliding window
	public long countSubarrays(int[] nums, int k) {
		var m = Integer.MIN_VALUE;
		for (int n : nums)
			m = Math.max(n, m);
		var cnt = 0;
		var ans = 0L;
		for (int left = 0, right = 0; right < nums.length; right++) {
			if (nums[right] == m)
				cnt++;
			while (cnt >= k) {
				ans += nums.length - right;
				if (nums[left++] == m)
					cnt--;
			}
		}
		return ans;
	}

}
