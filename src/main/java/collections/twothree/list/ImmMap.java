package collections.twothree.list;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
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
     * <pre>
     * Example:
     *     assert ImmCollections.emptyMap().size() == 0;
     *     assert ImmCollections.asMap(1,2).put(3,4).size() == 2;
     * </pre>
     * @return the size of the map
     */
    int size();

    /**
     * Returns a new map23 with <code>entries</code> added.
     * <p>This operation is O(n log n), where n = |this|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * <pre>
     * Example:
     *     assert ImmCollections.asMap(1,2,  3,4).addAll(ImmCollections.asMap(3,5,  4,6)).equals(ImmCollections.asMap(1,2,  3,5,  4,6));
     * </pre>
     * @param entries The entries to add.
     * @return a new map23 with <code>entries</code> added.
     */
    ImmMap<K, V> addAll(Iterable<? extends Entry<K ,V>> entries);

    /**
     * Returns a new map23 with the contents of <code>map</code> added.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * <pre>
     * Example:
     *     HashMap&lt;Integer, Integer&gt; hm = new HashMap&lt;&gt;();
     *     hm.put(3, 5);
     *     hm.put(4, 6);
     *     assert ImmCollections.asMap(1,2,  3,4).addAll(hm).equals(ImmCollections.asMap(1,2,  3,5,  4,6));
     * </pre>
     * @param map The map to copy.
     * @return a new map23 with the map added.
     */
    ImmMap<K, V> addAll(Map<K ,V> map);

    /**
     * Returns a new map23 with <code>key</code> associated with <code>value</code>.
     * <p>This operation is O(log n), where n = |this|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * <pre>
     * Example:
     *     assert ImmCollections.emptyMap().put(1,2).put(3,4).equals(ImmCollections.asMap(1,2,  3,4));
     * </pre>
     * @param key The key to add
     * @param value The value to add
     * @return a new map23 with <code>key</code> associated with <code>value</code>
     */
    ImmMap<K, V> put(final K key, final V value);
    
    /**
     * Returns true if this map contains <code>key</code>.
     * <p>This operation is O(log n), where n = |this|.
     * <pre>
     * Example:
     *     assert ImmCollections.emptyMap().containsKey(2) == false;
     *     assert ImmCollections.asMap(1,2,  3,4).containsKey(2) == false;
     *     assert ImmCollections.asMap(1,2,  3,4).containsKey(1) == true;
     * </pre>
     * @param key The key
     * @return true if this map contains <code>key</code>
     */
    boolean containsKey(final K key);

    /**
     * Returns a new map23 with <code>key</code> removed.
     * <p>This operation is O(log n), where n = |this|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * <pre>
     * Example:
     *     assert ImmCollections.emptyMap().removeKey(2).equals(ImmCollections.emptyMap());
     *     assert ImmCollections.asMap(1,2,  3,4).removeKey(2).equals(ImmCollections.asMap(1,2,  3,4));
     *     assert ImmCollections.asMap(1,2,  3,4).removeKey(1).equals(ImmCollections.asMap(3,4));
     * </pre>
     * @param key The key to remove
     * @return  a new map23 with <code>key</code> removed
     */
    ImmMap<K, V> removeKey(final K key);
    
    /**
     * Returns a new map23 with only the keys contained in <code>keys</code>.
     * <p>This operation is O(n * log (n)), where n = |this| + |other|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * <pre>
     * Example:
     *     assert ImmCollections.emptyMap().retainAllKeys(ImmCollections.asSet(3)).equals(ImmCollections.emptyMap());
     *     assert ImmCollections.asMap(1,2,  3,4).retainAllKeys(ImmCollections.asSet(2)).equals(ImmCollections.emptyMap());
     *     assert ImmCollections.asMap(1,2,  3,4).retainAllKeys(ImmCollections.asSet(1)).equals(ImmCollections.asMap(1,2));
     * </pre>
     * @param keys The key list.
     * @return a new map23 with only the keys contained in <code>keys</code>
     */
    ImmMap<K, V> retainAllKeys(final Iterable<? extends K> keys);

    /**
     * Returns a new map23 with only the keys than are *not* in <code>keys</code>.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * <pre>
     * Example:
     *     assert ImmCollections.emptyMap().removeAllKeysIn(ImmCollections.asSet(3)).equals(ImmCollections.emptyMap());
     *     assert ImmCollections.asMap(1,2,  3,4).removeAllKeysIn(ImmCollections.asSet(2)).equals(ImmCollections.asMap(1,2,  3,4));
     *     assert ImmCollections.asMap(1,2,  3,4).removeAllKeysIn(ImmCollections.asSet(1)).equals(ImmCollections.asMap(3,4));
     * </pre>
     * @param keys The key list.
     * @return a new map23 with only the keys than are *not* in <code>keys</code>
     */
    ImmMap<K, V> removeAllKeysIn(final Iterable<? extends K> keys);
    
    /**
     * Returns the value associated with <code>key</code>, null if not found.
     * <p>This operation is O(log n), where n = |this|.
     * <pre>
     * Example:
     *     assert ImmCollections.emptyMap().get(3) == null;
     *     assert ImmCollections.asMap(1,2,  3,4).get(2) == null;
     *     assert ImmCollections.asMap(1,2,  3,4).get(1).equals(2);
     * </pre>
     * @param key The key to lookup
     * @return the value associated with <code>key</code>, null if not found.
     */
    V get(final K key);

    /**
     * Returns the value associated with <code>key</code>, using a default if not found.
     * <p>This operation is O(log n), where n = |this|.
     * <pre>
     * Example:
     *     assert ImmCollections.emptyMap().getOrDefault(3, () -> 1).equals(1);
     *     assert ImmCollections.asMap(1,2,  3,4).getOrDefault(2, () -> 1).equals(1);
     *     assert ImmCollections.asMap(1,2,  3,4).getOrDefault(1, () -> 1).equals(2);
     * </pre>
     * @param key The key to lookup
     * @param defaultSupplier The supplier of the default
     * @return the value associated with the key, using a default if not found.
     */
    V getOrDefault(final K key, final Supplier<V> defaultSupplier);
    
    /**
     * Returns a classic read-only {@link Map} view of this Map23.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     HashMap&lt;Integer, String&gt; hm = new HashMap&lt;&gt;();
     *     hm.put(1, 2);
     *     hm.put(3, 4);
     *     assert ImmCollections.asMap(1,2,  3,4).asMap().equals(hm);
     * </pre>
     * @return a classic read-only {@link Map} view of this Map23.
     */
    Map<K, V> asMap();
    
    /**
     * Returns all keys in this map.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     assert ImmCollections.emptyMap().keys().equals(ImmCollections.asSet());
     *     assert ImmCollections.asMap(1,2,  3,4).keys().equals(ImmCollections.asSet(1,3));
     * </pre>
     * @return all keys in this map
     */
    ImmSet<K> keys();

    /**
     * Returns all values in this map.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     assert ImmCollections.emptyMap().values().equals(ImmCollections.asList());
     *     assert ImmCollections.asMap(1,2,  3,4).values().equals(ImmCollections.asList(2, 4)) || ImmCollections.asMap(1,2,  3,4).values().equals(ImmCollections.asList(4, 2));
     * </pre>
     * @return all values in this map
     */
    ImmCollection<V> values();
    
    /**
     * Return A map23 with keys that match <code>keyFilter</code>.
     * <p>This operation is O(n * log n), where n = |this|.
     * <pre>
     * Example:
     *     assert ImmCollections.asMap(1,2,  3,4).filterKeys(k -> k > 0).equals(ImmCollections.asMap(1,2,  3,4));
     *     assert ImmCollections.asMap(1,2,  3,4).filterKeys(k -> k > 1).equals(ImmCollections.asMap(3,4));
     *     assert ImmCollections.asMap(1,2,  3,4).filterKeys(k -> k > 3).equals(ImmCollections.emptyMap());
     * </pre>
     * @param keyFilter The key filter
     * @return A map23 will all keys matching <code>keyFilter</code>.
     */
    public ImmMap<K, V> filterKeys(final Predicate<K> keyFilter);

    /**
     * Return A map23 with entries that match <code>filter</code>.
     * <p>This operation is O(n * log n), where n = |this|.
     * <pre>
     * Example:
     *     assert ImmCollections.asMap(1,2,  3,1).filter((k,v) -> k > v).equals(ImmCollections.asMap(3,1));
     * </pre>
     * @param filter The entry filter
     * @return  A map23 with entries that match <code>filter</code>
     */
    ImmMap<K, V> filter(final BiPredicate<K,V> filter);
    
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
