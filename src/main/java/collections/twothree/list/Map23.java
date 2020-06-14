package collections.twothree.list;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Map23<K,V> extends Iterable<Entry<K,V>> {
    int size();

    Map23<K, V> add(Entry<K ,V> entry);

    Map23<K, V> addAll(Iterable<Entry<K ,V>> items);

    Map23<K, V> addAll(Map<K ,V> items);

    Map23<K, V> put(final K key, final V value);
    
    boolean containsKey(final K key);

    Map23<K, V> removeKey(final K key);
    
    Map23<K, V> retainAllKeys(final Iterable<K> other);

    Map23<K, V> removeAllKeysIn(final Iterable<K> keys);
    
    V get(final K key);

    V getOrDefault(final K key, final Supplier<V> supplier);
    
    Map<K, V> asMap();
    
    Set23<Entry<K,V>> asSet();
    
    Set23<K> keys();

    Collection23<V> values();
    
    Map23<K, V> filter(final Predicate<Entry<K,V>> filter);
    
    Stream<Entry<K,V>> stream();
    
    void forEach(BiConsumer<K, V> cond);
}
