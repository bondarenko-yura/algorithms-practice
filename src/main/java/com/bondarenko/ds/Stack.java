package com.bondarenko.algo.princeton.c1.sort;

public interface Stack<E> {
	void push(E element);

	E pop();

	boolean isEmpty();

	int size();
}
