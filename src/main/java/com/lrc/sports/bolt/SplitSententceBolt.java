package com.lrc.sports.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by home1 on 2016/11/7.
 */
public class SplitSententceBolt  extends BaseRichBolt{
    private OutputCollector outputCollector;
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector=outputCollector;
    }

    public void execute(Tuple tuple) {
        String sentence=tuple.getString(0);
        String[]words=sentence.split(" ");
        for(String word:words){
            List<Tuple> list=new ArrayList<Tuple>();
            list.add(tuple);
            outputCollector.emit(list,new Values(word.toLowerCase()));
        }
        outputCollector.ack(tuple);

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word"));

    }
}
