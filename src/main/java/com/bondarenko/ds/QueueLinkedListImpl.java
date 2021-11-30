package com.bondarenko.ds;

public class QueueLinkedListImpl<E> implements Queue<E> {

	private Node<E> head;
	private Node<E> tail;
	private int size;

	@Override
	public void enqueue(E element) {
		Node<E> oldHead = head;
		head = new Node<>(element);
		if (oldHead == null) {
			tail = head;
		} else {
			oldHead.next = head;
		}
		size++;
	}

	@Override
	public E dequeue() {
		if (tail == null) {
			return null;
		}
		size--;
		E element = tail.element;
		tail = tail.next;
		if (tail == null) {
			head = null;
		}
		return element;
	}

	@Override
	public boolean isEmpty() {
		return head == null;
	}

	@Override
	public int size() {
		return size;
	}

	private static final class Node<E> {
		private final E element;
		private Node<E> next;

		private Node(E element) {
			this.element = element;
		}
	}
}
