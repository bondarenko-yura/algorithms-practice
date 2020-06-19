package com.bondarenko.algo.dynamicconnectivity;

import com.bondarenko.algo.dynamicconnectivity.QuickUnionWeightingPathCompression;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuickUnionWeightingPathCompressionTest {

	@Test
	void test() {
		var quickFind = new QuickUnionWeightingPathCompression(10);
		quickFind.union(3, 4);
		quickFind.union(3, 8);
		quickFind.union(5, 6);
		quickFind.union(4, 8);
		quickFind.union(1, 2);
		quickFind.union(8, 9);
		quickFind.union(0, 5);
		quickFind.union(2, 7);
		quickFind.union(6, 1);

		assertThat(quickFind.isConnected(0, 7)).isTrue();
		assertThat(quickFind.isConnected(0, 2)).isTrue();
		assertThat(quickFind.isConnected(0, 5)).isTrue();
		assertThat(quickFind.isConnected(5, 7)).isTrue();
		assertThat(quickFind.isConnected(6, 2)).isTrue();
		assertThat(quickFind.isConnected(3, 4)).isTrue();
		assertThat(quickFind.isConnected(8, 9)).isTrue();
		assertThat(quickFind.isConnected(3, 8)).isTrue();
		assertThat(quickFind.isConnected(4, 9)).isTrue();

		assertThat(quickFind.isConnected(0, 9)).isFalse();
		assertThat(quickFind.isConnected(5, 4)).isFalse();
		assertThat(quickFind.isConnected(6, 8)).isFalse();
		assertThat(quickFind.isConnected(2, 3)).isFalse();
	}
}