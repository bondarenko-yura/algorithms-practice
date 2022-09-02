package com.bondarenko.ds.stack;

public interface Stack<E> {
	void push(E element);

	E pop();

	boolean isEmpty();

	int size();
}
