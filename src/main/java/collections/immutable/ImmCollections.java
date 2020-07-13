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
     * Returns an immutable list backed by a list of elements
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
     * Returns an immutable list backed by iterable of elements.
     * @param <E> The element type
     * @param elements The iterable of elements
     * @return an immutable list backed by iterable of elements
     */
    public static <E> ImmList<E> asList(final Iterable<? extends E> elements) {
        return TreeList23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Returns the empty set.
     * @param <E> The element type.
     * @return The empty set
     */
    public static <E> ImmSet<E> emptySet() {
        return HashSet23.empty();
    }

    /**
     * Returns an immutable set backed by a list of elements
     * represented by varargs.
     * @param <E> The element type.
     * @param elements The array of elements
     * @return An immutable set backed by a list of elements
     */
    @SafeVarargs
    public static <E> ImmSet<E> asSet(E... elements) {
        return HashSet23.of(Arrays.asList(elements));
    }
    
    /**
     * Returns an immutable set backed by a iterable of elements
     * represented by varargs.
     * @param <E> The element type.
     * @param elements The iterable of elements
     * @return An immutable set backed by a list of elements
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
     * Returns the empty sorted set.
     * @param <E> The element type.
     * @return The empty sorted set
     */
    public static <E> ImmSortedSet<E> emptySortedSet() {
        return TreeSet23.empty();
    }
    /**
     * Returns an immutable sorted set backed by a list of elements
     * represented by varargs.
     * @param <E> The element type.
     * @param elements The array of elements
     * @return An immutable sorted set backed by a list of elements
     */
    @SafeVarargs
    public static <E> ImmSortedSet<E> asSortedSet(E... elements) {
        return TreeSet23.of(Arrays.asList(elements));
    }

    /**
     * Returns an immutable sorted set backed by a iterable of elements
     * represented by varargs.
     * @param <E> The element type.
     * @param elements The iterable of elements
     * @return An immutable sorted set backed by a list of elements
     */
    public static <E> ImmSortedSet<E> asSortedSet(final Iterable<? extends E> elements) {
        return TreeSet23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
    }

    /**
     * Returns the empty sorted set with a custom comparator.
     * @param comparator The comparator
     * @param <E> The element type.
     * @return The empty sorted set
     */
    public static <E> ImmSortedSet<E> emptySortedSet(final Comparator<? super E> comparator) {
        return TreeSet23.empty(Requirements.require(comparator, Requirements.notNull(), () -> "comparator"));
    }

    /**
     * Returns an immutable sorted set with a custom comparator backed by a iterable of elements
     * represented by varargs.
     * @param <E> The element type.
     * @param comparator The comparator
     * @param elements The iterable of elements
     * @return An immutable sorted set backed by a list of elements
     */
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
        if (elements instanceof ImmMap) {
            @SuppressWarnings("unchecked")
            final ImmMap<K, V> elements2 = (ImmMap<K, V>)elements;
            return elements2;
        }
        return HashMap23.of(elements);
    }
    public static <K, V> ImmMap<K, V> asMap(final Map<K,V> map) {
        if (map instanceof SortedMap) {
            return TreeMap23.of(map);
        }
        return HashMap23.of(map);
    }

    public static <K, V> ImmSortedMap<K, V> emptySortedMap() {
        return TreeMap23.empty();
    }
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final K key, final V value) {
        return TreeMap23.singleton(key, value);
    }
    public static <K, V> ImmSortedMap<K, V> asSortedMap(
            final K key1, final V value1,
            final K key2, final V value2) {
        return asSortedMap(key1, value1).put(key2, value2);
    }
    public static <K, V> ImmSortedMap<K, V> asSortedMap(
            final K key1, final V value1,
            final K key2, final V value2,
            final K key3, final V value3) {
        return asSortedMap(key1, value1, key2, value2).put(key3, value3);
    }
    public static <K, V> ImmSortedMap<K, V> asSortedMap(final Iterable<? extends Entry<K,V>> elements) {
        return TreeMap23.of(Requirements.require(elements, Requirements.notNull(), () -> "elements"));
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
