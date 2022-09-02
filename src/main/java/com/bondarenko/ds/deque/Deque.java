package com.bondarenko.ds.deque;

public interface Deque<E> extends Iterable<E> {

	boolean isEmpty();

	int size();

	void addFirst(E item);

	void addLast(E item);

	E removeFirst();

	E removeLast();

}
