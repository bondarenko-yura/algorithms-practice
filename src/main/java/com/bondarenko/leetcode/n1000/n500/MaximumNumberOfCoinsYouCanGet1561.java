package com.bondarenko.leetcode.n1000.n500;

import java.util.Arrays;

/*
 URL: https://leetcode.com/problems/maximum-number-of-coins-you-can-get
 Time: NLogN
 Space: 1
 */
public class MaximumNumberOfCoinsYouCanGet1561 {

	// sort
	public int maxCoins(int[] piles) {
		Arrays.sort(piles);
		var ans = 0;
		var n = piles.length;
		var lim = n - n / 3 * 2 - 1;
		for (int i = n - 2; i >= lim; i -= 2)
			ans += piles[i];
		return ans;
	}

}
