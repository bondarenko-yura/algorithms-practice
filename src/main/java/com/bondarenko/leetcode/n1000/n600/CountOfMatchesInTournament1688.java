package com.bondarenko.leetcode.n1000.n600;

/*
 URL: https://leetcode.com/problems/count-of-matches-in-tournament/
 Time: 1
 Space: 1
 */
public class CountOfMatchesInTournament1688 {

	public int numberOfMatches(int n) {
		// There are n teams, and 1 winner. Thus, n - 1 teams will be eliminated.
		// Each match is played between two teams. One team wins, one team loses. Thus, each match
		// eliminates exactly one team.
		// As n - 1 teams will be eliminated, there will be n - 1 matches played, with each match
		// eliminating a team.
		return n - 1;
	}

}
