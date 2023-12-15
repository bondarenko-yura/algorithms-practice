package com.bondarenko.leetcode.n1000.n400;

import java.util.HashSet;
import java.util.List;

/*
 URL: https://leetcode.com/problems/destination-city
 M - number of paths
 N - path len
 Time: MN
 Space: M
 */
public class DestinationCity1436 {

	// iteration
	public String destCity(List<List<String>> paths) {
		var outgoing = new HashSet<String>();
		for (List<String> path: paths)
			outgoing.add(path.get(0));
		for (List<String> path: paths)
			if (!outgoing.contains(path.get(1)))
				return path.get(1);
		return "";
	}

}
