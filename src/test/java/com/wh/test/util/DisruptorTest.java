package com.wh.test.util;

import com.google.common.collect.Lists;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2018/9/8
 */
public class DisruptorTest {
  private static final Logger LOG = LoggerFactory.getLogger(DisruptorTest.class);

  private final List<Integer> source;
  private final int answer;

  private final ThreadFactory threadFactory = new NamedThreadFactory("disruptortest");

  public DisruptorTest() {
    final int param = 18;
    source = new ArrayList<>(param);
    int answer = 0;
    for (int i = 1; i <= param; i++) {
      source.add(i);
      answer += i;
    }
    this.answer = answer;
  }

  @Test
  public void test1P1C() {
    final int ringBufSize = 4;
    Disruptor<NumEvent> disruptor = new Disruptor<>(new NumEventFactory(), ringBufSize,
        threadFactory, ProducerType.SINGLE, new BusySpinWaitStrategy());

    Sink sink = new Sink();
    disruptor.handleEventsWith(sink).then(new NumEventClear());
    disruptor.start();

    Sourcer sourcer = new Sourcer(disruptor.getRingBuffer(), source);
    sourcer.run();

    disruptor.shutdown();
    Assert.assertEquals(answer, sink.sum);
  }

  @Test
  public void testMP1C() throws Exception {
    final int ringBufSize = 8;
    Disruptor<NumEvent> disruptor = new Disruptor<>(new NumEventFactory(), ringBufSize, threadFactory);
    Sink sink = new Sink();
    disruptor.handleEventsWith(sink).then(new NumEventClear());
    disruptor.start();

    final int producerNum = 4;
    List<List<Integer>> sources = Lists.partition(source, source.size() / producerNum);
    ExecutorService executor = Executors.newFixedThreadPool(producerNum);
    for (List<Integer> sub : sources) {
      executor.submit(() -> new Sourcer(disruptor.getRingBuffer(), sub).run());
    }

    executor.shutdown();
    while (!executor.awaitTermination(1, TimeUnit.MINUTES)) ;
    disruptor.shutdown();
    Assert.assertEquals(answer, sink.sum);
  }

  @Test
  public void test1PMC() {
    final int ringBufSize = 4;
    Disruptor<NumEvent> disruptor = new Disruptor<>(new NumEventFactory(), ringBufSize, threadFactory);

    Combiner combiner1 = new Combiner(2, 0);
    Combiner combiner2 = new Combiner(2, 1);
    Reducer reducer = new Reducer();
    disruptor.handleEventsWith(combiner1).then(reducer);
    disruptor.handleEventsWith(combiner2).then(reducer);
    disruptor.start();

    Sourcer sourcer = new Sourcer(disruptor.getRingBuffer(), source);
    sourcer.run();
    disruptor.shutdown();

    Assert.assertEquals(answer, reducer.sum);
  }

  private static final class NumEvent {
    private int num;
  }

  private static final class NumEventFactory implements EventFactory<NumEvent> {
    @Override
    public NumEvent newInstance() {
      return new NumEvent();
    }
  }

  private static final class NumEventTranslator implements EventTranslatorOneArg<NumEvent, Integer> {
    private static final NumEventTranslator INSTANCE = new NumEventTranslator();

    @Override
    public void translateTo(NumEvent ev, long seq, Integer num) {
      ev.num = num;
    }
  }

  // help for gc
  private static final class NumEventClear implements EventHandler<NumEvent> {
    @Override
    public void onEvent(NumEvent ev, long seq, boolean endOfBatch) {
      LOG.info("clear: {}, {}", ev.num, seq);
    }
  }

  // producer
  private static final class Sourcer {
    private final RingBuffer<NumEvent> ringBuf;
    private final Iterable<Integer> source;

    Sourcer(RingBuffer<NumEvent> ringBuf, Iterable<Integer> source) {
      this.ringBuf = ringBuf;
      this.source = source;
    }

    void run() {
      for (Integer num : source) {
        // ring.publishEvent((ev, seq, num) -> ev.num = num, num);
        ringBuf.publishEvent(NumEventTranslator.INSTANCE, num);
      }
    }
  }

  // consumer
  private static final class Sink implements EventHandler<NumEvent> {
    private int sum;

    @Override
    public void onEvent(NumEvent ev, long seq, boolean endOfBatch) {
      sum += ev.num;
    }
  }

  // consumer phase1
  private static final class Combiner implements EventHandler<NumEvent> {
    private final int combinerNum;
    private final int id;
    private int sum;

    Combiner(int combinerNum, int id) {
      this.combinerNum = combinerNum;
      this.id = id;
    }

    @Override
    public void onEvent(NumEvent ev, long seq, boolean endOfBatch) {
      if ((seq % combinerNum) == id) {
        sum += ev.num;
      }
      if (endOfBatch) {
        int added = Reducer.state.addAndGet(sum);
        LOG.info("combine-{}: {}. {}", id, sum, added);
        sum = 0;
      }
    }
  }

  // consumer phase2
  private static final class Reducer implements EventHandler<NumEvent> {
    private static final AtomicInteger state = new AtomicInteger(0);
    private int sum;

    @Override
    public void onEvent(NumEvent ev, long seq, boolean endOfBatch) {
      if (endOfBatch) {
        int st = state.get();
        LOG.info("reducer: {} {}", seq, st);
        synchronized (this) {
          sum = Math.max(st, sum);
        }
      }
    }
  }
}
