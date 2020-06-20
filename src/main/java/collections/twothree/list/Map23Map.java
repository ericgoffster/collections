package collections.twothree.list;

import java.util.AbstractMap;
import java.util.Set;

final class Map23Map<K, V> extends AbstractMap<K, V> {
	final Map23<K, V> map;

	public Map23Map(Map23<K, V> set) {
		super();
		this.map = set;
	}

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.asSet().asSet();
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
}