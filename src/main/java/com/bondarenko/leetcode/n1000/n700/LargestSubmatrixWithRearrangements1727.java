package com.bondarenko.leetcode.n1000.n700;

import java.util.Arrays;

/*
 URL: https://leetcode.com/problems/largest-submatrix-with-rearrangements/
 Time: R * CLogC
 Space: RC
 */
public class LargestSubmatrixWithRearrangements1727 {

	// prefix sum, sort
	public int largestSubmatrix(int[][] matrix) {
		for (int r = 1; r < matrix.length; r++)
			for (int c = 0; c < matrix[r].length; c++)
				if (matrix[r][c] != 0)
					matrix[r][c] += matrix[r - 1][c];
		var ans = 0;
		for (int[] row : matrix) {
			Arrays.sort(row);
			for (int c = 0; c < row.length; c++) {
				ans = Math.max(ans, (row.length - c) * row[c]);
			}
		}
		return ans;
	}

}
