package collections.twothree.list;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents an Immutable map where objects are ordered by a comparator on the keys in a {@link List23}.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.  The object is not modified in any way.
 * <p>that all operations are O(log n), but the results of those
 * operations are also guaranteed to be O(log n)
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public final class TreeMap23<K, V> implements ImmSortedMap<K, V> {
    final Comparator<? super K> keyComparator;
	final TreeList23<Entry<K, V>> entries;

	TreeMap23(final Comparator<? super K> keyComparator, final TreeList23<Entry<K, V>> entries) {
	    assert keyComparator != null;
        assert entries != null;
        this.keyComparator = keyComparator;
		this.entries = entries;
	}

	/**
	 * Returns an empty sorted map with a a custom key comparator.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     SortedMap23.empty(Integer::compare) == {}
     * </pre>
     * @param <K> The key type
     * @param <V> The value type
	 * @param keyComparator The custom key comparator
	 * @return an empty sorted map with a a custom key comparator
	 */
    public static <K,V> TreeMap23<K,V> empty(final Comparator<? super K> keyComparator) {
        return new TreeMap23<K, V>(keyComparator, TreeList23.empty());
    }

    /**
     * Returns an empty sorted map using the natural comparator associated with the keys.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     SortedMap23.empty() == {}
     * </pre>
     * @param <K> The key type
     * @param <V> The value type
     * @return an empty sorted map using the natural comparator associated with the keys.
     */
    public static <K extends Comparable<K>,V> TreeMap23<K,V> empty() {
        return empty(TreeList23::naturalCompare);
    }
   
    /**
     * Returns a sorted map with a single entry using the natural comparator associated with the key.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     SortedMap23.singleton(4, 1) == {4 =&gt; 1}
     * </pre>
     * @param key initial key
     * @param value initial value
     * @param <K> The key type
     * @param <V> The value type
     * @return an empty sorted map using the natural comparator associated with the keys.
     */
    public static <K extends Comparable<K>,V> TreeMap23<K,V> singleton(final K key, final V value) {
        return new TreeMap23<K, V>(TreeList23::naturalCompare, TreeList23.singleton(new AbstractMap.SimpleImmutableEntry<>(key, value)));
    }

    /**
     * Returns a sorted map populated from a java sorted map.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     TreeMap&lt;Integer,Integer&gt; tm = new TreeMap&lt;&gt;();
     *     tm.put(4,1);
     *     tm.put(2,2);
     *     tm.put(3,3);
     *     SortedMap23.of(tm) == {2=&gt;2,3=&gt;3,4=&gt;1}
     * </pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param sortedMap The map to get entries from
     * @return a sorted map populated from a java sorted map.
     */
    public static <K,V> TreeMap23<K,V> ofSorted(final SortedMap<K, V> sortedMap) {
        return of(getComparator(sortedMap), sortedMap.entrySet());
    }

    /**
     * Returns a sorted map populated from a java map and a custom comparator.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     TreeMap&lt;Integer,Integer&gt; tm = new TreeMap&lt;&gt;();
     *     tm.put(4,1);
     *     tm.put(2,2);
     *     tm.put(3,3);
     *     SortedMap23.of(Integer::compare, tm) == {2=&gt;2,3=&gt;3,4=&gt;1}
     * </pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param keyComparator The custom key comparator
     * @param map The map to get entries from
     * @return a sorted map populated from a java map.
     */
    public static <K,V> TreeMap23<K,V> of(final Comparator<? super K> keyComparator, final Map<K, V> map) {
        return of(keyComparator, map.entrySet());
    }
    
    /**
     * Returns a sorted map populated from a list of entries and a custom comparator.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     TreeMap&lt;Integer,Integer&gt; tm = new TreeMap&lt;&gt;();
     *     tm.put(4,1);
     *     tm.put(2,2);
     *     tm.put(3,3);
     *     SortedMap23.of(Integer::compare, tm.entrySet()) == {2=&gt;2,3=&gt;3,4=&gt;1}
     * </pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param keyComparator The key comparator
     * @param entries The list of entries
     * @return a sorted map populated from a list of entries and a custom comparator
     */
	public static <K,V> TreeMap23<K,V> of(final Comparator<? super K> keyComparator, final Iterable<? extends Entry<K, V>> entries) {
	    return new TreeMap23<K, V>(keyComparator, TreeList23.ofSortedUnique((a,b) -> keyComparator.compare(a.getKey(), b.getKey()), entries));
	}
	
	/**
     * Returns a sorted map populated from a list of entries and the natural key comparator.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     TreeMap&lt;Integer,Integer&gt; tm = new TreeMap&lt;&gt;();
     *     tm.put(4,1);
     *     tm.put(2,2);
     *     tm.put(3,3);
     *     SortedMap23.of(tm.entrySet()) == {2=&gt;2,3=&gt;3,4=&gt;1}
     * </pre>
     * @param <K> The key type
     * @param <V> The value type
     * @param entries The list of entries
	 * @return a sorted map populated from a list of entries and a natural comparator
	 */
    public static <K extends Comparable<K>,V> TreeMap23<K,V> of(final Iterable<? extends Entry<K, V>> entries) {
        return of(TreeList23::naturalCompare, entries);
    }
	
    @Override
	public int size() {
		return entries.size();
	}
	
    @Override
    public TreeMap23<K, V> add(Entry<K ,V> entry) {
        return put(entry.getKey(), entry.getValue());
    }

    @Override
    public TreeMap23<K, V> addAll(Iterable<? extends Entry<K ,V>> items) {
        TreeMap23<K, V> m = this;
        for(Entry<K,V> e: items) {
            m = m.add(e);
        }
        return m;
    }

    @Override
    public TreeMap23<K, V> addAll(Map<K ,V> items) {
        return addAll(items.entrySet());
    }

    @Override
	public TreeMap23<K, V> put(final K key, final V value) {
        TreeMap23<K, V> m = removeKey(key);
        final int index = m.keys().asList().naturalPosition(e -> keyComparator.compare(key, e));
        return new TreeMap23<>(keyComparator, m.entries.insertAt(index, new AbstractMap.SimpleImmutableEntry<>(key, value)));
	}
	
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
    public TreeMap23<K, V> ge(final K key) {
        return new TreeMap23<>(keyComparator, entries.tailAt(keys().asList().naturalPosition(e -> keyComparator.compare(key, e))));
    }

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
    public TreeMap23<K, V> lt(final K key) {
        return new TreeMap23<>(keyComparator, entries.headAt(keys().asList().naturalPosition(e -> keyComparator.compare(key, e))));
    }

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
    public TreeMap23<K, V> exclude(final K lowKey, final K highKey) {
        final int cmp = keyComparator.compare(lowKey, highKey);
        if (cmp > 0) {
            throw new IllegalArgumentException("low must be <= high");
        }
        if (keyComparator.compare(lowKey, highKey) == 0) {
            return this;
        }
        final TreeSet23<K> keys = keys();
        return new TreeMap23<>(keyComparator, entries.removeRange(
                keys.asList().naturalPosition(e -> keyComparator.compare(lowKey, e)),
                keys.asList().naturalPosition(e -> keyComparator.compare(highKey, e))));
    }

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
    public TreeMap23<K, V> subSet(final K lowKey, final K highKey) {
        final int cmp = keyComparator.compare(lowKey, highKey);
        if (cmp > 0) {
            throw new IllegalArgumentException("low must be <= high");
        }
        if (keyComparator.compare(lowKey, highKey) == 0) {
            return new TreeMap23<>(keyComparator, TreeList23.empty());
        }
        final TreeSet23<K> keys = keys();
        return new TreeMap23<>(keyComparator, entries.getRange(
                keys.asList().naturalPosition(e -> keyComparator.compare(lowKey, e)),
                keys.asList().naturalPosition(e -> keyComparator.compare(highKey, e))));
    }

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
	public TreeMap23<K, V> reversed() {
		return new TreeMap23<K, V>(keyComparator.reversed(), entries.reversed());
	}
	
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
    public int indexOfKey(final K key) {
        return keys().indexOf(key);
    }
    
    @Override
    public boolean containsKey(final K key) {
        return keys().contains(key);
    }

    @Override
	public TreeMap23<K, V> removeKey(final K key) {
        int index = indexOfKey(key);
        return index < 0 ? this : removeAt(index);
	}
	
    @Override
    public TreeMap23<K, V> retainAllKeys(final Iterable<? extends K> keys) {
        HashSet23<K> hs = HashSet23.of(keys);
        return filter(e -> hs.contains(e.getKey()));
    }

    @Override
    public TreeMap23<K, V> removeAllKeysIn(final Iterable<? extends K> keys) {
        TreeMap23<K, V> m = this;
        for(K key: keys) {
            m = m.removeKey(key);
        }
        return m;
    }
	
    @Override
    public TreeMap23<K, V> filterKeys(final Predicate<K> filter) {
        return filter(e -> filter.test(e.getKey()));
    }

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
    public Entry<K,V> getAt(final int index) {
        return entries.getAt(index);
    }
    
    @Override
    public V get(final K key) {
        return getOrDefault(key, () -> null);
    }

    @Override
    public V getOrDefault(final K key, final Supplier<V> supplier) {
        int index = indexOfKey(key);
        return index < 0 ? supplier.get() : entries.getAt(index).getValue();
    }
    
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
    public TreeMap23<K, V> removeAt(final int index) {
        return new TreeMap23<K, V>(keyComparator, entries.removeAt(index));
    }

    @Override
	public SortedMap23Map<K, V> asMap() {
		return new SortedMap23Map<>(this);
	}
	
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
	public TreeList23<Entry<K,V>> asList() {
		return entries;
	}
	
    @Override
	public TreeSet23<Entry<K,V>> asSet23() {
	    return new TreeSet23<>(this::entryCompare, entries);
	}
	

	@Override
	public TreeSet23<K> keys() {
	    return new TreeSet23<>(keyComparator, entries.map(e -> e.getKey()));
	}

    @Override
    public TreeList23<V> values() {
        return entries.map(e -> e.getValue());
    }
	
    @Override
    public TreeMap23<K, V> filter(final Predicate<Entry<K,V>> filter) {
        return new TreeMap23<>(keyComparator, entries.filter(filter));
    }
    
    @Override
	public int hashCode() {
		return asMap().hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Map23)) {
			return false;
		}
		Map23<?, ?> other = (Map23<?, ?>)obj;
		return asMap().equals(other.asMap());
	}
	
	@Override
	public String toString() {
		return asMap().toString();
	}
	
    @Override
    public Spliterator<Entry<K,V>> spliterator() {
        return entries.spliterator();
    }

    @Override
	public ListIterator<Entry<K,V>> iterator() {
		return entries.iterator();
	}
    
    @Override
    public Stream<Entry<K,V>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
    
    @Override
    public void forEach(BiConsumer<K, V> cond) {
        stream().forEach(e -> cond.accept(e.getKey(), e.getValue()));
    }
    
    @Override
    public Comparator<? super K> getKeyComparator() {
        return keyComparator;
    }

    int entryCompare(final Entry<K,V> a, final Entry<K,V> b) {
        int cmp = keyComparator.compare(a.getKey(), b.getKey());
        if (cmp != 0) {
            return cmp;
        }
        return HashSet23.compare(a.getValue(), b.getValue());
    }

    static <K, V> Comparator<? super K> getComparator(final SortedMap<K, V> items) {
        final Comparator<? super K> comparator = items.comparator();
        if (comparator == null) {
            return TreeSet23::unNaturalCompare;
        }
        return comparator;
    }
}
