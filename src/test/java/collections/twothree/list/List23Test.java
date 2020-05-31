package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class List23Test {
	
	private static String toString(List23<?> n) {
		if (n.root == null) {
			return "null";
		}
		return toString(n.root);
	}
	private static String toString(Node23<?> n) {
		if (n.b1() == null) {
			return String.valueOf(n.leafValue());
		}
		if (n.b2() == null) {
			return Arrays.asList(toString(n.b1())).toString();
		}
		if (n.b3() == null) {
			return Arrays.asList(toString(n.b1()),toString(n.b2())).toString();
		}
		return Arrays.asList(toString(n.b1()),toString(n.b2()),toString(n.b3())).toString();
	}

	@Test
	public void testConcat() {
		for(int sz = 0; sz < 20; sz++) {
			List23<String> l = new List23<String>(null, false);
			List23<String> l4 = new List23<String>(null, false);
			for(int i = 0; i < sz; i++) {
				l = l.insertAt(i, String.valueOf(i));
				l4 = l4.insertAt(i, String.valueOf(i));
			}
			List23<String> l2 = new List23<String>(null, false);
			for(int i = sz; i < sz * 2; i++) {
				l2 = l2.insertAt(i - sz, String.valueOf(i));
				l4 = l4.insertAt(i, String.valueOf(i));
			}
			List23<String> l3 = l.append(l2);
			assertTrue(l3.isValid());
			assertEquals(l3, l4);
		}
	}

	@Test
	public void testTail() {
		List23<String> l = new List23<String>(null, false);
		for(int i = 0; i < 31; i++) {
			l = l.insertAt(i, String.valueOf(i));
		}
		for(int i = 0; i <= 31; i++) {
			List23<String> l2 = l.tail(i);
            assertTrue(l2.isValid());
			for(int j = 0; j < 31 - i; j++) {
				assertEquals(l2.get(j), String.valueOf(j + i));
			}
		}
	}

	@Test
	public void testHead() {
		List23<String> l = new List23<String>(null, false);
		for(int i = 0; i < 31; i++) {
			l = l.insertAt(i, String.valueOf(i));
		}
		for(int i = 0; i <= 31; i++) {
			List23<String> l2 = l.head(i);
            assertTrue(l2.isValid());
			for(int j = 0; j < i; j++) {
				assertEquals(l2.get(j), String.valueOf(j));
			}
		}
	}

	@Test
	public void testSubList() {
		List23<String> l = new List23<String>(null, false);
		for(int i = 0; i < 31; i++) {
			l = l.insertAt(i, String.valueOf(i));
		}
		for(int i = 0; i <= 26; i++) {
			List23<String> l2 = l.subList(i, i + 5);
            assertTrue(l2.isValid());
			for(int j = 0; j < 5; j++) {
				assertEquals(l2.get(j), String.valueOf(i + j));
			}
		}
	}

    @Test
    public void testRemove() {
        List23<Integer> l = List23.of(1,2,3,4,5,6);
        assertEquals(l.remove(2), List23.of(1,3,4,5,6));
        assertEquals(l.remove(7), List23.of(1,2,3,4,5,6));
    }

    @Test
    public void testRemoveRange() {
        List23<Integer> l = List23.of(1,2,3,4,5,6);
        assertEquals(l.removeRange(2,4), List23.of(1,2,5,6));
        assertEquals(l.removeRange(0,4), List23.of(5,6));
        assertEquals(l.removeRange(2, 6), List23.of(1,2));
        assertEquals(l.removeRange(0, 6), List23.of());
    }

    @Test
    public void testStream() {
        List23<Integer> l = List23.of(1,2,3,4,5,6);
        List<Integer> t = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
        l.stream().forEach(t::remove);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testIterator() {
        List23<Integer> l = List23.of(1,2,3,4,5,6);
        List<Integer> t = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
        l.forEach(t::remove);
        assertTrue(t.isEmpty());
    }

    @Test
    public void testInsertList() {
        List23<Integer> l = List23.of(1,2,3,4,5,6);
        assertEquals(l.insertList(2, List23.of(7,8)), List23.of(1,2,7,8,3,4,5,6));
        assertEquals(l.insertList(0, List23.of(7,8)), List23.of(7,8,1,2,3,4,5,6));
        assertEquals(l.insertList(6, List23.of(7,8)), List23.of(1,2,3,4,5,6,7,8));
    }

    @Test
    public void testSet() {
        List23<Integer> l = List23.of(1,2,3,4,5,6);
        assertEquals(l.set(2, 9), List23.of(1,2,9,4,5,6));
        assertEquals(l.set(0, 9), List23.of(9,2,3,4,5,6));
        assertEquals(l.set(5, 9), List23.of(1,2,3,4,5,9));
    }

    @Test
    public void testOfSorted() {
        assertEquals(List23.ofSorted(4, 1, 5, 1 , 6, 2), List23.of(1, 1, 2, 4, 5, 6));
        assertEquals(List23.ofSorted(4), List23.of(4));
        assertEquals(List23.ofSorted(), List23.of());
        Comparator<Integer> comp = (i,j) -> i - j;
        assertEquals(List23.ofSorted(comp, 4, 1, 5, 1 , 6, 2), List23.of(1, 1, 2, 4, 5, 6));
        assertEquals(List23.ofSorted(4, 1, null, 1 , 6, null), List23.of(null, null, 1, 1, 4, 6));
    }
    
    @Test
    public void testMakeBranch() {
        List23<Integer> l = List23.of(1,2,3,4,5,6);
        {
            Node23<Integer> l2 = l.makeBranch(null, null);
            assertNull(l2);
        }
        {
            Node23<Integer> l2 = l.makeBranch(new Leaf<>(1), null);
            assertNotNull(l2.b1());
            assertNull(l2.b2());
        }
        {
            Node23<Integer> l2 = l.makeBranch(null, new Leaf<>(1));
            assertNotNull(l2.b1());
            assertNull(l2.b2());
        }
        {
            Node23<Integer> l2 = l.makeBranch(new Leaf<>(1), new Leaf<>(2));
            assertNotNull(l2.b1());
            assertNotNull(l2.b2());
        }
    }

    @Test
    public void testContains() {
        List23<Integer> l = List23.of(1,2,3,1,5,1);
        assertTrue(l.contains(3));
        assertTrue(l.contains(1));
        assertFalse(l.contains(7));
    }

    @Test
    public void testIndexOf() {
        List23<Integer> l = List23.of(1,2,3,1,5,1,8);
        assertEquals(l.indexOf(3), 2);
        assertEquals(l.indexOf(1), 0);
        assertEquals(l.indexOf(-1), -1);
        assertEquals(l.indexOf(7), -1);
        assertEquals(l.indexOf(9), -1);
        assertEquals(List23.of().indexOf(7), -1);
    }

    @Test
    public void testLastIndexOf() {
        List23<Integer> l = List23.of(1,2,3,1,5,1);
        assertEquals(l.lastIndexOf(3), 2);
        assertEquals(l.lastIndexOf(1), 5);
        assertEquals(l.lastIndexOf(7), -1);
        assertEquals(List23.of().lastIndexOf(7), -1);
    }

    @Test
    public void testReplace() {
        List23<Integer> l = List23.of(1,2,3,4,5,6);
        assertEquals(l.replace(0,2, List23.of(7,8)), List23.of(7,8,3,4,5,6));
        assertEquals(l.replace(1,4, List23.of(7,8)), List23.of(1,7,8,5,6));
        assertEquals(l.replace(1,4, List23.of()), List23.of(1,5,6));
        assertEquals(l.replace(1,6, List23.of(7)), List23.of(1,7));
        assertEquals(l.replace(0,6, List23.of(7)), List23.of(7));
    }

	@Test
	public void testReversed() {
		List23<Integer> l = List23.of(1,2,3,4,5,6);
		assertEquals(l.reverse(), List23.of(6,5,4,3,2,1));
        assertEquals(l.reverse().reverse(), l);
		assertEquals(l.reverse().add(7), List23.of(6,5,4,3,2,1,7));
		assertEquals(l.reverse().removeAt(0), List23.of(5,4,3,2,1));
		assertEquals(l.reverse().removeAt(5), List23.of(6,5,4,3,2));
		assertEquals(l.reverse().subList(1, 3), List23.of(5,4));
		assertEquals(l.reverse().subList(0, 6), List23.of(6,5,4,3,2,1));
	}

	@Test
	public void testInsertions() {
		for(int sz = 0; sz < 20; sz++) {
			List23<String> l = new List23<String>(null, false);
			for(int i = 0; i < sz; i++) {
				l = l.insertAt(i, String.valueOf(i));
	            assertTrue(l.isValid());
			}
			assertEquals(l.size(), sz);
			{
				for(int i = 0; i <= sz; i++) {
					List23<String> l2 = l.insertAt(i, "abc");
	                assertTrue(l2.isValid());
					assertEquals(l2.size(), sz + 1);
					for(int j = 0; j < i; j++) {
						assertEquals(l2.get(j), String.valueOf(j));
					}
					assertEquals(l2.get(i), "abc");
					for(int j = i + 1; j < l2.size(); j++) {
						assertEquals(l2.get(j), String.valueOf(j - 1));
					}
				}
			}
		}
	}
	
	private static List<String> makeList(List23<String> src) {
		return src.asList();
	}
	
	@Test
	public void testAsList() {
		List23<String> l1 = new List23<>(
				new Branch2<>(branch("1","2","3"),branch("4","5"))
				, false);
		List<String> l = l1.asList();
		assertEquals(l, Arrays.asList("1", "2", "3", "4", "5"));
	}

	@Test
	public void testToString() {
		List23<String> l1 = new List23<>(
				new Branch2<>(branch("1","2","3"),branch("4","5"))
				, false);
		assertEquals(l1.toString(), l1.asList().toString());
	}
	@Test
	public void testEquals() {
		{
			List23<String> l1 = new List23<>(
					new Branch2<>(branch("1","2","3"),branch("4","5"))
					, false);
			List23<String> l2 = new List23<>(
					new Branch2<>(branch("1","2"),branch("3","4","5"))
					, false);
			assertEquals(l1, l2);
			assertEquals(l1.hashCode(), l2.hashCode());
		}
		{
			List23<String> l1 = new List23<>(
					new Branch2<>(branch("1","2","3"),branch("4","5"))
					, false);
			List23<String> l2 = new List23<>(
					new Branch2<>(branch("1","2"),branch("3","5","4"))
					, false);
			assertNotEquals(l1, l2);
			assertNotEquals(l1.hashCode(), l2.hashCode());
		}
		{
			List23<String> l1 = new List23<>(
					new Branch2<>(branch("1","2"),branch("3","4"))
					, false);
			List23<String> l2 = new List23<>(
					new Branch2<>(branch("1","2"),branch("3","4","5"))
					, false);
			assertNotEquals(l1, l2);
			assertNotEquals(l1.hashCode(), l2.hashCode());
		}
		{
			List23<String> l1 = new List23<>(
					new Branch2<>(branch("1","2"),branch("3","4"))
					, false);
			assertNotEquals(l1, Arrays.asList("1","2","3","4"));
		}
	}
	
	private static Node23<String> branch(String a, String b) {
	    return new Branch2<>(new Leaf<>(a),new Leaf<>(b));
	}
    private static Node23<String> branch(String a, String b, String c) {
        return new Branch3<>(new Leaf<>(a),new Leaf<>(b),new Leaf<>(c));
    }

	@Test
	public void testDeletions2() {
		{
			List23<String> l = new List23<>(
			        branch("abc","def")
					, false);
			assertEquals(makeList(l.removeAt(0)), Arrays.asList("def"));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("abc"));
		}
		{
			List23<String> l = new List23<>(
			        branch("abc","def","ghi")
					, false);
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("def","ghi")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("abc","ghi"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("abc","def"));
		}
		{
			List23<String> l = new List23<>(
					new Branch2<>(branch("1","2"),branch("3","4"))
					, false);
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3"));
		}
		{
			List23<String> l = new List23<>(
					new Branch2<>(branch("1","2","3"),branch("4","5"))
					, false);
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4", "5"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4", "5"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3", "5"));
			assertEquals(makeList(l.removeAt(4)), Arrays.asList("1","2", "3", "4"));
		}
		{
			List23<String> l = new List23<>(
					new Branch2<>(branch("1","2","3"),branch("4","5","6"))
					, false);
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4", "5", "6"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4", "5", "6"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3", "5", "6"));
			assertEquals(makeList(l.removeAt(4)), Arrays.asList("1","2", "3", "4", "6"));
			assertEquals(makeList(l.removeAt(5)), Arrays.asList("1","2", "3", "4", "5"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(branch("1","2"),branch("3","4"),branch("5","6"))
					, false);
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4", "5", "6"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4", "5", "6"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3", "5", "6"));
			assertEquals(makeList(l.removeAt(4)), Arrays.asList("1","2", "3", "4", "6"));
			assertEquals(makeList(l.removeAt(5)), Arrays.asList("1","2", "3", "4", "5"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(branch("1","2","3"),branch("4","5"),branch("6","7"))
					, false);
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6", "7")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4", "5", "6", "7"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4", "5", "6", "7"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3", "5", "6", "7"));
			assertEquals(makeList(l.removeAt(4)), Arrays.asList("1","2", "3", "4", "6", "7"));
			assertEquals(makeList(l.removeAt(5)), Arrays.asList("1","2", "3", "4", "5", "7"));
			assertEquals(makeList(l.removeAt(6)), Arrays.asList("1","2", "3", "4", "5", "6"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(branch("1","2","3"),branch("4","5","6"),branch("7","8"))
					, false);
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6", "7", "8")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3", "5", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(4)), Arrays.asList("1","2", "3", "4", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(5)), Arrays.asList("1","2", "3", "4", "5", "7", "8"));
			assertEquals(makeList(l.removeAt(6)), Arrays.asList("1","2", "3", "4", "5", "6", "8"));
			assertEquals(makeList(l.removeAt(7)), Arrays.asList("1","2", "3", "4", "5", "6", "7"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(branch("1","2","3"),branch("4","5"),branch("6", "7","8"))
					, false);
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6", "7", "8")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3", "5", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(4)), Arrays.asList("1","2", "3", "4", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(5)), Arrays.asList("1","2", "3", "4", "5", "7", "8"));
			assertEquals(makeList(l.removeAt(6)), Arrays.asList("1","2", "3", "4", "5", "6", "8"));
			assertEquals(makeList(l.removeAt(7)), Arrays.asList("1","2", "3", "4", "5", "6", "7"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(branch("1","2"),branch("3","4","5"),branch("6", "7","8"))
					, false);
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6", "7", "8")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3", "5", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(4)), Arrays.asList("1","2", "3", "4", "6", "7", "8"));
			assertEquals(makeList(l.removeAt(5)), Arrays.asList("1","2", "3", "4", "5", "7", "8"));
			assertEquals(makeList(l.removeAt(6)), Arrays.asList("1","2", "3", "4", "5", "6", "8"));
			assertEquals(makeList(l.removeAt(7)), Arrays.asList("1","2", "3", "4", "5", "6", "7"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(branch("1","2","3"),branch("4","5","6"),branch("7","8","9"))
					, false);
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6", "7", "8", "9")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4", "5", "6", "7", "8", "9"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4", "5", "6", "7", "8", "9"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3", "5", "6", "7", "8", "9"));
			assertEquals(makeList(l.removeAt(4)), Arrays.asList("1","2", "3", "4", "6", "7", "8", "9"));
			assertEquals(makeList(l.removeAt(5)), Arrays.asList("1","2", "3", "4", "5", "7", "8", "9"));
			assertEquals(makeList(l.removeAt(6)), Arrays.asList("1","2", "3", "4", "5", "6", "8", "9"));
			assertEquals(makeList(l.removeAt(7)), Arrays.asList("1","2", "3", "4", "5", "6", "7", "9"));
			assertEquals(makeList(l.removeAt(8)), Arrays.asList("1","2", "3", "4", "5", "6", "7", "8"));
		}
	}
	
	@Test
	public void testDeletions() {
		for(int sz = 0; sz < 100; sz++) {
			List23<String> l = new List23<String>(null, false);
			for(int i = 0; i < sz; i++) {
				l = l.insertAt(i, String.valueOf(i));
                assertTrue(l.isValid());
			}
			assertEquals(l.size(), sz);
			{
				for(int i = 0; i < sz; i++) {
					List23<String> l2 = l.removeAt(i);
	                assertTrue(l2.isValid());
					assertEquals(l2.size(), sz - 1);
					for(int j = 0; j < i; j++) {
						assertEquals(l2.get(j), String.valueOf(j));
					}
					for(int j = i; j < l2.size(); j++) {
						assertEquals(l2.get(j), String.valueOf(j + 1));
					}
				}
			}
		}
	}

	@Test
	public void testErrors() {
		assertTrue(new Leaf<Integer>(5).isLeaf());
		assertEquals(new Leaf<Integer>(5).size(), 1);
        assertThrows(IndexOutOfBoundsException.class, () -> List23.of(1, 2).head(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> List23.of(1, 2).head(3));
        assertThrows(IndexOutOfBoundsException.class, () -> List23.of(1, 2).tail(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> List23.of(1, 2).tail(3));
        assertThrows(IndexOutOfBoundsException.class, () -> List23.of(1, 2).subList(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> List23.of(1, 2).subList(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> List23.of(1, 2).subList(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> List23.of(1, 2).subList(1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> List23.of(1, 2).subList(3, 0));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).removeAt(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insertAt(-1, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insertAt(1, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insertAt(0, "abc").insertAt(-1, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insertAt(0, "abc").insertAt(2, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insertAt(0, "abc").get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insertAt(0, "abc").get(1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insertAt(0, "abc").removeAt(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insertAt(0, "abc").removeAt(1));
	}
}
