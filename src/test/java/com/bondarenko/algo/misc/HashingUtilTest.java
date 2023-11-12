package com.bondarenko.algo.misc;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HashingUtilTest {

	@Test
	void hash() {
		BigInteger hash1 = HashingUtil.hash("Yurii");
		BigInteger hash2 = HashingUtil.hash("Yurii");
		BigInteger hash3 = HashingUtil.hash("Carol");
		assertThat(hash1).isEqualTo(hash2);
		assertThat(hash2).isNotEqualTo(hash3);
	}

}
