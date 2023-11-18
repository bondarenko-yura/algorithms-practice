package com.bondarenko.leetcode.n0000.n800;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

/*
 URL: https://leetcode.com/problems/bus-routes
 R - routes
 S - stops per route
 Time: RS
 Space: RS
 */
public class BusRoutes815 {

	// bfs
	public int numBusesToDestination(int[][] routes, int source, int target) {
		if (source == target)
			return 0;

		var stopToBus = new HashMap<Integer, List<Bus>>();
		for (int[] route : routes) {
			var bus = new Bus(route);
			for (int stop : route)
				stopToBus.computeIfAbsent(stop, v -> new ArrayList<>()).add(bus);
		}

		var queue = new ArrayDeque<Bus>();
		addToQueue(queue, stopToBus.remove(source));

		var busCount = 0;
		while (!queue.isEmpty()) {
			busCount++;
			var size = queue.size();
			while (size-- > 0) {
				var bus = queue.poll();
				for (int stop : bus.route) {
					if (stop == target)
						return busCount;
					addToQueue(queue, stopToBus.remove(stop));
				}
			}
		}

		return -1;
	}

	private static void addToQueue(Queue<Bus> q, List<Bus> buses) {
		if (buses != null) {
			for (Bus b : buses) {
				if (!b.seen) {
					b.seen = true;
					q.add(b);
				}
			}
		}
	}

	static class Bus {

		final int[] route;
		boolean seen;

		Bus(int[] route) {
			this.route = route;
		}

		@Override
		public String toString() {
			return Arrays.toString(route) + "#" + seen;
		}

	}

}
