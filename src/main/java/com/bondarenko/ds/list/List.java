package com.bondarenko.ds.list;

public interface List<E> {

	E get(int idx);

	E set(E item, int idx);

	void add(E item);

	boolean isEmpty();

	int size();

}
