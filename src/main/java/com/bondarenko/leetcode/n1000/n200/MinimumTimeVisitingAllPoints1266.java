package com.bondarenko.leetcode.n1000.n200;

/*
 URL: https://leetcode.com/problems/minimum-time-visiting-all-points
 Time: N
 Space: 1
 */
public class MinimumTimeVisitingAllPoints1266 {

	// iteration
	public int minTimeToVisitAllPoints(int[][] points) {
		var ans = 0;
		for (int i = 1; i < points.length; i++)
			ans += Math.max(
					Math.abs(points[i - 1][0] - points[i][0]),
					Math.abs(points[i - 1][1] - points[i][1]));
		return ans;
	}

}
