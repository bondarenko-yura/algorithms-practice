package com.bondarenko.leetcode.n0000.n000;

/*
 URL: https://leetcode.com/problems/median-of-two-sorted-arrays/
 Time: logN
 Space: 1
 */
public class MedianOfTwoSortedArrays4 {

	private static int getVal(int idx, int[] arr) {
		if (idx < 0)
			return Integer.MIN_VALUE;
		if (idx >= arr.length)
			return Integer.MAX_VALUE;
		return arr[idx];
	}

	// binary search
	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		var len = nums1.length + nums2.length;
		var half = len / 2;
		var lo = 0;
		var hi = nums1.length - 1;
		while (true) {
			var mid1 = (int) Math.floor((lo + hi) / 2d);
			var mid2 = half - mid1 - 2;
			var left1 = getVal(mid1, nums1);
			var right1 = getVal(mid1 + 1, nums1);
			var left2 = getVal(mid2, nums2);
			var right2 = getVal(mid2 + 1, nums2);
			if (left1 > right2) {
				hi = mid1 - 1;
			} else if (left2 > right1) {
				lo = mid1 + 1;
			} else {
				return len % 2 == 1
						? Math.min(right1, right2)
						: (Math.max(left1, left2) + Math.min(right1, right2)) / 2d;
			}
		}
	}

}
