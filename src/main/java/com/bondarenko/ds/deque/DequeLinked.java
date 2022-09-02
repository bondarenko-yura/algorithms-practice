package com.bondarenko.ds.deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DequeLinked<E> implements Deque<E> {

	private Node<E> head;
	private Node<E> tail;
	private int size;

	public DequeLinked() {
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void addFirst(E item) {
		validateNotNull(item);
		size++;
		if (head == null) {
			head = new Node<>(item);
			tail = head;
		} else {
			Node<E> curHead = head;
			head = new Node<>(item);
			head.tail = curHead;
			curHead.head = head;
		}
	}

	@Override
	public void addLast(E item) {
		validateNotNull(item);
		size++;
		if (tail == null) {
			tail = new Node<>(item);
			head = tail;
		} else {
			Node<E> nextTail = new Node<>(item);
			tail.tail = nextTail;
			nextTail.head = tail;
			tail = nextTail;
		}
	}

	@Override
	public E removeFirst() {
		validateNotEmpty();
		size--;
		Node<E> currHead = head;
		head = currHead.tail;
		if (head == null) {
			tail = null;
		} else {
			head.head = null;
		}
		return currHead.item;
	}

	@Override
	public E removeLast() {
		validateNotEmpty();
		size--;
		Node<E> currTail = tail;
		tail = currTail.head;
		if (tail == null) {
			head = null;
		} else {
			tail.tail = null;
		}
		return currTail.item;
	}

	@Override
	public Iterator<E> iterator() {
		return new DequeueIterator();
	}

	private void validateNotNull(E item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
	}

	private void validateNotEmpty() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
	}

	private final class DequeueIterator implements Iterator<E> {

		private Node<E> itHead = head;

		@Override
		public boolean hasNext() {
			return itHead != null;
		}

		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Node<E> head = itHead;
			itHead = head.tail;
			return head.item;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static final class Node<E> {
		private final E item;
		private Node<E> head;
		private Node<E> tail;

		private Node(E item) {
			this.item = item;
		}
	}
}
