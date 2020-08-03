package com.bondarenko.algo.princeton.c1.sort;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

	private Node<Item> head;
	private Node<Item> tail;
	private int size;

	// construct an empty deque
	public Deque() {
	}

	// is the deque empty?
	public boolean isEmpty() {
		return size() == 0;
	}

	// return the number of items on the deque
	public int size() {
		return size;
	}

	// add the item to the front
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

	// add the item to the back

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

	// remove and return the item from the front
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
	// remove and return the item from the back

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
	// return an iterator over items in order from front to back

	public Iterator<Item> iterator() {
		return new DequeueIterator();
	}
	// unit testing (required)

	public static void main(String[] args) {
		Deque<Integer> d = new Deque<>();
		StdOut.printf("Size %d%n", d.size());
		StdOut.printf("Empty %b%n", d.isEmpty());
		d.addFirst(10);
		StdOut.printf("Size %d%n", d.size());
		StdOut.printf("Empty %b%n", d.isEmpty());
		d.addLast(20);
		StdOut.printf("Size %d%n", d.size());
		StdOut.printf("Empty %b%n", d.isEmpty());
		for (Integer integer : d) {
			StdOut.printf("Val %d%n", integer);
		}
		StdOut.printf("First %d%n", d.removeFirst());
		StdOut.printf("Size %d%n", d.size());
		StdOut.printf("Empty %b%n", d.isEmpty());
		StdOut.printf("Last %d%n", d.removeLast());
		StdOut.printf("Size %d%n", d.size());
		StdOut.printf("Empty %b%n", d.isEmpty());
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