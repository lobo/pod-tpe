package ar.edu.itba.pod.hz.mr.quer2b;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class MovementCounterOverThousandsMapper implements Mapper<String, Integer, Integer, String> {
    @Override
    public void map(String s, Integer i, Context<Integer, String> context) {
        context.emit((i/1000)*1000,s);
    }
}
