package com.wh.test.thrift.service.impl;

import com.wh.test.thrift.service.KvStore;
import org.apache.thrift.TException;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 2018/4/26
 */
public class KvStoreImpl implements KvStore {

  private final ConcurrentMap<String, String> data = new ConcurrentHashMap<>();

  @Override
  public Optional<String> put(String key, String value) throws TException {
    return Optional.ofNullable(data.put(key, value));
  }

  @Override
  public Optional<String> get(String key) throws TException {
    return Optional.ofNullable(data.get(key));
  }
}
