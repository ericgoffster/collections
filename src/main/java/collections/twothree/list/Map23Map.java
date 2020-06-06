package collections.twothree.list;

import java.util.AbstractMap;
import java.util.Set;

final class Map23Map<K, V> extends AbstractMap<K, V> {
	final Map23<K, V> set;

	public Map23Map(Map23<K, V> set) {
		super();
		this.set = set;
	}

    @Override
    public Set<Entry<K, V>> entrySet() {
        return set.asSet().asSet();
    }
    
    @Override
    public boolean containsKey(Object key) {
        try {
            @SuppressWarnings("unchecked")
            K k = (K)key;
            return set.containsKey(k);
        } catch (ClassCastException e) {
            return false;
        }
    }
    
    @Override
    public V get(Object key) {
        try {
            @SuppressWarnings("unchecked")
            K k = (K)key;
            return set.get(k);
        } catch (ClassCastException e) {
            return null;
        }
    }
}