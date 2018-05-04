package com.wh.test.util;

import java.util.Collection;
import java.util.Map;

/**
 * 2018/5/4
 */
@SuppressWarnings("unused")
public class CollectionUtil {

  public static boolean isNullOrEmpty(Collection c) {
    return c == null || c.isEmpty();
  }

  public static boolean isNullOrEmpty(Map m) {
    return m == null || m.isEmpty();
  }
}
