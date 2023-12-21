package com.bondarenko.leetcode.n0000.n200;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FindMedianFromDataStream295Test {

	@Test
	public void test() {
		var offset = Offset.offset(Math.pow(10, -5));
		var m = new FindMedianFromDataStream295.MedianFinder();
		m.addNum(1);
		m.addNum(2);
		assertThat(m.findMedian()).isEqualTo(1.5, offset);
		m.addNum(3);
		assertThat(m.findMedian()).isEqualTo(2, offset);
	}

}
