package com.bondarenko.algo.sort;

public class StackArrayImpl<E> implements Stack<E> {


  private Object[] data = new Object[1];
  private int idx;

  @Override
  public void push(E element) {
    if (data.length == idx) {
      resize(data.length * 2);
    }

    data[idx++] = element;
  }

  @Override
  public E pop() {
    if (idx < data.length / 4) {
      resize(data.length / 2);
    }

    if (idx - 1 < 0) {
      return null;
    }

    E element = (E) data[--idx];
    data[idx] = null;
    return element;
  }

  @Override
  public boolean isEmpty() {
    return idx == 0;
  }

  @Override
  public int size() {
    return idx;
  }

  private void resize(int size) {
    Object[] resized = new Object[size];
    System.arraycopy(data, 0, resized, 0, idx);
    data = resized;
  }
}
