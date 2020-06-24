package com.bondarenko.algo.sort;

public interface Queue<E> {
  void enqueue(E element);
  E dequeue();
  boolean isEmpty();
  int size();
}
