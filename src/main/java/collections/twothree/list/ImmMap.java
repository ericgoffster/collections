package collections.twothree.list;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents an Immutable mapping of keys to values backed by a {@link ImmList} of entries.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.  The object is not modified in any way.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public interface ImmMap<K,V> extends Iterable<Entry<K,V>> {
    /**
     * Returns the size of the map.
     * <p>This operation is O(1).
     * @return the size of the map
     */
    int size();

    /**
     * Returns a new map23 with <code>entry</code> added.
     * <p>This operation is O(log n), where n = |this|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param entry The entry to add.
     * @return a new map23 with <code>entry</code> added.
     */
    ImmMap<K, V> add(Entry<K ,V> entry);

    /**
     * Returns a new map23 with <code>entries</code> added.
     * <p>This operation is O(n log n), where n = |this|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param entries The entries to add.
     * @return a new map23 with <code>entries</code> added.
     */
    ImmMap<K, V> addAll(Iterable<? extends Entry<K ,V>> entries);

    /**
     * Returns a new map23 with the contents of <code>map</code> added.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param map The map to copy.
     * @return a new map23 with the map added.
     */
    ImmMap<K, V> addAll(Map<K ,V> map);

    /**
     * Returns a new map23 with <code>key</code> associated with <code>value</code>.
     * <p>This operation is O(log n), where n = |this|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param key The key to add
     * @param value The value to add
     * @return a new map23 with <code>key</code> associated with <code>value</code>
     */
    ImmMap<K, V> put(final K key, final V value);
    
    /**
     * Returns true if this map contains <code>key</code>.
     * <p>This operation is O(log n), where n = |this|.
     * @param key The key
     * @return true if this map contains <code>key</code>
     */
    boolean containsKey(final K key);

    /**
     * Returns a new map23 with <code>key</code> removed.
     * <p>This operation is O(log n), where n = |this|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param key The key to remove
     * @return  a new map23 with <code>key</code> removed
     */
    ImmMap<K, V> removeKey(final K key);
    
    /**
     * Returns a new map23 with only the keys contained in <code>keys</code>.
     * <p>This operation is O(n * log (n)), where n = |this| + |other|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param keys The key list.
     * @return a new map23 with only the keys contained in <code>keys</code>
     */
    ImmMap<K, V> retainAllKeys(final Iterable<? extends K> keys);

    /**
     * Returns a new map23 with only the keys than are *not* in <code>keys</code>.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param keys The key list.
     * @return a new map23 with only the keys than are *not* in <code>keys</code>
     */
    ImmMap<K, V> removeAllKeysIn(final Iterable<? extends K> keys);
    
    /**
     * Returns the value associated with <code>key</code>, null if not found.
     * <p>This operation is O(log n), where n = |this|.
     * @param key The key to lookup
     * @return the value associated with <code>key</code>, null if not found.
     */
    V get(final K key);

    /**
     * Returns the value associated with <code>key</code>, using a default if not found.
     * <p>This operation is O(log n), where n = |this|.
     * @param key The key to lookup
     * @param defaultSupplier The supplier of the default
     * @return the value associated with the key, using a default if not found.
     */
    V getOrDefault(final K key, final Supplier<V> defaultSupplier);
    
    /**
     * Returns a classic read-only {@link Map} view of this Map23.
     * <p>This operation is O(1).
     * @return a classic read-only {@link Map} view of this Map23.
     */
    Map<K, V> asMap();
    
    /**
     * Returns all entries in the map as Set23.
     * <p>This operation is O(1).
     * @return all entries in the map as Set23.
     */
    ImmSet<Entry<K,V>> asSet23();
    
    /**
     * Returns all keys in this map.
     * <p>This operation is O(1).
     * @return all keys in this map
     */
    ImmSet<K> keys();

    /**
     * Returns all values in this map.
     * <p>This operation is O(1).
     * @return all values in this map
     */
    ImmCollection<V> values();
    
    /**
     * Return A map23 with keys that match <code>keyFilter</code>.
     * <p>This operation is O(n * log n), where n = |this|.
     * @param keyFilter The key filter
     * @return A map23 will all keys matching <code>keyFilter</code>.
     */
    public ImmMap<K, V> filterKeys(final Predicate<K> keyFilter);

    /**
     * Return A map23 with entries that match <code>filter</code>.
     * <p>This operation is O(n * log n), where n = |this|.
     * @param filter The entrr filter
     * @return  A map23 with entries that match <code>filter</code>
     */
    ImmMap<K, V> filter(final Predicate<Entry<K,V>> filter);
    
    /**
     * Streams the entries.
     * <p>This operation is O(1).
     * @return a stream of the entries.
     */
    Stream<Entry<K,V>> stream();
    
    /**
     * ForEach on the keys and values.
     * <p>This operation is O(n log n).
     * @param cons The consumer
     */
    void forEach(BiConsumer<K, V> cons);
}
