package com.bondarenko.leetcode.n1000.n800;

/*
 URL: https://leetcode.com/problems/count-nice-pairs-in-an-array
 Time: x
 Space: x
 */
public class CountNicePairsInAnArray1814 {

	// x
	public int countNicePairs(int[] nums) {
		var adj = new int[nums.length];
		for (int i = 0; i < nums.length; i++) {
			var k = nums[i];
			var r = 0;
			while (k > 0) {
				r = r * 10 + k % 10;
				k /= 10;
			}
			adj[i] = nums[i] - r;
		}
		var ans = 0;
		for (int i = 0; i < nums.length - 1; i++) {
			for (int j = i + 1; j < nums.length; j++) {
				if (adj[i] == adj[j])
					ans++;
				if (ans == 1000000007)
					ans = 0;
			}
		}
		return ans;
	}

}
