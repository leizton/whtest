package com.wh.test.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * 2018/6/15
 */
public class UnitListTest {

  @Test
  public void test() {
    UnitList<Integer> lst = new UnitList<>(5, 2);
    Random rand = new Random();
    for (int i = 0; i < 123; i++) {
      lst.add(rand.nextInt(1000));
    }
    lst.setImmutable();

    Assert.assertEquals(123, lst.size());

    lst.sort(Integer::compareTo);
    int last = -1;
    for (Integer i : lst) {
      Assert.assertTrue(last <= i);
      last = i;
    }

    lst.destory();
  }
}
