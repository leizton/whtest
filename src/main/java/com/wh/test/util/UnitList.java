package com.wh.test.util;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * 2018/6/15
 * <p>
 * friendly to gc
 */
@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public class UnitList<E> implements Iterable<E> {

  private final ArrayList<Object[]> units;
  private final int log2OfUnitSize;
  private final int unitSize;
  private volatile int unitPos = 0;
  private volatile int pos = 0;
  private volatile boolean immutable = false;

  public UnitList(int initUnitNum, int log2OfUnitSize) {
    Preconditions.checkArgument(initUnitNum >= 0, "initUnitNum must >= 0");
    Preconditions.checkArgument(log2OfUnitSize >= 0, "unitSize must > 0");
    this.units = new ArrayList<>(initUnitNum);
    this.log2OfUnitSize = log2OfUnitSize;
    this.unitSize = (1 << log2OfUnitSize);
  }

  public synchronized void add(E e) {
    Preconditions.checkState(!immutable, "units is immutable");
    if (pos == unitSize) {
      unitPos++;
      pos = 0;
    }
    while (unitPos >= units.size()) {
      units.add(new Object[unitSize]);
    }
    units.get(unitPos)[pos++] = e;
  }

  public void setImmutable() {
    immutable = true;
  }

  public void sort(Comparator<E> cmp) {
    Preconditions.checkState(immutable, "units is mutable");
    if (pos < unitSize && pos > 0) {
      units.set(unitPos, Arrays.copyOf(units.get(unitPos), pos));
    }
    int size = size();
    buildHeap(cmp, size);
    for (int i = size - 1; i > 0; i--) {
      swap(0, i);
      heapify(cmp, i - 1, 0);
    }
  }

  private void buildHeap(Comparator<E> cmp, int size) {
    size--;
    for (int i = size / 2; i >= 0; i--) {
      heapify(cmp, size, i);
    }
  }

  private void heapify(Comparator<E> cmp, int size, int i) {
    int l = (i << 1), r = (i << 1) + 1;
    int max = i;
    if (l <= size && cmp.compare(get(l), get(i)) > 0) max = l;
    if (r <= size && cmp.compare(get(r), get(max)) > 0) max = r;
    if (max != i) {
      swap(i, max);
      heapify(cmp, size, max);
    }
  }

  public int size() {
    return (unitPos << log2OfUnitSize) + pos;
  }

  private E get(int i) {
    int p = i >>> log2OfUnitSize;
    return (E) units.get(p)[i - (p << log2OfUnitSize)];
  }

  private void set(int i, E e) {
    int p = i >>> log2OfUnitSize;
    units.get(p)[i - (p << log2OfUnitSize)] = e;
  }

  private void swap(int i, int j) {
    E e = get(i);
    set(i, get(j));
    set(j, e);
  }

  public synchronized void destory() {
    Preconditions.checkState(immutable, "units is mutable");
    units.clear();
    unitPos = pos = 0;
  }

  @Override
  public Iterator<E> iterator() {
    Preconditions.checkState(immutable, "units is mutable");
    return new Iterator<E>() {
      int unitIndex = 0;
      int index = 0;

      @Override
      public boolean hasNext() {
        return unitIndex < unitPos || index < pos;
      }

      @Override
      public E next() {
        E e = (E) units.get(unitIndex)[index];
        if (++index == unitSize) {
          index = 0;
          unitIndex++;
        }
        return e;
      }
    };
  }
}
