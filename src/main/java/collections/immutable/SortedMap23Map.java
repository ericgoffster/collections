package collections.immutable;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

final class SortedMap23Map<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {
	final TreeMap23<K, V> map;

	public SortedMap23Map(TreeMap23<K, V> set) {
		super();
		this.map = set;
	}

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.asSet23().asCollection();
    }
    
    @Override
    public boolean containsKey(Object key) {
        @SuppressWarnings("unchecked")
        K k = (K)key;
        return map.containsKey(k);
    }
    
    @Override
    public V get(Object key) {
        @SuppressWarnings("unchecked")
        K k = (K)key;
        return map.get(k);
    }

    @Override
    public Comparator<? super K> comparator() {
        return map.getKeyComparator();
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return map.subSet(fromKey, toKey).asMap();
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        return map.lt(toKey).asMap();
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        return map.ge(fromKey).asMap();
    }

    @Override
    public K firstKey() {
        if (map.size() == 0) {
            throw new NoSuchElementException();
        }
        return map.getAt(0).getKey();
    }

    @Override
    public K lastKey() {
        if (map.size() == 0) {
            throw new NoSuchElementException();
        }
        return map.getAt(map.size() - 1).getKey();
    }
}