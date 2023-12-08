package com.bondarenko.leetcode.n0000.n000;

/*
 URL: https://leetcode.com/problems/longest-substring-without-repeating-characters
 Time: N
 Space: 1
 */
public class LongestSubstringWithoutRepeatingCharacters3 {

	// sliding window
	public int lengthOfLongestSubstring(String s) {
		var seen = new boolean[128];
		var ans = 0;
		for (int left = 0, right = 0; right < s.length(); right++) {
			var idx = s.charAt(right);
			while (seen[idx])
				seen[s.charAt(left++)] = false;
			seen[idx] = true;
			ans = Math.max(ans, right - left + 1);
		}
		return ans;
	}

}
