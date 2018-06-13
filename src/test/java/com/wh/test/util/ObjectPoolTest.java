package com.wh.test.util;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

/**
 * 2018/6/13
 */
public class ObjectPoolTest {
  private static final Logger LOG = LoggerFactory.getLogger(ObjectPoolTest.class);

  public static void main(String[] args) throws Exception {
    GenericObjectPool<Test> pool = createPool();

    final Test t0 = pool.borrowObject();  // call PooledObjectFactory.makeObject()
    LOG.info("borrowed {}", t0);
    pool.invalidateObject(t0);  // close t0 in PooledObjectFactory.destroyObject()
    LOG.info("");

    final Test t1 = pool.borrowObject();  // call PooledObjectFactory.makeObject()
    LOG.info("borrowed {}", t1);  // t1.id not equals t0.id
    pool.returnObject(t1);
    LOG.info("");

    final Test t2 = pool.borrowObject();  // won't call PooledObjectFactory.makeObject()
    LOG.info("borrowed {}", t2);
    pool.close();  // do not close t2
    LOG.info("to return t2");
    pool.returnObject(t2);  // close t2 in PooledObjectFactory.destroyObject()
    LOG.info("returned t2");
    LOG.info("");

    pool.close();
//    pool.borrowObject();  // throw IllegalStateException: Pool not open
    LOG.info("");

    GenericObjectPool<Test> pool1 = createPool();
    pool1.returnObject(pool1.borrowObject());
    pool1 = null;
    System.gc();  // won't close object
    Thread.sleep(2000);
  }

  private static GenericObjectPool<Test> createPool() {
    final int poolSize = 1;
    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
    config.setMaxTotal(poolSize);
    config.setMaxIdle(poolSize);
    config.setMinIdle(poolSize);

    return new GenericObjectPool<>(new PooledObjectFactory<Test>() {
      private volatile boolean closed = false;

      @Override
      public PooledObject<Test> makeObject() throws Exception {
        LOG.info("PooledObjectFactory::makeObject()");
        return new DefaultPooledObject<>(new Test(UUID.randomUUID().toString()));
      }

      @Override
      public void destroyObject(PooledObject<Test> p) throws Exception {
        closed = true;
        LOG.info("PooledObjectFactory::destroyObject()");
        p.getObject().close();
      }

      @Override
      public boolean validateObject(PooledObject<Test> p) {
        return !closed;
      }

      @Override
      public void activateObject(PooledObject<Test> p) throws Exception {
      }

      @Override
      public void passivateObject(PooledObject<Test> p) throws Exception {
      }
    }, config);
  }

  public static final class Test implements Closeable {
    private final String id;

    Test(String id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return "Test{" +
          "id='" + id + '\'' +
          '}';
    }

    @Override
    public void close() throws IOException {
      LOG.info("Test::close() " + this.toString());
    }
  }
}
