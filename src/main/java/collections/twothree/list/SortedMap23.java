package collections.twothree.list;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class SortedMap23<K, V> implements Map23<K, V> {
    final Comparator<? super K> keyComparator;
	final List23<Entry<K, V>> entries;

	SortedMap23(final Comparator<? super K> keyComparator, final List23<Entry<K, V>> entries) {
	    assert keyComparator != null;
        assert entries != null;
        this.keyComparator = keyComparator;
		this.entries = entries;
	}

    public static <K,V> SortedMap23<K,V> empty(final Comparator<? super K> keyComparator) {
        return of(keyComparator, Collections.emptyList());
    }
    public static <K extends Comparable<K>,V> SortedMap23<K,V> empty() {
        return empty(List23::naturalCompare);
    }
   
    public static <K,V> SortedMap23<K,V> ofSorted(final SortedMap<K, V> items) {
        Comparator<? super K> comparator = items.comparator();
        if (comparator == null) {
            comparator = List23::unNaturalCompare;
        }
        return of(comparator, items.entrySet());
    }
    public static <K,V> SortedMap23<K,V> of(final Comparator<? super K> keyComparator, final Map<K, V> items) {
        return of(keyComparator, items.entrySet());
    }
	public static <K,V> SortedMap23<K,V> of(final Comparator<? super K> keyComparator, final Iterable<Entry<K, V>> items) {
	    return new SortedMap23<K, V>(keyComparator, List23.empty()).addAll(items);
	}
    public static <K extends Comparable<K>,V> SortedMap23<K,V> of(final Iterable<Entry<K, V>> items) {
        return of(List23::naturalCompare, items);
    }
	
    @Override
	public int size() {
		return entries.size();
	}
	
    @Override
    public SortedMap23<K, V> add(Entry<K ,V> entry) {
        return put(entry.getKey(), entry.getValue());
    }

    @Override
    public SortedMap23<K, V> addAll(Iterable<Entry<K ,V>> items) {
        SortedMap23<K, V> m = this;
        for(Entry<K,V> e: items) {
            m = m.add(e);
        }
        return m;
    }

    @Override
    public SortedMap23<K, V> addAll(Map<K ,V> items) {
        return addAll(items.entrySet());
    }

    @Override
	public SortedMap23<K, V> put(final K key, final V value) {
        SortedMap23<K, V> m = remove(key);
        int index = m.keys().elements.naturalPosition(keyComparator, key);
        return new SortedMap23<>(keyComparator, m.entries.insertAt(index, new AbstractMap.SimpleImmutableEntry<>(key, value)));
	}
	
    public SortedMap23<K, V> ge(final K element) {
        return new SortedMap23<>(keyComparator, entries.tailAt(keys().elements.naturalPosition(keyComparator, element)));
    }

    public SortedMap23<K, V> lt(final K element) {
        return new SortedMap23<>(keyComparator, entries.headAt(keys().elements.naturalPosition(keyComparator, element)));
    }

    public SortedMap23<K, V> exclude(final K low, final K high) {
        int cmp = keyComparator.compare(low, high);
        if (cmp > 0) {
            throw new IllegalArgumentException("low must be <= high");
        }
        if (keyComparator.compare(low, high) == 0) {
            return this;
        }
        SortedSet23<K> keys = keys();
        return new SortedMap23<>(keyComparator, entries.removeRange(
                keys.elements.naturalPosition(keyComparator, low),
                keys.elements.naturalPosition(keyComparator, high)));
    }

    public SortedMap23<K, V> subSet(final K low, final K high) {
        int cmp = keyComparator.compare(low, high);
        if (cmp > 0) {
            throw new IllegalArgumentException("low must be <= high");
        }
        if (keyComparator.compare(low, high) == 0) {
            return new SortedMap23<>(keyComparator, List23.empty());
        }
        SortedSet23<K> keys = keys();
        return new SortedMap23<>(keyComparator, entries.getRange(
                keys.elements.naturalPosition(keyComparator, low),
                keys.elements.naturalPosition(keyComparator, high)));
    }

	
	public SortedMap23<K, V> reversed() {
		return new SortedMap23<K, V>(keyComparator.reversed(), entries.reversed());
	}
	
    public int indexOf(final K key) {
        return keys().indexOf(key);
    }
    
    @Override
    public boolean containsKey(final K key) {
        return keys().contains(key);
    }

    @Override
	public SortedMap23<K, V> remove(final K key) {
        int index = indexOf(key);
        return index < 0 ? this : removeAt(index);
	}
	
    @Override
    public SortedMap23<K, V> retainAll(final Set23<K> other) {
        return filter(e -> other.contains(e.getKey()));
    }

    @Override
    public SortedMap23<K, V> removeAll(final Iterable<K> keys) {
        SortedMap23<K, V> m = this;
        for(K key: keys) {
            m = m.remove(key);
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
        int index = indexOf(key);
        return index < 0 ? supplier.get() : entries.getAt(index).getValue();
    }
    
    public SortedMap23<K, V> removeAt(final int index) {
        return new SortedMap23<K, V>(keyComparator, entries.removeAt(index));
    }

    @Override
	public SortedMap<K, V> asMap() {
		return new SortedMap23Map<>(this);
	}
	
	public List23<Entry<K,V>> asList() {
		return entries;
	}
	
	public SortedSet23<Entry<K,V>> asSet() {
	    return new SortedSet23<>(this::entryCompare, entries);
	}
	
	public int entryCompare(final Entry<K,V> a, final Entry<K,V> b) {
	    int cmp = keyComparator.compare(a.getKey(), b.getKey());
	    if (cmp != 0) {
	        return cmp;
	    }
	    return HashSet23.compare(a.getValue(), b.getValue());
	}
	
	public SortedSet23<K> keys() {
	    return new SortedSet23<>(keyComparator, entries.map(e -> e.getKey()));
	}

    public List23<V> values() {
        return entries.map(e -> e.getValue());
    }
	
    public SortedMap23<K, V> filter(final Predicate<Entry<K,V>> filter) {
        return new SortedMap23<>(keyComparator, entries.filter(filter));
    }
    
    @Override
	public int hashCode() {
		return asMap().hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof SortedMap23)) {
			return false;
		}
		SortedMap23<?, ?> other = (SortedMap23<?, ?>)obj;
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
    
    public void forEach(BiConsumer<K, V> cond) {
        stream().forEach(e -> cond.accept(e.getKey(), e.getValue()));
    }
}