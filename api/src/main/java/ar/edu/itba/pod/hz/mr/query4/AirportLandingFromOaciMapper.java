package ar.edu.itba.pod.hz.mr.query4;

import ar.edu.itba.pod.hz.model.MovementData;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class AirportLandingFromOaciMapper implements Mapper<Integer, MovementData, String, Integer> {
    private String fromOaci ="";

    public AirportLandingFromOaciMapper(String fromOaci){
        this.fromOaci = fromOaci;
    }

    @Override
    public void map(Integer keyInput, MovementData valueinput, Context<String, Integer> context) {

        String originOACI = valueinput.getOriginOACI();
        String destOACI = valueinput.getDestOACI();
        if(fromOaci.equals(originOACI)){
            context.emit(destOACI, 1);
        }

    }
}
