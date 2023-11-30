package com.bondarenko.leetcode.n1000.n600;

/*
 URL: https://leetcode.com/problems/minimum-one-bit-operations-to-make-integers-zero
 Time: logN
 Space: 1
 */
public class MinimumOneBitOperationsToMakeIntegersZero1611 {

	// recursion
	public int minimumOneBitOperations(int n) {
		return minimumOneBitOperations(n, 0);
	}

	public int minimumOneBitOperations(int n, int res) {
		if (n == 0) return res;
		int b = 1;
		while ((b << 1) <= n)
			b = b << 1;
		return minimumOneBitOperations((b >> 1) ^ b ^ n, res + b);
	}

}
