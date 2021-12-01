package com.bondarenko.algo.graph;

import java.util.List;

import com.bondarenko.ds.graph.Edge;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DijkstraShortestPathTest {

	@Test
	public void findShortestPath() {
		var edges = List.of(
				new Edge(10, 20, 1),
				new Edge(10, 30, 4),
				new Edge(20, 30, 2),
				new Edge(20, 40, 6),
				new Edge(30, 40, 2)
		);

		DijkstraShortestPath shortestPath = new DijkstraShortestPath();
		assertThat(shortestPath.findShortestDistance(10, 10, edges)).isEqualTo(0);
		assertThat(shortestPath.findShortestDistance(10, 20, edges)).isEqualTo(1);
		assertThat(shortestPath.findShortestDistance(10, 30, edges)).isEqualTo(3);
		assertThat(shortestPath.findShortestDistance(10, 40, edges)).isEqualTo(5);
	}

}
