package com.bondarenko.ds.map;

public interface Map<K, V> {

	boolean put(K key, V value);

	V get(K key);

	boolean isEmpty();

	int size();

}
