package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class List23Test {
	
	private static void checkTree(List23<?> n) {
		if (n.root != null) {
			checkTree(n.root);
		}
	}
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
	private static int checkTree(Node23<?> n) {
		if (n.b1() == null) {
			return 1;
		}
		assertNotNull(n.b2());
		int d = checkTree(n.b1());
		int d2 = checkTree(n.b2());
		assertEquals(d, d2);
		if (n.b3() != null) {
			int d3 = checkTree(n.b3());
			assertEquals(d, d3);
		}
		return d + 1;
	}

	@Test
	public void testConcat() {
		for(int sz = 0; sz < 20; sz++) {
			List23<String> l = new List23<String>(null, false);
			List23<String> l4 = new List23<String>(null, false);
			for(int i = 0; i < sz; i++) {
				l = l.insert(i, String.valueOf(i));
				l4 = l4.insert(i, String.valueOf(i));
			}
			List23<String> l2 = new List23<String>(null, false);
			for(int i = sz; i < sz * 2; i++) {
				l2 = l2.insert(i - sz, String.valueOf(i));
				l4 = l4.insert(i, String.valueOf(i));
			}
			List23<String> l3 = l.append(l2);
			checkTree(l3);
			assertEquals(l3, l4);
		}
	}

	@Test
	public void testTail() {
		List23<String> l = new List23<String>(null, false);
		for(int i = 0; i < 31; i++) {
			l = l.insert(i, String.valueOf(i));
		}
		for(int i = 0; i <= 31; i++) {
			List23<String> l2 = l.tail(i);
			checkTree(l2);
			for(int j = 0; j < 31 - i; j++) {
				assertEquals(l2.get(j), String.valueOf(j + i));
			}
		}
	}

	@Test
	public void testHead() {
		List23<String> l = new List23<String>(null, false);
		for(int i = 0; i < 31; i++) {
			l = l.insert(i, String.valueOf(i));
		}
		for(int i = 0; i <= 31; i++) {
			List23<String> l2 = l.head(i);
			checkTree(l2);
			for(int j = 0; j < i; j++) {
				assertEquals(l2.get(j), String.valueOf(j));
			}
		}
	}

	@Test
	public void testSubList() {
		List23<String> l = new List23<String>(null, false);
		for(int i = 0; i < 31; i++) {
			l = l.insert(i, String.valueOf(i));
		}
		for(int i = 0; i <= 26; i++) {
			List23<String> l2 = l.subList(i, i + 5);
			checkTree(l2);
			for(int j = 0; j < 5; j++) {
				assertEquals(l2.get(j), String.valueOf(i + j));
			}
		}
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
    public void testInsertList() {
        List23<Integer> l = List23.of(1,2,3,4,5,6);
        assertEquals(l.insertList(2, List23.of(7,8)), List23.of(1,2,7,8,3,4,5,6));
        assertEquals(l.insertList(0, List23.of(7,8)), List23.of(7,8,1,2,3,4,5,6));
        assertEquals(l.insertList(6, List23.of(7,8)), List23.of(1,2,3,4,5,6,7,8));
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
		assertEquals(l.reverse().add(7), List23.of(6,5,4,3,2,1,7));
		assertEquals(l.reverse().remove(0), List23.of(5,4,3,2,1));
		assertEquals(l.reverse().remove(5), List23.of(6,5,4,3,2));
		assertEquals(l.reverse().subList(1, 3), List23.of(5,4));
		assertEquals(l.reverse().subList(0, 6), List23.of(6,5,4,3,2,1));
	}

	@Test
	public void testInsertions() {
		for(int sz = 0; sz < 20; sz++) {
			List23<String> l = new List23<String>(null, false);
			for(int i = 0; i < sz; i++) {
				l = l.insert(i, String.valueOf(i));
				checkTree(l);
			}
			assertEquals(l.size(), sz);
			{
				for(int i = 0; i <= sz; i++) {
					List23<String> l2 = l.insert(i, "abc");
					checkTree(l2);
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
				new Branch2<>(new Branch3<>(new Leaf<>("1"),new Leaf<>("2"),new Leaf<>("3")),new Branch2<>(new Leaf<>("4"),new Leaf<>("5")))
				, false);
		List<String> l = l1.asList();
		assertEquals(l, Arrays.asList("1", "2", "3", "4", "5"));
	}

	@Test
	public void testToString() {
		List23<String> l1 = new List23<>(
				new Branch2<>(new Branch3<>(new Leaf<>("1"),new Leaf<>("2"),new Leaf<>("3")),new Branch2<>(new Leaf<>("4"),new Leaf<>("5")))
				, false);
		assertEquals(l1.toString(), l1.asList().toString());
	}
	@Test
	public void testEquals() {
		{
			List23<String> l1 = new List23<>(
					new Branch2<>(new Branch3<>(new Leaf<>("1"),new Leaf<>("2"),new Leaf<>("3")),new Branch2<>(new Leaf<>("4"),new Leaf<>("5")))
					, false);
			List23<String> l2 = new List23<>(
					new Branch2<>(new Branch2<>(new Leaf<>("1"),new Leaf<>("2")),new Branch3<>(new Leaf<>("3"),new Leaf<>("4"),new Leaf<>("5")))
					, false);
			assertEquals(l1, l2);
			assertEquals(l1.hashCode(), l2.hashCode());
		}
		{
			List23<String> l1 = new List23<>(
					new Branch2<>(new Branch3<>(new Leaf<>("1"),new Leaf<>("2"),new Leaf<>("3")),new Branch2<>(new Leaf<>("4"),new Leaf<>("5")))
					, false);
			List23<String> l2 = new List23<>(
					new Branch2<>(new Branch2<>(new Leaf<>("1"),new Leaf<>("2")),new Branch3<>(new Leaf<>("3"),new Leaf<>("5"),new Leaf<>("4")))
					, false);
			assertNotEquals(l1, l2);
			assertNotEquals(l1.hashCode(), l2.hashCode());
		}
		{
			List23<String> l1 = new List23<>(
					new Branch2<>(new Branch2<>(new Leaf<>("1"),new Leaf<>("2")),new Branch2<>(new Leaf<>("3"),new Leaf<>("4")))
					, false);
			List23<String> l2 = new List23<>(
					new Branch2<>(new Branch2<>(new Leaf<>("1"),new Leaf<>("2")),new Branch3<>(new Leaf<>("3"),new Leaf<>("4"),new Leaf<>("5")))
					, false);
			assertNotEquals(l1, l2);
			assertNotEquals(l1.hashCode(), l2.hashCode());
		}
		{
			List23<String> l1 = new List23<>(
					new Branch2<>(new Branch2<>(new Leaf<>("1"),new Leaf<>("2")),new Branch2<>(new Leaf<>("3"),new Leaf<>("4")))
					, false);
			assertNotEquals(l1, Arrays.asList("1","2","3","4"));
		}
	}

	@Test
	public void testDeletions2() {
		{
			List23<String> l = new List23<>(
					new Branch2<>(new Leaf<>("abc"),new Leaf<>("def"))
					, false);
			assertEquals(makeList(l.remove(0)), Arrays.asList("def"));
			assertEquals(makeList(l.remove(1)), Arrays.asList("abc"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(new Leaf<>("abc"),new Leaf<>("def"),new Leaf<>("ghi"))
					, false);
			assertEquals(makeList(l.remove(0)), new ArrayList<>(Arrays.asList("def","ghi")));
			assertEquals(makeList(l.remove(1)), Arrays.asList("abc","ghi"));
			assertEquals(makeList(l.remove(2)), Arrays.asList("abc","def"));
		}
		{
			List23<String> l = new List23<>(
					new Branch2<>(new Branch2<>(new Leaf<>("1"),new Leaf<>("2")),new Branch2<>(new Leaf<>("3"),new Leaf<>("4")))
					, false);
			assertEquals(makeList(l.remove(0)), new ArrayList<>(Arrays.asList("2","3", "4")));
			assertEquals(makeList(l.remove(1)), Arrays.asList("1","3", "4"));
			assertEquals(makeList(l.remove(2)), Arrays.asList("1","2", "4"));
			assertEquals(makeList(l.remove(3)), Arrays.asList("1","2", "3"));
		}
		{
			List23<String> l = new List23<>(
					new Branch2<>(new Branch3<>(new Leaf<>("1"),new Leaf<>("2"),new Leaf<>("3")),new Branch2<>(new Leaf<>("4"),new Leaf<>("5")))
					, false);
			assertEquals(makeList(l.remove(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5")));
			assertEquals(makeList(l.remove(1)), Arrays.asList("1","3", "4", "5"));
			assertEquals(makeList(l.remove(2)), Arrays.asList("1","2", "4", "5"));
			assertEquals(makeList(l.remove(3)), Arrays.asList("1","2", "3", "5"));
			assertEquals(makeList(l.remove(4)), Arrays.asList("1","2", "3", "4"));
		}
		{
			List23<String> l = new List23<>(
					new Branch2<>(new Branch3<>(new Leaf<>("1"),new Leaf<>("2"),new Leaf<>("3")),new Branch3<>(new Leaf<>("4"),new Leaf<>("5"),new Leaf<>("6")))
					, false);
			assertEquals(makeList(l.remove(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6")));
			assertEquals(makeList(l.remove(1)), Arrays.asList("1","3", "4", "5", "6"));
			assertEquals(makeList(l.remove(2)), Arrays.asList("1","2", "4", "5", "6"));
			assertEquals(makeList(l.remove(3)), Arrays.asList("1","2", "3", "5", "6"));
			assertEquals(makeList(l.remove(4)), Arrays.asList("1","2", "3", "4", "6"));
			assertEquals(makeList(l.remove(5)), Arrays.asList("1","2", "3", "4", "5"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(new Branch2<>(new Leaf<>("1"),new Leaf<>("2")),new Branch2<>(new Leaf<>("3"),new Leaf<>("4")),new Branch2<>(new Leaf<>("5"),new Leaf<>("6")))
					, false);
			assertEquals(makeList(l.remove(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6")));
			assertEquals(makeList(l.remove(1)), Arrays.asList("1","3", "4", "5", "6"));
			assertEquals(makeList(l.remove(2)), Arrays.asList("1","2", "4", "5", "6"));
			assertEquals(makeList(l.remove(3)), Arrays.asList("1","2", "3", "5", "6"));
			assertEquals(makeList(l.remove(4)), Arrays.asList("1","2", "3", "4", "6"));
			assertEquals(makeList(l.remove(5)), Arrays.asList("1","2", "3", "4", "5"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(new Branch3<>(new Leaf<>("1"),new Leaf<>("2"),new Leaf<>("3")),new Branch2<>(new Leaf<>("4"),new Leaf<>("5")),new Branch2<>(new Leaf<>("6"),new Leaf<>("7")))
					, false);
			assertEquals(makeList(l.remove(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6", "7")));
			assertEquals(makeList(l.remove(1)), Arrays.asList("1","3", "4", "5", "6", "7"));
			assertEquals(makeList(l.remove(2)), Arrays.asList("1","2", "4", "5", "6", "7"));
			assertEquals(makeList(l.remove(3)), Arrays.asList("1","2", "3", "5", "6", "7"));
			assertEquals(makeList(l.remove(4)), Arrays.asList("1","2", "3", "4", "6", "7"));
			assertEquals(makeList(l.remove(5)), Arrays.asList("1","2", "3", "4", "5", "7"));
			assertEquals(makeList(l.remove(6)), Arrays.asList("1","2", "3", "4", "5", "6"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(new Branch3<>(new Leaf<>("1"),new Leaf<>("2"),new Leaf<>("3")),new Branch3<>(new Leaf<>("4"),new Leaf<>("5"),new Leaf<>("6")),new Branch2<>(new Leaf<>("7"),new Leaf<>("8")))
					, false);
			assertEquals(makeList(l.remove(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6", "7", "8")));
			assertEquals(makeList(l.remove(1)), Arrays.asList("1","3", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.remove(2)), Arrays.asList("1","2", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.remove(3)), Arrays.asList("1","2", "3", "5", "6", "7", "8"));
			assertEquals(makeList(l.remove(4)), Arrays.asList("1","2", "3", "4", "6", "7", "8"));
			assertEquals(makeList(l.remove(5)), Arrays.asList("1","2", "3", "4", "5", "7", "8"));
			assertEquals(makeList(l.remove(6)), Arrays.asList("1","2", "3", "4", "5", "6", "8"));
			assertEquals(makeList(l.remove(7)), Arrays.asList("1","2", "3", "4", "5", "6", "7"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(new Branch3<>(new Leaf<>("1"),new Leaf<>("2"),new Leaf<>("3")),new Branch2<>(new Leaf<>("4"),new Leaf<>("5")),new Branch3<>(new Leaf<>("6"), new Leaf<>("7"),new Leaf<>("8")))
					, false);
			assertEquals(makeList(l.remove(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6", "7", "8")));
			assertEquals(makeList(l.remove(1)), Arrays.asList("1","3", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.remove(2)), Arrays.asList("1","2", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.remove(3)), Arrays.asList("1","2", "3", "5", "6", "7", "8"));
			assertEquals(makeList(l.remove(4)), Arrays.asList("1","2", "3", "4", "6", "7", "8"));
			assertEquals(makeList(l.remove(5)), Arrays.asList("1","2", "3", "4", "5", "7", "8"));
			assertEquals(makeList(l.remove(6)), Arrays.asList("1","2", "3", "4", "5", "6", "8"));
			assertEquals(makeList(l.remove(7)), Arrays.asList("1","2", "3", "4", "5", "6", "7"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(new Branch2<>(new Leaf<>("1"),new Leaf<>("2")),new Branch3<>(new Leaf<>("3"),new Leaf<>("4"),new Leaf<>("5")),new Branch3<>(new Leaf<>("6"), new Leaf<>("7"),new Leaf<>("8")))
					, false);
			assertEquals(makeList(l.remove(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6", "7", "8")));
			assertEquals(makeList(l.remove(1)), Arrays.asList("1","3", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.remove(2)), Arrays.asList("1","2", "4", "5", "6", "7", "8"));
			assertEquals(makeList(l.remove(3)), Arrays.asList("1","2", "3", "5", "6", "7", "8"));
			assertEquals(makeList(l.remove(4)), Arrays.asList("1","2", "3", "4", "6", "7", "8"));
			assertEquals(makeList(l.remove(5)), Arrays.asList("1","2", "3", "4", "5", "7", "8"));
			assertEquals(makeList(l.remove(6)), Arrays.asList("1","2", "3", "4", "5", "6", "8"));
			assertEquals(makeList(l.remove(7)), Arrays.asList("1","2", "3", "4", "5", "6", "7"));
		}
		{
			List23<String> l = new List23<>(
					new Branch3<>(new Branch3<>(new Leaf<>("1"),new Leaf<>("2"),new Leaf<>("3")),new Branch3<>(new Leaf<>("4"),new Leaf<>("5"),new Leaf<>("6")),new Branch3<>(new Leaf<>("7"),new Leaf<>("8"),new Leaf<>("9")))
					, false);
			assertEquals(makeList(l.remove(0)), new ArrayList<>(Arrays.asList("2","3", "4", "5", "6", "7", "8", "9")));
			assertEquals(makeList(l.remove(1)), Arrays.asList("1","3", "4", "5", "6", "7", "8", "9"));
			assertEquals(makeList(l.remove(2)), Arrays.asList("1","2", "4", "5", "6", "7", "8", "9"));
			assertEquals(makeList(l.remove(3)), Arrays.asList("1","2", "3", "5", "6", "7", "8", "9"));
			assertEquals(makeList(l.remove(4)), Arrays.asList("1","2", "3", "4", "6", "7", "8", "9"));
			assertEquals(makeList(l.remove(5)), Arrays.asList("1","2", "3", "4", "5", "7", "8", "9"));
			assertEquals(makeList(l.remove(6)), Arrays.asList("1","2", "3", "4", "5", "6", "8", "9"));
			assertEquals(makeList(l.remove(7)), Arrays.asList("1","2", "3", "4", "5", "6", "7", "9"));
			assertEquals(makeList(l.remove(8)), Arrays.asList("1","2", "3", "4", "5", "6", "7", "8"));
		}
	}
	
	@Test
	public void testDeletions() {
		for(int sz = 0; sz < 100; sz++) {
			List23<String> l = new List23<String>(null, false);
			for(int i = 0; i < sz; i++) {
				l = l.insert(i, String.valueOf(i));
				checkTree(l);
			}
			assertEquals(l.size(), sz);
			{
				for(int i = 0; i < sz; i++) {
					List23<String> l2 = l.remove(i);
					checkTree(l2);
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
		assertNull(new Leaf<Integer>(5).b1());
		assertNull(new Leaf<Integer>(5).b2());
		assertNull(new Leaf<Integer>(5).b3());
		assertEquals(new Leaf<Integer>(5).size(), 1);
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).remove(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insert(-1, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insert(1, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insert(0, "abc").insert(-1, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insert(0, "abc").insert(2, "abc"));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insert(0, "abc").get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insert(0, "abc").get(1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insert(0, "abc").remove(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> new List23<String>(null, false).insert(0, "abc").remove(1));
	}
}
