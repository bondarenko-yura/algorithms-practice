package com.bondarenko.leetcode.n1000.n700;

/*
 URL: https://leetcode.com/problems/minimum-changes-to-make-alternating-binary-string
 Time: N
 Space: 1
 */
public class MinimumChangesToMakeAlternatingBinaryString1758 {

	// iteration
	public int minOperations(String s) {
		var cnt0 = 0;
		var cnt1 = 0;
		for (int i = 0; i < s.length(); i++) {
			var c = s.charAt(i);
			if (c != (i % 2 == 0 ? '0' : '1'))
				cnt0++;
			if (c != (i % 2 == 0 ? '1' : '0'))
				cnt1++;
		}
		return Math.min(cnt0, cnt1);
	}

}
