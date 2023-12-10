package com.bondarenko.leetcode.n0000.n000;

/*
 URL: https://leetcode.com/problems/word-search
 S - word len
 Time: NMS
 Space: S
 */
public class WordSearch79 {

	private static final int[][] DIR = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

	private static boolean dfs(int r, int c, char[][] b, String word, int w) {
		if (r < 0 || r == b.length || c < 0 || c == b[r].length || b[r][c] != word.charAt(w))
			return false;
		var ch = word.charAt(w);
		b[r][c] = '#';
		var match = w + 1 == word.length();
		for (int i = 0; i < DIR.length && !match; i++)
			match = dfs(r + DIR[i][0], c + DIR[i][1], b, word, w + 1);
		b[r][c] = ch;
		return match;
	}

	// dfs
	public boolean exist(char[][] board, String word) {
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				if (dfs(r, c, board, word, 0))
					return true;
		return false;
	}

}
