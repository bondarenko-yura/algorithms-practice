package com.bondarenko.leetcode.n2000.n200;

/*
 URL: https://leetcode.com/problems/largest-3-same-digit-number-in-string
 Time: N
 Space: 1
*/
public class Largest3SameDigitNumberInString2264 {

	// iteration
	public String largestGoodInteger(String num) {
		var ans = -1;
		for (int i = 0; i < num.length() - 2; i++) {
			if (num.charAt(i) != num.charAt(i + 1) || num.charAt(i) != num.charAt(i + 2))
				continue; // not same
			if (ans != -1 && num.charAt(ans) > num.charAt(i))
				continue; // current answer is better
			ans = i;
		}
		return ans == -1 ? "" : num.substring(ans, ans + 3);
	}

}
