package ar.edu.itba.pod.hz.mr.query5;

import ar.edu.itba.pod.hz.model.MovementData;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class MovementInternationalMapper implements Mapper<Integer, MovementData, String, Integer> {
    public void map(final Integer keyinput, final MovementData valueinput, final Context<String, Integer> context) {
        String originOACI = valueinput.getOriginOACI();
        String destOACI = valueinput.getDestOACI();
        String type =valueinput.getType();
        String clasification =valueinput.getClasification();

        if(clasification.equals("Internacional")){
            if(type.equals("Despegue")){
                context.emit(originOACI,1);
            }else{
                context.emit(destOACI,1);
            }

        }else{
            if(type.equals("Despegue")){
                context.emit(originOACI,-1);
            }else{
                context.emit(destOACI,-1);
            }
        }

    }
}
