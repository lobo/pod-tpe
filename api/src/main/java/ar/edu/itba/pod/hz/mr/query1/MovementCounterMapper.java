package ar.edu.itba.pod.hz.mr.query1;

import ar.edu.itba.pod.hz.model.MovementData;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class MovementCounterMapper implements Mapper<Integer, MovementData, String, Integer> {
	@Override
	public void map(final Integer keyinput, final MovementData valueinput, final Context<String, Integer> context) {
		String originOACI = valueinput.getOriginOACI();
		String destOACI = valueinput.getDestOACI();
		String type = valueinput.getType();
		if(type.equals("Despegue")){
			/** Add movement in origin airport */
			context.emit(originOACI, 1);
		}else{
			/** Add movement in destination airport */
			context.emit(destOACI, 1);
		}

	}
}