package collections.twothree.list;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents the Immutable Map23 interface.
 * Note *ALL OPERATIONS ARE IMMUTABLE*.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public interface Map23<K,V> extends Iterable<Entry<K,V>> {
    /**
     * Returns the size of the map.
     * @return the size of the map
     */
    int size();

    /**
     * Returns a new map23 with a copy of the item added.
     * Note *THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param entry The entry to add.
     * @return a new map23 with the entry added.
     */
    Map23<K, V> add(Entry<K ,V> entry);

    /**
     * Returns a new map23 with a copy of the entries added.
     * Note *THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param entries The entries to add.
     * @return a new map23 with the entries added.
     */
    Map23<K, V> addAll(Iterable<Entry<K ,V>> entries);

    /**
     * Returns a new map23 with a copy of the entries from the map added.
     * Note *THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param map The map to copy.
     * @return a new map23 with the map added.
     */
    Map23<K, V> addAll(Map<K ,V> map);

    /**
     * Returns a new map23 with the key associated with the value.
     * Note *THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param key The key to add
     * @param value The value to add
     * @return a new map23 with the key associated with the value
     */
    Map23<K, V> put(final K key, final V value);
    
    /**
     * Returns true if this map contains the key.
     * @param key The key
     * @return true if this map contains the key
     */
    boolean containsKey(final K key);

    /**
     * Returns a new map23 with the key removed.
     * Note *THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param key The key to remove
     * @return  a new map23 with the key removed
     */
    Map23<K, V> removeKey(final K key);
    
    /**
     * Returns a new map23 with only the keys contained in the given key list.
     * Note *THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param keys The key list.
     * @return a new map23 with only the keys contained in the given key list
     */
    Map23<K, V> retainAllKeys(final Iterable<K> keys);

    /**
     * Returns a new map23 with only the keys than are *not* in the given key list.
     * Note *THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param keys The key list.
     * @return a new map23 with only the keys than are *not* in the given key list
     */
    Map23<K, V> removeAllKeysIn(final Iterable<K> keys);
    
    /**
     * Returns the value associated with the key, null if not found.
     * @param key The key to lookup
     * @return the value associated with the key, null if not found.
     */
    V get(final K key);

    /**
     * Returns the value associated with the key, using a default if not found.
     * @param key The key to lookup
     * @param defaultSupplier The supplier of the default
     * @return the value associated with the key, using a default if not found.
     */
    V getOrDefault(final K key, final Supplier<V> defaultSupplier);
    
    /**
     * Returns a classic read-only {@link Map} view of this Map23.
     * @return a classic read-only {@link Map} view of this Map23.
     */
    Map<K, V> asMap();
    
    /**
     * Returns all entries in the map as Set23.
     * @return all entries in the map as Set23.
     */
    Set23<Entry<K,V>> asSet23();
    
    /**
     * Returns all keys in this map.
     * @return all keys in this map
     */
    Set23<K> keys();

    /**
     * Returns all values in this map.
     * @return all values in this map
     */
    Collection23<V> values();
    
    /**
     * Return A map23 with keys that match the filter.
     * @param filter The key filter
     * @return A map23 will all keys matching a filter.
     */
    public Map23<K, V> filterKeys(final Predicate<K> filter);

    /**
     * Return A map23 with entries that match the filter.
     * @param filter The entrr filter
     * @return  A map23 with entries that match the filter
     */
    Map23<K, V> filter(final Predicate<Entry<K,V>> filter);
    
    /**
     * Streams the entries.
     * @return a stream of the entries.
     */
    Stream<Entry<K,V>> stream();
    
    /**
     * ForEach on the keys and values.
     * @param cons The consumer
     */
    void forEach(BiConsumer<K, V> cons);
}
