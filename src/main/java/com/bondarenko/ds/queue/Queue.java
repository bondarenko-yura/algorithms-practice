package com.bondarenko.ds.queue;

public interface Queue<E> {
	void enqueue(E element);

	E dequeue();

	boolean isEmpty();

	int size();
}
