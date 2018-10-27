package ar.edu.itba.pod.hz.mr.query5;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class DepartmentPer100CounterReducerFactory implements ReducerFactory<String, Integer, Integer> {
	private static final long serialVersionUID = 7760070699178320492L;

	@Override
	public Reducer<Integer, Integer> newReducer(final String department) {
		return new Reducer<Integer, Integer>() {
			private int count;

			@Override
			public void beginReduce() {
				count = 0;
			}

			@Override
			public void reduce(final Integer value) {
				count++;
			}

			@Override
			public Integer finalizeReduce() {
				return count / 100;
			}
		};
	}
}
