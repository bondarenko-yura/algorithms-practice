package com.bondarenko.ds;

public class StackLinkedListImpl<E> implements Stack<E> {

	private Node<E> head;
	private int size;

	@Override
	public void push(E element) {
		size++;
		head = new Node<>(element, head);
	}

	@Override
	public E pop() {
		if (head == null) {
			return null;
		}

		size--;
		E element = head.element;
		head = head.next;
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
		private final Node<E> next;

		private Node(E element, Node<E> next) {
			this.element = element;
			this.next = next;
		}
	}
}
