package collections.twothree.list;

import java.util.AbstractMap;
import java.util.Set;

final class Map23Map<K, V> extends AbstractMap<K, V> {
	final SortedMap23<K, V> set;

	public Map23Map(SortedMap23<K, V> set) {
		super();
		this.set = set;
	}

    @Override
    public Set<Entry<K, V>> entrySet() {
        return set.asSet().asSet();
    }
    
    @Override
    public boolean containsKey(Object key) {
        @SuppressWarnings("unchecked")
        K k = (K)key;
        return set.containsKey(k);
    }
    
    @Override
    public V get(Object key) {
        @SuppressWarnings("unchecked")
        K k = (K)key;
        return set.get(k);
    }
}