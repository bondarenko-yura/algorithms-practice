package com.bondarenko.algo.princeton.c1.sort;

public interface Queue<E> {
	void enqueue(E element);

	E dequeue();

	boolean isEmpty();

	int size();
}
