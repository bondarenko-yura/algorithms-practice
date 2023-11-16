package com.bondarenko.leetcode.n1000.n900;

/*
 URL: https://leetcode.com/problems/find-unique-binary-string
 N = len(nums)
 M = len(nums[0])
 Time: NM
 Space: NM
 */
public class FindUniqueBinaryString1980 {

	public String findDifferentBinaryString(String[] nums) {
		var root = new Node();
		for (String nm : nums) {
			var cur = root;
			for (int i = 0; i < nm.length(); i++) {
				if (nm.charAt(i) == '0') {
					if (cur.zero == null)
						cur.zero = new Node();
					cur = cur.zero;
				} else {
					if (cur.one == null)
						cur.one = new Node();
					cur = cur.one;
				}
			}
		}
		var ans = new char[nums[0].length()];
		if (dfs(root.zero, '0', 0, ans) || dfs(root.one, '1', 0, ans))
			return new String(ans);
		return "";
	}

	private static boolean dfs(Node r, char expect, int idx, char[] ans) {
		if (idx == ans.length)
			return false;
		ans[idx] = expect;
		if (r == null) {
			for (int i = idx + 1; i < ans.length; i++)
				ans[i] = expect;
			return true;
		}
		return dfs(r.zero, '0', idx + 1, ans) || dfs(r.one, '1', idx + 1, ans);
	}

	private static class Node {

		private Node zero, one;

	}

}
