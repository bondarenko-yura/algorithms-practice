package com.bondarenko.algo.sort;

public class QueueArrayImpl<E> implements Queue<E> {

  private static final double SCALE_UP_LOAD = 0.8;
  private static final double SCALE_DOWN_LOAD = 0.2;

  private Object[] data = new Object[1];
  private int headIdx;
  private int tailIdx;

  @Override
  public void enqueue(E element) {
    resize();
    data[tailIdx++] = element;
  }

  @Override
  public E dequeue() {
    resize();
    if (isEmpty()) {
      return null;
    }
    E element = (E) data[headIdx];
    data[headIdx] = null;
    headIdx++;
    return element;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public int size() {
    return tailIdx - headIdx;
  }

  private void resize() {
    double load = size() / (double) data.length;
    if (data.length == tailIdx || (load > 0 && load < SCALE_DOWN_LOAD)) {
      Object[] resize;
      if (load > SCALE_UP_LOAD) {
        resize = new Object[data.length * 2];
      } else if (load < SCALE_DOWN_LOAD) {
        resize = new Object[data.length / 2];
      } else {
        resize = new Object[data.length];
      }
      System.arraycopy(data, headIdx, resize, 0, size());
      data = resize;
      tailIdx = size();
      headIdx = 0;
    }
  }
}
