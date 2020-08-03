package com.bondarenko.algo.princeton.c1.sort;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DijkstraTest {

	@Test
	void dijkstra() {
		assertThat(new Dijkstra("(1 + ((2 + 3) * (4 * 5)))").eval()).isEqualTo(101);
	}
}