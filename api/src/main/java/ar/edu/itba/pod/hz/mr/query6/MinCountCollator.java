package ar.edu.itba.pod.hz.mr.query6;

import ar.edu.itba.pod.hz.model.ProvinceTuple;
import com.hazelcast.mapreduce.Collator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by giulianoscaglioni on 28/10/18.
 */
public class MinCountCollator implements Collator<Map.Entry<ProvinceTuple, Integer>, Map<ProvinceTuple, Integer>> {
    private Integer n;

    public MinCountCollator(Integer n) {
        this.n = n;
    }

    @Override
    public Map<ProvinceTuple, Integer> collate(Iterable<Map.Entry<ProvinceTuple, Integer>> iterable) {
        Map<ProvinceTuple, Integer> output = new HashMap<>();
        for(Map.Entry<ProvinceTuple, Integer> entry : iterable) {
            if(entry.getValue() >= n) {
                output.put(entry.getKey(), entry.getValue());
            }
        }
        return output;
    }
}
