package com.bondarenko.algo.binary;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BinaryTest {

	@Test
	public void toBinaryToDecimal() {
		int input = 1000;
		String binaryString = Integer.toBinaryString(input);
		int resultVal = Integer.parseInt(binaryString, 2);
		assertThat(input).isEqualTo(resultVal);
	}


	@Test
	public void combineAndSplit() {
		int maxPossibleInput = 1000;
		int maxPossibleInputDecimalLen = Integer.toBinaryString(maxPossibleInput).length();
		int allOnesDecimal = constructAllOneNumOfBinaryLen(maxPossibleInputDecimalLen);

		int inputA = 555;
		int inputB = 777;

		int inputCombined = (inputA << maxPossibleInputDecimalLen) | inputB;

		int restoredA = inputCombined >> maxPossibleInputDecimalLen;
		int restoredB = inputCombined & allOnesDecimal;

		assertThat(restoredA).isEqualTo(inputA);
		assertThat(restoredB).isEqualTo(inputB);
	}

	private int constructAllOneNumOfBinaryLen(int len) {
		return Integer.valueOf(
				IntStream.range(0, len).mapToObj(i -> "1").collect(Collectors.joining()), 2);
	}
}
