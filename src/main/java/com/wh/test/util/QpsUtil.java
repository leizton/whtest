package com.wh.test.util;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 2018/5/29
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class QpsUtil implements TimerTask {
  private static final Logger LOG = LoggerFactory.getLogger(QpsUtil.class);

  private static final HashedWheelTimer timer = new HashedWheelTimer(
      new NamedThreadFactory("intqps"), 100, TimeUnit.MILLISECONDS, 10);

  private final String name;
  private final long periodSeconds;
  private final boolean printLog;
  private final AtomicLong sum = new AtomicLong(0);
  private volatile long lastSum = 0;
  private volatile int qps = 0;
  private volatile long lastTime;

  public QpsUtil() {
    this(TimeUnit.MINUTES.toSeconds(1));
  }

  public QpsUtil(long periodSeconds) {
    this("", periodSeconds, false);
  }

  public QpsUtil(String name, long periodSeconds, boolean printLog) {
    this.name = name;
    this.periodSeconds = periodSeconds;
    this.printLog = printLog;
    lastTime = System.currentTimeMillis() / 1000;
    timer.newTimeout(this, periodSeconds, TimeUnit.SECONDS);
  }

  public void inc() {
    sum.getAndIncrement();
  }

  public void inc(int n) {
    sum.getAndAdd(n);
  }

  public long sum() {
    return sum.get();
  }

  public int qps() {
    return qps;
  }

  @Override
  public void run(Timeout timeout) {
    long currTime = System.currentTimeMillis() / 1000;
    try {
      if (currTime == lastTime) {
        qps = 0;
      } else {
        long tmp = sum.get();
        qps = (int) ((tmp - lastSum) / (currTime - lastTime));
        lastSum = tmp;
        if (printLog) {
          LOG.info("qps-{}: " + qps, name);
        }
      }
    } catch (Exception e) {
      LOG.info("intqps refresh exception", e);
    } finally {
      lastTime = currTime;
      timer.newTimeout(this, periodSeconds, TimeUnit.SECONDS);
    }
  }
}
