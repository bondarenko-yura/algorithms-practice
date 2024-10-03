package com.bondarenko.leetcode.n1000.n500;

import java.util.HashMap;

/*
 URL: https://leetcode.com/problems/make-sum-divisible-by-p/
 Time: n
 Space: n
 */
public class MakeSumDivisibleByP1590 {
	// Prefix Sum Modulo
	public int minSubarray(int[] nums, int p) {
		int totalSum = 0;

		// Step 1: Calculate total sum and target remainder
		for (int num : nums)
			totalSum = (totalSum + num) % p;

		var target = totalSum % p;
		if (target == 0)
			return 0; // The array is already divisible by p

		// Step 2: Use a hash map to track prefix sum mods p
		var mods = new HashMap<Integer, Integer>();
		mods.put(0, -1); // To handle the case where the whole prefix is the answer

		var currentSum = 0;
		var minLen = nums.length;

		// Step 3: Iterate over the array
		for (int i = 0; i < nums.length; ++i) {
			currentSum = (currentSum + nums[i]) % p;

			// Calculate what we need to remove
			int needed = (currentSum - target + p) % p;

			// If we have seen the needed remainder, we can consider this subarray
			if (mods.containsKey(needed))
				minLen = Math.min(minLen, i - mods.get(needed));

			// Store the current remainder and index
			mods.put(currentSum, i);
		}

		// Step 4: Return result
		return minLen == nums.length ? -1 : minLen;
	}
}
