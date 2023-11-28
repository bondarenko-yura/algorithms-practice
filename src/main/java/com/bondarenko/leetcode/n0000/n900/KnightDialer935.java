package com.bondarenko.leetcode.n0000.n900;

import java.util.Arrays;

/*
 URL: https://leetcode.com/problems/knight-dialer
 Time: N
 Space: 1
*/
public class KnightDialer935 {

	private static final int[][] MOVES =
			{{4, 6}, {6, 8}, {7, 9}, {4, 8}, {3, 9, 0}, {}, {1, 7, 0}, {2, 6}, {1, 3}, {2, 4}};
	private static final int DIGITS = 10;
	private static final int MOD = 1000000007;

	// dp
	public int knightDialer(int n) {
		var cur = new int[DIGITS];
		var pre = new int[DIGITS];
		Arrays.fill(pre, 1);
		for (int i = 1; i < n; i++) {
			for (int j = 0; j < DIGITS; j++) {
				var cnt = 0;
				for (int m : MOVES[j])
					cnt = (cnt + pre[m]) % MOD;
				cur[j] = cnt;
			}
			var tmp = pre;
			pre = cur;
			cur = tmp;
		}
		var ans = 0;
		for (int p : pre)
			ans = (ans + p) % MOD;
		return ans;
	}

}
