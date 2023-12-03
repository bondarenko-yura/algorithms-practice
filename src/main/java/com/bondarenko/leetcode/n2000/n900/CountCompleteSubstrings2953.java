package com.bondarenko.leetcode.n2000.n900;

/*
 URL: https://leetcode.com/problems/count-complete-substrings
 Time: N
 Space: 1
 */
public class CountCompleteSubstrings2953 {

	// sliding window
	public int countCompleteSubstrings(String word, int k) {
		var ans = 0;
		for (int window = k; window <= 26 * k; window += k) {
			var need = 0;
			var cur = new int[26];
			for (int left = 0, right = 0; right < word.length(); right++) {
				need++;
				var idx = word.charAt(right) - 'a';
				if (++cur[idx] == k)
					need -= k;
				while (// too many letter duplicates
						cur[idx] > k
								// window too large
								|| (right - left + 1 > window)
								// invalid char dif
								|| (left < right && Math.abs(word.charAt(right) - word.charAt(right - 1)) > 2)) {
					need--;
					if (cur[word.charAt(left++) - 'a']-- == k)
						need += k;
				}
				if (need == 0 && right - left + 1 == window)
					ans++;
			}
		}
		return ans;
	}

}
