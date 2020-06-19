package com.bondarenko.algo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuickFindTest {

	@Test
	void test() {
		var quickFind = new QuickFind(10);
		quickFind.connect(3, 4);
		quickFind.connect(3, 8);
		quickFind.connect(5, 6);
		quickFind.connect(4, 8);
		quickFind.connect(1, 2);
		quickFind.connect(8, 9);
		quickFind.connect(0, 5);
		quickFind.connect(2, 7);
		quickFind.connect(6, 1);

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