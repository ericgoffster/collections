package collections.twothree.list;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class HashMap23<K, V> implements Map23<K, V> {
	final List23<Entry<K, V>> entries;

	HashMap23(final List23<Entry<K, V>> entries) {
        assert entries != null;
		this.entries = entries;
	}

    public static <K,V> HashMap23<K,V> empty() {
        return of(Collections.emptyList());
    }
   
    public static <K,V> HashMap23<K,V> of(final Map<K, V> items) {
        return of(items.entrySet());
    }
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
        int index = m.keys().elements.naturalPosition(HashSet23::compare, key);
        return new HashMap23<>(m.entries.insertAt(index, new AbstractMap.SimpleImmutableEntry<>(key, value)));
	}
	
    @Override
    public boolean containsKey(final K key) {
        return keys().contains(key);
    }

    @Override
	public HashMap23<K, V> removeKey(final K key) {
        int index = keys().elements.indexOf(key);
        return index < 0 ? this : removeAt(index);
	}
	
    @Override
    public HashMap23<K, V> retainAllKeys(final Iterable<K> other) {
        HashSet23<K> hs = HashSet23.of(other);
        return filter(e -> hs.contains(e.getKey()));
    }

    @Override
    public HashMap23<K, V> removeAllKeysIn(final Iterable<K> keys) {
        HashMap23<K, V> m = this;
        for(K key: keys) {
            m = m.removeKey(key);
        }
        return m;
    }
	
    public Entry<K,V> getAt(final int index) {
        return entries.getAt(index);
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
    
    public HashMap23<K, V> removeAt(final int index) {
        return new HashMap23<K, V>(entries.removeAt(index));
    }

    @Override
	public Map<K, V> asMap() {
		return new Map23Map<>(this);
	}
	
    @Override
	public Set23<Entry<K,V>> asSet() {
	    return new SortedSet23<>(this::entryCompare, entries);
	}
	
	public int entryCompare(final Entry<K,V> a, final Entry<K,V> b) {
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
        return entries.stream();
    }
    
    @Override
    public void forEach(BiConsumer<K, V> cond) {
        stream().forEach(e -> cond.accept(e.getKey(), e.getValue()));
    }
}
