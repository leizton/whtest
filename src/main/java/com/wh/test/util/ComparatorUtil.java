package com.wh.test.util;

import java.util.*;

/**
 * 2018/5/6
 */
@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
public class ComparatorUtil {

  public static final class SortedMapComparator<K extends Comparable<K>, V> implements Comparator<SortedMap<K, V>> {

    private final Comparator<V> valueCmp;

    public SortedMapComparator(Comparator<V> valueCmp) {
      this.valueCmp = valueCmp;
    }

    @Override
    public int compare(SortedMap<K, V> o1, SortedMap<K, V> o2) {
      if (o1 == null) {
        return -1;
      } else if (o2 == null) {
        return 1;
      } else if (o1.size() < o2.size()) {
        return -1;
      } else if (o1.size() > o2.size()) {
        return 1;
      } else {
        Iterator<Map.Entry<K, V>> it1 = o1.entrySet().iterator(), it2 = o2.entrySet().iterator();
        Map.Entry<K, V> e1, e2;
        int ret = 0;
        while (ret == 0 && it1.hasNext()) {
          e1 = it1.next();
          e2 = it2.next();
          ret = e1.getKey().compareTo(e2.getKey());
          if (ret != 0) return ret;
          ret = valueCmp.compare(e1.getValue(), e2.getValue());
        }
        return ret;
      }
    }
  }

  public static final class SortedSetComparator<E extends Comparable<E>> implements Comparator<SortedSet<E>> {

    public static final SortedSetComparator<String> STRING_SORTED_SET_COMPARATOR = new SortedSetComparator<>();

    @Override
    public int compare(SortedSet<E> o1, SortedSet<E> o2) {
      if (o1 == null) {
        return -1;
      } else if (o2 == null) {
        return 1;
      } else if (o1.size() < o2.size()) {
        return -1;
      } else if (o1.size() > o2.size()) {
        return 1;
      } else {
        Iterator<E> it1 = o1.iterator(), it2 = o2.iterator();
        int ret = 0;
        while (ret == 0 && it1.hasNext()) {
          ret = it1.next().compareTo(it2.next());
        }
        return ret;
      }
    }
  }
}
