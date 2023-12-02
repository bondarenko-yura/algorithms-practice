package com.bondarenko.leetcode.n1000.n100;

/*
 URL: https://leetcode.com/problems/find-words-that-can-be-formed-by-characters
 Time: N (sum of all words len)
 Space: 1
 */
public class FindWordsThatCanBeFormedByCharacters1160 {

	// count chars
	public int countCharacters(String[] words, String chars) {
		var letters = new int[26];
		for (int i = 0; i < chars.length(); i++)
			letters[chars.charAt(i) - 'a']++;
		var ans = 0;
		for (String w : words) {
			var match = true;
			var seen = new int[26];
			for (int i = 0; i < w.length() && match; i++) {
				int li = w.charAt(i) - 'a';
				seen[li]++;
				if (letters[li] < seen[li])
					match = false;
			}
			if (match)
				ans += w.length();
		}
		return ans;
	}

}
