package ar.edu.itba.pod.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class OrderByCollator<K extends Comparable<K>,V extends Comparable<V>>
    implements Collator<Map.Entry<K,V>, List<Entry<K,V>>> {

    private final Comparator<Entry<K,V>> comparator;

    public OrderByCollator(boolean ascending, boolean byKey) {
        if(byKey){
            comparator = (e1,e2) -> (ascending ? 1 : -1) * e1.getKey().compareTo(e2.getKey());
        } else {
            comparator = (e1,e2) -> (ascending ? 1 : -1) * e1.getValue().compareTo(e2.getValue());
        }
    }

    @Override
    public List<Entry<K,V>> collate(Iterable<Map.Entry<K, V>> iterable) {
        final List<Entry<K,V>> sorted = new LinkedList<>();
        iterable.forEach(sorted::add);
        sorted.sort(comparator);
        return sorted;
    }

}
