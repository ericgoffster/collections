package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.junit.Test;

public class MappedBranchTest {
    private static <T> Branch<T> branch(T a, T b) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b));
    }
    private static <T> Branch<T> branch(T a, T b, T c) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b),new Leaf<>(c));
    }
  
    @Test
    public void testIsLeaf() {
        assertTrue(new Leaf<>("1").map(i -> i).isLeaf());
        assertFalse(branch("2","3").map(i -> i).isLeaf());
    }
    @Test
    public void testIsValid() {
       assertTrue(branch("2","3").map(i -> i).isValid(2));
       assertTrue(branch("1","2","3").map(i -> i).isValid(2));
       assertFalse(new Branch<>(branch("2","3"), new Leaf<>("1"), new Leaf<>("4")).map(i -> i).isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), branch("2","3"), new Leaf<>("4")).map(i -> i).isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), new Leaf<>("4"), branch("2","3")).map(i -> i).isValid(3));
       assertFalse(new Branch<>(branch("2","3"), new Leaf<>("1")).map(i -> i).isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), branch("2","3")).map(i -> i).isValid(3));
       assertTrue(new Branch<>(branch("2","3"), branch("4","5","6")).map(i -> i).isValid(3));
    }
    @Test
    public void testHead() {
        assertEquals(branch("2","3").map(i -> i).head(0), null);
        assertEquals(branch("2","3").map(i -> i).head(1), new Leaf<>("2"));
        assertEquals(branch("2","3").map(i -> i).head(2), branch("2","3"));

        assertEquals(branch("2","3","4").map(i -> i).head(0), null);
        assertEquals(branch("2","3","4").map(i -> i).head(1), new Leaf<>("2"));
        assertEquals(branch("2","3","4").map(i -> i).head(2), branch("2","3"));
        assertEquals(branch("2","3","4").map(i -> i).head(3), branch("2","3", "4"));

        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).head(0), null);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).head(1), new Leaf<>("2"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).head(2), branch("2", "3"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).head(3), branch("2", "3", "4"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).head(4), new Branch<>(branch("2","3"),branch("4","5")));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).head(5), new Branch<>(branch("2","3"),branch("4","5","6")));
    }
    @Test
    public void testTail() {
        assertEquals(branch("2","3").map(i -> i).tail(2), null);
        assertEquals(branch("2","3").map(i -> i).tail(1), new Leaf<>("3"));
        assertEquals(branch("2","3").map(i -> i).tail(0), branch("2","3"));

        assertEquals(branch("2","3","4").map(i -> i).tail(3), null);
        assertEquals(branch("2","3","4").map(i -> i).tail(2), new Leaf<>("4"));
        assertEquals(branch("2","3","4").map(i -> i).tail(1), branch("3","4"));
        assertEquals(branch("2","3","4").map(i -> i).tail(0), branch("2","3", "4"));

        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).tail(5), null);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).tail(4), new Leaf<>("6"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).tail(3), branch("5", "6"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).tail(2), branch("4", "5", "6"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5", "6")).map(i -> i).tail(1), new Branch<>(branch("3","4"),branch("5","6")));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).tail(0), new Branch<>(branch("2","3"),branch("4","5","6")));
    }
    @Test
    public void testFirst() {
        assertEquals(branch("2","3").map(i -> i).first(), "2");
        assertEquals(branch("2","3","4").map(i -> i).first(), "2");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).first(), "2");
    }
    @Test
    public void testLast() {
        assertEquals(branch("2","3").map(i -> i).last(), "3");
        assertEquals(branch("2","3","4").map(i -> i).last(), "4");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).last(), "6");
    }
    @Test
    public void testMap() {
        assertEquals(branch("2","3").map(i -> i).map(s -> s+s), branch("22","33"));
        assertEquals(branch("2","3","4").map(i -> i).map(s -> s+s), branch("22","33","44"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).map(s -> s+s), new Branch<>(branch("22","33"),branch("44","55","66")));
    }

    @Test
    public void testGet() {
        assertEquals(branch("2","3").map(i -> i).get(1), "3");
        assertEquals(branch("2","3").map(i -> i).get(0), "2");

        assertEquals(branch("2","3","4").map(i -> i).get(2), "4");
        assertEquals(branch("2","3","4").map(i -> i).get(1), "3");
        assertEquals(branch("2","3","4").map(i -> i).get(0), "2");

        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).get(4), "6");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).get(3), "5");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).get(2), "4");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).get(1), "3");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(i -> i).get(0), "2");
    }

    @Test
    public void testDepth() {
        assertEquals(branch("2","3").map(i -> i).getDepth(), 2);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).map(i -> i).getDepth(), 3);
    }
    
    @Test
    public void testNumBranches() {
        assertEquals(branch("2","3").map(i -> i).numBranches(), 2);
        assertEquals(branch("2","3","4").map(i -> i).numBranches(), 3);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).map(i -> i).numBranches(), 2);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5").map(i -> i),branch("6","7")).numBranches(), 3);
    }
    
    @Test
    public void testGetBranch() {
        assertEquals(branch("2","3").map(i -> i).getBranch(0), new Leaf<>("2"));
        assertEquals(branch("2","3").map(i -> i).getBranch(1), new Leaf<>("3"));
        assertEquals(branch("2","3","4").map(i -> i).getBranch(0), new Leaf<>("2"));
        assertEquals(branch("2","3","4").map(i -> i).getBranch(1), new Leaf<>("3"));
        assertEquals(branch("2","3","4").map(i -> i).getBranch(2), new Leaf<>("4"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).map(i -> i).getBranch(0), branch("2","3"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).map(i -> i).getBranch(1), branch("4","5"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5").map(i -> i),branch("6","7")).getBranch(0), branch("2","3"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5").map(i -> i),branch("6","7")).getBranch(1), branch("4","5"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5").map(i -> i),branch("6","7")).getBranch(2), branch("6","7"));
    }
    
    @Test
    public void testReverse() {
        assertEquals(branch("2","3").reverse(), branch("3","2"));
        assertEquals(branch("2","3","4").reverse(), branch("4","3","2"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse(), new Branch<>(branch("6","5","4"),branch("3","2")));
    }

    @Test
    public void testBinarySearch() {
        assertEquals(branch(2,4).map(i -> i).binarySearch(i -> Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(branch(2,4).map(i -> i).binarySearch(i -> Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(branch(2,4).map(i -> i).binarySearch(i -> Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        
        assertEquals(branch(2,4,6).map(i -> i).binarySearch(i -> Integer.valueOf(1).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(branch(2,4,6).map(i -> i).binarySearch(i -> Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(branch(2,4,6).map(i -> i).binarySearch(i -> Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(branch(2,4,6).map(i -> i).binarySearch(i -> Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(branch(2,4,6).map(i -> i).binarySearch(i -> Integer.valueOf(5).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(branch(2,4,6).map(i -> i).binarySearch(i -> Integer.valueOf(6).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(branch(2,4,6).map(i -> i).binarySearch(i -> Integer.valueOf(7).compareTo(i), (e, i) -> i).intValue(),2);
       
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).binarySearch(i -> Integer.valueOf(1).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).binarySearch(i -> Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).binarySearch(i -> Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).binarySearch(i -> Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).binarySearch(i -> Integer.valueOf(5).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).binarySearch(i -> Integer.valueOf(6).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).binarySearch(i -> Integer.valueOf(7).compareTo(i), (e, i) -> i).intValue(),3);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).binarySearch(i -> Integer.valueOf(8).compareTo(i), (e, i) -> i).intValue(),3);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).binarySearch(i -> Integer.valueOf(9).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).binarySearch(i -> Integer.valueOf(10).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).binarySearch(i -> Integer.valueOf(11).compareTo(i), (e, i) -> i).intValue(),4);
    }

    @Test
    public void testStream() {
        assertEquals(branch(2,4).map(i -> i).stream().collect(Collectors.toList()),Arrays.asList(2, 4));
        assertEquals(branch(2,4, 6).map(i -> i).stream().collect(Collectors.toList()),Arrays.asList(2, 4, 6));
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).map(i -> i).stream().collect(Collectors.toList()),Arrays.asList(2, 4, 6, 8, 10));
        
        ListIterator<Integer> iter = branch(2,4).map(i -> i).iterator();
        assertEquals(iter.nextIndex(), 0);
        assertEquals(iter.next().intValue(), 2);
        assertEquals(iter.nextIndex(), 1);
        assertEquals(iter.next().intValue(), 4);
        assertFalse(iter.hasNext());
        assertTrue(iter.hasPrevious());
        assertEquals(iter.previousIndex(), 1);
        assertEquals(iter.previous().intValue(), 4);
        assertEquals(iter.previousIndex(), 0);
        assertEquals(iter.previous().intValue(), 2);
        assertTrue(iter.hasNext());
        assertFalse(iter.hasPrevious());
    }
    @Test
    public void testHash() {
        HashSet<Node23<Integer>> hm = new HashSet<>();
        hm.add(branch(2,3).map(i -> i));
        assertTrue(hm.contains(branch(2,3).map(i -> i)));
        assertFalse(hm.contains(branch(2,3).map(i -> i + 1)));
        assertFalse(hm.contains(new Leaf<>(2)));
    }
}
