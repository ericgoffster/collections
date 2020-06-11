package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class HashSet23Test {

    @Test
    public void testEmpty() {
        assertEquals(HashSet23.empty().asSet(), Collections.emptySet());
        assertEquals(HashSet23.of().asSet(), Collections.emptySet());
    }
    
	@Test
	public void testFilter() {
        assertEquals(HashSet23.of(0, 3, 6, 9, 12).filter(e -> e > 1 && e < 7),HashSet23.of(3, 6));
	}
	
    @Test
    public void testRetainAll() {
        assertEquals(HashSet23.of(0, 3, 6, 9, 12).intersection(HashSet23.of(6, 7, 9)),HashSet23.of(6, 9));
    }

    @Test
    public void testRemoveAll() {
        assertEquals(HashSet23.of(0, 3, 6, 9, 12).subtraction(HashSet23.of(6, 7, 9)),HashSet23.of(0, 3, 12));
    }

	@Test
	public void testInsertions() {
		assertEquals(HashSet23.<Integer>of().add(1),HashSet23.of(1));
        assertEquals(HashSet23.<Integer>of(1, 3).add(2),HashSet23.of(1, 2, 3));
		assertEquals(HashSet23.<Integer>of(1).add(2),HashSet23.of(1, 2));
		assertEquals(HashSet23.of(0, 3, 6, 9, 12).add(3),HashSet23.of(0, 3, 6, 9, 12));
		assertEquals(HashSet23.of(0, 3, 6, 9, 12).add(5),HashSet23.of(0, 3, 5, 6, 9, 12));
        assertEquals(HashSet23.of(0, 3, 6, 9, 12).union(HashSet23.of(2, 4, 6)),HashSet23.of(0, 2, 3, 4, 6, 9, 12));
        assertEquals(HashSet23.<Integer>of().union(HashSet23.of(2, 4, 6)),HashSet23.of(2, 4, 6));
	}
	
	public static class Foo {
	    final int hc;
	    final boolean eq;
	    
	    public Foo(int hc, boolean eq) {
            super();
            this.hc = hc;
            this.eq = eq;
        }
        @Override
	    public int hashCode() {
	        return hc;
	    }
	    @Override
	    public boolean equals(Object obj) {
	        return eq;
	    }
	}


    @Test
    public void testHashCompare() {
        assertFalse(HashSet23.hashCompare(2, 3) == 0);
        assertTrue(HashSet23.hashCompare(2, 2) == 0);
        assertTrue(HashSet23.hashCompare(HashSet23.of(2, 3), HashSet23.of(3, 2)) == 0);
        assertFalse(HashSet23.hashCompare(HashSet23.of(2), List23.of(2)) == 0);
        assertTrue(HashSet23.hashCompare(new Foo(0, true), new Foo(0, true)) == 0);
        assertFalse(HashSet23.hashCompare(new Foo(0, false), new Foo(0, false)) == 0);
        assertFalse(HashSet23.hashCompare(new Foo(0, true), new Foo(1, true)) == 0);
    }
  
	@Test
	public void testContains() {
		assertFalse(HashSet23.<Integer>of().contains(1));
		assertFalse(HashSet23.<Integer>of(1).contains(2));
		assertTrue(HashSet23.<Integer>of(1).contains(1));
		assertTrue(HashSet23.<Integer>of(1, 2).contains(1));
		assertTrue(HashSet23.<Integer>of(1, 2).contains(2));
		assertFalse(HashSet23.<Integer>of(1, 2).contains(3));
        assertNotEquals(HashSet23.<Integer>of(1, 2), Arrays.asList(1,2));
	}
	
	@Test
	public void testSize() {
		assertEquals(HashSet23.<Integer>of().size(), 0);
		assertEquals(HashSet23.<Integer>of(1).size(), 1);
		assertEquals(HashSet23.<Integer>of(1, 2).size(), 2);
	}
	
	@Test
	public void testDeletions() {
		assertEquals(HashSet23.<Integer>of().remove(1),HashSet23.of());
		assertEquals(HashSet23.<Integer>of(2).remove(1),HashSet23.of(2));
		assertEquals(HashSet23.<Integer>of(2, 3).remove(1),HashSet23.of(2, 3));
		assertEquals(HashSet23.<Integer>of(2, 1).remove(1),HashSet23.of(2));
		assertEquals(HashSet23.<Integer>of(1).remove(1),HashSet23.of());
	}
	
    @Test
    public void testStream() {
        HashSet23<Integer> l = HashSet23.of(1,2,3,4,5,6);
        Set<Integer> t = new HashSet<>(Arrays.asList(1,2,3,4,5,6));
        l.stream().forEach(t::remove);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testIterator() {
        HashSet23<Integer> l = HashSet23.of(1,2,3,4,5,6);
        Set<Integer> t = new HashSet<>(Arrays.asList(1,2,3,4,5,6));
        l.forEach(t::remove);
        assertTrue(t.isEmpty());
    }
	
	@Test
	public void testAsSet() {
		HashSet23<String> l1 = HashSet23.of("1","2","3","4","5",null);
		assertEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", null)));
		assertEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5", null)));
		assertNotEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5")));
		assertTrue(l1.asSet().contains("5"));
		assertFalse(l1.asSet().contains("6"));
	}
}