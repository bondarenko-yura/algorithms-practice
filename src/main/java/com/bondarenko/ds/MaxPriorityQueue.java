package com.bondarenko.ds;

public class MaxPriorityQueue<T extends Comparable<T>> {

	public static final int ROOT_IDX = 1;
	public static final int INITIAL_SIZE = 64;

	private int cursor = ROOT_IDX;
	private T[] data;

	public MaxPriorityQueue() {
		this.data = createArray(INITIAL_SIZE);
	}

	public void insert(T val) {
		if (val == null) {
			throw new IllegalArgumentException();
		}
		resize();

		data[cursor++] = val;
		swim(cursor - 1);
	}

	public T max() {
		if (isEmpty()) {
			throw new IllegalArgumentException();
		}

		return data[ROOT_IDX];
	}

	public T delMax() {
		if (isEmpty()) {
			throw new IllegalArgumentException();
		}
		resize();

		T val = data[ROOT_IDX];
		swap(ROOT_IDX, --cursor);
		sink(ROOT_IDX);
		data[cursor] = null;
		return val;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return cursor - ROOT_IDX;
	}

	private void swap(int i, int j) {
		var tmp = data[i];
		data[i] = data[j];
		data[j] = tmp;
	}

	private boolean less(int i, int j) {
		return data[i].compareTo(data[j]) < 0;
	}

	private void swim(int idx) {
		while (idx / 2 >= ROOT_IDX) {
			int parentIdx = idx / 2;
			if (less(parentIdx, idx)) {
				swap(parentIdx, idx);
				idx = parentIdx;
			} else {
				break;
			}
		}
	}

	private void sink(int sinkIdx) {
		while (sinkIdx * 2 < cursor) {
			int childIdx = sinkIdx * 2;
			if (childIdx + 1 < cursor && less(childIdx, childIdx + 1)) {
				childIdx++;
			}

			if (!less(sinkIdx, childIdx)) {
				break;
			}

			swap(sinkIdx, childIdx);
			sinkIdx = childIdx;
		}
	}

	@SuppressWarnings("unchecked")
	private T[] createArray(int size) {
		return (T[]) new Comparable[size];
	}

	private void resize() {
		if (cursor == data.length) {
			resizeToSize((int) (data.length * 1.5));
		} else if (((double) cursor / data.length) < 0.5 && data.length > INITIAL_SIZE) {
			resizeToSize(Math.max((int) (data.length * 0.75), INITIAL_SIZE));
		}
	}

	private void resizeToSize(int size) {
		T[] nextData = createArray(size);
		System.arraycopy(data, 0, nextData, 0, cursor);
		data = nextData;
	}
}
