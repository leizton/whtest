package com.wh.test.util;

import com.google.common.collect.ImmutableMap;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.consumer.ZookeeperConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.Decoder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 2018/5/4
 */
@SuppressWarnings("WeakerAccess")
public class KafkaUtil {

  public static final Decoder<String> STRING_DECODER = (Decoder<String>) String::new;

  public static <K, V> void consume(String clusterZk, String topic, String consumerGroup, int parallelNum,
                                    Decoder<K> keyDecoder, Decoder<V> valueDecoder,
                                    Consumer<MessageAndMetadata<K, V>> doConsume) {
    ConsumerConnector connector = ConsumerConnectorFactory.create(clusterZk, topic, consumerGroup);
    ExecutorService executor = Executors.newFixedThreadPool(parallelNum, new NamedThreadFactory("kafka-" + topic + "-" + consumerGroup));
    consume(connector, topic, parallelNum, keyDecoder, valueDecoder, doConsume, executor);
  }

  public static <K, V> void consume(ConsumerConnector connector, String topic, int parallelNum,
                                    Decoder<K> keyDecoder, Decoder<V> valueDecoder,
                                    Consumer<MessageAndMetadata<K, V>> doConsume, ExecutorService executor) {
    Map<String, List<KafkaStream<K, V>>> topicToStreams = connector.createMessageStreams(
        ImmutableMap.of(topic, parallelNum), keyDecoder, valueDecoder);
    List<KafkaStream<K, V>> kafkaStreams = topicToStreams.get(topic);
    kafkaStreams.forEach(kafkaStream -> executor.submit(() -> kafkaStream.forEach(doConsume)));
  }

  private static final class ConsumerConnectorFactory {

    private static ConsumerConnector create(String clusterZk, String topic, String consumerGroup) {
      Map<String, String> conf = MapUtil.<String, String>newMap()
          .put("zookeeper.connect", clusterZk)
          .put("topic", topic)
          .put("group.id", consumerGroup)
          .build();
      return new ZookeeperConsumerConnector(new ConsumerConfig(PropertiesUtil.fromMap(conf)));
    }
  }
}
