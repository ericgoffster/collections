package collections.twothree.list;

import java.util.Arrays;
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

    @SafeVarargs
    public static <E> ImmSet<E> asSet(E... elements) {
        return HashSet23.of(Arrays.asList(elements));
    }
    public static <E> ImmSet<E> asSet(final Iterable<E> elements) {
        return HashSet23.of(elements);
    }

    @SafeVarargs
    public static <E extends Comparable<E>> ImmSortedSet<E> asSortedSet(E... elements) {
        return TreeSet23.of(Arrays.asList(elements));
    }
    public static <E extends Comparable<E>> ImmSortedSet<E> asSortedSet(final Iterable<E> elements) {
        return TreeSet23.of(elements);
    }

    public static <K, V> ImmMap<K, V> emptyMap() {
        return HashMap23.empty();
    }
    public static <K, V> ImmMap<K, V> asMap(K key, V value) {
        return HashMap23.singleton(key, value);
    }
    public static <K, V> ImmMap<K, V> asMap(final Iterable<Entry<K,V>> elements) {
        return HashMap23.of(elements);
    }
    public static <K, V> ImmMap<K, V> asMap(final Map<K,V> map) {
        return HashMap23.of(map);
    }

    public static <K extends Comparable<K>, V> ImmMap<K, V> emptySortedMap() {
        return TreeMap23.empty();
    }
    public static <K extends Comparable<K>, V> ImmMap<K, V> asSortedMap(K key, V value) {
        return TreeMap23.singleton(key, value);
    }
    public static <K extends Comparable<K>, V> ImmMap<K, V> asSortedMap(final Iterable<Entry<K,V>> elements) {
        return TreeMap23.of(elements);
    }
    public static <K extends Comparable<K>, V> ImmMap<K, V> asSortedMap(final SortedMap<K,V> map) {
        return TreeMap23.ofSorted(map);
    }
}
