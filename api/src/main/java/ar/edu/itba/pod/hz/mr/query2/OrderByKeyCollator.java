package ar.edu.itba.pod.hz.mr.query2;


import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class OrderByKeyCollator<K extends Comparable<K>,V>
    implements Collator<Entry<K,V>, List<Entry<K,V>>> {

    private final Comparator<Entry<K,V>> comparator;

    public OrderByKeyCollator(boolean ascending) {
        comparator = (e1,e2) -> (ascending ? 1 : -1) * e1.getKey().compareTo(e2.getKey());
    }

    @Override
    public List<Entry<K,V>> collate(Iterable<Entry<K, V>> iterable) {
        final List<Entry<K,V>> sorted = new LinkedList<>();
        iterable.forEach(sorted::add);
        sorted.sort(comparator);
        return sorted;
    }

}
