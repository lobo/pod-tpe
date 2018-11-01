package ar.edu.itba.pod.hz.mr.query3;

import ar.edu.itba.pod.hz.model.AirportTuple;
import ar.edu.itba.pod.hz.model.BiIntegerTuple;
import ar.edu.itba.pod.hz.model.MovementData;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class OriginDestinationMapper implements Mapper<Integer, MovementData, AirportTuple, BiIntegerTuple>  {

    @Override
    public void map(Integer keyinput, MovementData valueinput, Context<AirportTuple, BiIntegerTuple> context) {
        String originOACI = valueinput.getOriginOACI();
        String destOACI = valueinput.getDestOACI();

        /** Add movement in origin airport */
        context.emit(new AirportTuple(originOACI,destOACI), new BiIntegerTuple(1l,0l));
        context.emit(new AirportTuple(destOACI,originOACI), new BiIntegerTuple(0l,1l));

    }
}
