package com.bondarenko.leetcode.n0000.n700;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 URL: https://leetcode.com/problems/number-of-atoms
 Time: N
 Space: N
 */
public class NumberOfAtoms726 {

	public String countOfAtoms(String formula) {
		var all = new HashMap<String, Integer>();
		var idx = 0;
		while (idx < formula.length())
			idx = parse(idx, formula, all);

		var entries = new ArrayList<>(all.entrySet());
		entries.sort(Map.Entry.comparingByKey());

		var ans = new StringBuilder();
		for (Map.Entry<String, Integer> e : entries)
			ans.append(e.getKey()).append(e.getValue() == 1 ? "" : e.getValue());
		return ans.toString();
	}

	private int parse(int idx, String f, Map<String, Integer> all) {
		var cur = new HashMap<String, Integer>();

		var c = f.charAt(idx++);
		if (c == '(') { // it's a sub formula
			while (idx < f.length())
				idx = parse(idx, f, cur);
		} else if (Character.isLetter(c)) { // element
			var sb = new StringBuilder();
			sb.append(c);
			while (idx < f.length()
					&& Character.isLetter(f.charAt(idx))
					&& Character.isLowerCase(f.charAt(idx))) {
				sb.append(f.charAt(idx));
				idx++;
			}
			cur.put(sb.toString(), 1);
		}

		// deal with multiplier
		var mult = 0;
		while (idx < f.length() && Character.isDigit(f.charAt(idx))) {
			mult *= 10;
			mult += Character.digit(f.charAt(idx), 10);
			idx++;
		}
		if (mult > 0) {
			for (Map.Entry<String, Integer> e : cur.entrySet())
				e.setValue(e.getValue() * mult);
		}

		// merge to answer
		for (Map.Entry<String, Integer> e : cur.entrySet())
			all.merge(e.getKey(), e.getValue(), Integer::sum);
		return idx;
	}

}
