package ar.edu.itba.pod.hz.mr.query3;

import ar.edu.itba.pod.hz.model.AirportTuple;
import ar.edu.itba.pod.hz.model.BiIntegerTuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class AirportTupleIntegerTupleReducerFactory implements ReducerFactory<AirportTuple, BiIntegerTuple, BiIntegerTuple> {
    @Override
    public Reducer<BiIntegerTuple, BiIntegerTuple> newReducer(final AirportTuple at) {
        return new Reducer<BiIntegerTuple, BiIntegerTuple>() {

            private BiIntegerTuple count;

            @Override
            public void beginReduce() {
                count = new BiIntegerTuple();
            }

            @Override
            public void reduce(final BiIntegerTuple tuple) {
                count.addTuple(tuple);
            }

            @Override
            public BiIntegerTuple finalizeReduce() {
                return count;
            }
        };
    }
}



