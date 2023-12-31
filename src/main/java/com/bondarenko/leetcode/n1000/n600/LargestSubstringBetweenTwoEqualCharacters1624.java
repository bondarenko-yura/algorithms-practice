package com.bondarenko.leetcode.n1000.n600;

import java.util.Arrays;

/*
 URL: https://leetcode.com/problems/largest-substring-between-two-equal-characters
 Time: N
 Space: 1
 */
public class LargestSubstringBetweenTwoEqualCharacters1624 {

	// memorize first position
	public int maxLengthBetweenEqualCharacters(String s) {
		var pos = new int[26];
		Arrays.fill(pos, -1);
		var ans = -1;
		for (int i = 0; i < s.length(); i++) {
			var idx = s.charAt(i) - 'a';
			if (pos[idx] == -1)
				pos[idx] = i;
			else
				ans = Math.max(ans, i - pos[idx] - 1);
		}
		return ans;
	}

}
