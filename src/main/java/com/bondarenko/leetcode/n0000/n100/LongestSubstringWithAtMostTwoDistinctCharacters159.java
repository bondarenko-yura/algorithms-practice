package com.bondarenko.leetcode.n0000.n100;

/*
 https://leetcode.com/problems/longest-substring-with-at-most-two-distinct-characters
 Time: N
 Space: 1
 */
public class LongestSubstringWithAtMostTwoDistinctCharacters159 {

	private static final int MAX_DST = 2;

	// sliding window
	public int lengthOfLongestSubstringTwoDistinct(String s) {
		var cnt = new int['z' - 'A' + 1];
		var dst = 0;
		var ans = 0;
		for (int left = 0, right = 0; right < s.length(); right++) {
			if (++cnt[s.charAt(right) - 'A'] == 1)
				dst++;
			while (dst > MAX_DST)
				if (--cnt[s.charAt(left++) - 'A'] == 0)
					dst--;
			ans = Math.max(ans, right - left + 1);
		}
		return ans;
	}

}
