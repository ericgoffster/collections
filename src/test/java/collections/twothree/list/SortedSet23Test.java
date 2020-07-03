package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.Test;

public class SortedSet23Test {

    @Test
    public void testOfSorted() {
        Comparator<Integer> comp = Integer::compare;
        assertEquals(SortedSet23.of(comp, Arrays.asList(4, 6, 5)).asList(), List23.of(4, 5, 6));
        assertEquals(SortedSet23.of(comp.reversed(), Arrays.asList(4, 6, 5)).asList(), List23.of(6, 5, 4));
        assertEquals(SortedSet23.of(SortedSet23.of(4, 6, 5)).asList(), List23.of(4, 5, 6));
        assertEquals(SortedSet23.ofSorted(SortedSet23.of(4, 6, 5).reversed().asCollection()).asList(), List23.of(6, 5, 4));
    }

	@Test
	public void testHeadSet() {
		SortedSet23<Integer> l = SortedSet23.of(0, 3, 6, 9, 12);
		assertEquals(l.lt(13),SortedSet23.of(0, 3, 6, 9, 12));
		assertEquals(l.lt(12),SortedSet23.of(0, 3, 6, 9));
		assertEquals(l.lt(3),SortedSet23.of(0));
		assertEquals(l.lt(2),SortedSet23.of(0));
		assertEquals(l.lt(0),SortedSet23.of());
	}

	@Test
	public void testTailSet() {
		SortedSet23<Integer> l = SortedSet23.of(0, 3, 6, 9, 12);
		assertEquals(l.ge(-1),SortedSet23.of(0, 3, 6, 9, 12));
		assertEquals(l.ge(0),SortedSet23.of(0, 3, 6, 9, 12));
		assertEquals(l.ge(1),SortedSet23.of(3, 6, 9, 12));
		assertEquals(l.ge(11),SortedSet23.of(12));
		assertEquals(l.ge(12),SortedSet23.of(12));
		assertEquals(l.ge(13),SortedSet23.of());
	}

	@Test
	public void testReverse() {
		SortedSet23<Integer> l = SortedSet23.of(0, 3, 6, 9, 12);
		assertTrue(l.reversed().contains(3));
		assertTrue(l.reversed().contains(6));
		assertTrue(l.reversed().contains(9));
		assertTrue(l.reversed().contains(12));
		assertEquals(l.reversed(), SortedSet23.of(12, 9, 6, 3, 0));
		assertEquals(l.reversed().subSet(9, 3), SortedSet23.of(9, 6));
		assertEquals(l.reversed().add(11), SortedSet23.of(12, 11, 9, 6, 3, 0));
        assertEquals(l.reversed().add(13), SortedSet23.of(13, 12, 9, 6, 3, 0));
        assertEquals(l.reversed().add(-1), SortedSet23.of(12, 9, 6, 3, 0, -1));
	}
	
	@Test
	public void testSubSet() {
		SortedSet23<Integer> l = SortedSet23.of(0, 3, 6, 9, 12);
		assertEquals(l.subSet(-1, 13),SortedSet23.of(0, 3, 6, 9, 12));
		assertEquals(l.subSet(0, 13),SortedSet23.of(0, 3, 6, 9, 12));
		assertEquals(l.subSet(0, 12),SortedSet23.of(0, 3, 6, 9));
		assertEquals(l.subSet(1, 12),SortedSet23.of(3, 6, 9));
		assertEquals(l.subSet(1, 11),SortedSet23.of(3, 6, 9));
		assertEquals(l.subSet(3, 11),SortedSet23.of(3, 6, 9));
		assertEquals(l.subSet(4, 11),SortedSet23.of(6, 9));
		assertEquals(l.subSet(4, 9),SortedSet23.of(6));
		assertEquals(l.subSet(6, 9),SortedSet23.of(6));
		assertEquals(l.subSet(6, 7),SortedSet23.of(6));
		assertEquals(l.subSet(6, 6),SortedSet23.of());
		l = SortedSet23.empty();
        assertEquals(l.subSet(0, 0),SortedSet23.of());
        assertEquals(SortedSet23.of(0, 3, 6, 9, 12).subSet(0, 0),SortedSet23.of());
        assertThrows(IllegalArgumentException.class, () -> SortedSet23.of(0, 3, 6, 9, 12).subSet(1, 0));
	}

	@Test
	public void testFilter() {
        assertEquals(SortedSet23.of(0, 3, 6, 9, 12).filter(e -> e > 1 && e < 7),SortedSet23.of(3, 6));
	}
	
    @Test
    public void testRetainAll() {
        assertEquals(SortedSet23.of(0, 3, 6, 9, 12).retain(SortedSet23.of(6, 7, 9)),SortedSet23.of(6, 9));
    }

    @Test
    public void testRemoveAll() {
        assertEquals(SortedSet23.of(0, 3, 6, 9, 12).removeAllIn(SortedSet23.of(6, 7, 9)),SortedSet23.of(0, 3, 12));
    }

