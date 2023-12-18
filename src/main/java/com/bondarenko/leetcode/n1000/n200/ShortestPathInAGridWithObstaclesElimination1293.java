package com.bondarenko.leetcode.n1000.n200;

import java.util.ArrayDeque;

/*
 URL: https://leetcode.com/problems/valid-anagram
 Time: NMK
 Space: NMK
 */
public class ShortestPathInAGridWithObstaclesElimination1293 {

	private static final int[][] DIR = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

	// dfs
	public int shortestPath(int[][] grid, int k) {
		int row = grid.length, col = grid[0].length;
		var memo = new int[row][col][k + 1];
		var q = new ArrayDeque<int[]>(); // {row, col, eliminateCnt}
		var initK = k - grid[0][0];
		if (initK >= 0) {
			if (row == 1 && col == 1)
				return 0;
			q.add(new int[]{0, 0, initK});
		}
		var ans = 0;
		while (!q.isEmpty()) {
			ans++;
			var size = q.size();
			while (size-- > 0) {
				var cur = q.poll();
				int r = cur[0], c = cur[1], e = cur[2];
				for (int[] d : DIR) {
					int nr = r + d[0], nc = c + d[1];
					if (nr < 0 || nr == row || nc < 0 || nc == col)
						continue;
					var ne = e - grid[nr][nc];
					if (ne < 0 || memo[nr][nc][ne] > 0)
						continue;
					if (nr == row - 1 && nc == col - 1)
						return ans;
					memo[nr][nc][ne] = ans;
					q.add(new int[] {nr, nc, ne});
				}
			}
		}
		return -1;
	}

}
