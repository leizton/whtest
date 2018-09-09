package com.wh.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * fanyaoqun 2018/4/18
 * copy from dubbo
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class NamedThreadFactory implements ThreadFactory {
  private static final Logger LOG = LoggerFactory.getLogger(NamedThreadFactory.class);

  private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

  private final AtomicInteger mThreadNum = new AtomicInteger(1);

  private final String mPrefix;

  private final boolean mDaemon;

  private final ThreadGroup mGroup;

  public NamedThreadFactory() {
    this("pool-" + POOL_SEQ.getAndIncrement(), false);
  }

  public NamedThreadFactory(String prefix) {
    this(prefix, false);
  }

  public NamedThreadFactory(String prefix, boolean daemon) {
    mPrefix = prefix + "-thread-";
    mDaemon = daemon;
    SecurityManager s = System.getSecurityManager();
    mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
  }

  @SuppressWarnings("NullableProblems")
  public Thread newThread(Runnable runnable) {
    String name = mPrefix + mThreadNum.getAndIncrement();
    Thread th = new Thread(mGroup, runnable, name, 0);
    th.setDaemon(mDaemon);
    LOG.info("create thread: {}", th.getName());
    return th;
  }

  public ThreadGroup getThreadGroup() {
    return mGroup;
  }
}
