package com.wh.test.thrift.processor;

import com.google.common.collect.ImmutableMap;
import com.wh.test.thrift.protocol.StringsType;
import com.wh.test.thrift.service.KvStore;
import org.apache.thrift.ProcessFunction;
import org.apache.thrift.TBaseProcessor;
import org.apache.thrift.TException;

import java.util.Optional;

/**
 * 2018/4/26
 */
public class KvProcessor extends TBaseProcessor<KvStore> {

  public KvProcessor(KvStore iface) {
    super(iface, ImmutableMap.of("put", new PutServiceFunction(), "get", new GetServiceFunction()));
  }

  private static abstract class AbstractServiceFunction<Service> extends ProcessFunction<Service, StringsType> {

    AbstractServiceFunction(String methodName) {
      super(methodName);
    }

    @Override
    protected boolean isOneway() {
      return false;
    }

    @Override
    public StringsType getEmptyArgsInstance() {
      return new StringsType();
    }
  }

  private static final class PutServiceFunction extends AbstractServiceFunction<KvStore> {

    PutServiceFunction() {
      super("put");
    }

    @Override
    public StringsType getResult(KvStore iface, StringsType args) throws TException {
      Optional<String> result = iface.put(args.getStrs()[0], args.getStrs()[1]);
      return result.map(StringsType::new).orElseGet(StringsType::new);
    }
  }

  private static final class GetServiceFunction extends AbstractServiceFunction<KvStore> {

    GetServiceFunction() {
      super("get");
    }

    @Override
    public StringsType getResult(KvStore iface, StringsType args) throws TException {
      Optional<String> result = iface.get(args.getStrs()[0]);
      return result.map(StringsType::new).orElseGet(StringsType::new);
    }
  }
}
