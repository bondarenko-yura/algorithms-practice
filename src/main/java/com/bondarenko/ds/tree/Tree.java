package com.bondarenko.ds.tree;

public interface Tree<T extends Comparable<T>> extends Iterable<T> {

	boolean contains(T key);

	T find();

	T min();

	T max();

	T predecessor(T key);

	T successor(T key);

	int rank(T key);

	boolean insert(T key);

	boolean delete(T key);

}
