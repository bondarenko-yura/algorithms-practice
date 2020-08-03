package com.bondarenko.algo.princeton.c1.sort;

import static java.lang.Character.isDigit;

public class Dijkstra {

	private final String expression;

	public Dijkstra(String expression) {
		this.expression = expression.replaceAll(" ", "");
	}

	public int eval() {
		Stack<Integer> vals = new StackArrayImpl<>();
		Stack<String> operation = new StackArrayImpl<>();

		int cursor = 0;
		while (cursor < expression.length()) {
			char charAt = expression.charAt(cursor);
			if ('(' == charAt) {
				cursor++;
			} else if (isOperator(charAt)) {
				operation.push(Character.toString(charAt));
				cursor++;
			} else if (isDigit(charAt)) {
				int start = cursor++;
				while (cursor < expression.length() && isDigit(expression.charAt(cursor))) {
					cursor++;
				}
				vals.push(Integer.parseInt(expression, start, cursor, 10));
			} else if (')' == charAt) {
				vals.push(calc(operation.pop(), vals.pop(), vals.pop()));
				cursor++;
			} else {
				throw new IllegalStateException();
			}
		}

		return vals.pop();
	}

	private int calc(String operation, int a, int b) {
		switch (operation) {
			case "+":
				return b + a;
			case "-":
				return b - a;
			case "*":
				return b * a;
			case "/":
				return b / a;
			default:
				throw new IllegalArgumentException();
		}
	}

	private boolean isOperator(char val) {
		switch (val) {
			case '+':
			case '-':
			case '*':
			case '/':
				return true;
			default:
				return false;
		}
	}
}
