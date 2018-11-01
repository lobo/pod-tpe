package ar.edu.itba.pod.hz.mr.query5;

import ar.edu.itba.pod.hz.model.BiIntegerTuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class MovementCounter2Reducer implements ReducerFactory<String, Integer, Integer> {
	@Override
	public Reducer<Integer, Integer> newReducer(final String category) {
		return new Reducer<Integer, Integer>() {
			private int count;
			private int total;

			@Override
			public void beginReduce() {
				count = 0;total=0;
			}

			@Override
			public void reduce(final Integer value) {
				if(value==1){
					count++;
				}
				total++;
			}

			@Override
			public Integer finalizeReduce() {
				return (int)Math.floor((double)count/total*100.0);
			}
		};
	}
}
