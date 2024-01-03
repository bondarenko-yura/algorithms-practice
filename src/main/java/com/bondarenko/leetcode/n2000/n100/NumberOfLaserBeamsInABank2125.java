package com.bondarenko.leetcode.n2000.n100;

/*
 URL: https://leetcode.com/problems/number-of-ways-to-divide-a-long-corridor
 Time: RC
 Space: 1
*/
public class NumberOfLaserBeamsInABank2125 {

	// sliding window
	public int numberOfBeams(String[] bank) {
		var ans = 0;
		var prev = 0;
		for (String b : bank) {
			var cnt = 0;
			for (int i = 0; i < b.length(); i++)
				if (b.charAt(i) == '1')
					cnt++;
			if (cnt > 0) {
				ans += prev * cnt;
				prev = cnt;
			}
		}
		return ans;
	}
}
