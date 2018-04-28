package com.wh.test.thrift.processor;

import com.wh.test.thrift.protocol.StringsType;
import com.wh.test.thrift.service.KvStore;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;

import java.util.Optional;

/**
 * 2018/4/26
 */
public class KvClient extends TServiceClient implements KvStore {

  public KvClient(TProtocol protocol) {
    super(protocol);
  }

  public Optional<String> put(String key, String value) throws TException {
    send("put", key, value);
    String[] ret = receive("put");
    return ret.length == 0 ? Optional.empty() : Optional.of(ret[0]);
  }

  public Optional<String> get(String key) throws TException {
    send("get", key);
    String[] ret = receive("get");
    return ret.length == 0 ? Optional.empty() : Optional.of(ret[0]);
  }

  private void send(String methodName, String... strs) throws TException {
    StringsType args = new StringsType(strs);
    sendBase(methodName, args);
  }

  private String[] receive(String methodName) throws TException {
    StringsType result = new StringsType();
    receiveBase(result, methodName);
    return result.getStrs();
  }
}
