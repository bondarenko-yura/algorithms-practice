package com.bondarenko.leetcode.n0000.n000;

/*
 URL: https://leetcode.com/problems/remove-duplicates-from-sorted-array
 Time: N
 Space: 1
 */
public class RemoveDuplicatesFromSortedArray26 {

	// iteration
	public int removeDuplicates(int[] nums) {
		var last = 0;
		for (int i = 1; i < nums.length; i++)
			if (nums[last] != nums[i])
				nums[++last] = nums[i];
		return last + 1;
	}

}
