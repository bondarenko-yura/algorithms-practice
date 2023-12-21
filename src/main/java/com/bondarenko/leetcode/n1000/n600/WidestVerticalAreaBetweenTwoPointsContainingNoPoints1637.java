package com.bondarenko.leetcode.n1000.n600;

import java.util.Arrays;
import java.util.Comparator;

/*
 URL: https://leetcode.com/problems/widest-vertical-area-between-two-points-containing-no-points
 Time: NLogN
 Space: 1
 */
public class WidestVerticalAreaBetweenTwoPointsContainingNoPoints1637 {

	// iteration
	public int maxWidthOfVerticalArea(int[][] points) {
		var pt = Arrays.copyOf(points, points.length);
		Arrays.sort(pt, Comparator.comparingInt(x -> x[0]));
		var ans = 0;
		for (int i = 1; i < pt.length; i++)
			ans = Math.max(ans, pt[i][0] - pt[i - 1][0]);
		return ans;
	}

}
