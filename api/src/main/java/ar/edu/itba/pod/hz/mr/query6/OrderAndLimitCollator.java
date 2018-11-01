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

    public OrderAndLimitCollator(int min) {
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

        return ret.stream().sorted(Comparator.comparing(Map.Entry::getValue)).collect(Collectors.toList());
    }
}
