package com.bondarenko.ds.deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DequeArray<Item> implements Deque<Item> {

	private static final double SCALE_UP_LOAD = 0.8;
	private static final double SCALE_DOWN_LOAD = 0.3;
	private static final int INITIAL_CAPACITY = 16;

	private Item[] data;
	private int headIdx;
	private int tailIdx;

	public DequeArray() {
		this.headIdx = INITIAL_CAPACITY / 2;
		this.tailIdx = headIdx;
		this.data = createArray(INITIAL_CAPACITY);
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		return tailIdx - headIdx;
	}

	@Override
	public void addFirst(Item item) {
		validateNotNull(item);
		resize();
		data[--headIdx] = item;
	}

	@Override
	public void addLast(Item item) {
		validateNotNull(item);
		resize();
		data[tailIdx++] = item;
	}

	@Override
	public Item removeFirst() {
		validateNotEmpty();
		resize();
		Item item = data[headIdx];
		data[headIdx] = null;
		headIdx++;
		return item;
	}

	// remove and return the item from the back
	@Override
	public Item removeLast() {
		validateNotEmpty();
		resize();
		Item item = data[--tailIdx];
		data[tailIdx] = null;
		return item;
	}
	// return an iterator over items in order from front to back

	@Override
	public Iterator<Item> iterator() {
		return new DequeueIterator();
	}
	// unit testing (required)

	private void resize() {
		double load = size() / (double) data.length;
		if (data.length == tailIdx
				|| headIdx == 0
				|| (load > 0 && load < SCALE_DOWN_LOAD)) {
			Item[] resize;
			if (load > SCALE_UP_LOAD) {
				resize = createArray((int) (data.length * 1.5));
			} else if (load < SCALE_DOWN_LOAD && data.length > INITIAL_CAPACITY) {
				resize = createArray(data.length / 2);
			} else {
				resize = data;
			}
			int currentSize = size();
			int nextHeadIdx = (resize.length - currentSize) / 2;
			System.arraycopy(data, headIdx, resize, nextHeadIdx, currentSize);
			data = resize;
			headIdx = nextHeadIdx;
			tailIdx = nextHeadIdx + currentSize;
		}
	}

	private void validateNotNull(Item item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
	}

	private void validateNotEmpty() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
	}

	@SuppressWarnings("unchecked")
	private Item[] createArray(int size) {
		return (Item[]) new Object[size];
	}

	private final class DequeueIterator implements Iterator<Item> {
		private int cursor = headIdx;

		@Override
		public boolean hasNext() {
			return cursor != tailIdx;
		}

		@Override
		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return data[cursor++];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