	@Test
	public void testInsertions() {
		assertEquals(SortedSet23.<Integer>of().add(1),SortedSet23.of(1));
        assertEquals(SortedSet23.<Integer>of(1, 3).add(2),SortedSet23.of(1, 2, 3));
		assertEquals(SortedSet23.<Integer>of(1).add(2),SortedSet23.of(1, 2));
		assertEquals(SortedSet23.of(0, 3, 6, 9, 12).add(3),SortedSet23.of(0, 3, 6, 9, 12));
		assertEquals(SortedSet23.of(0, 3, 6, 9, 12).add(5),SortedSet23.of(0, 3, 5, 6, 9, 12));
        assertEquals(SortedSet23.of(0, 3, 6, 9, 12).union(SortedSet23.of(2, 4, 6)),SortedSet23.of(0, 2, 3, 4, 6, 9, 12));
        assertEquals(SortedSet23.<Integer>of().union(SortedSet23.of(2, 4, 6)),SortedSet23.of(2, 4, 6));
	}
	
	@Test
	public void testContains() {
		assertFalse(SortedSet23.<Integer>of().contains(1));
		assertFalse(SortedSet23.<Integer>of(1).contains(2));
		assertTrue(SortedSet23.<Integer>of(1).contains(1));
		assertTrue(SortedSet23.<Integer>of(1, 2).contains(1));
		assertTrue(SortedSet23.<Integer>of(1, 2).contains(2));
		assertFalse(SortedSet23.<Integer>of(1, 2).contains(3));
        assertNotEquals(SortedSet23.<Integer>of(1, 2), Arrays.asList(1,2));
	}
	
	@Test
	public void testSize() {
		assertEquals(SortedSet23.<Integer>of().size(), 0);
		assertEquals(SortedSet23.<Integer>of(1).size(), 1);
		assertEquals(SortedSet23.<Integer>of(1, 2).size(), 2);
	}
	
	@Test
	public void testDeletions() {
		assertEquals(SortedSet23.<Integer>of().remove(1),SortedSet23.of());
		assertEquals(SortedSet23.<Integer>of(2).remove(1),SortedSet23.of(2));
		assertEquals(SortedSet23.<Integer>of(2, 3).remove(1),SortedSet23.of(2, 3));
		assertEquals(SortedSet23.<Integer>of(2, 1).remove(1),SortedSet23.of(2));
		assertEquals(SortedSet23.<Integer>of(1).remove(1),SortedSet23.of());
	}
	
    @Test
    public void testStream() {
        SortedSet23<Integer> l = SortedSet23.of(1,2,3,4,5,6);
        Set<Integer> t = new HashSet<>(Arrays.asList(1,2,3,4,5,6));
        l.stream().forEach(t::remove);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testGetAt() {
        SortedSet23<Integer> l = SortedSet23.of(3,2,1,4,5,6);
        assertEquals(l.getAt(0).intValue(), 1);
        assertEquals(l.getAt(1).intValue(), 2);
        assertEquals(l.getAt(2).intValue(), 3);
    }

    @Test
    public void testRemoveAt() {
        SortedSet23<Integer> l = SortedSet23.of(3,2,1,4,5,6);
        assertEquals(l.removeAt(0), SortedSet23.of(2,3,4,5,6));
    }
    @Test
    public void testExclude() {
        SortedSet23<Integer> l = SortedSet23.of(3,2,1,4,5,6);
        assertEquals(l.exclude(2,4), SortedSet23.of(1,4,5,6));
        assertEquals(l.exclude(2,8), SortedSet23.of(1));
        assertEquals(l.exclude(1,8), SortedSet23.of());
        assertEquals(l.exclude(-1,8), SortedSet23.of());
        assertEquals(l.exclude(4, 4), SortedSet23.of(3,2,1,4,5,6));
        assertThrows(IllegalArgumentException.class, () -> SortedSet23.of(0, 3, 6, 9, 12).exclude(1, 0));
    }

    @Test
    public void testIterator() {
        SortedSet23<Integer> l = SortedSet23.of(1,2,3,4,5,6);
        assertEquals(l.stream().collect(Collectors.toList()), Arrays.asList(1,2,3,4,5,6));
    }
	
    @Test
    public void testAsCollection() {
        SortedSet23<Integer> l = SortedSet23.of(1,2,3,4,5,6);
        assertEquals(new HashSet<>(l.asCollection()), new HashSet<>(Arrays.asList(1,2,3,4,5,6)));
    }
    
	@Test
	public void testBasic() {
		SortedSet23<String> l1 = SortedSet23.of("1","2","3","4","5",null);
		assertTrue(l1.contains(null));
		
		Set<SortedSet23<String>> s = new HashSet<>();
		s.add(l1);
		assertTrue(s.contains(SortedSet23.of("1","2","3","4","5",null)));
		assertTrue(s.contains(SortedSet23.of("1","2","3","5","4",null)));
		assertFalse(s.contains(SortedSet23.of("1","2","3","4",null)));
		assertFalse(s.contains(null));
		{
    		TreeSet<String> t = new TreeSet<>(List23::naturalCompare);
    		t.addAll(Arrays.asList("1", "2", "3", "4", "5", null));
    		assertEquals(l1.toString(), t.toString());
		}
        {
            TreeSet<String> t = new TreeSet<>();
            t.addAll(Arrays.asList("1", "2", "3", "4", "5"));
            assertEquals(SortedSet23.of("1","2","3","4","5"), SortedSet23.ofSorted(t));
        }
		
		assertThrows(IllegalArgumentException.class, () -> new SortedSet23<>(null,null));
        assertThrows(IllegalArgumentException.class, () -> new SortedSet23<>(null,List23.empty()));
        assertThrows(IllegalArgumentException.class, () -> new SortedSet23<>(Integer::compare,null));
	}
}
