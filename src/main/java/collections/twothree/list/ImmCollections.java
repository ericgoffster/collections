package collections.twothree.list;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

@SuppressWarnings("varargs")
public final class ImmCollections {
    private ImmCollections() {
    }

    @SafeVarargs
    public static <E> ImmList<E> asList(E... elements) {
        return TreeList23.of(Arrays.asList(elements));
    }
    public static <E> ImmList<E> asList(final Iterable<E> elements) {
        return TreeList23.of(elements);
    }

    public static <E> ImmSet<E> emptySet() {
        return HashSet23.empty();
    }
    @SafeVarargs
    public static <E> ImmSet<E> asSet(E... elements) {
        return HashSet23.of(Arrays.asList(elements));
    }
    public static <E> ImmSet<E> asSet(final Iterable<E> elements) {
        return HashSet23.of(elements);
    }

    public static <E extends Comparable<E>> ImmSortedSet<E> emptySortedSet() {
        return TreeSet23.empty();
    }
    @SafeVarargs
    public static <E extends Comparable<E>> ImmSortedSet<E> asSortedSet(E... elements) {
        return TreeSet23.of(Arrays.asList(elements));
    }
    public static <E extends Comparable<E>> ImmSortedSet<E> asSortedSet(final Iterable<E> elements) {
        return TreeSet23.of(elements);
    }

    public static <E> ImmSortedSet<E> emptySortedSet(Comparator<? super E> comparator) {
        return TreeSet23.empty(comparator);
    }
    public static <E> ImmSortedSet<E> asSortedSet(Comparator<? super E> comparator, final Iterable<E> elements) {
        return TreeSet23.of(comparator, elements);
    }

    public static <K, V> ImmMap<K, V> emptyMap() {
        return HashMap23.empty();
    }
    public static <K, V> ImmMap<K, V> asMap(K key, V value) {
        return HashMap23.singleton(key, value);
    }
    public static <K, V> ImmMap<K, V> asMap(K key1, V value1, K key2, V value2) {
        return asMap(key1, value1).put(key2, value2);
    }
    public static <K, V> ImmMap<K, V> asMap(K key1, V value1, K key2, V value2, K key3, V value3) {
        return asMap(key1, value1, key2, value2).put(key3, value3);
    }
    public static <K, V> ImmMap<K, V> asMap(final Iterable<Entry<K,V>> elements) {
        return HashMap23.of(elements);
    }
    public static <K, V> ImmMap<K, V> asMap(final Map<K,V> map) {
        return HashMap23.of(map);
    }

    public static <K extends Comparable<K>, V> ImmSortedMap<K, V> emptySortedMap() {
        return TreeMap23.empty();
    }
    public static <K extends Comparable<K>, V> ImmSortedMap<K, V> asSortedMap(K key, V value) {
        return TreeMap23.singleton(key, value);
    }
    public static <K extends Comparable<K>, V> ImmSortedMap<K, V> asSortedMap(K key1, V value1, K key2, V value2) {
        return asSortedMap(key1, value1).put(key2, value2);
    }
    public static <K extends Comparable<K>, V> ImmSortedMap<K, V> asSortedMap(K key1, V value1, K key2, V value2, K key3, V value3) {
        return asSortedMap(key1, value1, key2, value2).put(key3, value3);
    }
    public static <K extends Comparable<K>, V> ImmSortedMap<K, V> asSortedMap(final Iterable<Entry<K,V>> elements) {
        return TreeMap23.of(elements);
    }
    public static <K, V> ImmMap<K, V> asSortedMap(final SortedMap<K,V> map) {
        return TreeMap23.ofSorted(map);
    }

    public static <K, V> ImmSortedMap<K, V> emptySortedMap(Comparator<? super K> comparator) {
        return TreeMap23.empty(comparator);
    }
    public static <K, V> ImmSortedMap<K, V> asSortedMap(Comparator<? super K> comparator, final Iterable<Entry<K,V>> elements) {
        return TreeMap23.of(comparator, elements);
    }
}
