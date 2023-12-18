package com.bondarenko.leetcode.ds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Ints {

	private Ints() {
		throw new AssertionError("util clazz");
	}

	public static int[] p1D(String a) {
		if (a == null || a.trim().isEmpty())
			return null;
		var sub = a.substring(startIDx(a) + 1, endIDx(a));
		if (sub.trim().isEmpty())
			return new int[0];
		var split = sub.split(",");
		var ans = new int[split.length];
		for (int i = 0; i < split.length; i++)
			ans[i] = Integer.parseInt(split[i].trim());
		return ans;
	}

	public static List<Integer> p1DList(String s) {
		return Arrays.stream(p1D(s)).boxed().toList();
	}

	public static int[][] p2D(String a) {
		if (a == null)
			return null;
		var sub = a.substring(startIDx(a) + 1, endIDx(a));
		if (sub.trim().isEmpty())
			return new int[][]{};
		var res = new ArrayList<int[]>();
		for (int left = 0, right = 0; right < sub.length(); right++) {
			var c = sub.charAt(right);
			if (c == '[')
				left = right;
			else if (c == ']')
				res.add(p1D(sub.substring(left, right + 1)));
		}
		return res.toArray(int[][]::new);
	}

	public static List<List<Integer>> p2DList(String a) {
		return Arrays.stream(p2D(a))
				.map(v -> Arrays.stream(v).boxed().toList())
				.toList();
	}

	private static int startIDx(String a) {
		for (int i = 0; i < a.length(); i++) {
			var c = a.charAt(i);
			if (c == ' ')
				continue;
			if (c == '[')
				return i;
			throw new IllegalArgumentException("Illegal char: '" + c + "'");
		}
		throw new IllegalArgumentException("No array start '[': " + a);
	}

	private static int endIDx(String a) {
		for (int i = a.length() - 1; i >= 0; i--) {
			var c = a.charAt(i);
			if (c == ' ')
				continue;
			if (c == ']')
				return i;
			throw new IllegalArgumentException("Illegal char: '" + c + "'");
		}
		throw new IllegalArgumentException("No array end ']': " + a);
	}

}
