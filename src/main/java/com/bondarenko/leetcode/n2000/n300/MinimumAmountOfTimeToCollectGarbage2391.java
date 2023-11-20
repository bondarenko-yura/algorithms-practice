package com.bondarenko.leetcode.n2000.n300;

/*
 URL: https://leetcode.com/problems/minimum-amount-of-time-to-collect-garbage
 N - len(garbage)
 M - len(garbage[x])
 Time: NM
 Space: 1
 */
public class MinimumAmountOfTimeToCollectGarbage2391 {

	// array
	public int garbageCollection(String[] garbage, int[] travel) {
		int m = 0, p = 0, g = 0;
		for (int i = garbage.length - 1; i >= 0; i--) {
			for (int j = 0; j < garbage[i].length(); j++) {
				var ch = garbage[i].charAt(j);
				if (ch == 'M')
					m++;
				else if (ch == 'P')
					p++;
				else
					g++;
			}
			var price = (i == 0 ? 0 : travel[i - 1]);
			if (m > 0)
				m += price;
			if (p > 0)
				p += price;
			if (g > 0)
				g += price;
		}
		return m + p + g;
	}
}
