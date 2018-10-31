package ar.edu.itba.pod.hz.mr.query6;

import ar.edu.itba.pod.hz.model.ProvinceTuple;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

/**
 * Created by giulianoscaglioni on 31/10/18.
 */
public class MinCountFilterMapper implements Mapper<ProvinceTuple, Integer, ProvinceTuple, Integer> {
    Integer minValue;

    public MinCountFilterMapper(Integer minValue) {
        this.minValue = minValue;
    }

    @Override
    public void map(ProvinceTuple provinceTuple, Integer integer, Context<ProvinceTuple, Integer> context) {
        if(integer >= minValue) {
            context.emit(provinceTuple, integer);
        }
    }
}
