package com.wh.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2018/6/19
 */
public class ForkJoinPoolTest {
  private static final Logger LOG = LoggerFactory.getLogger(ForkJoinPoolTest.class);

  public static void main(String[] args) {
    final int parallelism = 4;
    ForkJoinPool pool = new ForkJoinPool(parallelism);
    final long start = System.currentTimeMillis();
    int r = pool.invoke(new Task(-1, 1, 8));
    LOG.info("result: {}, {}, {}", r, Task.ID_GEN.get(),
        (System.currentTimeMillis() - start) / 1000);  // 8 15 8/parallelism
  }

  private static final class Task extends RecursiveTask<Integer> {
    private static final AtomicInteger ID_GEN = new AtomicInteger(0);
    private final int id = ID_GEN.getAndIncrement();
    private final int parentId;
    private final int level;
    private final int num;

    Task(int parentId, int level, int num) {
      this.parentId = parentId;
      this.level = level;
      this.num = num;
    }

    @Override
    protected Integer compute() {
      if (num < 2) {
        TestUtil.sleepSec(1);
        LOG.info("{}d: {}, {}, {}", aid('<'), parentId, id, num);
        return num;
      }
      LOG.info("{}s: {}, {}, {}", aid('-'), parentId, id, num);
      Task t1 = new Task(id, level + 1, num >> 1);
      Task t2 = new Task(id, level + 1, num - (num >> 1));
      invokeAll(t1, t2);
      return t1.join() + t2.join();
    }

    private String aid(char c) {
      StringBuilder str = new StringBuilder(level * 2);
      for (int i = 0; i < level; i++) {
        str.append(c).append(c);
      }
      return str.toString();
    }
  }
}
