package com.bondarenko.leetcode.n1000.n700;

/*
 URL: https://leetcode.com/problems/calculate-money-in-leetcode-bank
 Time: N
 Space: 1
 */
public class CalculateMoneyInLeetcodeBank1716 {

	private static final int DAYS_PER_WEEK = 7;

	private static int sumOfArithmeticProgression(int first, int elemCount, int dif) {
		//https://en.wikipedia.org/wiki/Arithmetic_progression
		return (elemCount * (2 * first + (elemCount - 1)) * dif) / 2;
	}

	public int totalMoney(int n) {
		var ans = 0;
		var totalWeeksCount = (n + DAYS_PER_WEEK - 1) / DAYS_PER_WEEK;
		var dayOfWeek = n % DAYS_PER_WEEK == 0 ? DAYS_PER_WEEK : n % DAYS_PER_WEEK;
		for (int i = 1; i <= DAYS_PER_WEEK; i++) {
			var includeWeeks = i <= dayOfWeek ? totalWeeksCount : totalWeeksCount - 1;
			ans += sumOfArithmeticProgression(i, includeWeeks, 1);
		}
		return ans;
	}

}
