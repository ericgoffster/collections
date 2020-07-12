package collections.immutable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

public class SortedMap23Test {
    
    <E> boolean same(Iterable<E> a, Iterable<E> b) {
        Iterator<E> i = a.iterator();
        Iterator<E> j = b.iterator();
        while(i.hasNext() && j.hasNext()) {
            E x = i.next();
            E y = j.next();
            if (!Objects.equals(x, y)) {
                return false;
            }
        }
        return !i.hasNext() && !j.hasNext();
    }

    @Test
    public void testEmpty() {
        assertEquals(TreeMap23.empty().asMap(), Collections.emptyMap());
    }
    
    static <K,V> Entry<K, V> makeEntry(K k, V v) {
        return new AbstractMap.SimpleImmutableEntry<>(k, v);
    }
    
    @Test
    public void testFilterKeys() {
        assertEquals(TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).filterKeys(k -> k < 6),
                HashMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2))));
    }
    @Test
    public void testComparator() {
        Comparator<? super Integer> keyComparator = Integer::compare;
        SortedMap<Integer, Integer> l = TreeMap23.of(keyComparator, Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).asMap();
        assertTrue(l.comparator() == keyComparator);
    }
    @Test
    public void testHeadSet() {
        ImmSortedMap<Integer,Integer> l = TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5)));
        assertEquals(l.lt(13),TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.lt(12),TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.lt(3),TreeMap23.of(Arrays.asList(makeEntry(0,1))));
        assertEquals(l.lt(2),TreeMap23.of(Arrays.asList(makeEntry(0,1))));
        assertEquals(l.lt(0),TreeMap23.of(Arrays.asList()));
    }

    @Test
    public void testTailSet() {
        ImmSortedMap<Integer,Integer> l = TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5)));
        assertEquals(l.ge(-1),TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.ge(0),TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.ge(1),TreeMap23.of(Arrays.asList(makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.ge(11),TreeMap23.of(Arrays.asList(makeEntry(12,5))));
        assertEquals(l.ge(12),TreeMap23.of(Arrays.asList(makeEntry(12,5))));
        assertEquals(l.ge(13),TreeMap23.of(Arrays.asList()));
    }
    
    @Test
    public void testSubSet() {
        ImmSortedMap<Integer,Integer> l = TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5)));
        assertEquals(l.subSet(-1, 13),TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.subSet(0, 13),TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.subSet(0, 12),TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.subSet(1, 12),TreeMap23.of(Arrays.asList(makeEntry(3,2), makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.subSet(1, 11),TreeMap23.of(Arrays.asList(makeEntry(3,2), makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.subSet(3, 11),TreeMap23.of(Arrays.asList(makeEntry(3,2), makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.subSet(4, 11),TreeMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.subSet(4, 9),TreeMap23.of(Arrays.asList(makeEntry(6,3))));
        assertEquals(l.subSet(6, 9),TreeMap23.of(Arrays.asList(makeEntry(6,3))));
        assertEquals(l.subSet(6, 7),TreeMap23.of(Arrays.asList(makeEntry(6,3))));
        assertEquals(l.subSet(6, 6),TreeMap23.of(Arrays.asList()));
        l = TreeMap23.empty();
        assertEquals(l.subSet(0, 0),TreeMap23.of(Arrays.asList()));
        assertEquals(TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).subSet(0, 0),TreeMap23.of(Arrays.asList()));
        assertThrows(IllegalArgumentException.class, () -> TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).subSet(1, 0));
    }
    @Test
    public void testExclude() {
        ImmSortedMap<Integer,Integer> l = TreeMap23.of(Arrays.asList(makeEntry(3,1),makeEntry(2,3),makeEntry(1,4),makeEntry(4,4),makeEntry(5,5),makeEntry(6,6)));
        assertEquals(l.exclude(2,4), TreeMap23.of(Arrays.asList(makeEntry(1,4),makeEntry(4,4),makeEntry(5,5),makeEntry(6,6))));
        assertEquals(l.exclude(2,8), TreeMap23.of(Arrays.asList(makeEntry(1,4))));
        assertEquals(l.exclude(1,8), TreeMap23.of(Arrays.asList()));
        assertEquals(l.exclude(-1,8), TreeMap23.of(Arrays.asList()));
        assertEquals(l.exclude(4, 4), TreeMap23.of(Arrays.asList(makeEntry(3,1),makeEntry(2,3),makeEntry(1,4),makeEntry(4,4),makeEntry(5,5),makeEntry(6,6))));
        assertThrows(IllegalArgumentException.class, () -> TreeMap23.of(Arrays.asList(makeEntry(3,1),makeEntry(2,3),makeEntry(1,4),makeEntry(4,4),makeEntry(5,5),makeEntry(6,6))).exclude(1, 0));
    }
    @Test
    public void testRemoveAll() {
        assertEquals(TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).removeAllKeysIn(TreeSet23.singleton(6).add(7).add(9)),
                TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(12,5))));
    }
    @Test
    public void testRetainAll() {
        assertEquals(TreeMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).retainAllKeys(TreeSet23.singleton(6).add(7).add(9)),
                TreeMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,4))));
    }

	@Test
	public void testInsertions() {
		assertTrue(same(TreeMap23.singleton(1,2).put(3, 4),TreeList23.singleton(makeEntry(1,2)).add(makeEntry(3, 4))));
		assertTrue(same(TreeMap23.singleton(3, 4).put(1, 2),TreeList23.singleton(makeEntry(1,2)).add(makeEntry(3, 4))));
        assertFalse(same(TreeMap23.singleton(3, 4).put(1, 3),TreeList23.singleton(makeEntry(1,2)).add(makeEntry(3, 4))));
        assertFalse(same(TreeMap23.singleton(1, 3).put(3, 4),TreeList23.singleton(makeEntry(1,2)).add(makeEntry(3, 4))));
        ImmSortedMap<Integer,Integer>  m = TreeMap23.singleton(5,6).put(7, 8);
        assertTrue(same(TreeMap23.singleton(1, 3).addAll(m),TreeList23.singleton(makeEntry(1,3)).add(makeEntry(5, 6)).add(makeEntry(7, 8))));
        assertTrue(same(TreeMap23.singleton(1, 3).addAll(m.asMap()),TreeList23.singleton(makeEntry(1,3)).add(makeEntry(5, 6)).add(makeEntry(7, 8))));
	}

    @Test
    public void testReversed() {
        assertTrue(same(TreeMap23.singleton(1,2).put(3, 4).reversed(),TreeList23.singleton(makeEntry(3,4)).add(makeEntry(1,2))));
        assertTrue(same(TreeMap23.empty().reversed(),TreeList23.empty()));
     }

    @Test
    public void testfromMap() {
        TreeMap<Integer, Integer> ts2 = new TreeMap<>(Integer::compare);
        ts2.put(1, 2);
        ts2.put(3, 4);
        TreeMap<Integer, Integer> ts = new TreeMap<>();
        ts.put(1, 2);
        ts.put(3, 4);
        assertTrue(same(TreeMap23.of(ts.entrySet()),TreeList23.singleton(makeEntry(1,2)).add(makeEntry(3,4))));
        assertTrue(same(TreeMap23.<Integer,Integer>empty().addAll(ts.entrySet()),TreeList23.singleton(makeEntry(1,2)).add(makeEntry(3,4))));
        assertTrue(same(TreeMap23.of((i,j) -> j.compareTo(i), ts),TreeList23.singleton(makeEntry(3,4)).add(makeEntry(1,2))));
        assertTrue(same(TreeMap23.of(ts),TreeList23.singleton(makeEntry(1,2)).add(makeEntry(3,4))));
        assertTrue(same(TreeMap23.of(ts2),TreeList23.singleton(makeEntry(1,2)).add(makeEntry(3,4))));
        assertTrue(same(TreeMap23.empty().reversed(),TreeList23.empty()));
     }

	@Test
	public void testContains() {
		assertFalse(TreeMap23.<Integer, Integer>empty().containsKey(1));
		assertFalse(TreeMap23.singleton(1, 2).containsKey(2));
		assertTrue(TreeMap23.singleton(1, 2).containsKey(1));
		assertTrue(TreeMap23.singleton(1, 2).put(3, 4).containsKey(1));
		assertTrue(TreeMap23.singleton(1, 2).put(3, 4).containsKey(3));
		assertFalse(TreeMap23.singleton(1, 2).put(3, 4).containsKey(5));
        assertNotEquals(TreeMap23.singleton(1,2).put(3, 4), TreeList23.singleton(makeEntry(1,2)).add(makeEntry(3, 4)));
	}
	
	@Test
	public void testSize() {
		assertEquals(TreeMap23.empty().size(), 0);
		assertEquals(TreeMap23.singleton(1, 2).size(), 1);
		assertEquals(TreeMap23.singleton(1, 2).put(3, 4).size(), 2);
	}
	
	@Test
	public void testDeletions() {
		assertEquals(TreeMap23.<Integer, Integer>empty().removeKey(1),TreeMap23.empty());
        assertEquals(TreeMap23.singleton(1,2).put(3, 4).removeKey(5),TreeMap23.singleton(1,2).put(3, 4));
        assertEquals(TreeMap23.singleton(1,2).put(3, 4).removeKey(1),TreeMap23.singleton(3, 4));
	}
	
    @Test
    public void testEntries() {
        assertTrue(TreeMap23.singleton(1, 2).put(3, 4).asSet23()
                .contains(makeEntry(1, 2)));
        assertFalse(TreeMap23.singleton(1, 2).put(3, 4).asSet23()
                .contains(makeEntry(1, 3)));
        assertFalse(TreeMap23.singleton(1, 2).put(3, 4).asSet23()
                .contains(makeEntry(2, 2)));
    }
    @Test
    public void testGet() {
        assertEquals(TreeMap23.singleton(1, 2).put(3, 4).get(1).intValue(), 2);
        assertEquals(TreeMap23.singleton(1, 2).put(3, 4).get(3).intValue(), 4);
        assertEquals(TreeMap23.singleton(1, 2).put(3, 4).get(4), null);
    }

    @Test
    public void testKeys() {
        assertEquals(TreeMap23.singleton(1, 2).put(3, 4).keys(), TreeSet23.singleton(1).add(3));
    }

    @Test
    public void testValues() {
        assertEquals(TreeMap23.singleton(1, 2).put(3, 4).values(), TreeList23.singleton(2).add(4));
    }

    @Test
    public void testStream() {
        Map<Integer, Integer> m = new HashMap<>();
        TreeMap23.singleton(1, 2).put(3, 4).forEach((k, v) -> m.put(k, v));
        assertEquals(m, TreeMap23.singleton(1, 2).put(3, 4).asMap());
    }
    @Test
    public void testToString() {
        assertEquals(TreeMap23.singleton(1, 2).put(3, 4).toString(),"{1=2, 3=4}");
    }
    @Test
    public void testEquals() {
        Set<ImmSortedMap<Integer, Integer>> s = new HashSet<>();
        s.add(TreeMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,4))));
        assertTrue(s.contains(TreeMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,4)))));
        assertFalse(s.contains(TreeMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,5)))));
    }
}
