package com.bondarenko.leetcode.n2000.n700;

/*
 URL: https://leetcode.com/problems/buy-two-chocolates/
 Time: N
 Space: 1
 */
public class BuyTwoChocolates2706 {

	// iteration
	public int buyChoco(int[] prices, int money) {
		var min1 = Integer.MAX_VALUE;
		var min2 = Integer.MAX_VALUE;
		for (int p : prices) {
			if (p < min1) {
				min2 = min1;
				min1 = p;
			} else if (p < min2) {
				min2 = p;
			}
		}
		var sum = min1 + min2;
		return sum <= money ? money - sum : money;
	}

}
