package com.wh.test.byted;

import com.google.common.base.Strings;
import com.wh.test.util.JsonUtil;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 2018/4/25
 */
public class TsdcTopology implements Watcher {

  public static void main(String[] args) throws Exception {
    TsdcTopology topo = new TsdcTopology();
    topo.test();
  }

  private void test() throws Exception {
    TreeMap<String, TreeMap<Integer, List<String>>> map = new TreeMap<>();

    List<String> profiles = zk.getChildren("/", this).stream().filter(e -> !Strings.isNullOrEmpty(e) && e.contains("tsdc")).collect(Collectors.toList());
    profiles.clear();
    profiles.add("tsdc_test");

    for (String profile : profiles) {
      map.put(profile, getTopology(profile));
    }

    System.out.println(map);
  }

  // <shardId, endpoints>
  private TreeMap<Integer, List<String>> getTopology(String profile) throws Exception {
    String path1 = "/" + profile + "/shards";
    List<Integer> shardIds = zk.getChildren(path1, this).stream().map(Integer::parseInt).collect(Collectors.toList());

    TreeMap<Integer, List<String>> endpointsOfShard = new TreeMap<>();
    for (Integer shardId : shardIds) {
      String path2 = path1 + "/" + shardId;
      List<String> endpoints = zk.getChildren(path2, this).stream().map(n2 -> getEndpoint(path2 + "/" + n2)).sorted().collect(Collectors.toList());
      endpointsOfShard.put(shardId, endpoints);
    }
    return endpointsOfShard;
  }

  @SuppressWarnings("unchecked")
  private String getEndpoint(String path) {
    try {
      byte[] desc = zk.getData(path, this, null);
      if (desc == null || desc.length == 0) {
        return "";
      }
      Map<String, String> data = JsonUtil.MAPPER.readValue(desc, Map.class);
      return data.get("uri");
    } catch (Exception e) {
      return "";
    }
  }

  private final ZooKeeper zk;

  private TsdcTopology() throws Exception {
    String zkQuorum = "10.6.132.15:2185,10.6.131.237:2185,10.6.131.217:2185,10.6.129.108:2185,10.6.132.225:2185";
    zk = new ZooKeeper(zkQuorum, 60000, this);
  }

  @Override
  public void process(WatchedEvent watchedEvent) {
  }
}
