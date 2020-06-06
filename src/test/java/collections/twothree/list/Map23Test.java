package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

public class Map23Test {

    @Test
    public void testEmpty() {
        assertEquals(Map23.empty().asMap(), Collections.emptyMap());
    }
    
    static <K,V> Entry<K, V> makeEntry(K k, V v) {
        return new AbstractMap.SimpleImmutableEntry<>(k, v);
    }
    
	@Test
	public void testInsertions() {
		assertEquals(Map23.<Integer, Integer>empty().put(1,2).put(3, 4).asList(),List23.of(makeEntry(1,2), makeEntry(3, 4)));
        assertEquals(Map23.<Integer, Integer>empty().put(3, 4).put(1, 2).asList(),List23.of(makeEntry(1,2), makeEntry(3, 4)));
        assertNotEquals(Map23.<Integer, Integer>empty().put(3, 4).put(1, 3).asList(),List23.of(makeEntry(1,2), makeEntry(3, 4)));
        assertNotEquals(Map23.<Integer, Integer>empty().put(1, 3).add(makeEntry(3, 4)).asList(),List23.of(makeEntry(1,2), makeEntry(3, 4)));
        Map23<Integer,Integer>  m = Map23.<Integer, Integer>empty().put(5,6).put(7, 8);
        assertEquals(Map23.<Integer, Integer>empty().put(1, 3).addAll(m).asList(),List23.of(makeEntry(1,3), makeEntry(5, 6), makeEntry(7, 8)));
        assertEquals(Map23.<Integer, Integer>empty().put(1, 3).addAll(m.asMap()).asList(),List23.of(makeEntry(1,3), makeEntry(5, 6), makeEntry(7, 8)));
	}

    @Test
    public void testReversed() {
        assertEquals(Map23.<Integer, Integer>empty().put(1,2).put(3, 4).reversed().asList(),List23.of(makeEntry(3,4), makeEntry(1,2)));
        assertEquals(Map23.<Integer, Integer>empty().reversed().asList(),List23.empty());
     }

    @Test
    public void testfromMap() {
        TreeMap<Integer, Integer> ts2 = new TreeMap<>(Integer::compare);
        ts2.put(1, 2);
        ts2.put(3, 4);
        TreeMap<Integer, Integer> ts = new TreeMap<>();
        ts.put(1, 2);
        ts.put(3, 4);
        assertEquals(Map23.of(ts.entrySet()).asList(),List23.of(makeEntry(1,2), makeEntry(3,4)));
        assertEquals(Map23.<Integer, Integer>empty().addAll(ts.entrySet()).asList(),List23.of(makeEntry(1,2), makeEntry(3,4)));
        assertEquals(Map23.of((i,j) -> j.compareTo(i), ts).asList(),List23.of(makeEntry(3,4), makeEntry(1,2)));
        assertEquals(Map23.ofSorted(ts).asList(),List23.of(makeEntry(1,2), makeEntry(3,4)));
        assertEquals(Map23.ofSorted(ts2).asList(),List23.of(makeEntry(1,2), makeEntry(3,4)));
        assertEquals(Map23.<Integer, Integer>empty().reversed().asList(),List23.empty());
     }

	@Test
	public void testContains() {
		assertFalse(Map23.<Integer, Integer>empty().containsKey(1));
		assertFalse(Map23.<Integer, Integer>empty().put(1, 2).containsKey(2));
		assertTrue(Map23.<Integer, Integer>empty().put(1, 2).containsKey(1));
		assertTrue(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).containsKey(1));
		assertTrue(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).containsKey(3));
		assertFalse(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).containsKey(5));
        assertNotEquals(Map23.<Integer, Integer>empty().put(1,2).put(3, 4), List23.of(makeEntry(1,2), makeEntry(3, 4)));
	}
	
	@Test
	public void testSize() {
		assertEquals(Map23.<Integer, Integer>empty().size(), 0);
		assertEquals(Map23.<Integer, Integer>empty().put(1, 2).size(), 1);
		assertEquals(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).size(), 2);
	}
	
	@Test
	public void testDeletions() {
		assertEquals(Map23.<Integer, Integer>empty().remove(1),Map23.<Integer, Integer>empty());
        assertEquals(Map23.<Integer, Integer>empty().put(1,2).put(3, 4).remove(5),Map23.<Integer, Integer>empty().put(1,2).put(3, 4));
        assertEquals(Map23.<Integer, Integer>empty().put(1,2).put(3, 4).remove(1),Map23.<Integer, Integer>empty().put(3, 4));
	}
	
    @Test
    public void testGetAt() {
        assertEquals(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).getAt(0), makeEntry(1, 2));
        assertEquals(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).getAt(1), makeEntry(3, 4));
    }

    @Test
    public void testEntries() {
        assertTrue(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).asSet().asSet()
                .contains(makeEntry(1, 2)));
        assertFalse(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).asSet().asSet()
                .contains(makeEntry(1, 3)));
        assertFalse(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).asSet().asSet()
                .contains(makeEntry(2, 2)));
    }
    @Test
    public void testGet() {
        assertEquals(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).get(1).intValue(), 2);
        assertEquals(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).get(3).intValue(), 4);
        assertEquals(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).get(4), null);
    }

    @Test
    public void testKeys() {
        assertEquals(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).keys(), Set23.of(1, 3));
    }

    @Test
    public void testValues() {
        assertEquals(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).values(), List23.of(2, 4));
    }

    @Test
    public void testRemoveAt() {
        assertEquals(Map23.<Integer, Integer>empty().put(1, 2).put(3, 4).removeAt(0), Map23.<Integer, Integer>empty().put(3, 4));
    }
}
