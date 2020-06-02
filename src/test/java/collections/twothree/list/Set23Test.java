package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class Set23Test {

	@Test
	public void testHeadSet() {
		Set23<Integer> l = Set23.of(0, 3, 6, 9, 12);
		assertEquals(l.headSet(13),Set23.of(0, 3, 6, 9, 12));
		assertEquals(l.headSet(12),Set23.of(0, 3, 6, 9));
		assertEquals(l.headSet(3),Set23.of(0));
		assertEquals(l.headSet(2),Set23.of(0));
		assertEquals(l.headSet(0),Set23.of());
	}

	@Test
	public void testTailSet() {
		Set23<Integer> l = Set23.of(0, 3, 6, 9, 12);
		assertEquals(l.tailSet(-1),Set23.of(0, 3, 6, 9, 12));
		assertEquals(l.tailSet(0),Set23.of(0, 3, 6, 9, 12));
		assertEquals(l.tailSet(1),Set23.of(3, 6, 9, 12));
		assertEquals(l.tailSet(11),Set23.of(12));
		assertEquals(l.tailSet(12),Set23.of(12));
		assertEquals(l.tailSet(13),Set23.of());
	}

	@Test
	public void testReverse() {
		Set23<Integer> l = Set23.of(0, 3, 6, 9, 12);
		assertTrue(l.reversed().contains(3));
		assertTrue(l.reversed().contains(6));
		assertTrue(l.reversed().contains(9));
		assertTrue(l.reversed().contains(12));
		assertEquals(l.reversed(), Set23.of(12, 9, 6, 3, 0));
		assertEquals(l.reversed().subSet(9, 3), Set23.of(9, 6));
	}
	
	@Test
	public void testSubSet() {
		Set23<Integer> l = Set23.of(0, 3, 6, 9, 12);
		assertEquals(l.subSet(-1, 13),Set23.of(0, 3, 6, 9, 12));
		assertEquals(l.subSet(0, 13),Set23.of(0, 3, 6, 9, 12));
		assertEquals(l.subSet(0, 12),Set23.of(0, 3, 6, 9));
		assertEquals(l.subSet(1, 12),Set23.of(3, 6, 9));
		assertEquals(l.subSet(1, 11),Set23.of(3, 6, 9));
		assertEquals(l.subSet(3, 11),Set23.of(3, 6, 9));
		assertEquals(l.subSet(4, 11),Set23.of(6, 9));
		assertEquals(l.subSet(4, 9),Set23.of(6));
		assertEquals(l.subSet(6, 9),Set23.of(6));
		assertEquals(l.subSet(6, 7),Set23.of(6));
		assertEquals(l.subSet(6, 6),Set23.of());
	}

	@Test
	public void testInsertions() {
		assertEquals(Set23.<Integer>of().add(1),Set23.of(1));
		assertEquals(Set23.<Integer>of(1).add(2),Set23.of(1, 2));
		assertEquals(Set23.of(0, 3, 6, 9, 12).add(3),Set23.of(0, 3, 6, 9, 12));
		assertEquals(Set23.of(0, 3, 6, 9, 12).add(5),Set23.of(0, 3, 5, 6, 9, 12));
	}


	@Test
	public void testContains() {
		assertFalse(Set23.<Integer>of().contains(1));
		assertFalse(Set23.<Integer>of(1).contains(2));
		assertTrue(Set23.<Integer>of(1).contains(1));
		assertTrue(Set23.<Integer>of(1, 2).contains(1));
		assertTrue(Set23.<Integer>of(1, 2).contains(2));
		assertFalse(Set23.<Integer>of(1, 2).contains(3));
	}
	
	@Test
	public void testSize() {
		assertEquals(Set23.<Integer>of().size(), 0);
		assertEquals(Set23.<Integer>of(1).size(), 1);
		assertEquals(Set23.<Integer>of(1, 2).size(), 2);
	}
	
	@Test
	public void testDeletions() {
		assertEquals(Set23.<Integer>of().remove(1),Set23.of());
		assertEquals(Set23.<Integer>of(2).remove(1),Set23.of(2));
		assertEquals(Set23.<Integer>of(2, 3).remove(1),Set23.of(2, 3));
		assertEquals(Set23.<Integer>of(2, 1).remove(1),Set23.of(2));
		assertEquals(Set23.<Integer>of(1).remove(1),Set23.of());
	}
	
	@Test
	public void testAsSet() {
		Set23<String> l1 = Set23.of("1","2","3","4","5",null);
		assertEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", null)));
		assertEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5", null)));
		assertNotEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5")));
		assertTrue(l1.asSet().contains("5"));
		assertFalse(l1.asSet().contains("6"));
	}
	
	@Test
	public void testBasic() {
		Set23<String> l1 = Set23.of("1","2","3","4","5",null);
		assertTrue(l1.contains(null));
		
		Set<Set23<String>> s = new HashSet<>();
		s.add(l1);
		assertTrue(s.contains(Set23.of("1","2","3","4","5",null)));
		assertTrue(s.contains(Set23.of("1","2","3","5","4",null)));
		assertFalse(s.contains(Set23.of("1","2","3","4",null)));
		assertFalse(s.contains(null));
		TreeSet<String> t = new TreeSet<>(List23::naturalCompare);
		t.addAll(Arrays.asList("1", "2", "3", "4", "5", null));
		assertEquals(l1.toString(), t.toString());
	}
}
