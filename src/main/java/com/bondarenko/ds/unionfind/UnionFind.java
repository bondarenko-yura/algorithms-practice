package com.bondarenko.ds.unionfind;

import static com.bondarenko.ds.unionfind.UnionFind.Unique;

public interface UnionFind<K, V extends Unique<K>> {

	void union(V v1, V v2);

	V find(V v);

	boolean connected(V v1, V v2);

	int size();

	int connectedComponentsSize();

	interface Unique<K> {

		K getKey();

	}

}
