package com.lrc.sports;


import backtype.storm.Config;
import backtype.storm.StormSubmitter;

import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import com.lrc.sports.bolt.SplitSententceBolt;
import com.lrc.sports.util.Constants;
import kafka.Kafka;
import kafka.api.OffsetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.bolt.KafkaBolt;
import storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import storm.kafka.bolt.selector.DefaultTopicSelector;

import java.util.Properties;

public class StormKafkaTopology {
    private static final Logger logger = LoggerFactory.getLogger(StormKafkaTopology.class);
    private static final String TOPIC_NAME = "my-replicated-test";
    private static final String ZK_ROOTS = "/storm/topology/root";
    private static final String ZK_ID = "wordCount";


    public static void main(String[] args) {
        ZkHosts zkHosts = new ZkHosts(Constants.ZK_CLUSTER);//zookeeper地址
        SpoutConfig spoutConfig = new SpoutConfig(zkHosts, TOPIC_NAME, ZK_ROOTS, ZK_ID);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());


        //用KafkaBoltkafka topic中写入消息


        Config conf = new Config();
        Properties props = new Properties();
        props.put("metadata.broker.list", Constants.KAFKA_BROKER);
        props.put("producer.type", "async");
        props.put("request.required.acks", "0"); // 0 ,-1 ,1
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        conf.put(KafkaBolt.KAFKA_BROKER_PROPERTIES, props);
        KafkaBolt kafkaBolt = new KafkaBolt().withTopicSelector(new DefaultTopicSelector("test"))
                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper("test", "word"));


        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafkaTopicSpout", new KafkaSpout(spoutConfig));
        builder.setBolt("splitSentenceBolt", new SplitSententceBolt(), 4).shuffleGrouping("kafkaTopicSpout");
        builder.setBolt("kafkaWriteBolt", kafkaBolt, 4).shuffleGrouping("splitSentenceBolt");
        try {
            StormSubmitter.submitTopology("kafkaStormTestTopology", conf, builder.createTopology());
        } catch (Exception e) {
            logger.error("submit error", e);
        }


    }
}
