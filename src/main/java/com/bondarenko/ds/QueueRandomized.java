package com.bondarenko.ds;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class QueueRandomized<Item> implements Iterable<Item> {

	private Item[] data;
	private int tailIdx;

	// construct an empty randomized queue
	public QueueRandomized() {
		this.data = createArray(16);
	}

	// is the randomized queue empty?
	public boolean isEmpty() {
		return size() == 0;
	}

	// return the number of items on the randomized queue
	public int size() {
		return tailIdx;
	}

	// add the item
	public void enqueue(Item item) {
		validateNotNull(item);
		resize();
		data[tailIdx++] = item;
		swapWithinRange(data, tailIdx - 1);
	}

	// remove and return a random item
	public Item dequeue() {
		validateNotEmpty();
		resize();
		Item element = data[--tailIdx];
		data[tailIdx] = null;
		return element;
	}

	// return a random item (but do not remove it)
	public Item sample() {
		validateNotEmpty();
		swapWithinRange(data, tailIdx - 1);
		return data[tailIdx - 1];
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator(tailIdx, data);
	}

	private void resize() {
		double load = size() / (double) data.length;
		Item[] resize = null;
		if (load > 0 && load < 0.3) {
			resize = createArray(data.length / 2);
		} else if (data.length == tailIdx) {
			resize = createArray((int) (data.length * 1.5));
		}

		if (resize != null) {
			System.arraycopy(data, 0, resize, 0, size());
			data = resize;
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

	private void swapWithinRange(Object[] items, int lastIdx) {
		int rndIdx = StdRandom.uniform(0, lastIdx + 1);
		Object tmp = items[lastIdx];
		items[lastIdx] = items[rndIdx];
		items[rndIdx] = tmp;
	}

	@SuppressWarnings("unchecked")
	private Item[] createArray(int size) {
		return (Item[]) new Object[size];
	}

	private final class RandomizedQueueIterator implements Iterator<Item> {

		private final Item[] dataCopy;
		private int cursor;

		private RandomizedQueueIterator(int tailIndex, Item[] data) {
			this.cursor = tailIndex;
			this.dataCopy = createArray(tailIndex);
			System.arraycopy(data, 0, this.dataCopy, 0, tailIndex);
		}

		@Override
		public boolean hasNext() {
			return cursor > 0;
		}

		@Override
		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			swapWithinRange(dataCopy, cursor - 1);
			return dataCopy[--cursor];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
