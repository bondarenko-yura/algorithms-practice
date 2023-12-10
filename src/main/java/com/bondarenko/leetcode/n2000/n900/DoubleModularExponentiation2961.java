package com.bondarenko.leetcode.n2000.n900;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/*
 URL: https://leetcode.com/problems/double-modular-exponentiation
 Time: N
 Space: 1
 */
public class DoubleModularExponentiation2961 {

	// big decimal
	public List<Integer> getGoodIndices(int[][] variables, int target) {
		var ans = new ArrayList<Integer>();
		var t = BigInteger.valueOf(target);
		for (int i = 0; i < variables.length; i++) {
			var v = variables[i];
			var a = BigInteger.valueOf(v[0]);
			var b = v[1];
			var c = v[2];
			var d = BigInteger.valueOf(v[3]);
			if (a.pow(b).mod(BigInteger.TEN).pow(c).mod(d).equals(t))
				ans.add(i);
		}
		return ans;
	}

}
