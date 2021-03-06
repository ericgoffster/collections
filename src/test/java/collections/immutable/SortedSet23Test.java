package collections.immutable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.Test;

public class SortedSet23Test {

    @SafeVarargs
    @SuppressWarnings("varargs")
    private static <E extends Comparable<E>> ImmSortedSet<E> of(E ... elements) {
        List<E> asList = Arrays.asList(elements);
        return TreeSet23.of(asList);
    }
    

    @Test
    public void testOfSorted() {
        Comparator<Integer> comp = Integer::compare;
        assertEquals(TreeSet23.of(comp, Arrays.asList(4, 6, 5)).asList(), TreeList23.singleton(4).add(5).add(6));
        assertEquals(TreeSet23.of(comp.reversed(), Arrays.asList(4, 6, 5)).asList(), TreeList23.singleton(6).add(5).add(4));
        assertEquals(TreeSet23.of(of(4, 6, 5)).asList(), TreeList23.singleton(4).add(5).add(6));
        assertEquals(TreeSet23.of(of(4, 6, 5).reversed().asCollection()).asList(), TreeList23.singleton(6).add(5).add(4));
    }

	@Test
	public void testHeadSet() {
		ImmSortedSet<Integer> l = of(0, 3, 6, 9, 12);
		assertEquals(l.lt(13),of(0, 3, 6, 9, 12));
		assertEquals(l.lt(12),of(0, 3, 6, 9));
		assertEquals(l.lt(3),of(0));
		assertEquals(l.lt(2),of(0));
		assertEquals(l.lt(0),TreeSet23.empty());
	}

	@Test
	public void testTailSet() {
		ImmSortedSet<Integer> l = of(0, 3, 6, 9, 12);
		assertEquals(l.ge(-1),of(0, 3, 6, 9, 12));
		assertEquals(l.ge(0),of(0, 3, 6, 9, 12));
		assertEquals(l.ge(1),of(3, 6, 9, 12));
		assertEquals(l.ge(11),of(12));
		assertEquals(l.ge(12),of(12));
		assertEquals(l.ge(13),TreeSet23.empty());
	}

	@Test
	public void testReverse() {
		ImmSortedSet<Integer> l = of(0, 3, 6, 9, 12);
		assertTrue(l.reversed().contains(3));
		assertTrue(l.reversed().contains(6));
		assertTrue(l.reversed().contains(9));
		assertTrue(l.reversed().contains(12));
		assertEquals(l.reversed(), of(12, 9, 6, 3, 0));
		assertEquals(l.reversed().subSet(9, 3), of(9, 6));
		assertEquals(l.reversed().add(11), of(12, 11, 9, 6, 3, 0));
        assertEquals(l.reversed().add(13), of(13, 12, 9, 6, 3, 0));
        assertEquals(l.reversed().add(-1), of(12, 9, 6, 3, 0, -1));
	}
	
	@Test
	public void testSubSet() {
		ImmSortedSet<Integer> l = of(0, 3, 6, 9, 12);
		assertEquals(l.subSet(-1, 13),of(0, 3, 6, 9, 12));
		assertEquals(l.subSet(0, 13),of(0, 3, 6, 9, 12));
		assertEquals(l.subSet(0, 12),of(0, 3, 6, 9));
		assertEquals(l.subSet(1, 12),of(3, 6, 9));
		assertEquals(l.subSet(1, 11),of(3, 6, 9));
		assertEquals(l.subSet(3, 11),of(3, 6, 9));
		assertEquals(l.subSet(4, 11),of(6, 9));
		assertEquals(l.subSet(4, 9),of(6));
		assertEquals(l.subSet(6, 9),of(6));
		assertEquals(l.subSet(6, 7),of(6));
		assertEquals(l.subSet(6, 6),TreeSet23.empty());
		l = TreeSet23.empty();
        assertEquals(l.subSet(0, 0),TreeSet23.empty());
        assertEquals(of(0, 3, 6, 9, 12).subSet(0, 0),TreeSet23.empty());
        assertThrows(IllegalArgumentException.class, () -> of(0, 3, 6, 9, 12).subSet(1, 0));
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
		assertEquals(TreeSet23.<Integer>empty().add(1),of(1));
        assertEquals(of(1, 3).add(2),of(1, 2, 3));
		assertEquals(of(1).add(2),of(1, 2));
		assertEquals(of(0, 3, 6, 9, 12).add(3),of(0, 3, 6, 9, 12));
		assertEquals(of(0, 3, 6, 9, 12).add(5),of(0, 3, 5, 6, 9, 12));
        assertEquals(of(0, 3, 6, 9, 12).union(of(2, 4, 6)),of(0, 2, 3, 4, 6, 9, 12));
        assertEquals(TreeSet23.<Integer>empty().union(of(2, 4, 6)),of(2, 4, 6));
	}
	
