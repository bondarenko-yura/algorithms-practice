package com.bondarenko.leetcode.n1000.n200;

/*
 URL: https://leetcode.com/problems/element-appearing-more-than-25-in-sorted-array
 Time: N
 Space: 1
 */
public class ElementAppearingMoreThan25PercentInSortedArray1287 {

	// iteration
	public int findSpecialInteger(int[] arr) {
		var ans = -1;
		var maxCnt = 0;
		var lastVal = -1;
		var cnt = 0;
		for (int v : arr) {
			if (v != lastVal) {
				cnt = 1;
				lastVal = v;
			}
			if (++cnt > maxCnt) {
				ans = v;
				maxCnt = cnt;
			}
		}
		return ans;
	}

}
