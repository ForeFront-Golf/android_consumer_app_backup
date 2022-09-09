package com.rhinoactive.generalutilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Hunter Andrin on 2017-10-06.
 */

public class CollectionUtils {

    /**
     *
     * @param map The map that will have its values sorted.
     * @param <K> The type of the key for the map.
     * @param <V> The type of the values for the map. This type must implement the {@link Comparable interface}
     * @return A sorted list of values.
     */
    public static <K,V extends Comparable<? super V>> List<V> getListSortedByValues(Map<K,V> map) {
        Comparator<V> comparator = new Comparator<V>() {
            @Override
            public int compare(V e1, V e2) {
                return e1.compareTo(e2);
            }
        };
        return sortValues(map, comparator);
    }

    /**
     *
     * @param map The map that will have its values sorted.
     * @param comparator The comparator that will sort the values of the map.
     * @param <K> The type of the key for the map.
     * @param <V> The type of the values for the map. This type must implement the {@link Comparable interface}
     * @return A sorted list of values.
     */
    public static <K,V extends Comparable<? super V>> List<V> getListSortedByValues(Map<K,V> map, Comparator<V> comparator) {
        return sortValues(map, comparator);
    }

    /**
     *
     * @param set The set that will be sorted.
     * @param comparator The comparator that will sort the values of the set.
     * @param <V> The type of values that is contained in the set. This type must implement the {@link Comparable interface}
     * @return A sorted list of values.
     */
    public static <V extends Comparable<? super V>> List<V> getListSortedByValues(Set<V> set, Comparator<V> comparator) {
        List<V> sortedList = new ArrayList<>();
        sortedList.addAll(set);
        Collections.sort(sortedList, comparator);
        return sortedList;
    }

    private static <K,V extends Comparable<? super V>> List<V> sortValues(Map<K,V> map, Comparator<V> comparator) {
        List<V> sortedList = new ArrayList<>();
        sortedList.addAll(map.values());
        Collections.sort(sortedList, comparator);
        return sortedList;
    }
}
