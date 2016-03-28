package com.twitter.heron.spi.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.twitter.heron.api.Config;
import com.twitter.heron.api.HeronSubmitter;
import com.twitter.heron.api.HeronTopology;
import com.twitter.heron.api.bolt.BaseBasicBolt;
import com.twitter.heron.api.bolt.BasicOutputCollector;
import com.twitter.heron.api.generated.TopologyAPI;
import com.twitter.heron.api.spout.BaseRichSpout;
import com.twitter.heron.api.spout.SpoutOutputCollector;
import com.twitter.heron.api.topology.BoltDeclarer;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyBuilder;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Fields;
import com.twitter.heron.api.tuple.Tuple;

import com.twitter.heron.spi.common.Constants;
import com.twitter.heron.spi.common.PackingPlan;

public class TopologyTests {
  /**
   * Create Topology proto object using HeronSubmitter API.
   *
   * @param heronConfig desired config params.
   * @param spouts spoutName -> parallelism
   * @param bolts boltName -> parallelism
   * @param connections connect default stream from value to key.
   * @return topology proto.
   */
  public static TopologyAPI.Topology createTopologyWithConnection(String topologyName,
                                                                  Config heronConfig,
                                                                  Map<String, Integer> spouts,
                                                                  Map<String, Integer> bolts,
                                                                  Map<String, String> connections) {
    TopologyBuilder builder = new TopologyBuilder();
    BaseRichSpout baseSpout = new BaseRichSpout() {
      public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("field1"));
      }

      public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
      }

      public void nextTuple() {
      }
    };
    BaseBasicBolt basicBolt = new BaseBasicBolt() {
      public void execute(Tuple input, BasicOutputCollector collector) {
      }

      public void declareOutputFields(OutputFieldsDeclarer declarer) {
      }
    };

    for (String spout : spouts.keySet()) {
      builder.setSpout(spout, baseSpout, spouts.get(spout));
    }

    for (String bolt : bolts.keySet()) {
      BoltDeclarer boltDeclarer = builder.setBolt(bolt, basicBolt, bolts.get(bolt));
      if (connections.containsKey(bolt)) {
        boltDeclarer.shuffleGrouping(connections.get(bolt));
      }
    }

    HeronTopology heronTopology = builder.createTopology();
    try {
      HeronSubmitter.submitTopology(topologyName, heronConfig, heronTopology);
    } catch (Exception e) {
    }

    return heronTopology.
        setName(topologyName).
        setConfig(heronConfig).
        setState(TopologyAPI.TopologyState.RUNNING).
        getTopology();
  }

  public static TopologyAPI.Topology createTopology(String topologyName,
                                                    Config heronConfig,
                                                    Map<String, Integer> spouts,
                                                    Map<String, Integer> bolts) {
    return createTopologyWithConnection(
        topologyName, heronConfig, spouts, bolts, new HashMap<String, String>());
  }
}
