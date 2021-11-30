package com.bondarenko.ds;

public interface Deque<Item> extends Iterable<Item> {

	boolean isEmpty();

	int size();

	void addFirst(Item item);

	void addLast(Item item);

	Item removeFirst();

	Item removeLast();

}
