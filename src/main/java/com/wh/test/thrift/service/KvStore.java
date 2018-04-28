package com.wh.test.thrift.service;

import org.apache.thrift.TException;

import java.util.Optional;

/**
 * 2018/4/26
 */
public interface KvStore {

  Optional<String> put(String key, String value) throws TException;

  Optional<String> get(String key) throws TException;
}
