package com.wh.test.util;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 2018/5/4
 */
@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
public class MapUtil {

  public static <K, V> MapBuilder<K, V, Map<K, V>> newMap() {
    return new MapBuilder<>(new HashMap<>());
  }

  public static <K, V> MapBuilder<K, V, SortedMap<K, V>> newSortedMap() {
    return new MapBuilder<>(new TreeMap<>());
  }

  public static <K, V> MapBuilder<K, V, ConcurrentMap<K, V>> newConcurrentMap() {
    return new MapBuilder<>(new ConcurrentHashMap<>());
  }

  public static <K, V> MapBuilder<K, V, ConcurrentSkipListMap<K, V>> newConcurrentSkipListMap() {
    return new MapBuilder<>(new ConcurrentSkipListMap<>());
  }

  public static class MapBuilder<K, V, M> {
    private final Map<K, V> map;

    private MapBuilder(Map<K, V> map) {
      this.map = map;
    }

    public MapBuilder<K, V, M> put(K k, V v) {
      map.put(k, v);
      return this;
    }

    public M build() {
      return (M) map;
    }
  }
}
