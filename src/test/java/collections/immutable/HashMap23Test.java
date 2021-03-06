package collections.immutable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

public class HashMap23Test {

    @Test
    public void testEmpty() {
        assertEquals(HashMap23.empty().asMap(), Collections.emptyMap());
    }
    
    static <K,V> Entry<K, V> makeEntry(K k, V v) {
        return new AbstractMap.SimpleImmutableEntry<>(k, v);
    }
    
    @Test
    public void testfromMap() {
        Map<Integer,Integer> m = new HashMap<Integer, Integer>();
        m.put(0, 1);
        m.put(3, 2);
        assertEquals(HashMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2))),
                HashMap23.of(m));
    }
    @Test
    public void testFilterKeys() {
        assertEquals(HashMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).filterKeys(k -> k < 6),
                HashMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2))));
    }
    @Test
    public void testRemoveAll() {
        assertEquals(HashMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).removeAllKeysIn(TreeSet23.singleton(6).add(7).add(9)),
                HashMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(12,5))));
    }
    @Test
    public void testRetainAll() {
        assertEquals(HashMap23.of(Arrays.asList(makeEntry(0,1), makeEntry(3,2), makeEntry(6,3), makeEntry(9,4), makeEntry(12,5))).retainAllKeys(TreeSet23.singleton(6).add(7).add(9)),
                HashMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,4))));
    }

	@Test
	public void testInsertions() {
		assertEquals(HashMap23.singleton(1,2).put(3, 4).asSet23(),HashSet23.singleton(makeEntry(1,2)).add(makeEntry(3, 4)));
        assertEquals(HashMap23.singleton(3, 4).put(1, 2).asSet23(),HashSet23.singleton(makeEntry(1,2)).add(makeEntry(3, 4)));
        assertNotEquals(HashMap23.singleton(3, 4).put(1, 3).asSet23(),HashSet23.singleton(makeEntry(1,2)).add(makeEntry(3, 4)));
        assertNotEquals(HashMap23.singleton(1, 3).put(3, 4).asSet23(),HashSet23.singleton(makeEntry(1,2)).add(makeEntry(3, 4)));
        HashMap23<Integer,Integer>  m = HashMap23.singleton(5,6).put(7, 8);
        assertEquals(HashMap23.singleton(1, 3).addAll(m).asSet23(),HashSet23.singleton(makeEntry(1,3)).add(makeEntry(5, 6)).add(makeEntry(7, 8)));
        assertEquals(HashMap23.singleton(1, 3).addAll(m.asMap()).asSet23(),HashSet23.singleton(makeEntry(1,3)).add(makeEntry(5, 6)).add(makeEntry(7, 8)));
	}

	@Test
	public void testContains() {
		assertFalse(HashMap23.empty().containsKey(1));
		assertFalse(HashMap23.singleton(1, 2).containsKey(2));
		assertTrue(HashMap23.singleton(1, 2).containsKey(1));
		assertTrue(HashMap23.singleton(1, 2).put(3, 4).containsKey(1));
		assertTrue(HashMap23.singleton(1, 2).put(3, 4).containsKey(3));
		assertFalse(HashMap23.singleton(1, 2).put(3, 4).containsKey(5));
        assertNotEquals(HashMap23.singleton(1,2).put(3, 4), TreeList23.singleton(makeEntry(1,2)).add(makeEntry(3, 4)));
	}
	
	@Test
	public void testSize() {
		assertEquals(HashMap23.empty().size(), 0);
		assertEquals(HashMap23.singleton(1, 2).size(), 1);
		assertEquals(HashMap23.singleton(1, 2).put(3, 4).size(), 2);
	}
	
	@Test
	public void testDeletions() {
		assertEquals(HashMap23.empty().removeKey(1),HashMap23.empty());
        assertEquals(HashMap23.singleton(1,2).put(3, 4).removeKey(5),HashMap23.singleton(1,2).put(3, 4));
        assertEquals(HashMap23.singleton(1,2).put(3, 4).removeKey(1),HashMap23.singleton(3, 4));
	}
	
    @Test
    public void testEntries() {
        assertTrue(HashMap23.singleton(1, 2).put(3, 4).asSet23()
                .contains(makeEntry(1, 2)));
        assertFalse(HashMap23.singleton(1, 2).put(3, 4).asSet23()
                .contains(makeEntry(1, 3)));
        assertFalse(HashMap23.singleton(1, 2).put(3, 4).asSet23()
                .contains(makeEntry(2, 2)));
    }
    @Test
    public void testGet() {
        assertEquals(HashMap23.singleton(1, 2).put(3, 4).get(1).intValue(), 2);
        assertEquals(HashMap23.singleton(1, 2).put(3, 4).get(3).intValue(), 4);
        assertEquals(HashMap23.singleton(1, 2).put(3, 4).get(4), null);
    }

    @Test
    public void testKeys() {
        assertEquals(HashMap23.singleton(1, 2).put(3, 4).keys(), TreeSet23.singleton(1).add(3));
    }

    @Test
    public void testValues() {
        assertEquals(HashMap23.singleton(1, 2).put(3, 4).values(), TreeList23.singleton(2).add(4));
    }

    @Test
    public void testStream() {
        Map<Integer, Integer> m = new HashMap<>();
        HashMap23.singleton(1, 2).put(3, 4).forEach((k, v) -> m.put(k, v));
        assertEquals(m, HashMap23.singleton(1, 2).put(3, 4).asMap());
    }
    @Test
    public void testToString() {
        assertEquals(HashMap23.singleton(1, 2).toString(),"{1=2}");
    }
    @Test
    public void testEquals() {
        Set<HashMap23<Integer, Integer>> s = new HashSet<>();
        s.add(HashMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,4))));
        assertTrue(s.contains(HashMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,4)))));
        assertFalse(s.contains(HashMap23.of(Arrays.asList(makeEntry(6,3), makeEntry(9,5)))));
    }
}
