package com.bondarenko.leetcode.n2000.n100;

/*
 URL: https://leetcode.com/problems/longest-palindrome-by-concatenating-two-letter-words
 Time: N
 Space: 1
*/
public class LongestPalindromeByConcatenatingTwoLetterWords2131 {

	public int longestPalindrome(String[] words) {
		var wordCnt = new int[26][26];
		var dupCnt = 0;
		var ans = 0;
		for (String word : words) {
			var c1 = word.charAt(0) - 'a';
			var c2 = word.charAt(1) - 'a';
			if (wordCnt[c2][c1] > 0) { // found
				ans += 4;
				if (c1 == c2)
					dupCnt--;
				wordCnt[c2][c1]--;
			} else {
				if (c1 == c2)
					dupCnt++;
				wordCnt[c1][c2]++;
			}
		}
		return ans + (dupCnt > 0 ? 2 : 0);
	}

}
