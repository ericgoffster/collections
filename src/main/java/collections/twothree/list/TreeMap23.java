package collections.twothree.list;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents an Immutable map where objects are ordered by a comparator on the keys in a {@link ImmList}.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.  The object is not modified in any way.
 * <p>that all operations are O(log n), but the results of those
 * operations are also guaranteed to be O(log n)
 *
 * @param <K> The key type
 * @param <V> The value type
 */
final class TreeMap23<K, V> implements ImmSortedMap<K, V> {
    final Comparator<? super K> keyComparator;
	final TreeList23<Entry<K, V>> entries;

	TreeMap23(final Comparator<? super K> keyComparator, final TreeList23<Entry<K, V>> entries) {
	    assert keyComparator != null;
        assert entries != null;
        this.keyComparator = keyComparator;
		this.entries = entries;
	}

    static <K,V> TreeMap23<K,V> empty(final Comparator<? super K> keyComparator) {
        return new TreeMap23<K, V>(keyComparator, TreeList23.empty());
    }

    static <K extends Comparable<K>,V> TreeMap23<K,V> empty() {
        return empty(TreeList23::naturalCompare);
    }
   
    static <K extends Comparable<K>,V> TreeMap23<K,V> singleton(final K key, final V value) {
        return new TreeMap23<K, V>(TreeList23::naturalCompare, TreeList23.singleton(new AbstractMap.SimpleImmutableEntry<>(key, value)));
    }

    static <K,V> TreeMap23<K,V> ofSorted(final SortedMap<K, V> sortedMap) {
        return of(getComparator(sortedMap), sortedMap.entrySet());
    }

    static <K,V> TreeMap23<K,V> of(final Comparator<? super K> keyComparator, final Map<K, V> map) {
        return of(keyComparator, map.entrySet());
    }
    
    static <K,V> TreeMap23<K,V> of(final Comparator<? super K> keyComparator, final Iterable<? extends Entry<K, V>> entries) {
	    return new TreeMap23<K, V>(keyComparator, TreeList23.ofSortedUnique((a,b) -> keyComparator.compare(a.getKey(), b.getKey()), entries));
	}
	
	static <K extends Comparable<K>,V> TreeMap23<K,V> of(final Iterable<? extends Entry<K, V>> entries) {
        return of(TreeList23::naturalCompare, entries);
    }
	
    @Override
	public int size() {
		return entries.size();
	}
	
    @Override
    public TreeMap23<K, V> addAll(Iterable<? extends Entry<K ,V>> items) {
        TreeMap23<K, V> m = this;
        for(Entry<K,V> e: items) {
            m = m.put(e.getKey(), e.getValue());
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
	
    @Override
    public TreeMap23<K, V> ge(final K key) {
        return new TreeMap23<>(keyComparator, entries.tailAt(keys().asList().naturalPosition(e -> keyComparator.compare(key, e))));
    }

    @Override
    public TreeMap23<K, V> lt(final K key) {
        return new TreeMap23<>(keyComparator, entries.headAt(keys().asList().naturalPosition(e -> keyComparator.compare(key, e))));
    }

    @Override
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

    @Override
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

    @Override
	public TreeMap23<K, V> reversed() {
		return new TreeMap23<K, V>(keyComparator.reversed(), entries.reversed());
	}
	
    @Override
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
        return filter((k, v) -> hs.contains(k));
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
        return filter((k, v) -> filter.test(k));
    }

    @Override
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
    
    @Override
    public TreeMap23<K, V> removeAt(final int index) {
        return new TreeMap23<K, V>(keyComparator, entries.removeAt(index));
    }

    @Override
	public SortedMap23Map<K, V> asMap() {
		return new SortedMap23Map<>(this);
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
    public TreeMap23<K, V> filter(final BiPredicate<K,V> filter) {
        return new TreeMap23<>(keyComparator, entries.filter(e -> filter.test(e.getKey(), e.getValue())));
    }
    
    @Override
	public int hashCode() {
		return asMap().hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof ImmMap)) {
			return false;
		}
		ImmMap<?, ?> other = (ImmMap<?, ?>)obj;
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

    TreeSet23<Entry<K,V>> asSet23() {
        return new TreeSet23<>(this::entryCompare, entries);
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
