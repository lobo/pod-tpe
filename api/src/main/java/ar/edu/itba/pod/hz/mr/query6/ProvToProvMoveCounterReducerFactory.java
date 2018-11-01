package ar.edu.itba.pod.hz.mr.query6;

import ar.edu.itba.pod.hz.model.ProvinceTuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class ProvToProvMoveCounterReducerFactory implements ReducerFactory<ProvinceTuple, Integer, Integer> {
    @Override
    public Reducer<Integer, Integer> newReducer(ProvinceTuple provinceTuple) {
        return new Reducer<Integer, Integer>() {
            private Integer count = 0;

            @Override
            public void reduce(Integer integer) {
                count+=integer;
            }

            @Override
            public Integer finalizeReduce() {
                return count;
            }
        };
    }
}
