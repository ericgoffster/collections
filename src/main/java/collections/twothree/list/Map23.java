package collections.twothree.list;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.granitesoft.requirement.Requirements;

public final class Map23<K, V> implements Iterable<Entry<K, V>> {
    final Comparator<? super K> keyComparator;
	final List23<Entry<K, V>> entries;

	Map23(final Comparator<? super K> keyComparator, final List23<Entry<K, V>> entries) {
        this.keyComparator = Requirements.require(keyComparator, Requirements.notNull(), () -> "keyComparator");
		this.entries = Requirements.require(entries, Requirements.notNull(), () -> "entries");
	}

    public static <K,V> Map23<K,V> empty(final Comparator<? super K> keyComparator) {
        return of(keyComparator, Collections.emptyList());
    }
    public static <K extends Comparable<K>,V> Map23<K,V> empty() {
        return empty(List23::naturalCompare);
    }
   
    public static <K,V> Map23<K,V> ofSorted(final SortedMap<K, V> items) {
        Comparator<? super K> comparator = items.comparator();
        if (comparator == null) {
            comparator = List23::unNaturalCompare;
        }
        return of(comparator, items.entrySet());
    }
    public static <K,V> Map23<K,V> of(final Comparator<? super K> keyComparator, final Map<K, V> items) {
        return of(keyComparator, items.entrySet());
    }
	public static <K,V> Map23<K,V> of(final Comparator<? super K> keyComparator, final Iterable<Entry<K, V>> items) {
	    return new Map23<K, V>(keyComparator, List23.empty()).addAll(items);
	}
    public static <K extends Comparable<K>,V> Map23<K,V> of(final Iterable<Entry<K, V>> items) {
        return of(List23::naturalCompare, items);
    }
	
	public int size() {
		return entries.size();
	}
	
    public Map23<K, V> add(Entry<K ,V> entry) {
        return put(entry.getKey(), entry.getValue());
    }

    public Map23<K, V> merge(Iterable<Entry<K ,V>> items, BiFunction<V, Entry<K,V>, V> function) {
        Map23<K, V> m = this;
        for(Entry<K,V> e: items) {
            if (!m.containsKey(e.getKey())) {
                m = m.add(e);
            } else {
                m = m.put(e.getKey(), function.apply(m.get(e.getKey()), e));
            }
        }
        return m;
    }

    public Map23<K, V> addAll(Iterable<Entry<K ,V>> items) {
        Map23<K, V> m = this;
        for(Entry<K,V> e: items) {
            m = m.add(e);
        }
        return m;
    }

    public Map23<K, V> merge(Map<K ,V> items, BiFunction<V, Entry<K,V>, V> function) {
        return merge(items.entrySet(), function);
    }

    public Map23<K, V> addAll(Map<K ,V> items) {
        return addAll(items.entrySet());
    }

	public Map23<K, V> put(final K key, final V value) {
        Map23<K, V> m = remove(key);
        int index = m.keys().naturalPosition(key);
        return new Map23<>(keyComparator, m.entries.insertAt(index, new AbstractMap.SimpleImmutableEntry<>(key, value)));
	}
	
	public Map23<K, V> reversed() {
		return new Map23<K, V>(keyComparator.reversed(), entries.reversed());
	}
	
    public int indexOf(final K key) {
        return keys().indexOf(key);
    }
    
    public boolean containsKey(final K key) {
        return keys().contains(key);
    }

	public Map23<K, V> remove(final K key) {
        int index = indexOf(key);
        return index < 0 ? this : removeAt(index);
	}
	
    public Map23<K, V> removeAll(final Iterable<K> keys) {
        Map23<K, V> m = this;
        for(K key: keys) {
            m = m.remove(key);
        }
        return m;
    }
	
    public Entry<K,V> getAt(final int index) {
        return entries.getAt(index);
    }
    
    public V get(final K key) {
        return getOrDefault(key, () -> null);
    }

    public V getOrDefault(final K key, final Supplier<V> supplier) {
        int index = indexOf(key);
        return index < 0 ? supplier.get() : entries.getAt(index).getValue();
    }
    
    public Map23<K, V> removeAt(final int index) {
        return new Map23<K, V>(keyComparator, entries.removeAt(index));
    }

	public Map<K, V> asMap() {
		return new Map23Map<>(this);
	}
	
	public List23<Entry<K,V>> asList() {
		return entries;
	}
	
	public Set23<Entry<K,V>> asSet() {
	    return new Set23<>(this::entryCompare, entries);
	}
	
	public int entryCompare(final Entry<K,V> a, final Entry<K,V> b) {
	    int cmp = keyComparator.compare(a.getKey(), b.getKey());
	    if (cmp != 0) {
	        return cmp;
	    }
	    return Set23.hashCompare(a.getValue(), b.getValue());
	}
	
	public Set23<K> keys() {
	    return new Set23<>(keyComparator, entries.map(e -> e.getKey()));
	}

    public List23<V> values() {
        return entries.map(e -> e.getValue());
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
	
	public ListIterator<Entry<K,V>> iterator() {
		return entries.iterator();
	}
    
    public Stream<Entry<K,V>> stream() {
        return entries.stream();
    }
}
