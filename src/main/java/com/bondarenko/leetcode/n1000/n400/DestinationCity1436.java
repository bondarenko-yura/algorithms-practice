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
		var dest = new HashSet<String>();
		for (List<String> route : paths)
			dest.add(route.get(route.size() - 1));
		for (List<String> route : paths)
			for (int i = 0; i < route.size() - 1; i++)
				dest.remove(route.get(i));
		var it = dest.iterator();
		return it.hasNext() ? it.next() : "";
	}

}
