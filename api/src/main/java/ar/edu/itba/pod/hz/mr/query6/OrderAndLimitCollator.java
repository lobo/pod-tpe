package ar.edu.itba.pod.hz.mr.query6;

import ar.edu.itba.pod.hz.model.ProvinceTuple;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by giulianoscaglioni on 31/10/18.
 */
public class OrderAndLimitCollator implements Collator<Map.Entry<ProvinceTuple, Integer>, List<Map.Entry<ProvinceTuple, Integer>>> {
    private int min;

    private final Comparator<Map.Entry<ProvinceTuple, Integer>> comparator;
    Comparator<Map.Entry<ProvinceTuple, Integer>> comparatorKey;
    Comparator<Map.Entry<ProvinceTuple, Integer>> comparatorValue;


    public OrderAndLimitCollator(int min,boolean firstKey, boolean ascending_key, boolean ascending_value) {

        comparatorKey = (e1,e2) -> (ascending_key ? 1 : -1) * e1.getKey().compareTo(e2.getKey());
        comparatorValue = (e1,e2) -> (ascending_value ? 1 : -1) * e1.getValue().compareTo(e2.getValue());

        if(firstKey){
            comparator = (e1,e2) -> (e1.getKey().equals(e2.getKey()) ? comparatorValue.compare(e1,e2): comparatorKey.compare(e1,e2));
        } else {
            comparator = (e1,e2) -> (e1.getValue().equals(e2.getValue()) ? comparatorKey.compare(e1,e2): comparatorValue.compare(e1,e2));
        }

        this.min = min;
    }

    @Override
    public List<Map.Entry<ProvinceTuple, Integer>> collate(Iterable<Map.Entry<ProvinceTuple, Integer>> iterable) {
        List<Map.Entry<ProvinceTuple, Integer>> ret = new ArrayList<>();

        for(Map.Entry<ProvinceTuple, Integer> entry : iterable) {
            if(entry.getValue() >= min) {
                ret.add(entry);
            }
        }

        return ret.stream().sorted(comparator).collect(Collectors.toList());
    }
}
