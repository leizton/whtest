package com.wh.test.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2018/6/18
 */
public class GcTest {

  public static void main(String[] args) {
    Test t1 = new Test(), t2 = new Test();
    t1 = null;
    System.out.println("before gc");
    System.gc();
    System.out.println("after gc");
  }

  private static final class Test {
    private static final AtomicInteger ID = new AtomicInteger(0);
    private final int id = ID.getAndIncrement();

    @Override
    public String toString() {
      return "Test{" +
          "id=" + id +
          '}';
    }

    @Override
    protected void finalize() {
      System.out.println("finalize " + id);
    }
  }
}
