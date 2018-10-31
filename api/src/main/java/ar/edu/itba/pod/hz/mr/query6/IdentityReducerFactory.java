package ar.edu.itba.pod.hz.mr.query6;

import ar.edu.itba.pod.hz.model.ProvinceTuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

/**
 * Created by giulianoscaglioni on 31/10/18.
 */
public class IdentityReducerFactory implements ReducerFactory<ProvinceTuple, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(ProvinceTuple provinceTuple) {
        return new Reducer<Integer, Integer>() {
            Integer value;

            @Override
            public void reduce(Integer integer) {
                value = integer;
            }

            @Override
            public Integer finalizeReduce() {
                return value;
            }
        };
    }
}
