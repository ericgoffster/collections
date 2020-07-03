package collections.twothree.list;

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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

public class SortedMap23Test {

    @Test
    public void testEmpty() {
        assertEquals(SortedMap23.empty().asMap(), Collections.emptyMap());
    }
    
    static <K,V> Entry<K, V> makeEntry(K k, V v) {
        return new AbstractMap.SimpleImmutableEntry<>(k, v);
    }
    
    @Test
    public void testComparator() {
        Comparator<? super Integer> keyComparator = Integer::compare;
        SortedMap<Integer, Integer> l = SortedMap23.of(keyComparator, Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).asMap();
        assertTrue(l.comparator() == keyComparator);
    }
    @Test
    public void testHeadSet() {
        SortedMap23<Integer,Integer> l = SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5)));
        assertEquals(l.lt(13),SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.lt(12),SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.lt(3),SortedMap23.of(Arrays.asList(makeEntry(0,1))));
        assertEquals(l.lt(2),SortedMap23.of(Arrays.asList(makeEntry(0,1))));
        assertEquals(l.lt(0),SortedMap23.of(Arrays.asList()));
    }

    @Test
    public void testTailSet() {
        SortedMap23<Integer,Integer> l = SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5)));
        assertEquals(l.ge(-1),SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.ge(0),SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.ge(1),SortedMap23.of(Arrays.asList(makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.ge(11),SortedMap23.of(Arrays.asList(makeEntry(12,5))));
        assertEquals(l.ge(12),SortedMap23.of(Arrays.asList(makeEntry(12,5))));
        assertEquals(l.ge(13),SortedMap23.of(Arrays.asList()));
    }
    
    @Test
    public void testSubSet() {
        SortedMap23<Integer,Integer> l = SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5)));
        assertEquals(l.subSet(-1, 13),SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.subSet(0, 13),SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))));
        assertEquals(l.subSet(0, 12),SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.subSet(1, 12),SortedMap23.of(Arrays.asList(makeEntry(3,2), makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.subSet(1, 11),SortedMap23.of(Arrays.asList(makeEntry(3,2), makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.subSet(3, 11),SortedMap23.of(Arrays.asList(makeEntry(3,2), makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.subSet(4, 11),SortedMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,4))));
        assertEquals(l.subSet(4, 9),SortedMap23.of(Arrays.asList(makeEntry(6,3))));
        assertEquals(l.subSet(6, 9),SortedMap23.of(Arrays.asList(makeEntry(6,3))));
        assertEquals(l.subSet(6, 7),SortedMap23.of(Arrays.asList(makeEntry(6,3))));
        assertEquals(l.subSet(6, 6),SortedMap23.of(Arrays.asList()));
        l = SortedMap23.empty();
        assertEquals(l.subSet(0, 0),SortedMap23.of(Arrays.asList()));
        assertEquals(SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).subSet(0, 0),SortedMap23.of(Arrays.asList()));
        assertThrows(IllegalArgumentException.class, () -> SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).subSet(1, 0));
    }
    @Test
    public void testExclude() {
        SortedMap23<Integer,Integer> l = SortedMap23.of(Arrays.asList(makeEntry(3,1),makeEntry(2,3),makeEntry(1,4),makeEntry(4,4),makeEntry(5,5),makeEntry(6,6)));
        assertEquals(l.exclude(2,4), SortedMap23.of(Arrays.asList(makeEntry(1,4),makeEntry(4,4),makeEntry(5,5),makeEntry(6,6))));
        assertEquals(l.exclude(2,8), SortedMap23.of(Arrays.asList(makeEntry(1,4))));
        assertEquals(l.exclude(1,8), SortedMap23.of(Arrays.asList()));
        assertEquals(l.exclude(-1,8), SortedMap23.of(Arrays.asList()));
        assertEquals(l.exclude(4, 4), SortedMap23.of(Arrays.asList(makeEntry(3,1),makeEntry(2,3),makeEntry(1,4),makeEntry(4,4),makeEntry(5,5),makeEntry(6,6))));
        assertThrows(IllegalArgumentException.class, () -> SortedMap23.of(Arrays.asList(makeEntry(3,1),makeEntry(2,3),makeEntry(1,4),makeEntry(4,4),makeEntry(5,5),makeEntry(6,6))).exclude(1, 0));
    }
    @Test
    public void testRemoveAll() {
        assertEquals(SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).removeAllKeysIn(SortedSet23.of(6, 7, 9)),
                SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(12,5))));
    }
    @Test
    public void testRetainAll() {
        assertEquals(SortedMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).retainAllKeys(SortedSet23.of(6, 7, 9)),
                SortedMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,4))));
    }

	@Test
	public void testInsertions() {
		assertEquals(SortedMap23.<Integer, Integer>empty().put(1,2).put(3, 4).asList(),List23.of(makeEntry(1,2), makeEntry(3, 4)));
        assertEquals(SortedMap23.<Integer, Integer>empty().put(3, 4).put(1, 2).asList(),List23.of(makeEntry(1,2), makeEntry(3, 4)));
        assertNotEquals(SortedMap23.<Integer, Integer>empty().put(3, 4).put(1, 3).asList(),List23.of(makeEntry(1,2), makeEntry(3, 4)));
        assertNotEquals(SortedMap23.<Integer, Integer>empty().put(1, 3).add(makeEntry(3, 4)).asList(),List23.of(makeEntry(1,2), makeEntry(3, 4)));
        SortedMap23<Integer,Integer>  m = SortedMap23.<Integer, Integer>empty().put(5,6).put(7, 8);
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 3).addAll(m).asList(),List23.of(makeEntry(1,3), makeEntry(5, 6), makeEntry(7, 8)));
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 3).addAll(m.asMap()).asList(),List23.of(makeEntry(1,3), makeEntry(5, 6), makeEntry(7, 8)));
	}

    @Test
    public void testReversed() {
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1,2).put(3, 4).reversed().asList(),List23.of(makeEntry(3,4), makeEntry(1,2)));
        assertEquals(SortedMap23.<Integer, Integer>empty().reversed().asList(),List23.empty());
     }

    @Test
    public void testfromMap() {
        TreeMap<Integer, Integer> ts2 = new TreeMap<>(Integer::compare);
        ts2.put(1, 2);
        ts2.put(3, 4);
        TreeMap<Integer, Integer> ts = new TreeMap<>();
        ts.put(1, 2);
        ts.put(3, 4);
        assertEquals(SortedMap23.of(ts.entrySet()).asList(),List23.of(makeEntry(1,2), makeEntry(3,4)));
        assertEquals(SortedMap23.<Integer, Integer>empty().addAll(ts.entrySet()).asList(),List23.of(makeEntry(1,2), makeEntry(3,4)));
        assertEquals(SortedMap23.of((i,j) -> j.compareTo(i), ts).asList(),List23.of(makeEntry(3,4), makeEntry(1,2)));
        assertEquals(SortedMap23.ofSorted(ts).asList(),List23.of(makeEntry(1,2), makeEntry(3,4)));
        assertEquals(SortedMap23.ofSorted(ts2).asList(),List23.of(makeEntry(1,2), makeEntry(3,4)));
        assertEquals(SortedMap23.<Integer, Integer>empty().reversed().asList(),List23.empty());
     }

	@Test
	public void testContains() {
		assertFalse(SortedMap23.<Integer, Integer>empty().containsKey(1));
		assertFalse(SortedMap23.<Integer, Integer>empty().put(1, 2).containsKey(2));
		assertTrue(SortedMap23.<Integer, Integer>empty().put(1, 2).containsKey(1));
		assertTrue(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).containsKey(1));
		assertTrue(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).containsKey(3));
		assertFalse(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).containsKey(5));
        assertNotEquals(SortedMap23.<Integer, Integer>empty().put(1,2).put(3, 4), List23.of(makeEntry(1,2), makeEntry(3, 4)));
	}
	
	@Test
	public void testSize() {
		assertEquals(SortedMap23.<Integer, Integer>empty().size(), 0);
		assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 2).size(), 1);
		assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).size(), 2);
	}
	
	@Test
	public void testDeletions() {
		assertEquals(SortedMap23.<Integer, Integer>empty().removeKey(1),SortedMap23.<Integer, Integer>empty());
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1,2).put(3, 4).removeKey(5),SortedMap23.<Integer, Integer>empty().put(1,2).put(3, 4));
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1,2).put(3, 4).removeKey(1),SortedMap23.<Integer, Integer>empty().put(3, 4));
	}
	
    @Test
    public void testGetAt() {
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).getAt(0), makeEntry(1, 2));
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).getAt(1), makeEntry(3, 4));
    }

    @Test
    public void testEntries() {
        assertTrue(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).asSet23()
                .contains(makeEntry(1, 2)));
        assertFalse(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).asSet23()
                .contains(makeEntry(1, 3)));
        assertFalse(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).asSet23()
                .contains(makeEntry(2, 2)));
    }
    @Test
    public void testGet() {
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).get(1).intValue(), 2);
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).get(3).intValue(), 4);
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).get(4), null);
    }

    @Test
    public void testKeys() {
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).keys(), SortedSet23.of(1, 3));
    }

    @Test
    public void testValues() {
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).values(), List23.of(2, 4));
    }

    @Test
    public void testRemoveAt() {
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).removeAt(0), SortedMap23.<Integer, Integer>empty().put(3, 4));
    }
    @Test
    public void testStream() {
        Map<Integer, Integer> m = new HashMap<>();
        SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).forEach((k, v) -> m.put(k, v));
        assertEquals(m, SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).asMap());
    }
    @Test
    public void testToString() {
        assertEquals(SortedMap23.<Integer, Integer>empty().put(1, 2).put(3, 4).toString(),"{1=2, 3=4}");
    }
    @Test
    public void testEquals() {
        Set<SortedMap23<Integer, Integer>> s = new HashSet<>();
        s.add(SortedMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,4))));
        assertTrue(s.contains(SortedMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,4)))));
        assertFalse(s.contains(SortedMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,5)))));
    }
}
