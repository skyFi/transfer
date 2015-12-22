package com.darcytech.transfer.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by darcy on 2015/12/15.
 */
public class MapSortUtil {

    public static <K, V> Map<K, V> orderByValue(Map<K, V> originalMap, final Comparator<Map.Entry<K, V>> comparator) {
        LinkedHashMap<K, V> sortedMap = new LinkedHashMap<>();

        List<Map.Entry<K, V>> entries = new ArrayList<>(originalMap.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> lhs, Map.Entry<K, V> rhs) {
                return comparator.compare(lhs, rhs);
            }
        });

        for (Map.Entry<K, V> e : entries) {
            sortedMap.put(e.getKey(), e.getValue());
        }

        return sortedMap;
    }

}
