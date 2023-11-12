package com.bondarenko.algo.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.bondarenko.ds.graph.Edge;

public class DijkstraShortestPath {

	public Integer findShortestDistance(int source, int target, List<Edge> graphEdges) {
		var graph = new HashMap<Integer, Set<Edge>>();
		for (Edge e : graphEdges)
			graph.computeIfAbsent(e.getV1(), v -> new HashSet<>()).add(e);

		var seen = new HashSet<Integer>();
		seen.add(source);

		var shortestDistances = new HashMap<Integer, Integer>();
		shortestDistances.put(source, 0);

		var queue = new PriorityQueue<Score>();
		for (Edge e : graph.getOrDefault(source, Collections.emptySet()))
			queue.add(new Score(e, e.getWeight()));

		while (!queue.isEmpty()) {
			var closestEdge = queue.poll().edge;
			if (!seen.add(closestEdge.getV2()))
				continue;
			var currentDist = shortestDistances.get(closestEdge.getV1()) + closestEdge.getWeight();
			shortestDistances.put(closestEdge.getV2(), currentDist);
			var edges = graph.getOrDefault(closestEdge.getV2(), Collections.emptySet());
			for (Edge edge : edges) {
				if (!seen.contains(edge.getV2()))
					queue.add(new Score(edge, edge.getWeight() + currentDist));
			}
		}

		return shortestDistances.get(target);
	}

	private static final class Score implements Comparable<Score> {

		private final Edge edge;
		private final int score;

		public Score(Edge edge, int score) {
			this.edge = edge;
			this.score = score;
		}

		@Override
		public int compareTo(Score that) {
			return this.score - that.score;
		}

		@Override
		public String toString() {
			return "[" + edge + "," + score + "]";
		}

	}

}
