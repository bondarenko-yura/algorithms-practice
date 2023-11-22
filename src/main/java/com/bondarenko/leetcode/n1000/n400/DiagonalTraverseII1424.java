package com.bondarenko.leetcode.n1000.n400;

import java.util.ArrayDeque;
import java.util.List;

/*
 URL: https://leetcode.com/problems/diagonal-traverse-ii
 Time: RC
 Space: R
 */
public class DiagonalTraverseII1424 {

	// array
	public int[] findDiagonalOrder(List<List<Integer>> nums) {
		var ansSize = 0;
		for (List<Integer> r : nums)
			ansSize += r.size();
		var a = 0;
		var ans = new int[ansSize];
		var rows = new ArrayDeque<Row>();
		var rIdx = 0;
		while (a < ans.length) {
			if (rIdx < nums.size())
				rows.addLast(new Row(nums.get(rIdx++)));
			var rSize = rows.size();
			while (rSize-- > 0) {
				var r = rows.pollLast();
				if (r.hasNext()) {
					ans[a++] = r.next();
					rows.addFirst(r);
				}
			}
		}
		return ans;
	}

	private static final class Row {

		private final List<Integer> r;
		private int idx;

		Row(List<Integer> r) {
			this.r = r;
		}

		Integer next() {
			return r.get(idx++);
		}

		boolean hasNext() {
			return idx < r.size();
		}

	}

}
