package com.bondarenko.leetcode.n0000.n000;

/*
 URL: https://leetcode.com/problems/valid-parentheses
 Time: N
 Space: N
 */
public class ValidParentheses20 {

	private static boolean isLeft(char c) {
		return c == '(' || c == '{' || c == '[';
	}

	private static char flipToLeft(char ch) {
		return switch (ch) {
			case ')' -> '(';
			case '}' -> '{';
			default -> '[';
		};
	}

	// iteration
	public boolean isValid(String s) {
		if (s.length() % 2 == 1)
			return false;
		var si = -1;
		var stack = new char[s.length() / 2];
		for (int i = 0; i < s.length(); i++) {
			var ch = s.charAt(i);
			if (isLeft(ch)) {
				if (si + 1 == stack.length)
					return false;
				stack[++si] = ch;
			} else {
				if (si == -1 || stack[si] != flipToLeft(ch))
					return false;
				si--;
			}
		}
		return si == -1;
	}

}
