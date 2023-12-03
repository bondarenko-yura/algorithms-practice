package com.bondarenko.leetcode.n2000.n900;

import java.util.ArrayList;
import java.util.List;

/*
 URL: https://leetcode.com/problems/find-the-peaks
 Time: N
 Space: 1
 */
public class FindThePeaks2951 {

	public List<Integer> findPeaks(int[] mountain) {
		var ans = new ArrayList<Integer>();
		for (int i = 1; i < mountain.length - 1; i++)
			if (mountain[i - 1] < mountain[i] && mountain[i] > mountain[i + 1])
				ans.add(i);
		return ans;
	}

}
