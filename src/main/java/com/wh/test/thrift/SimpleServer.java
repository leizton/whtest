package com.wh.test.thrift;

import com.wh.test.thrift.processor.KvClient;
import com.wh.test.thrift.processor.KvProcessor;
import com.wh.test.thrift.service.KvStore;
import com.wh.test.thrift.service.impl.KvStoreImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 2018/4/26
 */
public class SimpleServer {
  private static final Logger LOG = LoggerFactory.getLogger(SimpleServer.class);

  private static volatile TServer server;
  private static final CountDownLatch waitServerStart = new CountDownLatch(1);
  private static final CountDownLatch waitServerStop = new CountDownLatch(1);

  public static void main(String[] args) throws Exception {
    new Thread(SimpleServer::runServer, "server-runner").start();
    waitServerStart.await();
    runClient();
    server.stop();
    waitServerStop.await();
  }

  private static void runServer() {
    try {
      TProcessor processor = new KvProcessor(new KvStoreImpl());
      TServerTransport transport = new TServerSocket(10001);
      TServer.Args serverArgs = new TServer.Args(transport)
          .processor(processor)
          .protocolFactory(new TCompactProtocol.Factory());
      server = new TSimpleServer(serverArgs);

      waitServerStart.countDown();
      LOG.info("server start");
      server.serve();
      LOG.info("server exit");
    } catch (Exception e) {
      LOG.error("runServer exception", e);
    } finally {
      waitServerStop.countDown();
    }
  }

  private static void runClient() throws Exception {
    try {
      TTransport transport = new TSocket("localhost", 10001);
      transport.open();

      TProtocol protocol = new TCompactProtocol(transport);
      KvStore service = new KvClient(protocol);
      System.out.println("get: " + service.get("a").isPresent());
      System.out.println("put: " + service.put("a", "65").isPresent());
      System.out.println("put: " + service.put("a", "97").isPresent());
      System.out.println("put: " + service.put("a", "97").get());
      System.out.println("get: " + service.get("a").isPresent());
      System.out.println("get: " + service.get("a").get());

      transport.close();
    } catch (Exception e) {
      LOG.error("runClient exception", e);
    }
  }
}