	@Test
	public void testContains() {
		assertFalse(TreeSet23.<Integer>empty().contains(1));
		assertFalse(of(1).contains(2));
		assertTrue(TreeSet23.singleton(1).contains(1));
		assertTrue(of(1, 2).contains(1));
		assertTrue(of(1, 2).contains(2));
		assertFalse(of(1, 2).contains(3));
        assertNotEquals(of(1, 2), Arrays.asList(1,2));
	}
	
	@Test
	public void testSize() {
		assertEquals(TreeSet23.empty().size(), 0);
		assertEquals(of(1).size(), 1);
		assertEquals(of(1, 2).size(), 2);
	}
	
	@Test
	public void testDeletions() {
		assertEquals(TreeSet23.<Integer>empty().remove(1),TreeSet23.empty());
		assertEquals(of(2).remove(1),of(2));
		assertEquals(of(2, 3).remove(1),of(2, 3));
		assertEquals(of(2, 1).remove(1),of(2));
		assertEquals(of(1).remove(1),TreeSet23.empty());
	}
	
    @Test
    public void testStream() {
        ImmSortedSet<Integer> l = of(1,2,3,4,5,6);
        Set<Integer> t = new HashSet<>(Arrays.asList(1,2,3,4,5,6));
        l.stream().forEach(t::remove);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testExclude() {
        ImmSortedSet<Integer> l = of(3,2,1,4,5,6);
        assertEquals(l.exclude(2,4), of(1,4,5,6));
        assertEquals(l.exclude(2,8), of(1));
        assertEquals(l.exclude(1,8), TreeSet23.empty());
        assertEquals(l.exclude(-1,8), TreeSet23.empty());
        assertEquals(l.exclude(4, 4), of(3,2,1,4,5,6));
        assertThrows(IllegalArgumentException.class, () -> of(0, 3, 6, 9, 12).exclude(1, 0));
    }

    @Test
    public void testIterator() {
        ImmSortedSet<Integer> l = of(1,2,3,4,5,6);
        assertEquals(l.stream().collect(Collectors.toList()), Arrays.asList(1,2,3,4,5,6));
    }
	
    @Test
    public void testAsCollection() {
        ImmSortedSet<Integer> l = of(1,2,3,4,5,6);
        assertEquals(new HashSet<>(l.asCollection()), new HashSet<>(Arrays.asList(1,2,3,4,5,6)));
    }
    
	@Test
	public void testBasic() {
		ImmSortedSet<String> l1 = of("1","2","3","4","5",null);
		assertTrue(l1.contains(null));
		
		Set<ImmSortedSet<String>> s = new HashSet<>();
		s.add(l1);
		assertTrue(s.contains(of("1","2","3","4","5",null)));
		assertTrue(s.contains(of("1","2","3","5","4",null)));
		assertFalse(s.contains(of("1","2","3","4",null)));
		assertFalse(s.contains(null));
		{
    		TreeSet<String> t = new TreeSet<>(TreeList23::naturalCompare);
    		t.addAll(Arrays.asList("1", "2", "3", "4", "5", null));
    		assertEquals(l1.toString(), t.toString());
		}
        {
            TreeSet<String> t = new TreeSet<>();
            t.addAll(Arrays.asList("1", "2", "3", "4", "5"));
            assertEquals(of("1","2","3","4","5"), TreeSet23.of(t));
        }
	}
}
