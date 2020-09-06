package com.bondarenko.algo.binary;

import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
	public void mergeAndSplit() {
		final int maxPossibleInput = 1000;
		final int inputOrig = 555;
		int inputA = inputOrig;
		final int inputB = 777;
		final int maxPossibleInputDecimalLen = Integer.toBinaryString(maxPossibleInput).length();
		final int allOnesDecimal = constructAllOneNumOfBinaryLen(maxPossibleInputDecimalLen);

		printBinary(Integer.MAX_VALUE);
		printBinary(maxPossibleInput);
		printBinary(inputA);
		printBinary(inputB);
		printBinary(allOnesDecimal);

		inputA <<= maxPossibleInputDecimalLen;
		printBinary(inputA);

		inputA |= inputB;
		printBinary(inputA);

		printBinary(allOnesDecimal);

		int resB = inputA & allOnesDecimal;
		int resA = inputA >> maxPossibleInputDecimalLen;

		printBinary(resA);
		printBinary(resB);

		assertThat(resA).isEqualTo(inputOrig);
		assertThat(resB).isEqualTo(inputB);
	}

	private int constructAllOneNumOfBinaryLen(int len) {
		return Integer.valueOf(IntStream.range(0, len).mapToObj(i -> "1").collect(Collectors.joining()), 2);
	}

	private void printBinary(int val) {
		String binaryString = Integer.toBinaryString(val);
		System.out.println("int " + val + " = " + binaryString + " (" + binaryString.length() + ")");
	}
}
