package collections.immutable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class HashSet23Test {

    @SafeVarargs
    @SuppressWarnings("varargs")
    private static <E> HashSet23<E> of(E ... elements) {
        List<E> asList = Arrays.asList(elements);
        return HashSet23.of(asList);
    }

    @Test
    public void testEmpty() {
        assertEquals(HashSet23.empty().asCollection(), Collections.emptySet());
        assertEquals(HashSet23.empty().asCollection(), Collections.emptySet());
    }
    
	@Test
	public void testFilter() {
        assertEquals(of(0, 3, 6, 9, 12).filter(e -> e > 1 && e < 7),of(3, 6));
	}
	
    @Test
    public void testRetainAll() {
        assertEquals(of(0, 3, 6, 9, 12).retain(of(6, 7, 9)),of(6, 9));
    }

    @Test
    public void testRemoveAll() {
        assertEquals(of(0, 3, 6, 9, 12).removeAllIn(of(6, 7, 9)),of(0, 3, 12));
    }

	@Test
	public void testInsertions() {
		assertEquals(HashSet23.empty().add(1),of(1));
        assertEquals(of(1, 3).add(2),of(1, 2, 3));
		assertEquals(of(1).add(2),of(1, 2));
		assertEquals(of(0, 3, 6, 9, 12).add(3),of(0, 3, 6, 9, 12));
		assertEquals(of(0, 3, 6, 9, 12).add(5),of(0, 3, 5, 6, 9, 12));
        assertEquals(of(0, 3, 6, 9, 12).union(of(2, 4, 6)),of(0, 2, 3, 4, 6, 9, 12));
        assertEquals(HashSet23.empty().union(of(2, 4, 6)),of(2, 4, 6));
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
        assertFalse(HashSet23.compare(2, 3) == 0);
        assertTrue(HashSet23.compare(2, 2) == 0);
        assertTrue(HashSet23.compare(of(2, 3), of(3, 2)) == 0);
        assertFalse(HashSet23.compare(of(2), TreeList23.singleton(2)) == 0);
        assertTrue(HashSet23.compare(new Foo(0, true), new Foo(0, true)) == 0);
        assertFalse(HashSet23.compare(new Foo(0, false), new Foo(0, false)) == 0);
        assertFalse(HashSet23.compare(new Foo(0, true), new Foo(1, true)) == 0);
    }
  
	@Test
	public void testContains() {
		assertFalse(HashSet23.empty().contains(1));
		assertFalse(of(1).contains(2));
		assertTrue(of(1).contains(1));
		assertTrue(of(1, 2).contains(1));
		assertTrue(of(1, 2).contains(2));
		assertFalse(of(1, 2).contains(3));
        assertNotEquals(of(1, 2), Arrays.asList(1,2));
	}
	
	@Test
	public void testSize() {
		assertEquals(HashSet23.empty().size(), 0);
		assertEquals(HashSet23.singleton(1).size(), 1);
		assertEquals(of(1, 2).size(), 2);
	}
	
	@Test
	public void testDeletions() {
		assertEquals(HashSet23.empty().remove(1),HashSet23.empty());
		assertEquals(HashSet23.singleton(2).remove(1),of(2));
		assertEquals(of(2, 3).remove(1),of(2, 3));
		assertEquals(of(2, 1).remove(1),of(2));
		assertEquals(of(1).remove(1),HashSet23.empty());
	}
	
    @Test
    public void testStream() {
        HashSet23<Integer> l = of(1,2,3,4,5,6);
        Set<Integer> t = new HashSet<>(Arrays.asList(1,2,3,4,5,6));
        l.stream().forEach(t::remove);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testIterator() {
        HashSet23<Integer> l = of(1,2,3,4,5,6);
        Set<Integer> t = new HashSet<>(Arrays.asList(1,2,3,4,5,6));
        l.forEach(t::remove);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testAsCollection() {
        HashSet23<Integer> l = of(1,2,3,4,5,6);
        assertEquals(new HashSet<Integer>(l.asCollection()), new HashSet<Integer>(Arrays.asList(1,2,3,4,5,6)));
    }
    
	
	@Test
	public void testAsSet() {
		HashSet23<String> l1 = of("1","2","3","4","5",null);
		assertEquals(l1.asCollection(), new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", null)));
		assertEquals(l1.asCollection(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5", null)));
		assertNotEquals(l1.asCollection(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5")));
		assertTrue(l1.asCollection().contains("5"));
		assertFalse(l1.asCollection().contains("6"));
	}
    @Test
    public void testBasic() {
        HashSet23<String> l1 = of("1","2","3","4","5",null);
        assertTrue(l1.contains(null));
        
        Set<HashSet23<String>> s = new HashSet<>();
        s.add(l1);
        assertTrue(s.contains(of("1","2","3","4","5",null)));
        assertTrue(s.contains(of("1","2","3","5","4",null)));
        assertFalse(s.contains(of("1","2","3","4",null)));
        assertFalse(s.contains(null));
        {
            TreeSet<String> t = new TreeSet<>(TreeList23::naturalCompare);
            t.addAll(Arrays.asList("1"));
            assertEquals(of("1").toString(), t.toString());
        }
    }
}
