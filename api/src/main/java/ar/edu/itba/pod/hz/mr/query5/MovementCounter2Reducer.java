package ar.edu.itba.pod.hz.mr.query5;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class MovementCounter2Reducer implements ReducerFactory<String, Integer, Integer> {
	@Override
	public Reducer<Integer, Integer> newReducer(final String category) {
		return new Reducer<Integer, Integer>() {
			private int count;

			@Override
			public void beginReduce() {
				count = 0;
			}

			@Override
			public void reduce(final Integer value) {
				count+=value;
			}

			@Override
			public Integer finalizeReduce() {
				return count;
			}
		};
	}
}
