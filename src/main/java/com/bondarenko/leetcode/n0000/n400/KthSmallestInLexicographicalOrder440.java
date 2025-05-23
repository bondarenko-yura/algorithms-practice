package com.bondarenko.leetcode.n0000.n400;

/*
 URL: https://leetcode.com/problems/k-th-smallest-in-lexicographical-order
 Time: x
 Space: x
*/
public class KthSmallestInLexicographicalOrder440 {
	public int findKthNumber(int n, int k) {
		var curr = 1;
		k--;

		while (k > 0) {
			var step = countSteps(n, curr, curr + 1);
			// If the steps are less than or equal to k, we skip this prefix's subtree
			if (step <= k) {
				// Move to the next prefix and decrease k by the number of steps we skip
				curr++;
				k -= step;
			} else {
				// Move to the next level of the tree and decrement k by 1
				curr *= 10;
				k--;
			}
		}

		return curr;
	}

	// To count how many numbers exist between prefix1 and prefix2
	private int countSteps(int n, long prefix1, long prefix2) {
		var steps = 0;
		while (prefix1 <= n) {
			steps += (int) (Math.min(n + 1, prefix2) - prefix1);
			prefix1 *= 10;
			prefix2 *= 10;
		}
		return steps;
	}
}
