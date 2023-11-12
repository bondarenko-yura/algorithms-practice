package com.bondarenko.algo.array;

import java.util.Arrays;
import java.util.Random;

import com.bondarenko.algo.sort.SortQuick;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("heavy")
class QuickSelectionTest {

	private final Random random = new Random(55);
	private final SortQuick sort = new SortQuick();
	private final QuickSelection quickSelection = new QuickSelection();

	@Test
	void findGreatestValues() {
		for (int i = 1; i < 10000; i++) {
			runTest(i, random.nextInt(i) + 1);
		}
	}

	private void runTest(int problemSize, int valCount) {
		int[] arr = new int[problemSize];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = random.nextInt(problemSize * 5);
		}
		int[] inputCopy = Arrays.copyOf(arr, arr.length);

		int[] result = quickSelection.findGreatestValues(arr, valCount);
		assertThat(result.length).isEqualTo(valCount);

		sort.sort(result);
		sort.sort(inputCopy);

		int[] expected = Arrays.copyOfRange(inputCopy, inputCopy.length - valCount, inputCopy.length);
		assertThat(result).isEqualTo(expected);
	}
}
