package com.bondarenko.leetcode.n0000.n000;

/*
 URL: https://leetcode.com/problems/trapping-rain-water
 Time: N
 Space: 1
 */
public class TrappingRainWater42 {

	// two pointer
	public int trap(int[] height) {
		int ans = 0, lmax = 0, l = 0, r = height.length - 1, rmax = height.length - 1;
		while (l < r) {
			if (height[l] < height[r]) {
				l++;
				if (height[l] >= height[lmax])
					lmax = l;
				else
					ans += height[lmax] - height[l];
			} else {
				r--;
				if (height[r] >= height[rmax])
					rmax = r;
				else
					ans += height[rmax] - height[r];
			}
		}
		return ans;
	}

}
