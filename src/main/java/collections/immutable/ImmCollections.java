package collections.immutable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import org.granitesoft.requirement.Requirements;

import java.util.SortedMap;

@SuppressWarnings("varargs")
public final class ImmCollections {
    private ImmCollections() {
    }

    @SafeVarargs
    public static <E> ImmList<E> asList(E... elements) {
        return TreeList23.of(Arrays.asList(elements));
    }
    public static <E> ImmList<E> asList(final Iterable<? extends E> elements) {
        return TreeList23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    public static <E> ImmSet<E> emptySet() {
        return HashSet23.empty();
    }
    @SafeVarargs
    public static <E> ImmSet<E> asSet(E... elements) {
        return HashSet23.of(Arrays.asList(elements));
    }
    public static <E> ImmSet<E> asSet(final Iterable<? extends E> elements) {
        return HashSet23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    public static <E extends Comparable<E>> ImmSortedSet<E> emptySortedSet() {
        return TreeSet23.empty();
    }
    @SafeVarargs
    public static <E extends Comparable<E>> ImmSortedSet<E> asSortedSet(E... elements) {
        return TreeSet23.of(Arrays.asList(elements));
    }
    public static <E extends Comparable<E>> ImmSortedSet<E> asSortedSet(final Iterable<? extends  E> elements) {
        return TreeSet23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    public static <E> ImmSortedSet<E> emptySortedSet(final Comparator<? super E> comparator) {
        return TreeSet23.empty(Requirements.require(comparator, Requirements.notNull(), () -> "comparator"));
    }
    public static <E> ImmSortedSet<E> asSortedSet(final Comparator<? super E> comparator, final Iterable<? extends E> elements) {
        return TreeSet23.of(Requirements.require(comparator, Requirements.notNull(), () -> "comparator"),
                Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    public static <K, V> ImmMap<K, V> emptyMap() {
        return HashMap23.empty();
    }
    public static <K, V> ImmMap<K, V> asMap(final K key, final V value) {
        return HashMap23.singleton(key, value);
    }
    public static <K, V> ImmMap<K, V> asMap(
            final K key1, final V value1,
            final K key2, final V value2) {
        return asMap(key1, value1).put(key2, value2);
    }
    public static <K, V> ImmMap<K, V> asMap(
            final K key1, final V value1,
            final K key2, final V value2,
            final K key3, final V value3) {
        return asMap(key1, value1, key2, value2).put(key3, value3);
    }
    public static <K, V> ImmMap<K, V> asMap(final Iterable<? extends Entry<K,V>> elements) {
        return HashMap23.of(elements);
    }
    public static <K, V> ImmMap<K, V> asMap(final Map<K,V> map) {
        return HashMap23.of(map);
    }

    public static <K extends Comparable<K>, V> ImmSortedMap<K, V> emptySortedMap() {
        return TreeMap23.empty();
    }
    public static <K extends Comparable<K>, V> ImmSortedMap<K, V> asSortedMap(final K key, final V value) {
        return TreeMap23.singleton(key, value);
    }
    public static <K extends Comparable<K>, V> ImmSortedMap<K, V> asSortedMap(
            final K key1, final V value1,
            final K key2, final V value2) {
        return asSortedMap(key1, value1).put(key2, value2);
    }
    public static <K extends Comparable<K>, V> ImmSortedMap<K, V> asSortedMap(
            final K key1, final V value1,
            final K key2, final V value2,
            final K key3, final V value3) {
        return asSortedMap(key1, value1, key2, value2).put(key3, value3);
    }
    public static <K extends Comparable<K>, V> ImmSortedMap<K, V> asSortedMap(final Iterable<? extends Entry<K,V>> elements) {
        return TreeMap23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }
    public static <K, V> ImmMap<K, V> asSortedMap(final SortedMap<K,V> map) {
        return TreeMap23.ofSorted(Requirements.require(map, Requirements.notNull(), () -> "map"));
    }

    public static <K, V> ImmSortedMap<K, V> emptySortedMap(final Comparator<? super K> comparator) {
        return TreeMap23.empty(Requirements.require(comparator, Requirements.notNull(), () -> "comparator"));
    }
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final Comparator<? super K> comparator, final Iterable<? extends Entry<K,V>> elements) {
        return TreeMap23.of(Requirements.require(comparator, Requirements.notNull(), () -> "comparator"),
                Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final Comparator<? super K> comparator, final Map<K,V> map) {
        return TreeMap23.of(Requirements.require(comparator, Requirements.notNull(), () -> "comparator"),
                Requirements.require(map, Requirements.notNull(), () -> "map"));
    }
}
