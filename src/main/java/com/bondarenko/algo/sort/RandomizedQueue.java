package com.bondarenko.algo.sort;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private Item[] data;
  private int tailIdx;

  // construct an empty randomized queue
  public RandomizedQueue() {
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

  // unit testing (required)
  public static void main(String[] args) {
    RandomizedQueue<Integer> d = new RandomizedQueue<>();
    StdOut.printf("Size %d%n", d.size());
    StdOut.printf("Empty %b%n", d.isEmpty());
    d.enqueue(10);
    StdOut.printf("Size %d%n", d.size());
    StdOut.printf("Empty %b%n", d.isEmpty());
    d.enqueue(20);
    StdOut.printf("Size %d%n", d.size());
    StdOut.printf("Empty %b%n", d.isEmpty());
    d.enqueue(30);
    d.enqueue(40);
    d.enqueue(50);
    d.enqueue(60);
    d.enqueue(70);
    d.enqueue(80);
    d.enqueue(90);
    d.enqueue(100);
    d.enqueue(0);
    for (Integer integer : d) {
      StdOut.printf("Val %d%n", integer);
    }
    StdOut.printf("First %d%n", d.dequeue());
    StdOut.printf("Size %d%n", d.size());
    StdOut.printf("Sample %d%n", d.sample());
    StdOut.printf("Empty %b%n", d.isEmpty());
    StdOut.printf("Last %d%n", d.dequeue());
    StdOut.printf("Size %d%n", d.size());
    StdOut.printf("Sample %d%n", d.sample());
    StdOut.printf("Last %d%n", d.dequeue());
    StdOut.printf("Last %d%n", d.dequeue());
    StdOut.printf("Last %d%n", d.dequeue());
    StdOut.printf("Last %d%n", d.dequeue());
    StdOut.printf("Last %d%n", d.dequeue());
    StdOut.printf("Last %d%n", d.dequeue());
    StdOut.printf("Last %d%n", d.dequeue());
    StdOut.printf("Last %d%n", d.dequeue());
    StdOut.printf("Last %d%n", d.dequeue());
    StdOut.printf("Empty %b%n", d.isEmpty());
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