package ar.edu.itba.pod.hz.mr.query5;

import ar.edu.itba.pod.hz.model.BiIntegerTuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class MovementCounter2Reducer implements ReducerFactory<String, Integer, BiIntegerTuple> {
	@Override
	public Reducer<Integer, BiIntegerTuple> newReducer(final String category) {
		return new Reducer<Integer, BiIntegerTuple>() {
			private int count;
			private int total;

			@Override
			public void beginReduce() {
				count = 0;total=0;
			}

			@Override
			public void reduce(final Integer value) {
				count+=value;
				total++;
			}

			@Override
			public BiIntegerTuple finalizeReduce() {
				return new BiIntegerTuple((long)count,(long)total);
			}
		};
	}
}
