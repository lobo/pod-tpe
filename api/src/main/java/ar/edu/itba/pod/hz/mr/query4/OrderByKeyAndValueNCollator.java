package ar.edu.itba.pod.hz.mr.query4;

import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrderByKeyAndValueNCollator<K extends Comparable<K>,V extends Comparable<V>>
        implements Collator<Map.Entry<K,V>, List<Map.Entry<K,V>>> {

    private int n=0;
    private final Comparator<Map.Entry<K,V>> comparator;
    Comparator<Map.Entry<K,V>> comparatorKey;
    Comparator<Map.Entry<K,V>> comparatorValue;


    public OrderByKeyAndValueNCollator(int n,boolean firstKey, boolean ascending_key, boolean ascending_value){

        comparatorKey = (e1,e2) -> (ascending_key ? 1 : -1) * e1.getKey().compareTo(e2.getKey());
        comparatorValue = (e1,e2) -> (ascending_value ? 1 : -1) * e1.getValue().compareTo(e2.getValue());

        if(firstKey){
            comparator = (e1,e2) -> (e1.getKey().equals(e2.getKey()) ? comparatorValue.compare(e1,e2): comparatorKey.compare(e1,e2));
        } else {
            comparator = (e1,e2) -> (e1.getValue().equals(e2.getValue()) ? comparatorKey.compare(e1,e2): comparatorValue.compare(e1,e2));
        }

    }

    @Override
    public List<Map.Entry<K, V>> collate(Iterable<Map.Entry<K, V>> iterable) {
        final List<Map.Entry<K,V>> sorted = new LinkedList<>();
        final List<Map.Entry<K,V>> ret = new LinkedList<>();
        int i=0;
        iterable.forEach(sorted::add);
        sorted.sort(comparator);

        for(Map.Entry<K,V> elem: sorted){
            ret.add(elem);
            i++;
            if(i>this.n){
                break;
            }
        }
        return ret;
    }
}
