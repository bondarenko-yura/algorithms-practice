package com.bondarenko.leetcode.n0000.n400;

/*
 URL: https://leetcode.com/problems/max-consecutive-ones-ii
 Time: N
 Space: 1
*/
public class MaxConsecutiveOnesII487 {

	// sliding window
	public int findMaxConsecutiveOnes(int[] nums) {
		var flip = 1;
		var ans = 0;
		for (int left = 0, right = 0; right < nums.length; right++) {
			if (nums[right] == 0)
				flip--;
			while (flip < 0)
				if (nums[left++] == 0)
					flip++;
			ans = Math.max(ans, right - left + 1);
		}
		return ans;
	}

}
