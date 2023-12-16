package com.bondarenko.leetcode.n1000.n200;

/*
 URL: https://leetcode.com/problems/valid-anagram
 Time: N
 Space: 1
 */
public class ValidAnagram242 {

	// letter count
	public boolean isAnagram(String s, String t) {
		if (s.length() != t.length())
			return false;

		var dif = 0;
		var alphabet = new int[26];
		for (int i = 0; i < s.length(); i++) {
			if (++alphabet[s.charAt(i) - 'a'] == 1)
				dif++;
			if (--alphabet[t.charAt(i) - 'a'] == 0)
				dif--;
		}

		return dif == 0;
	}

}
