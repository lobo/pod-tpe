package ar.edu.itba.pod.hz.mr.query6;

import ar.edu.itba.pod.hz.model.ProvinceTuple;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

/**
 * Created by giulianoscaglioni on 31/10/18.
 */
public class ProvinceTupleCombiner implements CombinerFactory<ProvinceTuple, Integer, Integer> {
    @Override
    public Combiner<Integer, Integer> newCombiner(ProvinceTuple provinceTuple) {
        return new Combiner<Integer, Integer>() {
            private int count = 0;

            @Override
            public void combine(Integer integer) {
                count += integer;
            }

            @Override
            public Integer finalizeChunk() {
                return count;
            }
        };
    }
}
