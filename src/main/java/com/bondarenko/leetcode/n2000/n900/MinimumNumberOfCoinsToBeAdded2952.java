package com.bondarenko.leetcode.n2000.n900;

import java.util.Arrays;

/*
 URL: https://leetcode.com/problems/minimum-number-of-coins-to-be-added/
 Time: NLogN
 Space: 1
 */
public class MinimumNumberOfCoinsToBeAdded2952 {

	// sort
	public int minimumAddedCoins(int[] coins, int target) {
		Arrays.sort(coins);
		var reachable = 0;
		var addition = 0;
		var c = 0;
		while (reachable < target) {
			if (c < coins.length && coins[c] <= reachable + 1) {
				reachable += coins[c++];
			} else {
				addition++;
				reachable += reachable + 1;
			}
		}
		return addition;
	}

}
