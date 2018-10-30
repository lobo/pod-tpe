package ar.edu.itba.pod.hz.mr.query2;

import ar.edu.itba.pod.hz.model.AirportTuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SameKeyGrouperReducerFactory implements ReducerFactory<Integer, String, List<AirportTuple>> {
    @Override
    public Reducer<String, List<AirportTuple>> newReducer(Integer integer) {
        return new Reducer<String, List<AirportTuple>>() {
            private List<String> airports = new ArrayList<>();
            private List<AirportTuple> tuples = new ArrayList<>();

            @Override
            public void reduce(String s) {
                airports.add(s);
            }

            @Override
            public List<AirportTuple> finalizeReduce() {
                // add tuples in the upper half of the matrix (airports^2)
                for(int i = 0; i < airports.size(); i++) {
                    for(int j = i + 1; j < airports.size(); j++) {
                        String oaci1 = airports.get(i);
                        String oaci2 = airports.get(j);
                        tuples.add(new AirportTuple(oaci1, oaci2));
                    }
                }
                tuples.sort(new Comparator<AirportTuple>() {
                    @Override
                    public int compare(AirportTuple o1, AirportTuple o2) {
                        return o1.compareTo(o2);
                    }
                });
                return tuples;
            }
        };
    }
}
