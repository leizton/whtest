package com.wh.test.util.java.detail;

/**
 * 2018/9/5
 */
public class L1CacheTest {

  public static void main(String[] args) {
    final int colnum = 1024 * 1024, rownum = 8;
    long[][] mat = new long[colnum][];
    for (int i = 0; i < colnum; i++) {
      mat[i] = new long[rownum];
      for (int j = 0; j < rownum; j++) {
        mat[i][j] = i + j;
      }
    }

    long sum1 = 0;
    long time1 = System.currentTimeMillis();
    for (int i = 0; i < colnum; i++) {
      // l1 cache hit
      for (int j = 0; j < rownum; j++) {
        sum1 += mat[i][j];
      }
    }
    time1 = System.currentTimeMillis() - time1;

    long sum2 = 0;
    long time2 = System.currentTimeMillis();
    for (int i = 0; i < rownum; i++) {
      // l1 cache miss
      for (int j = 0; j < colnum; j++) {
        sum2 += mat[j][i];
      }
    }
    time2 = System.currentTimeMillis() - time2;

    System.out.println(sum1);
    System.out.println(sum2);
    System.out.println(time1);  // 16ms
    System.out.println(time2);  // 50ms
  }
}
