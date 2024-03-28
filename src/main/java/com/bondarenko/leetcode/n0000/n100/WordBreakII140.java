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
		dfs(0, s.toCharArray(), trie, new StringBuilder(), ans);
		return ans;
	}

	private void dfs(int i, char[] s, Trie trie, StringBuilder sb, List<String> ans) {
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
			if (root.word != null) {
				var baseLen = sb.length();
				if (baseLen > 0)
					sb.append(' ');
				sb.append(root.word);
				dfs(i, s, trie, sb, ans);
				sb.delete(baseLen, sb.length());
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
			root.word = w;
		}
		return trie;
	}

	private static final class Trie {
		Trie[] nodes;
		String word;
	}

}
