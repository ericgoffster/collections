package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class List23Test {
	@Test
	public void testToCollection() {
        List23<Integer> l = of(1,2,3,4,5,6);
        assertEquals(new HashSet<Integer>(l.asCollection()), new HashSet<Integer>(Arrays.asList(1,2,3,4,5,6)));
	}
	
    @SafeVarargs
    @SuppressWarnings("varargs")
	private static <E> List23<E> of(E ... elements) {
	    return List23.of(Arrays.asList(elements));
	}
	
    @Test
    public void testMap() {
        assertEquals(of("1","2",null).map(x -> x == null ? null : Integer.parseInt(x)),of(1,2,null));
        assertEquals(of("1","2",null).map(x -> x == null ? null : Integer.parseInt(x)).indexOf(1),0);
        assertEquals(of("1","2",null).map(x -> x == null ? null : Integer.parseInt(x)).indexOf(2),1);
        assertEquals(of("1","2",null).map(x -> x == null ? null : Integer.parseInt(x)).indexOf(null),2);
        assertEquals(of("1","2",null).map(x -> x == null ? null : Integer.parseInt(x)).reversed(),of(null, 2, 1));
       
        assertEquals(of("1","2").map(x -> x == null ? null : Integer.parseInt(x)),of(1,2));
        assertEquals(of("1","2").map(x -> x == null ? null : Integer.parseInt(x)).indexOf(1),0);
        assertEquals(of("1","2").map(x -> x == null ? null : Integer.parseInt(x)).indexOf(2),1);
        assertEquals(of("1","2").map(x -> x == null ? null : Integer.parseInt(x)).reversed(),of(2, 1));
        
        assertEquals(of("1").map(x -> x == null ? null : Integer.parseInt(x)),of(1));
        assertEquals(of("1").map(x -> x == null ? null : Integer.parseInt(x)).indexOf(1),0);
        assertEquals(of("1").map(x -> x == null ? null : Integer.parseInt(x)).reversed(),of(1));
    }
  
	@Test
	public void testAppend() {
		for(int sz = 0; sz < 20; sz++) {
			List23<String> l = new List23<String>(null);
			List23<String> l4 = new List23<String>(null);
			for(int i = 0; i < sz; i++) {
				l = l.insertAt(i, String.valueOf(i));
				l4 = l4.insertAt(i, String.valueOf(i));
			}
			List23<String> l2 = new List23<String>(null);
			for(int i = sz; i < sz * 2; i++) {
				l2 = l2.insertAt(i - sz, String.valueOf(i));
				l4 = l4.insertAt(i, String.valueOf(i));
			}
			List23<String> l3 = l.appendList(l2);
			assertEquals(l3, l4);
		}
	}
	
    @Test
    public void testRetainAll() {
        assertEquals(of(0, 3, 6, 9, 12).retain(SortedSet23.singleton(6).add(7).add(9)),of(6, 9));
    }

    @Test
    public void testRemoveAll() {
        assertEquals(of(0, 3, 6, 9, 12).removeAllIn(SortedSet23.singleton(6).add(7).add(9)),of(0, 3, 12));
    }

	@Test
	public void testTail() {
		List23<String> l = new List23<String>(null);
		for(int i = 0; i < 31; i++) {
			l = l.insertAt(i, String.valueOf(i));
		}
		for(int i = 0; i <= 31; i++) {
			List23<String> l2 = l.tailAt(i);
			for(int j = 0; j < 31 - i; j++) {
				assertEquals(l2.getAt(j), String.valueOf(j + i));
			}
		}
	}

	@Test
	public void testHead() {
		List23<String> l = new List23<String>(null);
		for(int i = 0; i < 31; i++) {
			l = l.insertAt(i, String.valueOf(i));
		}
		for(int i = 0; i <= 31; i++) {
			List23<String> l2 = l.headAt(i);
			for(int j = 0; j < i; j++) {
				assertEquals(l2.getAt(j), String.valueOf(j));
			}
		}
	}

	@Test
	public void testSubList() {
		List23<String> l = new List23<String>(null);
		for(int i = 0; i < 31; i++) {
			l = l.insertAt(i, String.valueOf(i));
		}
		for(int i = 0; i <= 26; i++) {
			List23<String> l2 = l.getRange(i, i + 5);
			for(int j = 0; j < 5; j++) {
				assertEquals(l2.getAt(j), String.valueOf(i + j));
			}
		}
	}

    @Test
    public void testRemove() {
        List23<Integer> l = of(1,2,3,4,5,6);
        assertEquals(l.remove(2), of(1,3,4,5,6));
        assertEquals(l.remove(7), of(1,2,3,4,5,6));
    }

    @Test
    public void testExclude() {
        List23<Integer> l = of(1,2,3,4,5,6);
        assertEquals(l.removeRange(2,4), of(1,2,5,6));
        assertEquals(l.removeRange(0,4), of(5,6));
        assertEquals(l.removeRange(2, 6), of(1,2));
        assertEquals(l.removeRange(0, 6), List23.empty());
        assertEquals(l.removeRange(2, 2), l);
    }

    @Test
    public void testStream() {
        assertEquals(of(1,2,3,4,5,6).stream().collect(Collectors.toList()),Arrays.asList(1,2,3,4,5,6));
        assertEquals(List23.empty().stream().collect(Collectors.toList()),Arrays.asList());
    }

    @Test
    public void testIterator() {
        {
            List23<Integer> l = of(1,2,3,4);
            ListIterator<Integer> i = l.iterator();
            assertEquals(i.nextIndex(), 0);
            assertEquals(i.next().intValue(), 1);
            assertEquals(i.nextIndex(), 1);
            assertEquals(i.next().intValue(), 2);
            assertEquals(i.nextIndex(), 2);
            assertEquals(i.next().intValue(), 3);
            assertEquals(i.nextIndex(), 3);
            assertEquals(i.next().intValue(), 4);
            assertFalse(i.hasNext());
            assertEquals(i.previousIndex(), 3);
            assertEquals(i.previous().intValue(), 4);
            assertEquals(i.previousIndex(), 2);
            assertEquals(i.previous().intValue(), 3);
            assertEquals(i.previousIndex(), 1);
            assertEquals(i.previous().intValue(), 2);
            assertEquals(i.previousIndex(), 0);
            assertEquals(i.previous().intValue(), 1);
            assertFalse(i.hasPrevious());
        }
        {
            List<Integer> l = Arrays.asList(1,2,3,4);
            ListIterator<Integer> i = l.listIterator();
            assertEquals(i.nextIndex(), 0);
            assertEquals(i.next().intValue(), 1);
            assertEquals(i.nextIndex(), 1);
            assertEquals(i.next().intValue(), 2);
            assertEquals(i.nextIndex(), 2);
            assertEquals(i.next().intValue(), 3);
            assertEquals(i.nextIndex(), 3);
            assertEquals(i.next().intValue(), 4);
            assertFalse(i.hasNext());
            assertEquals(i.previousIndex(), 3);
            assertEquals(i.previous().intValue(), 4);
            assertEquals(i.previousIndex(), 2);
            assertEquals(i.previous().intValue(), 3);
            assertEquals(i.previousIndex(), 1);
            assertEquals(i.previous().intValue(), 2);
            assertEquals(i.previousIndex(), 0);
            assertEquals(i.previous().intValue(), 1);
            assertFalse(i.hasPrevious());
        }
    }

    @Test
    public void testInsertList() {
        List23<Integer> l = of(1,2,3,4,5,6);
        assertEquals(l.insertListAt(2, of(7,8)), of(1,2,7,8,3,4,5,6));
        assertEquals(l.insertListAt(0, of(7,8)), of(7,8,1,2,3,4,5,6));
        assertEquals(l.insertListAt(6, of(7,8)), of(1,2,3,4,5,6,7,8));
    }

    @Test
    public void testSet() {
        List23<Integer> l = of(1,2,3,4,5,6);
        assertEquals(l.setAt(2, 9), of(1,2,9,4,5,6));
        assertEquals(l.setAt(0, 9), of(9,2,3,4,5,6));
        assertEquals(l.setAt(5, 9), of(1,2,3,4,5,9));
    }

    @Test
    public void testOfSorted() {
        assertEquals(List23.ofSorted(Arrays.asList(4, 1, 5, 1 , 6, 2)), of(1, 1, 2, 4, 5, 6));
        assertEquals(List23.ofSorted(Arrays.asList(4)), of(4));
        assertEquals(List23.ofSorted(Arrays.asList()), List23.empty());
        assertEquals(List23.ofSorted(Arrays.asList(4, 1, null, 1 , 6, null)), of(null, null, 1, 1, 4, 6));
    }
    
    @Test
    public void testOfSortedUnique() {
        assertEquals(List23.ofSortedUnique(Arrays.asList(4, 1, 5, 1 , 6, 2)), of(1, 2, 4, 5, 6));
        assertEquals(List23.ofSortedUnique(Arrays.asList(4)), of(4));
        assertEquals(List23.ofSortedUnique(Arrays.asList()), List23.empty());
        assertEquals(List23.ofSortedUnique(Arrays.asList(4, 1, null, 1 , 6, null)), of(null, 1, 4, 6));
    }
    
    @Test
    public void testMakeBranch() {
        {
            Node23<Integer> l2 = new Branch<>(new Leaf<>(1), new Leaf<>(2));
            assertNotNull(l2.getBranch(0));
            assertNotNull(l2.getBranch(1));
        }
    }

    @Test
    public void testContains() {
        List23<Integer> l = of(1,2,3,1,5,1);
        assertTrue(l.contains(3));
        assertTrue(l.contains(1));
        assertFalse(l.contains(7));
    }

    @Test
    public void testIndexOf() {
        List23<Integer> l = of(1,2,3,1,5,1,8);
        assertEquals(l.indexOf(3), 2);
        assertEquals(l.indexOf(1), 0);
        assertEquals(l.indexOf(-1), -1);
        assertEquals(l.indexOf(7), -1);
        assertEquals(l.indexOf(9), -1);
        assertEquals(List23.empty().indexOf(7), -1);
    }

    @Test
    public void testLastIndexOf() {
        List23<Integer> l = of(1,2,3,1,5,1);
        assertEquals(l.lastIndexOf(3), 2);
        assertEquals(l.lastIndexOf(1), 5);
        assertEquals(l.lastIndexOf(7), -1);
        assertEquals(List23.empty().lastIndexOf(7), -1);
    }

    @Test
    public void testReplace() {
        List23<Integer> l = of(1,2,3,4,5,6);
        assertEquals(l.replaceRange(0,2, of(7,8)), of(7,8,3,4,5,6));
        assertEquals(l.replaceRange(1,4, of(7,8)), of(1,7,8,5,6));
        assertEquals(l.replaceRange(1,4, List23.empty()), of(1,5,6));
        assertEquals(l.replaceRange(1,6, of(7)), of(1,7));
        assertEquals(l.replaceRange(0,6, of(7)), of(7));
    }

	@Test
	public void testReversed() {
		List23<Integer> l = of(1,2,3,4,5,6);
		assertEquals(l.reversed(), of(6,5,4,3,2,1));
        assertEquals(l.reversed().reversed(), l);
		assertEquals(l.reversed().add(7), of(6,5,4,3,2,1,7));
		assertEquals(l.reversed().removeAt(0), of(5,4,3,2,1));
		assertEquals(l.reversed().removeAt(5), of(6,5,4,3,2));
		assertEquals(l.reversed().getRange(1, 3), of(5,4));
		assertEquals(l.reversed().getRange(0, 6), of(6,5,4,3,2,1));
        assertEquals(l.appendList(l.reversed()), of(1,2,3,4,5,6,6,5,4,3,2,1));
        assertEquals(l.reversed().appendList(l), of(6,5,4,3,2,1,1,2,3,4,5,6));
	}

	@Test
	public void testInsertions() {
		for(int sz = 0; sz < 20; sz++) {
			List23<String> l = new List23<String>(null);
			for(int i = 0; i < sz; i++) {
				l = l.insertAt(i, String.valueOf(i));
			}
			assertEquals(l.size(), sz);
			{
				for(int i = 0; i <= sz; i++) {
					List23<String> l2 = l.insertAt(i, "abc");
					assertEquals(l2.size(), sz + 1);
					for(int j = 0; j < i; j++) {
						assertEquals(l2.getAt(j), String.valueOf(j));
					}
					assertEquals(l2.getAt(i), "abc");
					for(int j = i + 1; j < l2.size(); j++) {
						assertEquals(l2.getAt(j), String.valueOf(j - 1));
					}
				}
			}
		}
	}
	
	private static List<String> makeList(List23<String> src) {
		return src.asCollection();
	}
	
	@Test
	public void testAsList() {
		List23<String> l1 = new List23<>(
				new Branch<>(branch("1","2","3"),branch("4","5")));
		List<String> l = l1.asCollection();
		assertEquals(l, Arrays.asList("1", "2", "3", "4", "5"));
	}

	@Test
	public void testToString() {
		List23<String> l1 = new List23<>(
				new Branch<>(branch("1","2","3"),branch("4","5")));
		assertEquals(l1.toString(), l1.asCollection().toString());
        assertEquals(branch("1","2","3").toString(), "[1 2 3]");
        assertEquals(branch("1","2","3").reverse().toString(), "[3 2 1]");
        assertEquals(branch("1","2").toString(), "[1 2]");
        assertEquals(branch("1","2").reverse().toString(), "[2 1]");
	}
	@Test
	public void testEquals() {
		{
			List23<String> l1 = new List23<>(
					new Branch<>(branch("1","2","3"),branch("4","5")));
			List23<String> l2 = new List23<>(
					new Branch<>(branch("1","2"),branch("3","4","5")));
			assertEquals(l1, l2);
			assertEquals(l1.hashCode(), l2.hashCode());
		}
		{
			List23<String> l1 = new List23<>(
					new Branch<>(branch("1","2","3"),branch("4","5")));
			List23<String> l2 = new List23<>(
					new Branch<>(branch("1","2"),branch("3","5","4")));
			assertNotEquals(l1, l2);
			assertNotEquals(l1.hashCode(), l2.hashCode());
		}
		{
			List23<String> l1 = new List23<>(
					new Branch<>(branch("1","2"),branch("3","4")));
			List23<String> l2 = new List23<>(
					new Branch<>(branch("1","2"),branch("3","4","5")));
			assertNotEquals(l1, l2);
			assertNotEquals(l1.hashCode(), l2.hashCode());
		}
		{
			List23<String> l1 = new List23<>(
					new Branch<>(branch("1","2"),branch("3","4")));
			assertNotEquals(l1, Arrays.asList("1","2","3","4"));
		}
	}
	
	private static Node23<String> branch(String a, String b) {
	    return new Branch<>(new Leaf<>(a),new Leaf<>(b));
	}
    private static Node23<String> branch(String a, String b, String c) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b),new Leaf<>(c));
    }

	@Test
	public void testDeletions2() {
		{
			List23<String> l = new List23<>(
			        branch("abc","def"));
			assertEquals(makeList(l.removeAt(0)), Arrays.asList("def"));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("abc"));
		}
		{
			List23<String> l = new List23<>(
			        branch("abc","def","ghi"));
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("def","ghi")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("abc","ghi"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("abc","def"));
		}
		{
			List23<String> l = new List23<>(
					new Branch<>(branch("1","2"),branch("3","4")));
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3"));
		}
		{
			List23<String> l = new List23<>(
					new Branch<>(branch("1","2","3"),branch("4","5")));
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4", "5"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4", "5"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3", "5"));
			assertEquals(makeList(l.removeAt(4)), Arrays.asList("1","2", "3", "4"));
		}
		{
			List23<String> l = new List23<>(
					new Branch<>(branch("1","2","3"),branch("4","5","6")));
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4", "5", "6"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4", "5", "6"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3", "5", "6"));
			assertEquals(makeList(l.removeAt(4)), Arrays.asList("1","2", "3", "4", "6"));
			assertEquals(makeList(l.removeAt(5)), Arrays.asList("1","2", "3", "4", "5"));
		}
		{
			List23<String> l = new List23<>(
					new Branch<>(branch("1","2"),branch("3","4"),branch("5","6")));
			assertEquals(makeList(l.removeAt(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6")));
			assertEquals(makeList(l.removeAt(1)), Arrays.asList("1","3", "4", "5", "6"));
			assertEquals(makeList(l.removeAt(2)), Arrays.asList("1","2", "4", "5", "6"));
			assertEquals(makeList(l.removeAt(3)), Arrays.asList("1","2", "3", "5", "6"));
			assertEquals(makeList(l.removeAt(4)), Arrays.asList("1","2", "3", "4", "6"));
			assertEquals(makeList(l.removeAt(5)), Arrays.asList("1","2", "3", "4", "5"));
		}
		{
			List23<String> l = new List23<>(
					new Branch<>(branch("1","2","3"),branch("4","5"),branch("6","7")));
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
					new Branch<>(branch("1","2","3"),branch("4","5","6"),branch("7","8")));
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
					new Branch<>(branch("1","2","3"),branch("4","5"),branch("6", "7","8")));
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
					new Branch<>(branch("1","2"),branch("3","4","5"),branch("6", "7","8")));
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
					new Branch<>(branch("1","2","3"),branch("4","5","6"),branch("7","8","9")));
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
			List23<String> l = new List23<String>(null);
			for(int i = 0; i < sz; i++) {
				l = l.insertAt(i, String.valueOf(i));
			}
			assertEquals(l.size(), sz);
			{
				for(int i = 0; i < sz; i++) {
					List23<String> l2 = l.removeAt(i);
					assertEquals(l2.size(), sz - 1);
					for(int j = 0; j < i; j++) {
						assertEquals(l2.getAt(j), String.valueOf(j));
					}
					for(int j = i; j < l2.size(); j++) {
						assertEquals(l2.getAt(j), String.valueOf(j + 1));
					}
				}
			}
		}
	}

    @Test
    public void testCompare() {
        assertTrue(SortedSet23.unNaturalCompare(null, null) == 0);
        assertTrue(SortedSet23.unNaturalCompare(2, null) > 0);
        assertTrue(SortedSet23.unNaturalCompare(null, 2) < 0);
        assertTrue(SortedSet23.unNaturalCompare(2, 3) < 0);
        assertTrue(SortedSet23.unNaturalCompare(3, 2) > 0);
        assertTrue(SortedSet23.unNaturalCompare(3, 3) == 00);
    }
    
    @Test
    public void makeSet() {
        Set<Integer> s = new HashSet<>();
        s.add(3);
        s.add(4);
        assertTrue(SortedSet23.unNaturalCompare(2, null) > 0);
        assertTrue(SortedSet23.unNaturalCompare(null, 2) < 0);
        assertTrue(SortedSet23.unNaturalCompare(2, 3) < 0);
        assertTrue(SortedSet23.unNaturalCompare(3, 2) > 0);
        assertTrue(SortedSet23.unNaturalCompare(3, 3) == 00);
    }
   
	@Test
	public void testErrors() {
		assertTrue(new Leaf<Integer>(5).isLeaf());
		assertEquals(new Leaf<Integer>(5).size(), 1);
		List<Integer> lnull = null;
        assertThrows(UnsupportedOperationException.class, () -> branch("1","2").leafValue());
        assertThrows(IllegalArgumentException.class, () -> List23.ofSorted(null,lnull));
        assertThrows(IllegalArgumentException.class, () -> List23.ofSorted(Integer::compare,lnull));
        assertThrows(IllegalArgumentException.class, () -> List23.ofSorted(lnull));
        assertThrows(IllegalArgumentException.class, () -> List23.of(lnull));
        assertThrows(IllegalArgumentException.class, () -> of(1, 2).appendList(null));
        assertThrows(IllegalArgumentException.class, () -> of(1, 2).insertListAt(0, null));
        assertThrows(IllegalArgumentException.class, () -> of(1, 2).replaceRange(0, 0, null));
        assertThrows(IndexOutOfBoundsException.class, () -> of(1, 2).headAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> of(1, 2).headAt(3));
        assertThrows(IndexOutOfBoundsException.class, () -> of(1, 2).tailAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> of(1, 2).tailAt(3));
        assertThrows(IndexOutOfBoundsException.class, () -> of(1, 2).getRange(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> of(1, 2).getRange(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> of(1, 2).getRange(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> of(1, 2).getRange(1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> of(1, 2).getRange(3, 0));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null).getAt(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null).removeAt(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null).insertAt(-1, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null).insertAt(1, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null).insertAt(0, "abc").insertAt(-1, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null).insertAt(0, "abc").insertAt(2, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null).insertAt(0, "abc").getAt(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null).insertAt(0, "abc").getAt(1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null).insertAt(0, "abc").removeAt(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null).insertAt(0, "abc").removeAt(1));
        assertThrows(UnsupportedOperationException.class, () -> new Leaf<String>("abc").getBranch(0));
        assertThrows(UnsupportedOperationException.class, () -> new Leaf<String>("abc").getBranch(1));
        assertThrows(UnsupportedOperationException.class, () -> new Leaf<String>("abc").getBranch(2));
	}

	@Test
	public void testNaturalPosition() {
        List23<String> l = new List23<>(new Branch<>(branch("1","2"),branch("3","4"),branch("5","6","7")));
        assertEquals(l.naturalPosition(String.valueOf("0")::compareTo), 0);
        assertEquals(l.naturalPosition(String.valueOf("1")::compareTo), 0);
        assertEquals(l.naturalPosition(String.valueOf("2")::compareTo), 1);
        assertEquals(l.naturalPosition(String.valueOf("3")::compareTo), 2);
        assertEquals(l.naturalPosition(String.valueOf("4")::compareTo), 3);
        assertEquals(l.naturalPosition(String.valueOf("5")::compareTo), 4);
        assertEquals(l.naturalPosition(String.valueOf("6")::compareTo), 5);
        assertEquals(l.naturalPosition(String.valueOf("7")::compareTo), 6);
        assertEquals(l.naturalPosition(String.valueOf("8")::compareTo), 7);
	}

    @Test
    public void testNaturalPositionReversed() {
        List23<String> l = new List23<>(new Branch<>(branch("1","2"),branch("3","4"),branch("5","6","7"))).reversed();
        assertEquals(l.naturalPosition(e -> -String.valueOf("0").compareTo(e)), 7);
        assertEquals(l.naturalPosition(e -> -String.valueOf("1").compareTo(e)), 6);
        assertEquals(l.naturalPosition(e -> -String.valueOf("2").compareTo(e)), 5);
        assertEquals(l.naturalPosition(e -> -String.valueOf("3").compareTo(e)), 4);
        assertEquals(l.naturalPosition(e -> -String.valueOf("4").compareTo(e)), 3);
        assertEquals(l.naturalPosition(e -> -String.valueOf("5").compareTo(e)), 2);
        assertEquals(l.naturalPosition(e -> -String.valueOf("6").compareTo(e)), 1);
        assertEquals(l.naturalPosition(e -> -String.valueOf("7").compareTo(e)), 0);
        assertEquals(l.naturalPosition(e -> -String.valueOf("8").compareTo(e)), 0);
    }
}
