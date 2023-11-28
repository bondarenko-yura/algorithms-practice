package com.bondarenko.leetcode.n2000.n100;

/*
 URL: https://leetcode.com/problems/number-of-ways-to-divide-a-long-corridor
 Time: N
 Space: 1
*/
public class NumberOfWaysToDivideALongCorridor2147 {

	private static final int MOD = 1000000007;

	// sliding window
	public int numberOfWays(String corridor) {
		var cnt = 0;
		var ans = 1L;
		for (int left = 0, right = 0; right < corridor.length(); right++) {
			if (corridor.charAt(right) == 'P')
				continue;
			cnt++;
			if (cnt < 2)
				continue;
			if (cnt % 2 == 0)
				left = right + 1;
			else
				ans = (ans * (right - left + 1)) % MOD;
		}
		return cnt > 0 && cnt % 2 == 0 ? (int) ans : 0;
	}

}
