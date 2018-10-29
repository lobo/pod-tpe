package ar.edu.itba.pod.hz.mr.query6;

import ar.edu.itba.pod.hz.model.ProvinceTuple;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

/**
 * Created by giulianoscaglioni on 28/10/18.
 */
public class ProvToProvMoveCounterMapper implements Mapper<Integer, ProvinceTuple, ProvinceTuple, Integer> {
    @Override
    public void map(Integer integer, ProvinceTuple provinceTuple, Context<ProvinceTuple, Integer> context) {
        context.emit(provinceTuple, 1);
    }
}
