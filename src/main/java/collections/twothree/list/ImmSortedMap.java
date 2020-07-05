package collections.twothree.list;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.function.Predicate;

/**
 * Represents an Immutable map where objects are ordered by a comparator on the keys in a {@link ImmList}.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.  The object is not modified in any way.
 * <p>that all operations are O(log n), but the results of those
 * operations are also guaranteed to be O(log n)
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
     *     SortedMap23&lt;Integer,Integer&gt; tm = SortedMap23.singleton(4,1).put(2,2).put(3,3);
     *     m.ge(2) == {2=&gt;2, 3=&gt;3, 4=&gt;1}
     *     m.ge(4) == {4=&gt;1}
     *     m.ge(0) == {2=&gt;2, 3=&gt;3, 4=&gt;1}
     *     m.ge(5) == {}
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
     *     SortedMap23&lt;Integer,Integer&gt; tm = SortedMap23.singleton(4,1).put(2,2).put(3,3);
     *     m.lt(2) == {}
     *     m.lt(4) == {2=&gt;2, 3=&gt;3}
     *     m.lt(0) == {}
     *     m.lt(5) == {2=&gt;2, 3=&gt;3, 4=&gt;1}
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
     *     SortedMap23&lt;Integer,Integer&gt; tm = SortedMap23.singleton(4,1).put(2,2).put(3,3);
     *     m.exclude(2, 3) == {3=&gt;3, 4=&gt;1}
     *     m.exclude(2, 4) == {4=&gt;1}
     *     m.exclude(0, 4) == {4=&gt;1}
     *     m.exclude(0, 5) == {}
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
     *     SortedMap23&lt;Integer,Integer&gt; tm = SortedMap23.singleton(4,1).put(2,2).put(3,3);
     *     m.subSet(2, 3) == {2=&gt;2}
     *     m.subSet(2, 4) == {2=&gt;2, 3=&gt;3}
     *     m.subSet(0, 4) == {2=&gt;2, 3=&gt;3}
     *     m.subSet(0, 5) == {2=&gt;2, 3=&gt;3, 4=&gt;1}
     *     m.subSet(3, 3) == {}
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
     *     SortedMap23&lt;Integer,Integer&gt; tm = SortedMap23.singleton(4,1).put(2,2).put(3,3);
     *     m.reversed() = {4=&gt;1,3=&gt;2,2=&gt;1}
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
     *     SortedMap23&lt;Integer,Integer&gt; tm = SortedMap23.singleton(4,1).put(2,2).put(3,3);
     *     m.indexOf(2) = 1
     *     m.indexOf(4) = 2
     *     m.indexOf(5) = -1
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
     *     SortedMap23&lt;Integer,Integer&gt; tm = SortedMap23.singleton(4,1).put(2,2).put(3,3);
     *     m.getAt(0) = 2
     *     m.getAt(1) = 3
     *     m.getAt(2) = 4
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
     *     SortedMap23&lt;Integer,Integer&gt; tm = SortedMap23.singleton(4,1).put(2,2).put(3,3);
     *     m.removeAt(0) = {3 =gt; 3,4 =gt; 1}
     *     m.removeAt(1) = {2 =gt; 2,4 =gt; 1}
     *     m.removeAt(2) = {2 =gt; 2,3 =gt; 3}
     * </pre>
     * <p>*THIS OPERATION IS IMMUTABLE, THE PREVIOUS Map23 IS UNCHANGED!*.
     * @param index The index
     * @return a map with the entry at the given index removed
     */
    ImmSortedMap<K, V> removeAt(int index);

    @Override
	SortedMap<K, V> asMap();
	
    /**
     * Returns all of the entries as a list.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     SortedMap23&lt;Integer,Integer&gt; tm = SortedMap23.singleton(4,1).put(2,2).put(3,3);
     *     m.asList() = [{2=gt;2},{3=gt;3},{4=gt;1}]
     * </pre>
     * @return all of the entries as a list
     */
	ImmList<Entry<K,V>> asList();
	
    @Override
	ImmSortedSet<Entry<K,V>> asSet23();

	@Override
	ImmSortedSet<K> keys();

    @Override
    ImmSortedMap<K, V> filter(Predicate<Entry<K,V>> filter);

    Comparator<? super K> getKeyComparator();    
}
