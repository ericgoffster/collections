package collections.immutable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;

import org.granitesoft.requirement.Requirements;

/**
 * Entry point to the immutable collections library.
 * As always, everything is *IMMUTABLE*.  All operations
 * leave the original object unchanged.
 * @author egoff
 *
 */
@SuppressWarnings("varargs")
public final class ImmCollections {
    private ImmCollections() {
    }

    /**
     * Creates an {@link ImmList immutable list} from a varargs array of elements.
     * <p>Example:
     * <pre>{@code
     *    ImmList<Integer> l = ImmCollections.asList(1,2,3);
     * }</pre>
     * @param <E> The element type
     * @param elements The array of elements
     * @return An {@link ImmList immutable list} from a varargs array of elements
     */
    @SafeVarargs
    public static <E> ImmList<E> asList(E... elements) {
        return TreeList23.of(Arrays.asList(elements));
    }
    
    /**
     * Creates an {@link ImmList immutable list} from an {@link Iterable iterable} of elements.
     * <p>Example:
     * <pre>{@code
     *    ImmList<Integer> l = ImmCollections.asList(Arrays.asList(1,2,3));
     * }</pre>
     * @param <E> The element type
     * @param elements The iterable of elements
     * @return an immutable list from an {@link Iterable iterable} of elements
     */
    public static <E> ImmList<E> asList(final Iterable<? extends E> elements) {
        return TreeList23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Creates an empty {@link ImmSet immutable set}.
     * <p>Example:
     * <pre>{@code
     *    ImmSet<Integer> l = ImmCollections.emptySet();
     * }</pre>
     * @param <E> The element type
     * @return The empty {@link ImmSet immutable set}
     */
    public static <E> ImmSet<E> emptySet() {
        return HashSet23.empty();
    }

    /**
     * Creates an {@link ImmSet immutable set} from a varargs array of elements.
     * <p>Example:
     * <pre>{@code
     *    ImmSet<Integer> l = ImmCollections.asSet(1,2,3);
     * }</pre>
     * @param <E> The element type
     * @param elements The array of elements
     * @return An {@link ImmSet immutable set} from a varargs array of elements
     */
    @SafeVarargs
    public static <E> ImmSet<E> asSet(E... elements) {
        return HashSet23.of(Arrays.asList(elements));
    }
    
    /**
     * Creates an {@link ImmSet immutable set} from an {@link Iterable iterable} of elements.
     * <p>Example:
     * <pre>{@code
     *    ImmSet<Integer> l = ImmCollections.asSet(Arrays.asList(1,2,3));
     * }</pre>
     * @param <E> The element type
     * @param elements The iterable of elements
     * @return An {@link ImmSet immutable set} from an {@link Iterable iterable} of elements
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
     * Creates an empty {@link ImmSortedSet immutable sorted set}.
     * <p>Example:
     * <pre>{@code
     *    ImmSortedSet<Integer> l = ImmCollections.emptySortedSet();
     * }</pre>
     * @param <E> The element type
     * @return The empty {@link ImmSortedSet immutable sorted set}
     */
    public static <E> ImmSortedSet<E> emptySortedSet() {
        return TreeSet23.empty();
    }

    /**
     * Creates an {@link ImmSortedSet immutable sorted set} from a varargs array of elements.
     * <p>Example:
     * <pre>{@code
     *    ImmSortedSet<Integer> l = ImmCollections.asSortedSet(1,2,3);
     * }</pre>
     * @param <E> The element type
     * @param elements The array of elements
     * @return An {@link ImmSortedSet immutable sorted set} from a varargs array of elements
     */
    @SafeVarargs
    public static <E> ImmSortedSet<E> asSortedSet(E... elements) {
        return TreeSet23.of(Arrays.asList(elements));
    }

    /**
     * Creates an {@link ImmSortedSet immutable sorted set} from an {@link Iterable iterable} of elements.
     * <p>Example:
     * <pre>{@code
     *    ImmSortedSet<Integer> l = ImmCollections.asSortedSet(Arrays.asList(1,2,3));
     * }</pre>
     * @param <E> The element type
     * @param elements The iterable of elements
     * @return An {@link ImmSortedSet immutable sorted set} from an {@link Iterable iterable} of elements
     */
    public static <E> ImmSortedSet<E> asSortedSet(final Iterable<? extends E> elements) {
        return TreeSet23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Creates an empty {@link ImmSortedSet immutable sorted set} with a custom {@link Comparator comparator}.
     * <p>Example:
     * <pre>{@code
     *    ImmSortedSet<Integer> l = ImmCollections.emptySortedSet(Integer::compare);
     * }</pre>
     * @param comparator The comparator
     * @param <E> The element type
     * @return The empty {@link ImmSortedSet immutable sorted set}
     */
    public static <E> ImmSortedSet<E> emptySortedSet(final Comparator<? super E> comparator) {
        return TreeSet23.empty(Requirements.require(comparator, Requirements.notNull(), () -> "comparator"));
    }

    /**
     * Creates an {@link ImmSortedSet immutable sorted set} with a custom {@link Comparator comparator} from an {@link Iterable iterable} of elements.
     * <p>Example:
     * <pre>{@code
     *    ImmSortedSet<Integer> l = ImmCollections.asSortedSet(Integer::compare, Arrays.asList(1,2,3));
     * }</pre>
     * @param <E> The element type
     * @param comparator The comparator
     * @param elements The iterable of elements
     * @return An {@link ImmSortedSet immutable sorted set} from an {@link Iterable iterable} of elements
     */
    public static <E> ImmSortedSet<E> asSortedSet(final Comparator<? super E> comparator, final Iterable<? extends E> elements) {
        return TreeSet23.of(Requirements.require(comparator, Requirements.notNull(), () -> "comparator"),
                Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Creates an empty {@link ImmMap immutable map}.
     * <p>Example:
     * <pre>{@code
     *    ImmMap<Integer, String> l = ImmCollections.emptyMap();
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @return The empty {@link ImmMap immutable map}
     */
    public static <K, V> ImmMap<K, V> emptyMap() {
        return HashMap23.empty();
    }

    /**
     * Creates an {@link ImmMap immutable map} from a single key value pair.
     * <p>Example:
     * <pre>{@code
     *    ImmMap<Integer, String> l = ImmCollections.asMap(1, "a");
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param key The key
     * @param value The value
     * @return An {@link ImmMap immutable map} from a single key value pair
     */
    public static <K, V> ImmMap<K, V> asMap(final K key, final V value) {
        return HashMap23.singleton(key, value);
    }

    /**
     * Creates an {@link ImmMap immutable map} from two key value pairs.
     * <p>Example:
     * <pre>{@code
     *    ImmMap<Integer, String> l = ImmCollections.asMap(1, "a",     2, "b");
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param key1 Key #1
     * @param value1 Value #1
     * @param key2 Key #2
     * @param value2 Value #2
     * @return An {@link ImmMap immutable map} from two key value pairs
     */
    public static <K, V> ImmMap<K, V> asMap(
            final K key1, final V value1,
            final K key2, final V value2) {
        return asMap(key1, value1).put(key2, value2);
    }

    /**
     * Creates an {@link ImmMap immutable map} from three key value pairs.
     * <p>Example:
     * <pre>{@code
     *    ImmMap<Integer, String> l = ImmCollections.asMap(1, "a",     2, "b",      3, "c");
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param key1 Key #1
     * @param value1 Value #1
     * @param key2 Key #2
     * @param value2 Value #2
     * @param key3 Key #3
     * @param value3 Value #3
     * @return An {@link ImmMap immutable map} from three key value pairs
     */
    public static <K, V> ImmMap<K, V> asMap(
            final K key1, final V value1,
            final K key2, final V value2,
            final K key3, final V value3) {
        return asMap(key1, value1, key2, value2).put(key3, value3);
    }

    /**
     * Creates an {@link ImmMap immutable map} from an {@link Iterable iterable} of entries.
     * <p>Example:
     * <pre>{@code
     *    Map<Integer, String> m = new HashMap<>();
     *    m.put(1, "a");
     *    m.put(2, "b");
     *    m.put(3, "c");
     *    ImmMap<Integer, String> l = ImmCollections.asMap(m.entrySet());
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param elements Iterable of elements.
     * @return An {@link ImmMap immutable map} from an {@link Iterable iterable} of entries
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
     * Creates an {@link ImmMap immutable map} from another {@link Map map}
     * <p>Example:
     * <pre>{@code
     *    Map<Integer, String> m = new HashMap<>();
     *    m.put(1, "a");
     *    m.put(2, "b");
     *    m.put(3, "c");
     *    ImmMap<Integer, String> l = ImmCollections.asMap(m);
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param map The other map
     * @return An {@link ImmMap immutable map} from another {@link Map map}
     */
    public static <K, V> ImmMap<K, V> asMap(final Map<K,V> map) {
        if (map instanceof SortedMap) {
            return TreeMap23.of(map);
        }
        return HashMap23.of(map);
    }

    /**
     * Creates an empty {@link ImmSortedMap immutable sorted map}.
     * <p>Example:
     * <pre>{@code
     *    ImmSortedMap<Integer, String> l = ImmCollections.emptySortedMap();
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @return The empty {@link ImmSortedMap immutable sorted map}
     */
    public static <K, V> ImmSortedMap<K, V> emptySortedMap() {
        return TreeMap23.empty();
    }

    /**
     * Creates an {@link ImmSortedMap immutable sorted map} from a single key value pair.
     * <p>Example:
     * <pre>{@code
     *    ImmSortedMap<Integer, String> l = ImmCollections.asSortedMap(1, "a");
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param key The key
     * @param value The value
     * @return An {@link ImmSortedMap immutable sorted map} from a single key value pair
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final K key, final V value) {
        return TreeMap23.singleton(key, value);
    }

    /**
     * Creates an {@link ImmSortedMap immutable sorted map} from two key value pairs.
     * <p>Example:
     * <pre>{@code
     *    ImmSortedMap<Integer, String> l = ImmCollections.asSortedMap(1, "a",     2, "b");
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param key1 Key #1
     * @param value1 Value #1
     * @param key2 Key #1
     * @param value2 Value #1
     * @return An {@link ImmSortedMap immutable sorted map} from two key value pairs
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(
            final K key1, final V value1,
            final K key2, final V value2) {
        return asSortedMap(key1, value1).put(key2, value2);
    }

    /**
     * Creates an {@link ImmSortedMap immutable sorted map} from three key value pairs.
     * <p>Example:
     * <pre>{@code
     *    ImmSortedMap<Integer, String> l = ImmCollections.asSortedMap(1, "a",     2, "b",     3, "c");
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param key1 Key #1
     * @param value1 Value #1
     * @param key2 Key #2
     * @param value2 Value #2
     * @param key3 Key #3
     * @param value3 Value #3
     * @return An {@link ImmSortedMap immutable sorted map} from three key value pairs
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(
            final K key1, final V value1,
            final K key2, final V value2,
            final K key3, final V value3) {
        return asSortedMap(key1, value1, key2, value2).put(key3, value3);
    }
  
    /**
     * Creates an {@link ImmSortedMap immutable sorted map} from an {@link Iterable iterable} of entries.
     * <p>Example:
     * <pre>{@code
     *    Map<Integer, String> m = new HashMap<>();
     *    m.put(1, "a");
     *    m.put(2, "b");
     *    m.put(3, "c");
     *    ImmSortedMap<Integer, String> l = ImmCollections.asSortedMap(m.entrySet());
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param elements Iterable of elements.
     * @return An {@link ImmSortedMap immutable sorted map} from an {@link Iterable iterable} of entries
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final Iterable<? extends Entry<K,V>> elements) {
        return TreeMap23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Creates an empty {@link ImmSortedMap immutable sorted map} with a custom {@link Comparator key comparator}.
     * <p>Example:
     * <pre>{@code
     *    ImmSortedMap<Integer, String> l = ImmCollections.emptySortedMap(Integer::compare);
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param keyComparator the key comparator
     * @return The empty {@link ImmSortedMap immutable sorted map}
     */
    public static <K, V> ImmSortedMap<K, V> emptySortedMap(final Comparator<? super K> keyComparator) {
        return TreeMap23.empty(Requirements.require(keyComparator, Requirements.notNull(), () -> "keyComparator"));
    }

    /**
     * Creates an {@link ImmSortedMap immutable sorted map} from an {@link Iterable iterable} of entries and with a custom {@link Comparator key comparator}.
     * <p>Example:
     * <pre>{@code
     *    Map<Integer, String> m = new HashMap<>();
     *    m.put(1, "a");
     *    m.put(2, "b");
     *    m.put(3, "c");
     *    ImmSortedMap<Integer, String> l = ImmCollections.asSortedMap(Integer::compare, m.entrySet());
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param keyComparator the key comparator
     * @param elements Iterable of elements.
     * @return An {@link ImmSortedMap immutable sorted map} from an {@link Iterable iterable} of entries and with a custom {@link Comparator key comparator}
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final Comparator<? super K> keyComparator, final Iterable<? extends Entry<K,V>> elements) {
        return TreeMap23.of(Requirements.require(keyComparator, Requirements.notNull(), () -> "keyComparator"),
                Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Creates an {@link ImmSortedMap immutable sorted map} from another {@link Map map} and a custom {@link Comparator key comparator}
     * <p>Example:
     * <pre>{@code
     *    Map<Integer, String> m = new HashMap<>();
     *    m.put(1, "a");
     *    m.put(2, "b");
     *    m.put(3, "c");
     *    ImmSortedMap<Integer, String> l = ImmCollections.asSortedMap(Integer::compare, m);
     * }</pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param keyComparator Key comparator
     * @param map The other map
     * @return An {@link ImmSortedMap immutable sorted map} from another map
     */
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final Comparator<? super K> keyComparator, final Map<K,V> map) {
        return TreeMap23.of(Requirements.require(keyComparator, Requirements.notNull(), () -> "keyComparator"),
                Requirements.require(map, Requirements.notNull(), () -> "map"));
    }
}
