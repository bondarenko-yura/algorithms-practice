package com.bondarenko.leetcode.n1000.n200;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/*
 URL: https://leetcode.com/problems/minimum-time-visiting-all-points
 Time: V+E where V represents the number of vertices and E represents the number of edges
 Space: V+E
 */
public class CourseScheduleII210 {

	// topological sort, Kahn's algorithm
	public int[] findOrder(int numCourses, int[][] prerequisites) {
		var inDegree = new int[numCourses];
		var adjList = new HashMap<Integer, List<Integer>>();
		for (int[] p : prerequisites) {
			inDegree[p[1]]++;
			adjList.computeIfAbsent(p[0], v -> new ArrayList<>()).add(p[1]);
		}
		var queue = new ArrayDeque<Integer>();
		for (int i = 0; i < inDegree.length; i++)
			if (inDegree[i] == 0)
				queue.push(i);
		var ans = new int[numCourses];
		var a = ans.length;
		while (!queue.isEmpty()) {
			var v = queue.poll();
			ans[--a] = v;
			for (Integer w : adjList.getOrDefault(v, Collections.emptyList()))
				if (--inDegree[w] == 0)
					queue.push(w);
		}
		return a == 0 ? ans : new int[0];
	}

}
