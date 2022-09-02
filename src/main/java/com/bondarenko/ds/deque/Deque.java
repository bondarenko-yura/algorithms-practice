package com.bondarenko.ds.deque;

import java.util.Objects;
import java.util.TreeSet;

public interface Deque<Item> extends Iterable<Item> {

	boolean isEmpty();

	int size();

	void addFirst(Item item);

	void addLast(Item item);

	Item removeFirst();

	Item removeLast();

	static final class Range {

		private TreeSet<Range> ranges = new TreeSet<>((r1, r2) -> r1.from - r2.from);
		private final int from;
		private final int to;

		Range(int from, int to) {
			this.from = from;
			this.to = to;
		}

		@Override
		public boolean equals(Object that) {
			var thatRange = (Range) that;
			return this.from == thatRange.from && this.to == thatRange.to;
		}

		@Override
		public int hashCode() {
			return Objects.hash(from, to);
		}

	}

}
