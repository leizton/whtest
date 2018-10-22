package com.wh.test.util.java.detail;

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
    // 在after gc后，t1.finalize被调用了, t2.finalize未被调用
  }

  private static final class Test {
    private static final AtomicInteger ID_GEN = new AtomicInteger(0);
    private final int id = ID_GEN.getAndIncrement();

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
