package com.bondarenko.leetcode.n0000.n100;

/*
 https://leetcode.com/problems/number-of-1-bits
 Time: N (binary len of n)
 Space: 1
 */
public class NumberOf1Bits191 {

	// iteration
	public int hammingWeight(int n) {
		var c = 0;
		while (n != 0) {
			c += n & 1;
			n = n >>> 1;
		}
		return c;
	}

	// java api
	public int hammingWeightJavaAPI(int n) {
		return Integer.bitCount(n);
	}

}
