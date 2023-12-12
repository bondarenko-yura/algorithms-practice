package com.bondarenko.leetcode.n0000.n200;

public class IntegerToEnglishWords273 {

	private static final int BILLION = 1000000000;
	private static final int MILLION = 1000000;
	private static final int THOUSAND = 1000;
	private static final int HUNDRED = 100;

	private static final String[] D10 =
			{"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"};
	private static final String[] D19 =
			{"", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
	private static final String[] D100 =
			{"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety", "Hundred"};

	public String numberToWords(int remainder) {
		if (remainder == 0)
			return D10[0];
		var sb = new StringBuilder();
		remainder = addSegment(remainder, sb, BILLION, "Billion");
		remainder = addSegment(remainder, sb, MILLION, "Million");
		remainder = addSegment(remainder, sb, THOUSAND, "Thousand");
		addHundred(remainder, sb);
		return sb.toString();
	}

	private int addSegment(int n, StringBuilder sb, int divisor, String divisorName) {
		if (n >= divisor) {
			addTailSpace(sb);
			addHundred(n / divisor, sb);
			sb.append(" ").append(divisorName);
			n = n - n / divisor * divisor;
		}
		return n;
	}

	private void addHundred(int n, StringBuilder sb) {
		if (n >= HUNDRED) {
			addTailSpace(sb);
			sb.append(D10[n / HUNDRED]);
			sb.append(" Hundred");
			n = n - n / HUNDRED * HUNDRED;
		}
		addTens(n, sb);
	}

	private void addTens(int n, StringBuilder sb) {
		if (n == 0)
			return;
		addTailSpace(sb);
		if (0 < n && n < 10)            // a). 1..9
			sb.append(D10[n]);
		else if (n % 10 == 0)           // b). 10,20,..,90
			sb.append(D100[n / 10]);
		else if (10 < n && n < 20)      // c). 11,12,..,19
			sb.append(D19[n - 10]);
		else                            // d). b + a
			sb.append(D100[n / 10]).append(" ").append(D10[n - n / 10 * 10]);
	}

	private void addTailSpace(StringBuilder sb) {
		if (!sb.isEmpty() && sb.charAt(sb.length() - 1) != ' ')
			sb.append(' ');
	}

}
