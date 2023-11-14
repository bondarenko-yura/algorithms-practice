package com.bondarenko.leetcode.n1000.n900;

/*
 URL: https://leetcode.com/problems/unique-length-3-palindromic-subsequences
 Time: N
 Space: N
 */
public class UniqueLength3PalindromicSubsequences1930 {

	private static final int NOT_FOUND = -1;

	public int countPalindromicSubsequence(String s) {
		var mm = new int[26][2]; // letter -> min/max position
		for (int i = 0; i < mm.length; i++)
			mm[i][0] = mm[i][1] = NOT_FOUND;
		for (int i = 0; i < s.length(); i++) {
			var c = s.charAt(i) - 'a';
			if (mm[c][0] == NOT_FOUND)
				mm[c][0] = i;
			mm[c][1] = i;
		}

		// count number of distinct letters between each pair
		var ans = 0;
		for (int[] m : mm) {
			if (m[1] - m[0] < 2)
				continue;
			var present = new boolean[26];
			for (int i = m[0] + 1; i < m[1]; i++) {
				var p = s.charAt(i) - 'a';
				if (present[p])
					continue;
				present[p] = true;
				ans++;
			}
		}
		return ans;
	}

}
