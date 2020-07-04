package collections.twothree.list;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents an Immutable map where objects are ordered by their hashes in a {@link List23}.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.  The object is not modified in any way.
 * <p>Unlike the java counterpart {@link HashMap}, lookup's are O(log n).
 * <p>Use @link {@link SortedMap23}, if at all possible, the only thing
 * HashMap23 provides is the ability to have a map, when comparators
 * are inconvenient.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public final class HashMap23<K, V> implements Map23<K, V> {
	final List23<Entry<K, V>> entries;

	HashMap23(final List23<Entry<K, V>> entries) {
        assert entries != null;
		this.entries = entries;
	}

	/**
     * Returns the empty hashmap.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     HashMap23.empty() == {}
     * </pre>
     * @param <K> The key type
     * @param <V> The value type
	 * @return the empty hashmap
	 */
    public static <K,V> HashMap23<K,V> empty() {
        return of(Collections.emptyList());
    }
   
    /**
     * Returns a hashmap23 seeded from another <code>Map</code>.
     * <p>This operation is O(n log n), where n = |items|.
     * <pre>
     * Example:
     *     HashMap&lt;Integer, String&gt; hm = new HashMap&lt;&gt;();
     *     hm.put(1, "2");
     *     hm.put(3, "4");
     *     HashMap23.of(hm) == {1 =&gt; "2", 3 =&gt; "4"}
     * </pre>
     * @param items map of items to copy
     * @param <K> The key type
     * @param <V> The value type
     * @return a hashmap23 seeded from another <code>Map</code>
     */
    public static <K,V> HashMap23<K,V> of(final Map<K, V> items) {
        return of(items.entrySet());
    }

    /**
     * Returns a hashmap23 seeded from another collection of entries.
     * <p>This operation is O(n log n), where n = |items|.
     * <pre>
     * Example:
     *     HashMap&lt;Integer, String&gt; hm = new HashMap&lt;&gt;();
     *     hm.put(1, "2");
     *     hm.put(3, "4");
     *     HashMap23.of(hm.entrySet()) == {1 =&gt; "2", 3 =&gt; "4"}
     * </pre>
     * @param items set of items to copy
     * @param <K> The key type
     * @param <V> The value type
     * @return a hashmap23 seeded from another <code>Map</code>
     */
	public static <K,V> HashMap23<K,V> of(final Iterable<Entry<K, V>> items) {
	    return new HashMap23<K, V>(List23.ofSortedUnique((a,b) -> HashSet23.compare(a.getKey(), b.getKey()), items));
	}

    @Override
	public int size() {
		return entries.size();
	}
	
    @Override
    public HashMap23<K, V> add(Entry<K ,V> entry) {
        return put(entry.getKey(), entry.getValue());
    }

    @Override
    public HashMap23<K, V> addAll(Iterable<Entry<K ,V>> items) {
        HashMap23<K, V> m = this;
        for(Entry<K,V> e: items) {
            m = m.add(e);
        }
        return m;
    }

    @Override
    public HashMap23<K, V> addAll(Map<K ,V> items) {
        return addAll(items.entrySet());
    }

    @Override
	public HashMap23<K, V> put(final K key, final V value) {
        HashMap23<K, V> m = removeKey(key);
        int index = m.keys().elements.naturalPosition(e -> HashSet23.compare(key, e));
        return new HashMap23<>(m.entries.insertAt(index, new AbstractMap.SimpleImmutableEntry<>(key, value)));
	}
	
    @Override
    public boolean containsKey(final K key) {
        return keys().contains(key);
    }

    @Override
	public HashMap23<K, V> removeKey(final K key) {
        int index = keys().elements.indexOf(key);
        return index < 0 ? this : new HashMap23<K, V>(entries.removeAt(index));
	}
	
    @Override
    public HashMap23<K, V> retainAllKeys(final Iterable<K> other) {
        HashSet23<K> hs = HashSet23.of(other);
        return filter(e -> hs.contains(e.getKey()));
    }
    
    @Override
    public HashMap23<K, V> filterKeys(final Predicate<K> filter) {
        return filter(e -> filter.test(e.getKey()));
    }

    @Override
    public HashMap23<K, V> removeAllKeysIn(final Iterable<K> keys) {
        HashMap23<K, V> m = this;
        for(K key: keys) {
            m = m.removeKey(key);
        }
        return m;
    }
	
    @Override
    public V get(final K key) {
        return getOrDefault(key, () -> null);
    }

    @Override
    public V getOrDefault(final K key, final Supplier<V> supplier) {
        int index = keys().elements.indexOf(key);
        return index < 0 ? supplier.get() : entries.getAt(index).getValue();
    }
    
    @Override
	public Map<K, V> asMap() {
		return new Map23Map<>(this);
	}
	
    @Override
	public Set23<Entry<K,V>> asSet23() {
	    return new SortedSet23<>(HashMap23::entryCompare, entries);
	}
	
	static <K,V> int entryCompare(final Entry<K,V> a, final Entry<K,V> b) {
	    int cmp = HashSet23.compare(a.getKey(), b.getKey());
	    if (cmp != 0) {
	        return cmp;
	    }
	    return HashSet23.compare(a.getValue(), b.getValue());
	}
	
    @Override
	public HashSet23<K> keys() {
	    return new HashSet23<>(entries.map(e -> e.getKey()));
	}

    @Override
    public Collection23<V> values() {
        return entries.map(e -> e.getValue());
    }
	
    @Override
    public HashMap23<K, V> filter(final Predicate<Entry<K,V>> filter) {
        return new HashMap23<>(entries.filter(filter));
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
	public Iterator<Entry<K,V>> iterator() {
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
}
