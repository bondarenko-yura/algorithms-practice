package com.bondarenko.leetcode.n2000.n900;

/*
 URL: https://leetcode.com/problems/count-tested-devices-after-test-operations
 Time: N
 Space: 1
 */
public class CountTestedDevicesAfterTestOperations2960 {

	// iteration
	public int countTestedDevices(int[] batteryPercentages) {
		var ans = 0;
		for (int b : batteryPercentages)
			if (b - ans > 0)
				ans++;
		return ans;
	}

}
