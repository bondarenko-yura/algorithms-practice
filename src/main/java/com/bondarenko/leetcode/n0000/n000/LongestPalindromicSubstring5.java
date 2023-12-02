package com.bondarenko.leetcode.n0000.n000;

/*
 URL: https://leetcode.com/problems/longest-palindromic-substring
 Time: N^2
 Space: 1
 */
public class LongestPalindromicSubstring5 {

	// bruteforce
	public String longestPalindrome(String s) {
		var rlt = 0;
		var rrt = 0;
		for (int i = 0; i < s.length(); i++) {
			if (i > 0 && s.charAt(i - 1) == s.charAt(i))
				continue;
			var lt = i;
			var rt = i;
			while (rt + 1 < s.length() && s.charAt(lt) == s.charAt(rt + 1))
				rt++;
			while (lt > 0 && rt + 1 < s.length() && s.charAt(lt - 1) == s.charAt(rt + 1)) {
				lt--;
				rt++;
			}
			if (rrt - rlt < rt - lt) {
				rlt = lt;
				rrt = rt;
			}
		}
		return s.substring(rlt, rrt + 1);
	}

}
