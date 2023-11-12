package com.bondarenko.ds.tree;

// todo https://www.geeksforgeeks.org/data-structures/
public class SuffixTree {

	public static void main(String[] args) {
		System.out.println(new SuffixTree().lengthOfLIS(new int[]{1, 3, 3, 4}, 1));
		System.out.println(new SuffixTree().lengthOfLIS(new int[]{4, 2, 1, 4, 3, 4, 5, 8, 15}, 3));
		System.out.println(new SuffixTree().lengthOfLIS(new int[]{7, 4, 5, 1, 8, 12, 4, 7}, 5));
		System.out.println(new SuffixTree().lengthOfLIS(new int[]{1, 5}, 1));
	}

	public int lengthOfLIS(int[] nums, int k) {
		var minVal = Integer.MAX_VALUE;
		var maxVal = Integer.MIN_VALUE;
		for (int n : nums) {
			minVal = Math.min(minVal, n);
			maxVal = Math.max(maxVal, n);
		}

		SegmentTree root = new SegmentTree(minVal, maxVal);
		for (int num : nums) {
			var preMax = 1 + root.rangeMaxQuery(root, num - k, num - 1);
			root.update(root, num, preMax);
		}
		return root.val;
	}

	static final class SegmentTree {

		final int lo;
		final int hi;
		SegmentTree left;
		SegmentTree right;
		int val;

		SegmentTree(int lo, int hi) {
			this.lo = lo;
			this.hi = hi;
			if (lo != hi) {
				var mid = lo + (hi - lo) / 2;
				this.left = new SegmentTree(lo, mid);
				this.right = new SegmentTree(mid + 1, hi);
			}
		}

		void update(SegmentTree node, int index, int val) {
			if (index < node.lo || node.hi < index) // out of range
				return;
			if (node.lo == node.hi) { // found node
				node.val = val;
				return;
			}
			update(node.left, index, val);
			update(node.right, index, val);
			node.val = Math.max(node.left.val, node.right.val);
		}

		int rangeMaxQuery(SegmentTree node, int lo, int hi) {
			if (hi < node.lo || node.hi < lo) // not overlap
				return 0;
			if (lo <= node.lo && node.hi <= hi) // in range
				return node.val;
			return Math.max(rangeMaxQuery(node.left, lo, hi), rangeMaxQuery(node.right, lo, hi));
		}

		@Override
		public String toString() {
			return "[" + lo + "," + hi + "]->" + val;
		}

	}

}
