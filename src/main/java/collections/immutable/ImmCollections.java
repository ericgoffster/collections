package collections.immutable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;

import org.granitesoft.requirement.Requirements;

@SuppressWarnings("varargs")
public final class ImmCollections {
    private ImmCollections() {
    }

    /**
     * Creates an immutable list from a list of elements
     * represented by varargs.
     * @param <E> The element type
     * @param elements The array of elements
     * @return An immutable list backed by a list of elements
     */
    @SafeVarargs
    public static <E> ImmList<E> asList(E... elements) {
        return TreeList23.of(Arrays.asList(elements));
    }
    
    /**
     * Creates an immutable list from an iterable of elements.
     * @param <E> The element type
     * @param elements The iterable of elements
     * @return an immutable list backed by iterable of elements
     */
    public static <E> ImmList<E> asList(final Iterable<? extends E> elements) {
        return TreeList23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Creates an empty set.
     * @param <E> The element type
     * @return The empty set
     */
    public static <E> ImmSet<E> emptySet() {
        return HashSet23.empty();
    }

    /**
     * Creates an immutable set from a list of elements
     * represented by varargs.
     * @param <E> The element type
     * @param elements The array of elements
     * @return An immutable set backed by a list of elements
     */
    @SafeVarargs
    public static <E> ImmSet<E> asSet(E... elements) {
        return HashSet23.of(Arrays.asList(elements));
    }
    
    /**
     * Creates an immutable set from a iterable of elements
     * represented by varargs.
     * @param <E> The element type
     * @param elements The iterable of elements
     * @return An immutable set from a list of elements
     */
    public static <E> ImmSet<E> asSet(final Iterable<? extends E> elements) {
        if (elements instanceof ImmSet) {
            @SuppressWarnings("unchecked")
            final ImmSet<E> elements2 = (ImmSet<E>)elements;
            return elements2;
        }
        if (elements instanceof SortedSet) {
            return TreeSet23.of(elements);
        }
        return HashSet23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Creates an empty sorted set.
     * @param <E> The element type
     * @return The empty sorted set
     */
    public static <E> ImmSortedSet<E> emptySortedSet() {
        return TreeSet23.empty();
    }
    /**
     * Creates an immutable sorted set from a list of elements
     * represented by varargs.
     * @param <E> The element type
     * @param elements The array of elements
     * @return An immutable sorted set from a list of elements
     */
    @SafeVarargs
    public static <E> ImmSortedSet<E> asSortedSet(E... elements) {
        return TreeSet23.of(Arrays.asList(elements));
    }

    /**
     * Creates an immutable sorted set from a iterable of elements
     * represented by varargs.
     * @param <E> The element type
     * @param elements The iterable of elements
     * @return An immutable sorted set from a list of elements
     */
    public static <E> ImmSortedSet<E> asSortedSet(final Iterable<? extends E> elements) {
        return TreeSet23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Creates an empty sorted set with a custom comparator.
     * @param comparator The comparator
     * @param <E> The element type
     * @return The empty sorted set
     */
    public static <E> ImmSortedSet<E> emptySortedSet(final Comparator<? super E> comparator) {
        return TreeSet23.empty(Requirements.require(comparator, Requirements.notNull(), () -> "comparator"));
    }

    /**
     * Creates an immutable sorted set with a custom comparator from a iterable of elements
     * represented by varargs.
     * @param <E> The element type
     * @param comparator The comparator
     * @param elements The iterable of elements
     * @return An immutable sorted set from a list of elements
     */
    public static <E> ImmSortedSet<E> asSortedSet(final Comparator<? super E> comparator, final Iterable<? extends E> elements) {
        return TreeSet23.of(Requirements.require(comparator, Requirements.notNull(), () -> "comparator"),
                Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Creates an empty map.
     * @param <K> The key type
     * @param <V> The value type
     * @return The empty map
     */
    public static <K, V> ImmMap<K, V> emptyMap() {
        return HashMap23.empty();
    }

    /**
     * Creates an immutable map from a single key value pair.
     * @param <K> The key type
     * @param <V> The value type
     * @param key The key
     * @param value The value
     * @return An immutable map from a single key value pair
     */
    public static <K, V> ImmMap<K, V> asMap(final K key, final V value) {
        return HashMap23.singleton(key, value);
    }

    /**
     * Creates an immutable map from two key value pairs.
     * @param <K> The key type
     * @param <V> The value type
     * @param key1 Key #1
     * @param value1 Value #1
     * @param key2 Key #2
     * @param value2 Value #2
     * @return An immutable map from two key value pairs
     */
    public static <K, V> ImmMap<K, V> asMap(
            final K key1, final V value1,
            final K key2, final V value2) {
        return asMap(key1, value1).put(key2, value2);
    }

    /**
     * Creates an immutable map from three key value pairs.
     * @param <K> The key type
     * @param <V> The value type
     * @param key1 Key #1
     * @param value1 Value #1
     * @param key2 Key #2
     * @param value2 Value #2
     * @param key3 Key #3
     * @param value3 Value #3
     * @return An immutable map from three key value pairs
     */
    public static <K, V> ImmMap<K, V> asMap(
            final K key1, final V value1,
            final K key2, final V value2,
            final K key3, final V value3) {
        return asMap(key1, value1, key2, value2).put(key3, value3);
    }

    /**
     * Creates an immutable map from an iterable of entries.
     * @param <K> The key type
     * @param <V> The value type
     * @param elements Iterable of elements.
     * @return An immutable map from an iterable of entries
     */
    public static <K, V> ImmMap<K, V> asMap(final Iterable<? extends Entry<K,V>> elements) {
        if (elements instanceof ImmMap) {
            @SuppressWarnings("unchecked")
            final ImmMap<K, V> elements2 = (ImmMap<K, V>)elements;
            return elements2;
        }
        return HashMap23.of(elements);
    }

    /**
     * Creates an immutable map from another map
     * @param <K> The key type
     * @param <V> The value type
     * @param map The other map
     * @return An immutable map from another map
     */
    public static <K, V> ImmMap<K, V> asMap(final Map<K,V> map) {
        if (map instanceof SortedMap) {
            return TreeMap23.of(map);
        }
        return HashMap23.of(map);
    }

    /**
     * Creates an empty sorted map.
     * @param <K> The key type
     * @param <V> The value type
     * @return The empty sordted map
     */
    public static <K, V> ImmSortedMap<K, V> emptySortedMap() {
        return TreeMap23.empty();
    }

    /**
     * Creates an immutable sorted map from a single key value pair.
     * @param <K> The key type
     * @param <V> The value type
     * @param key The key
     * @param value The value
     * @return An immutable sorted map from a single key value pair
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final K key, final V value) {
        return TreeMap23.singleton(key, value);
    }

    /**
     * Creates an immutable sorted map from two key value pairs.
     * @param <K> The key type
     * @param <V> The value type
     * @param key1 Key #1
     * @param value1 Value #1
     * @param key2 Key #1
     * @param value2 Value #1
     * @return An immutable sorted map from two key value pairs
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(
            final K key1, final V value1,
            final K key2, final V value2) {
        return asSortedMap(key1, value1).put(key2, value2);
    }

    /**
     * Creates an immutable sorted map from three key value pairs.
     * @param <K> The key type
     * @param <V> The value type
     * @param key1 Key #1
     * @param value1 Value #1
     * @param key2 Key #2
     * @param value2 Value #2
     * @param key3 Key #3
     * @param value3 Value #3
     * @return An immutable sorted map from three key value pairs
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(
            final K key1, final V value1,
            final K key2, final V value2,
            final K key3, final V value3) {
        return asSortedMap(key1, value1, key2, value2).put(key3, value3);
    }
  
    /**
     * Creates an immutable sorted map from an iterable of entries.
     * @param <K> The key type
     * @param <V> The value type
     * @param elements Iterable of elements.
     * @return An immutable sorted map from an iterable of entries
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final Iterable<? extends Entry<K,V>> elements) {
        return TreeMap23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Creates an empty sorted map with a custom comparator.
     * @param <K> The key type
     * @param <V> The value type
     * @param keyComparator the key comparator
     * @return The empty sorted map
     */
    public static <K, V> ImmSortedMap<K, V> emptySortedMap(final Comparator<? super K> keyComparator) {
        return TreeMap23.empty(Requirements.require(keyComparator, Requirements.notNull(), () -> "keyComparator"));
    }

    /**
     * Creates an immutable sorted map from an iterable of entries and with a custom comparator.
     * @param <K> The key type
     * @param <V> The value type
     * @param keyComparator the key comparator
     * @param elements Iterable of elements.
     * @return An immutable sorted map from an iterable of entries and with a custom comparator
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final Comparator<? super K> keyComparator, final Iterable<? extends Entry<K,V>> elements) {
        return TreeMap23.of(Requirements.require(keyComparator, Requirements.notNull(), () -> "keyComparator"),
                Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Creates an immutable map from another map and a custom comparator
     * @param <K> The key type
     * @param <V> The value type
     * @param keyComparator Key comparator
     * @param map The other map
     * @return An immutable map from another map
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final Comparator<? super K> keyComparator, final Map<K,V> map) {
        return TreeMap23.of(Requirements.require(keyComparator, Requirements.notNull(), () -> "keyComparator"),
                Requirements.require(map, Requirements.notNull(), () -> "map"));
    }
}
