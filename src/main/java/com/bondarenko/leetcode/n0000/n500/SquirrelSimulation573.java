package com.bondarenko.leetcode.n0000.n500;

public class SquirrelSimulation573 {

	/*
	URL: https://leetcode.com/problems/squirrel-simulation
	Time: N
	Space: N
	 */
	public int minDistance(int height, int width, int[] tree, int[] squirrel, int[][] nuts) {
		var dist = 0;
		var d = Integer.MIN_VALUE;
		for (int[] nut : nuts) {
			dist += (distance(nut, tree) * 2);
			d = Math.max(d, distance(nut, tree) - distance(nut, squirrel));
		}
		return dist - d;
	}

	private static int distance(int[] a, int[] b) {
		return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
	}
}
