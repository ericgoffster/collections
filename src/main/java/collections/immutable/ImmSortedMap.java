package collections.immutable;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Represents an Immutable sorted mapping of keys to values.   Entries are arranged
 * in order of their keys.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.
 * Operations such as put and removeKey return map's with the specified operation
 * applied leaving the origin map unmodified.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public interface ImmSortedMap<K, V> extends ImmMap<K, V> {
    @Override
    ImmSortedMap<K, V> addAll(Iterable<? extends Entry<K ,V>> items);

    @Override
    ImmSortedMap<K, V> addAll(Map<K ,V> items);

    @Override
	ImmSortedMap<K, V> put(K key, V value);
	
    /**
     * Returns a sorted map with all entries &gt;= the given key. 
     * <p>This operation is O(log n).
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * <pre>
     * Example:
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).ge(2).equals(ImmCollections.asSortedMap(4,1,  2,2,  3,3));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).ge(4).equals(ImmCollections.asSortedMap(4,1));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).ge(0).equals(ImmCollections.asSortedMap(4,1,  2,2,  3,3));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).ge(5).equals(ImmCollections.emptySortedMap());
     * </pre>
     * @param key The min key
     * @return a sorted map with all entries &gt;= the given key
     */
    ImmSortedMap<K, V> ge(K key);

    /**
     * Returns a sorted map with all entries &lt; the given key. 
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).lt(2).equals(ImmCollections.emptySortedMap());
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).lt(4).equals(ImmCollections.asSortedMap(2,2,  3,3));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).lt(0).equals(ImmCollections.emptySortedMap());
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).lt(5).equals(ImmCollections.asSortedMap(4,1,  2,2,  3,3));
     * </pre>
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param key The max key
     * @return a sorted map with all entries &lt; the given key
     */
    ImmSortedMap<K, V> lt(K key);

    /**
     * Returns a sorted map with no keys between lowKey and highKey.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).exclude(2, 3).equals(ImmCollections.asSortedMap(3,3, 4,1));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).exclude(2, 4).equals(ImmCollections.asSortedMap(4,1));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).exclude(0, 4).equals(ImmCollections.asSortedMap(4,1));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).exclude(0, 5).equals(ImmCollections.emptySortedMap());
     * </pre>
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param lowKey The min key.  (inclusive)
     * @param highKey The max key.  (exclusive)
     * @return a sorted map with no keys between lowKey and highKey
     */
    ImmSortedMap<K, V> exclude(K lowKey, K highKey);

    /**
     * Returns a sorted map with all keys between lowKey and highKey.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).subSet(2, 3).equals(ImmCollections.asSortedMap(2,2));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).subSet(2, 4).equals(ImmCollections.asSortedMap(2,2,   3,3));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).subSet(0, 4).equals(ImmCollections.asSortedMap(2,2,   3,3));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).subSet(0, 5).equals(ImmCollections.asSortedMap(2,2,   3,3,   4,1));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).subSet(3, 3).equals(ImmCollections.emptySortedMap());
     * </pre>
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param lowKey The min key.  (inclusive)
     * @param highKey The max key.  (exclusive)
     * @return a sorted map with all keys between lowKey and highKey.
     */
    ImmSortedMap<K, V> subSet(K lowKey, K highKey);

	/**
	 * Returns a sorted map with all elements reversed.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     assert ImmCollections.asSortedMap(2,2,  3,3,  4,1).reversed().equals(ImmCollections.asSortedMap(4,1,  3,3,   2,2));
     * </pre>
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
	 * @return a sorted map with all elements reversed
	 */
	ImmSortedMap<K, V> reversed();
	
	/**
	 * Returns the index of the given key.  Returns -1 if not found.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).indexOfKey(2) == 0;
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).indexOfKey(4) == 2;
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).indexOfKey(5) == -1;
     * </pre>
	 * @param key The key to get an index of.
	 * @return the index of the given key
	 */
    int indexOfKey(K key);
    
    @Override
	ImmSortedMap<K, V> removeKey(K key);
	
    @Override
    ImmSortedMap<K, V> retainAllKeys(Iterable<? extends K> keys);

    @Override
    ImmSortedMap<K, V> removeAllKeysIn(Iterable<? extends K> keys);
	
    @Override
    ImmSortedMap<K, V> filterKeys(Predicate<K> filter);

    /**
     * Returns the entry at the given index.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).getAt(0).getKey() == 2;
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).getAt(1).getKey() == 3;
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).getAt(2).getKey() == 4;
     * </pre>
     * @param index The index
     * @return  the entry at the given index
     */
    Entry<K,V> getAt(int index);
    
    /**
     * Returns a map with the entry at the given index removed.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).removeAt(0).equals(ImmCollections.asSortedMap(4,1,  3,3));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).removeAt(1).equals(ImmCollections.asSortedMap(4,1,  2,2));
     *     assert ImmCollections.asSortedMap(4,1,  2,2,  3,3).removeAt(2).equals(ImmCollections.asSortedMap(3,3,  2,2));
     * </pre>
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param index The index
     * @return a map with the entry at the given index removed
     */
    ImmSortedMap<K, V> removeAt(int index);

    @Override
	SortedMap<K, V> asMap();
	
	@Override
	ImmSortedSet<K> keys();

    @Override
    ImmSortedMap<K, V> filter(BiPredicate<K, V> filter);

    Comparator<? super K> getKeyComparator();    
}
