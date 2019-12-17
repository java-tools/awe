package com.almis.awe.model.util.data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dfuentes
 */
public class MapUtil {

  // Private constructor
  private MapUtil() {
  }

  /**
   * Sorts the given hashmap by value
   *
   * @param <K>
   * @param <V>
   * @param map<K,V>
   * @return map<K,V>
   */
  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
    return sortWithComparator(map, Map.Entry.comparingByValue());
  }

  /**
   * Sorts the given hashmap by value in the reverse order
   *
   * @param <K>
   * @param <V>
   * @param map<K , V>
   * @return map<K, V>
   */
  public static <K, V extends Comparable<? super V>> Map<K, V> reverseSortByValue(Map<K, V> map) {
    return sortWithComparator(map, Collections.reverseOrder(Map.Entry.comparingByValue()));
  }

  /**
   * Sort a map with a comparator
   * @param map Map
   * @param comparator Comparator
   * @param <K> Key
   * @param <V> Value
   * @return Sorted linked map
   */
  private static <K, V extends Comparable<? super V>> Map<K, V> sortWithComparator(Map<K, V> map, Comparator<? super Map.Entry<K, V>> comparator) {
    List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
    list.sort(comparator);
    return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
  }
}
