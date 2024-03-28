package com.bondarenko.leetcode.n0000.n100;

import java.util.ArrayList;
import java.util.List;

/*
 https://leetcode.com/problems/word-break-ii
 Time: MN
 Space: MN
 */
public class WordBreakII140 {

	public List<String> wordBreak(String s, List<String> wordDict) {
		var trie = createTrie(wordDict);
		var ans = new ArrayList<String>();
		searchSentences(0, s.toCharArray(), trie, new StringBuilder(), ans);
		return ans;
	}

	private void searchSentences(int i, char[] s, Trie trie, StringBuilder sb, List<String> ans) {
		if (i == s.length) {
			ans.add(sb.toString());
			return;
		}
		var root = trie;
		while (i < s.length) {
			int c = s[i++] - 'a';
			if (root.nodes == null || root.nodes[c] == null)
				break;
			root = root.nodes[c];
			if (root.words != null) {
				if (sb.length() > 0)
					sb.append(' ');
				for (String w : root.words) {
					sb.append(w);
					searchSentences(i, s, trie, sb, ans);
					sb.delete(sb.length() - w.length(), sb.length());
				}
				if (sb.length() > 0)
					sb.deleteCharAt(sb.length() - 1);
			}
		}
	}

	private static Trie createTrie(List<String> wordDict) {
		var trie = new Trie();
		for (String w : wordDict) {
			var root = trie;
			for (int i = 0; i < w.length(); i++) {
				if (root.nodes == null)
					root.nodes = new Trie[26];
				int c = w.charAt(i) - 'a';
				if (root.nodes[c] == null)
					root.nodes[c] = new Trie();
				root = root.nodes[c];
			}
			if (root.words == null)
				root.words = new ArrayList<>();
			root.words.add(w);
		}
		return trie;
	}

	private static final class Trie {
		Trie[] nodes;
		List<String> words;
	}
}
