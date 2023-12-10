package com.bondarenko.leetcode.n0000.n800;

/*
 URL: https://leetcode.com/problems/transpose-matrix
 Time: MN
 Space: 1
 */
public class TransposeMatrix867 {

	// iteration
	public int[][] transpose(int[][] matrix) {
		var row = matrix.length;
		var col = matrix[0].length;
		var res = new int[col][row];
		for (int r = 0; r < row; r++)
			for (int c = 0; c < col; c++)
				res[c][r] = matrix[r][c];
		return res;
	}

}
