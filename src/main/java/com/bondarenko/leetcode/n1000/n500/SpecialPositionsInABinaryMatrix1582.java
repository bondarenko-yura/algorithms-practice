package com.bondarenko.leetcode.n1000.n500;

/*
 URL: https://leetcode.com/problems/special-positions-in-a-binary-matrix
 Time: MN
 Space: M + N
 */
public class SpecialPositionsInABinaryMatrix1582 {

	// count by each row and col
	public int numSpecial(int[][] mat) {
		var row = new int[mat.length];
		var col = new int[mat[0].length];
		for (int r = 0; r < mat.length; r++) {
			for (int c = 0; c < mat[r].length; c++) {
				if (mat[r][c] == 1) {
					row[r]++;
					col[c]++;
				}
			}
		}
		var ans = 0;
		for (int r = 0; r < mat.length; r++)
			for (int c = 0; c < mat[r].length; c++)
				if (mat[r][c] == 1 && row[r] == 1 && col[c] == 1)
					ans++;
		return ans;
	}

}
