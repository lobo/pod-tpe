package ar.edu.itba.pod.hz.mr.query6;

import ar.edu.itba.pod.hz.model.AirportData;
import ar.edu.itba.pod.hz.model.MovementData;
import ar.edu.itba.pod.hz.model.ProvinceTuple;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

/**
 * Created by giulianoscaglioni on 31/10/18.
 */
public class MovementToProvinceTupleMapper implements Mapper<Integer, MovementData, ProvinceTuple, Integer>, HazelcastInstanceAware {
    private IMap<String, AirportData> airports;

    private transient HazelcastInstance instance;
    private String mapName;

    public MovementToProvinceTupleMapper(String mapName) {
        this.mapName = mapName;
    }

    @Override
    public void map(Integer integer, MovementData movementData, Context<ProvinceTuple, Integer> context) {
        IMap<String, AirportData> airports = instance.getMap(mapName);

        String oaciOrigin = movementData.getDestOACI();
        String oaciDest = movementData.getOriginOACI();

        AirportData originAirport = airports.get(oaciOrigin);
        AirportData destAirport = airports.get(oaciDest);

        if(originAirport != null && destAirport != null) {
            if(!originAirport.getProvince().equals(destAirport.getProvince())) {
                context.emit(new ProvinceTuple(originAirport.getProvince(), destAirport.getProvince()), 1);
            }
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.instance = hazelcastInstance;
    }
}
