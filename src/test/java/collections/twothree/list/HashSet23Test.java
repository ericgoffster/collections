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
        assertEquals(Set23.emptyHash().asSet(), Collections.emptySet());
        assertEquals(Set23.hashOf().asSet(), Collections.emptySet());
    }
    
	@Test
	public void testFilter() {
        assertEquals(Set23.hashOf(0, 3, 6, 9, 12).filter(e -> e > 1 && e < 7),Set23.of(3, 6));
	}
	
    @Test
    public void testRetainAll() {
        assertEquals(Set23.hashOf(0, 3, 6, 9, 12).intersection(Set23.of(6, 7, 9)),Set23.of(6, 9));
    }

    @Test
    public void testRemoveAll() {
        assertEquals(Set23.hashOf(0, 3, 6, 9, 12).subtraction(Set23.of(6, 7, 9)),Set23.of(0, 3, 12));
    }

	@Test
	public void testInsertions() {
		assertEquals(Set23.<Integer>hashOf().add(1),Set23.of(1));
        assertEquals(Set23.<Integer>hashOf(1, 3).add(2),Set23.of(1, 2, 3));
		assertEquals(Set23.<Integer>hashOf(1).add(2),Set23.of(1, 2));
		assertEquals(Set23.hashOf(0, 3, 6, 9, 12).add(3),Set23.of(0, 3, 6, 9, 12));
		assertEquals(Set23.hashOf(0, 3, 6, 9, 12).add(5),Set23.of(0, 3, 5, 6, 9, 12));
        assertEquals(Set23.hashOf(0, 3, 6, 9, 12).union(Set23.of(2, 4, 6)),Set23.of(0, 2, 3, 4, 6, 9, 12));
        assertEquals(Set23.<Integer>hashOf().union(Set23.of(2, 4, 6)),Set23.of(2, 4, 6));
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
        assertFalse(Set23.hashCompare(2, 3) == 0);
        assertTrue(Set23.hashCompare(2, 2) == 0);
        assertTrue(Set23.hashCompare(Set23.of(2, 3), Set23.of(3, 2)) == 0);
        assertFalse(Set23.hashCompare(Set23.of(2), List23.of(2)) == 0);
        assertTrue(Set23.hashCompare(new Foo(0, true), new Foo(0, true)) == 0);
        assertFalse(Set23.hashCompare(new Foo(0, false), new Foo(0, false)) == 0);
        assertFalse(Set23.hashCompare(new Foo(0, true), new Foo(1, true)) == 0);
    }
  
	@Test
	public void testContains() {
		assertFalse(Set23.<Integer>hashOf().contains(1));
		assertFalse(Set23.<Integer>hashOf(1).contains(2));
		assertTrue(Set23.<Integer>hashOf(1).contains(1));
		assertTrue(Set23.<Integer>hashOf(1, 2).contains(1));
		assertTrue(Set23.<Integer>hashOf(1, 2).contains(2));
		assertFalse(Set23.<Integer>hashOf(1, 2).contains(3));
        assertNotEquals(Set23.<Integer>hashOf(1, 2), Arrays.asList(1,2));
	}
	
	@Test
	public void testSize() {
		assertEquals(Set23.<Integer>hashOf().size(), 0);
		assertEquals(Set23.<Integer>hashOf(1).size(), 1);
		assertEquals(Set23.<Integer>hashOf(1, 2).size(), 2);
	}
	
	@Test
	public void testDeletions() {
		assertEquals(Set23.<Integer>hashOf().remove(1),Set23.of());
		assertEquals(Set23.<Integer>hashOf(2).remove(1),Set23.of(2));
		assertEquals(Set23.<Integer>hashOf(2, 3).remove(1),Set23.of(2, 3));
		assertEquals(Set23.<Integer>hashOf(2, 1).remove(1),Set23.of(2));
		assertEquals(Set23.<Integer>hashOf(1).remove(1),Set23.of());
	}
	
    @Test
    public void testStream() {
        Set23<Integer> l = Set23.hashOf(1,2,3,4,5,6);
        Set<Integer> t = new HashSet<>(Arrays.asList(1,2,3,4,5,6));
        l.stream().forEach(t::remove);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testIterator() {
        Set23<Integer> l = Set23.hashOf(1,2,3,4,5,6);
        Set<Integer> t = new HashSet<>(Arrays.asList(1,2,3,4,5,6));
        l.forEach(t::remove);
        assertTrue(t.isEmpty());
    }
	
	@Test
	public void testAsSet() {
		Set23<String> l1 = Set23.hashOf("1","2","3","4","5",null);
		assertEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", null)));
		assertEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5", null)));
		assertNotEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5")));
		assertTrue(l1.asSet().contains("5"));
		assertFalse(l1.asSet().contains("6"));
	}
}
