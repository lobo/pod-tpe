package ar.edu.itba.pod.hz.mr.query2;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class MovementCounterDividerReducerFactory implements ReducerFactory<String, Integer, Integer> {
    private final Integer DIVISOR = 1;

    @Override
    public Reducer<Integer, Integer> newReducer(String s) {
        return new Reducer<Integer, Integer>() {
            private Integer count = 0;
            @Override
            public void reduce(Integer integer) {
                count++;
            }

            @Override
            public Integer finalizeReduce() {
                return count/DIVISOR;
            }
        };
    }
}
