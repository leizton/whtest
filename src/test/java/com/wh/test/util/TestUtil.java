package com.wh.test.util;

import java.util.concurrent.TimeUnit;

/**
 * 2018/6/19
 */
public class TestUtil {

  public static void sleepSec(int sec) {
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(sec));
    } catch (Exception ignore) {
    }
  }
}
