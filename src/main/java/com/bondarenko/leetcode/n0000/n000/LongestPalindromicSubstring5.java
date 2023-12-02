package com.bondarenko.leetcode.n0000.n000;

/*
 URL: https://leetcode.com/problems/longest-palindromic-substring
 Time: N^2
 Space: 1
 */
public class LongestPalindromicSubstring5 {

	// bruteforce (expand from center)
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

	// Time N - Manacher's Algorithm
	public String longestPalindromeManachersAlgorithm(String s) {
		var sPrime = new StringBuilder("#");
		for (char c : s.toCharArray())
			sPrime.append(c).append("#");

		var n = sPrime.length();
		var palindromeRadii = new int[n];
		var center = 0;
		var radius = 0;

		for (int i = 0; i < n; i++) {
			var mirror = 2 * center - i;

			if (i < radius)
				palindromeRadii[i] = Math.min(radius - i, palindromeRadii[mirror]);

			while (i + 1 + palindromeRadii[i] < n
					&& i - 1 - palindromeRadii[i] >= 0
					&& sPrime.charAt(i + 1 + palindromeRadii[i]) == sPrime.charAt(i - 1 - palindromeRadii[i])) {
				palindromeRadii[i]++;
			}

			if (i + palindromeRadii[i] > radius) {
				center = i;
				radius = i + palindromeRadii[i];
			}
		}

		var maxLength = 0;
		var centerIndex = 0;
		for (int i = 0; i < n; i++) {
			if (palindromeRadii[i] > maxLength) {
				maxLength = palindromeRadii[i];
				centerIndex = i;
			}
		}

		var startIndex = (centerIndex - maxLength) / 2;
		return s.substring(startIndex, startIndex + maxLength);
	}
}
