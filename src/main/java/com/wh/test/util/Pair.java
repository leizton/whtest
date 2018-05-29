package com.wh.test.util;

/**
 * 2018/5/29
 */
@SuppressWarnings("unused")
public class Pair<K, V> {

  private K key;
  private V value;

  public Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public K key() {
    return key;
  }

  public void setKey(K key) {
    this.key = key;
  }

  public V value() {
    return value;
  }

  public void setValue(V value) {
    this.value = value;
  }
}
