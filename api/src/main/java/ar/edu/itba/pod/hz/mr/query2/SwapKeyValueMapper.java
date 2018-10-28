package ar.edu.itba.pod.hz.mr.query2;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class SwapKeyValueMapper implements Mapper<String, Integer, Integer, String> {
    @Override
    public void map(String keyIn, Integer valueIn, Context<Integer, String> context) {
        context.emit(valueIn, keyIn);
    }
}
