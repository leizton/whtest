package com.wh.test.util;

import java.util.Arrays;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 2018/6/4
 */
@SuppressWarnings({"UnusedAssignment", "MismatchedReadAndWriteOfArray"})
public class ConcurrentSkipListTest {

  public static void main(String[] args) {
    final int NUM = 1024 * 1024;
    final int[] src = new int[NUM];
    for (int i = 0; i < NUM; i++) {
      src[i] = i;
    }
    ShuffleUtil.shuffle(src);

    long t1 = System.currentTimeMillis();
    for (int i = 0; i < 100; i++) {
      int[] tmp = Arrays.copyOf(src, src.length);
      Arrays.sort(tmp);
      tmp = null;
      System.gc();
    }
    t1 = System.currentTimeMillis() - t1;

    long t2 = System.currentTimeMillis();
    for (int i = 0; i < 100; i++) {
      ConcurrentSkipListSet<Integer> set = new ConcurrentSkipListSet<>();
      for (int n : src) {
        set.add(n);
      }
      set = null;
      System.gc();
    }
    t2 = System.currentTimeMillis() - t2;

    System.out.println(t1 + ", " + t2);  // 9783, 202519
  }
}
