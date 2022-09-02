package com.bondarenko.ds.deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DequeLinked<Item> implements Deque<Item> {

	private Node<Item> head;
	private Node<Item> tail;
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
	public void addFirst(Item item) {
		validateNotNull(item);
		size++;
		if (head == null) {
			head = new Node<>(item);
			tail = head;
		} else {
			Node<Item> curHead = head;
			head = new Node<>(item);
			head.tail = curHead;
			curHead.head = head;
		}
	}

	@Override
	public void addLast(Item item) {
		validateNotNull(item);
		size++;
		if (tail == null) {
			tail = new Node<>(item);
			head = tail;
		} else {
			Node<Item> nextTail = new Node<>(item);
			tail.tail = nextTail;
			nextTail.head = tail;
			tail = nextTail;
		}
	}

	@Override
	public Item removeFirst() {
		validateNotEmpty();
		size--;
		Node<Item> currHead = head;
		head = currHead.tail;
		if (head == null) {
			tail = null;
		} else {
			head.head = null;
		}
		return currHead.item;
	}

	@Override
	public Item removeLast() {
		validateNotEmpty();
		size--;
		Node<Item> currTail = tail;
		tail = currTail.head;
		if (tail == null) {
			head = null;
		} else {
			tail.tail = null;
		}
		return currTail.item;
	}

	@Override
	public Iterator<Item> iterator() {
		return new DequeueIterator();
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

	private final class DequeueIterator implements Iterator<Item> {

		private Node<Item> itHead = head;

		@Override
		public boolean hasNext() {
			return itHead != null;
		}

		@Override
		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Node<Item> head = itHead;
			itHead = head.tail;
			return head.item;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static final class Node<Item> {
		private final Item item;
		private Node<Item> head;
		private Node<Item> tail;

		private Node(Item item) {
			this.item = item;
		}
	}
}
