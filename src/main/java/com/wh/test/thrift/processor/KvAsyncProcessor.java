package com.wh.test.thrift.processor;

import org.apache.thrift.TBaseAsyncProcessor;

import java.util.Map;

/**
 * 2018/4/26
 */
public class KvAsyncProcessor extends TBaseAsyncProcessor {

  public KvAsyncProcessor(Object iface, Map processMap) {
    super(iface, processMap);
  }
}
