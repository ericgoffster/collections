package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

public class SortedMap23MapTest {

    @Test
    public void testEmpty() {
        assertEquals(SortedMap23.empty().asMap(), Collections.emptyMap());
    }
    
    static <K,V> Entry<K, V> makeEntry(K k, V v) {
        return new AbstractMap.SimpleImmutableEntry<>(k, v);
    }
    
    @Test
    public void testFirstKey() {
        SortedMap<Integer, Integer> l = SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).asMap();
        assertEquals(l.firstKey().intValue(), 0);
        assertEquals(l.lastKey().intValue(),12);
        
        assertThrows(NoSuchElementException.class, () -> SortedMap23.empty().asMap().firstKey());
        assertThrows(NoSuchElementException.class, () -> SortedMap23.empty().asMap().lastKey());
    }

    @Test
    public void testContainsKey() {
        SortedMap<Integer, Integer> l = SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).asMap();
        assertTrue(l.containsKey(3));
        assertFalse(l.containsKey(4));
    }

    @Test
    public void testHeadSet() {
        SortedMap<Integer, Integer> l = SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).asMap();
        TreeMap<Integer, Integer> tm = new TreeMap<Integer, Integer>();
        tm.put(3, 2); tm.put(6, 3); tm.put(9, 4); tm.put(0, 1); assertEquals(l.headMap(12), tm);
    }

    @Test
    public void testTailSet() {
        SortedMap<Integer, Integer> l = SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).asMap();
        TreeMap<Integer, Integer> tm = new TreeMap<Integer, Integer>();
        tm.put(3, 2); tm.put(6, 3); tm.put(9, 4); tm.put(12, 5); assertEquals(l.tailMap(1), tm);
    }
    
    @Test
    public void testSubSet() {
        SortedMap<Integer, Integer> l = SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).asMap();
        TreeMap<Integer, Integer> tm = new TreeMap<Integer, Integer>();
        tm.put(3, 2); tm.put(6, 3); tm.put(9, 4); assertEquals(l.subMap(1, 12), tm);
    }

    @Test
    public void testReversed() {
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1,2).put(3, 4).reversed().asList(),List23.of(makeEntry(3,4), makeEntry(1,2)));
        assertEquals(SortedMap23.<Integer, Integer>empty().reversed().asList(),List23.empty());
     }
}
