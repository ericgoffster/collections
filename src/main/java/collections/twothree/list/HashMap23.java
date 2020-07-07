package collections.twothree.list;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents an Immutable map where objects are ordered by their hashes in a {@link ImmList}.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.  The object is not modified in any way.
 * <p>Unlike the java counterpart {@link HashMap}, lookup's are O(log n).
 * <p>Use @link {@link ImmSortedMap}, if at all possible, the only thing
 * HashMap23 provides is the ability to have a map, when comparators
 * are inconvenient.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
final class HashMap23<K, V> implements ImmMap<K, V> {
	final TreeList23<Entry<K, V>> entries;

	HashMap23(final TreeList23<Entry<K, V>> entries) {
        assert entries != null;
		this.entries = entries;
	}

	static <K,V> HashMap23<K,V> empty() {
        return new HashMap23<>(TreeList23.empty());
    }
   
    static <K,V> HashMap23<K,V> singleton(final K key, final V value) {
        return new HashMap23<>(TreeList23.singleton(new AbstractMap.SimpleImmutableEntry<>(key, value)));
    }

    static <K,V> HashMap23<K,V> of(final Map<K, V> map) {
        return of(map.entrySet());
    }

    static <K,V> HashMap23<K,V> of(final Iterable<? extends Entry<K, V>> entries) {
	    return new HashMap23<K, V>(TreeList23.ofSortedUnique((a,b) -> HashSet23.compare(a.getKey(), b.getKey()), entries));
	}

    @Override
	public int size() {
		return entries.size();
	}
	
    @Override
    public HashMap23<K, V> addAll(final Iterable<? extends Entry<K ,V>> entries) {
        HashMap23<K, V> m = this;
        for(Entry<K,V> e: entries) {
            m = m.put(e.getKey(), e.getValue());
        }
        return m;
    }

    @Override
    public HashMap23<K, V> addAll(final Map<K ,V> map) {
        return addAll(map.entrySet());
    }

    @Override
	public HashMap23<K, V> put(final K key, final V value) {
        final HashMap23<K, V> m = removeKey(key);
        final int index = m.keys().elements.naturalPosition(e -> HashSet23.compare(key, e));
        return new HashMap23<>(m.entries.insertAt(index, new AbstractMap.SimpleImmutableEntry<>(key, value)));
	}
	
    @Override
    public boolean containsKey(final K key) {
        return keys().contains(key);
    }

    @Override
	public HashMap23<K, V> removeKey(final K key) {
        final int index = keys().elements.indexOf(key);
        return index < 0 ? this : new HashMap23<K, V>(entries.removeAt(index));
	}
	
    @Override
    public HashMap23<K, V> retainAllKeys(final Iterable<? extends K> keys) {
        final HashSet23<K> hs = HashSet23.of(keys);
        return filter((k, v) -> hs.contains(k));
    }
    
    @Override
    public HashMap23<K, V> filterKeys(final Predicate<K> keyFilter) {
        return filter((k, v) -> keyFilter.test(k));
    }

    @Override
    public HashMap23<K, V> removeAllKeysIn(final Iterable<? extends K> keys) {
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
    public V getOrDefault(final K key, final Supplier<V> defaultSupplier) {
        final int index = keys().elements.indexOf(key);
        return index < 0 ? defaultSupplier.get() : entries.getAt(index).getValue();
    }
    
    @Override
	public Map<K, V> asMap() {
		return new Map23Map<>(this);
	}
	
	@Override
	public HashSet23<K> keys() {
	    return new HashSet23<>(entries.map(e -> e.getKey()));
	}

    @Override
    public ImmCollection<V> values() {
        return entries.map(e -> e.getValue());
    }
	
    @Override
    public HashMap23<K, V> filter(final BiPredicate<K, V> filter) {
        return new HashMap23<>(entries.filter(e -> filter.test(e.getKey(), e.getValue())));
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
		final ImmMap<?, ?> other = (ImmMap<?, ?>)obj;
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
    public void forEach(final BiConsumer<K, V> cond) {
        stream().forEach(e -> cond.accept(e.getKey(), e.getValue()));
    }

    ImmSet<Entry<K,V>> asSet23() {
        return new TreeSet23<>(HashMap23::entryCompare, entries);
    }

    static <K,V> int entryCompare(final Entry<K,V> a, final Entry<K,V> b) {
        final int cmp = HashSet23.compare(a.getKey(), b.getKey());
        if (cmp != 0) {
            return cmp;
        }
        return HashSet23.compare(a.getValue(), b.getValue());
    }
}
