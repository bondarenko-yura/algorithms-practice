package com.bondarenko.ds.graph;

import java.util.Objects;

public class Edge implements Comparable<Edge> {

	private final int v1;
	private final int v2;
	private final int weight;

	public Edge(int v1, int v2) {
		this(v1, v2, 0);
	}

	public Edge(int v1, int v2, int weight) {
		this.v1 = v1;
		this.v2 = v2;
		this.weight = weight;
	}

	public int getV1() {
		return v1;
	}

	public int getV2() {
		return v2;
	}

	public int getWeight() {
		return weight;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Edge edge = (Edge) o;
		return v1 == edge.v1
				&& v2 == edge.v2
				&& weight == edge.weight;
	}

	@Override
	public int hashCode() {
		return Objects.hash(v1, v2, weight);
	}

	@Override
	public String toString() {
		return "[" + v1 + "," + v2 + "," + weight + "]";
	}

	@Override
	public int compareTo(Edge that) {
		return this.getWeight() - that.getWeight();
	}

}
