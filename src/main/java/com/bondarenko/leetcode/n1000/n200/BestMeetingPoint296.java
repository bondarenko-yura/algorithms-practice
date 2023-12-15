package com.bondarenko.leetcode.n1000.n200;

/*
 URL: https://leetcode.com/problems/best-meeting-point
 Time: MN
 Space: M + N
 */
public class BestMeetingPoint296 {

	// prefix sum
	public int minTotalDistance(int[][] grid) {
		var rowStartDist = 0;
		var row = new int[grid.length];
		var col = new int[grid[0].length];
		for (int r = 0; r < row.length; r++) {
			for (int c = 0; c < col.length; c++) {
				if (grid[r][c] == 1) {
					rowStartDist += r + c;
					row[r]++;
					col[c]++;
				}
			}
		}
		prefSum(row);
		prefSum(col);
		var ans = rowStartDist;
		for (int upCnt : row) {
			var cur = rowStartDist;
			ans = Math.min(ans, cur);
			for (int c = 1; c < col.length; c++) {
				var rightCnt = col[col.length - 1] - col[c - 1];
				var leftCnt = col[c - 1];
				cur = cur - rightCnt + leftCnt;
				ans = Math.min(ans, cur);
			}
			var downCnt = row[row.length - 1] - upCnt;
			rowStartDist = rowStartDist - downCnt + upCnt;
		}
		return ans;
	}

	private void prefSum(int[] arr) {
		for (int i = 1; i < arr.length; i++)
			arr[i] += arr[i - 1];
	}

}
