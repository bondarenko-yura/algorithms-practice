package com.bondarenko.leetcode.n1000.n600;

/*
 URL: https://leetcode.com/problems/check-if-two-string-arrays-are-equivalent
 Time: N (sum of w1 and w2 lengths)
 Space: 1
 */
public class CheckIfTwoStringArraysAreEquivalent1662 {

	// iteration
	public boolean arrayStringsAreEqual(String[] word1, String[] word2) {
		int w1 = 0, w2 = 0, l1 = 0, l2 = 0;
		while (w1 < word1.length && w2 < word2.length
				&& word1[w1].charAt(l1++) == word2[w2].charAt(l2++)) {
			if (l1 == word1[w1].length()) {
				w1++;
				l1 = 0;
			}
			if (l2 == word2[w2].length()) {
				w2++;
				l2 = 0;
			}
		}
		return w1 == word1.length && w2 == word2.length;
	}

}
