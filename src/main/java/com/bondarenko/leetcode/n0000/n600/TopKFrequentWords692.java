package com.bondarenko.leetcode.n0000.n600;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/*
 URL: https://leetcode.com/problems/top-k-frequent-words
 Time: NLogK
 Space: N
 */
public class TopKFrequentWords692 {

	// selection sort
	private static final Random RAND = new Random(0);

	public List<String> topKFrequent(String[] words, int k) {
		var map = new HashMap<String, WordCount>();
		for (String w : words)
			map.computeIfAbsent(w, WordCount::new).count++;

		var sortedList = new ArrayList<>(map.values());
		partialSort(0, sortedList.size() - 1, k, sortedList);

		var result = new ArrayList<String>();
		for (int i = sortedList.size() - 1; i >= sortedList.size() - k; i--)
			result.add(sortedList.get(i).word);
		return result;
	}

	void partialSort(int baseLo, int baseHi, int k, List<WordCount> words) {
		if (baseHi - baseLo <= 1)
			return;
		var pivot = words.get(RAND.nextInt(baseHi - baseLo) + baseLo);
		var lo = baseLo;
		var hi = baseHi;
		while (lo < hi) {
			while (lo < hi && pivot.compareTo(words.get(lo)) > 0)
				lo++;
			while (lo < hi && pivot.compareTo(words.get(hi)) < 0)
				hi--;
			if (lo < hi)
				Collections.swap(words, lo, hi);
		}
		if (baseHi - lo + 1 < k)
			partialSort(baseLo, lo, k, words);
		partialSort(hi, baseHi, k, words);
	}

	static final class WordCount implements Comparable<WordCount> {

		final String word;
		int count;

		WordCount(String word) {
			this.word = word;
		}

		@Override
		public int compareTo(WordCount that) {
			if (this.count != that.count)
				return Integer.compare(this.count, that.count);
			return that.word.compareTo(this.word);
		}

	}

}
