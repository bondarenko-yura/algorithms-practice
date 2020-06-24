package com.bondarenko.algo.dynamicconnectivity.percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private static final int TOP_IDX = 0;
  private final int bottomIdx;
  private final int sideLen;

  private final WeightedQuickUnionUF percolation;
  private final WeightedQuickUnionUF full;
  private final boolean[] openSides;

  private int openSidesCount;
  private boolean percolates;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }

    sideLen = n;

    int weightedQUSize = n * n + 2;
    percolation = new WeightedQuickUnionUF(weightedQUSize);
    bottomIdx = weightedQUSize - 1;

    full = new WeightedQuickUnionUF(weightedQUSize - 1);
    openSides = new boolean[weightedQUSize - 1];
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    if (!isOpen(row, col)) {
      int currentIdx = getIdx(row, col);
      openSidesCount++;
      openSides[currentIdx] = true;

      if (row == 1) {
        union(TOP_IDX, currentIdx);
      } else if (row - 1 > 0 && isOpen(row - 1, col)) {
        union(currentIdx, getIdx(row - 1, col));
      }

      if (row == sideLen) {
        percolation.union(bottomIdx, currentIdx);
      } else if (row + 1 <= sideLen && isOpen(row + 1, col)) {
        union(currentIdx, getIdx(row + 1, col));
      }

      if (col - 1 > 0 && isOpen(row, col - 1)) {
        union(currentIdx, getIdx(row, col - 1));
      }

      if (col + 1 <= sideLen && isOpen(row, col + 1)) {
        union(currentIdx, getIdx(row, col + 1));
      }
    }
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    validateArgs(row, col);
    return openSides[getIdx(row, col)];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    return isOpen(row, col)
        && full.find(getIdx(row, col)) == full.find(TOP_IDX);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openSidesCount;
  }

  // does the system percolate?
  public boolean percolates() {
    if (!percolates && percolation.find(TOP_IDX) == percolation.find(bottomIdx)) {
      percolates = true;
    }
    return percolates;
  }

  private int getIdx(int row, int col) {
    return (row - 1) * sideLen + col;
  }

  private void union(int p, int q) {
    full.union(p, q);
    if (!percolates) {
      percolation.union(p, q);
    }
  }

  private void validateArgs(int row, int col) {
    if (row < 1 || row > sideLen || col < 1 || col > sideLen) {
      throw new IllegalArgumentException();
    }
  }
}