package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.junit.Test;

public class BranchTest {
    private static <T> Branch<T> branch(T a, T b) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b));
    }
    private static <T> Branch<T> branch(T a, T b, T c) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b),new Leaf<>(c));
    }
  
    @Test
    public void testIsLeaf() {
       assertFalse(branch("2","3").isLeaf());
    }
    @Test
    public void testIsValid() {
       assertTrue(branch("2","3").isValid(2));
       assertTrue(branch("1","2","3").isValid(2));
       assertFalse(new Branch<>(branch("2","3"), new Leaf<>("1"), new Leaf<>("4")).isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), branch("2","3"), new Leaf<>("4")).isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), new Leaf<>("4"), branch("2","3")).isValid(3));
       assertFalse(new Branch<>(branch("2","3"), new Leaf<>("1")).isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), branch("2","3")).isValid(3));
       assertTrue(new Branch<>(branch("2","3"), branch("4","5","6")).isValid(3));
    }
    @Test
    public void testHead() {
        assertEquals(branch("2","3").head(0), null);
        assertEquals(branch("2","3").head(1), new Leaf<>("2"));
        assertEquals(branch("2","3").head(2), branch("2","3"));

        assertEquals(branch("2","3","4").head(0), null);
        assertEquals(branch("2","3","4").head(1), new Leaf<>("2"));
        assertEquals(branch("2","3","4").head(2), branch("2","3"));
        assertEquals(branch("2","3","4").head(3), branch("2","3", "4"));

        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).head(0), null);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).head(1), new Leaf<>("2"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).head(2), branch("2", "3"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).head(3), branch("2", "3", "4"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).head(4), new Branch<>(branch("2","3"),branch("4","5")));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).head(5), new Branch<>(branch("2","3"),branch("4","5","6")));
    }
    @Test
    public void testTail() {
        assertEquals(branch("2","3").tail(2), null);
        assertEquals(branch("2","3").tail(1), new Leaf<>("3"));
        assertEquals(branch("2","3").tail(0), branch("2","3"));

        assertEquals(branch("2","3","4").tail(3), null);
        assertEquals(branch("2","3","4").tail(2), new Leaf<>("4"));
        assertEquals(branch("2","3","4").tail(1), branch("3","4"));
        assertEquals(branch("2","3","4").tail(0), branch("2","3", "4"));

        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).tail(5), null);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).tail(4), new Leaf<>("6"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).tail(3), branch("5", "6"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).tail(2), branch("4", "5", "6"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5", "6")).tail(1), new Branch<>(branch("3","4"),branch("5","6")));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).tail(0), new Branch<>(branch("2","3"),branch("4","5","6")));
    }
    @Test
    public void testFirst() {
        assertEquals(branch("2","3").first(), "2");
        assertEquals(branch("2","3","4").first(), "2");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).first(), "2");
    }
    @Test
    public void testLast() {
        assertEquals(branch("2","3").last(), "3");
        assertEquals(branch("2","3","4").last(), "4");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).last(), "6");
    }
    @Test
    public void testMap() {
        assertEquals(branch("2","3").map(s -> s+s), branch("22","33"));
        assertEquals(branch("2","3","4").map(s -> s+s), branch("22","33","44"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).map(s -> s+s), new Branch<>(branch("22","33"),branch("44","55","66")));
    }

    @Test
    public void testGet() {
        assertEquals(branch("2","3").get(1), "3");
        assertEquals(branch("2","3").get(0), "2");

        assertEquals(branch("2","3","4").get(2), "4");
        assertEquals(branch("2","3","4").get(1), "3");
        assertEquals(branch("2","3","4").get(0), "2");

        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).get(4), "6");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).get(3), "5");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).get(2), "4");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).get(1), "3");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).get(0), "2");
    }

    @Test
    public void testDepth() {
        assertEquals(branch("2","3").getDepth(), 2);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).getDepth(), 3);
    }
    
    @Test
    public void testNumBranches() {
        assertEquals(branch("2","3").numBranches(), 2);
        assertEquals(branch("2","3","4").numBranches(), 3);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).numBranches(), 2);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5"),branch("6","7")).numBranches(), 3);
    }
    
    @Test
    public void testGetBranch() {
        assertEquals(branch("2","3").getBranch(0), new Leaf<>("2"));
        assertEquals(branch("2","3").getBranch(1), new Leaf<>("3"));
        assertEquals(branch("2","3","4").getBranch(0), new Leaf<>("2"));
        assertEquals(branch("2","3","4").getBranch(1), new Leaf<>("3"));
        assertEquals(branch("2","3","4").getBranch(2), new Leaf<>("4"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).getBranch(0), branch("2","3"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).getBranch(1), branch("4","5"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5"),branch("6","7")).getBranch(0), branch("2","3"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5"),branch("6","7")).getBranch(1), branch("4","5"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5"),branch("6","7")).getBranch(2), branch("6","7"));
    }
    
    @Test
    public void testReverse() {
        assertEquals(branch("2","3").reverse(), branch("3","2"));
        assertEquals(branch("2","3","4").reverse(), branch("4","3","2"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse(), new Branch<>(branch("6","5","4"),branch("3","2")));
    }

    @Test
    public void testBinarySearch() {
        assertEquals(branch(2,4).binarySearch(i -> Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(branch(2,4).binarySearch(i -> Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(branch(2,4).binarySearch(i -> Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        
        assertEquals(branch(2,4,6).binarySearch(i -> Integer.valueOf(1).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(branch(2,4,6).binarySearch(i -> Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(branch(2,4,6).binarySearch(i -> Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(branch(2,4,6).binarySearch(i -> Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(branch(2,4,6).binarySearch(i -> Integer.valueOf(5).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(branch(2,4,6).binarySearch(i -> Integer.valueOf(6).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(branch(2,4,6).binarySearch(i -> Integer.valueOf(7).compareTo(i), (e, i) -> i).intValue(),2);
       
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).binarySearch(i -> Integer.valueOf(1).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).binarySearch(i -> Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).binarySearch(i -> Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).binarySearch(i -> Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).binarySearch(i -> Integer.valueOf(5).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).binarySearch(i -> Integer.valueOf(6).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).binarySearch(i -> Integer.valueOf(7).compareTo(i), (e, i) -> i).intValue(),3);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).binarySearch(i -> Integer.valueOf(8).compareTo(i), (e, i) -> i).intValue(),3);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).binarySearch(i -> Integer.valueOf(9).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).binarySearch(i -> Integer.valueOf(10).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).binarySearch(i -> Integer.valueOf(11).compareTo(i), (e, i) -> i).intValue(),4);
    }

    @Test
    public void testStream() {
        assertEquals(branch(2,4).stream().collect(Collectors.toList()),Arrays.asList(2, 4));
        assertEquals(branch(2,4, 6).stream().collect(Collectors.toList()),Arrays.asList(2, 4, 6));
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).stream().collect(Collectors.toList()),Arrays.asList(2, 4, 6, 8, 10));
    }
    @Test
    public void testHash() {
        HashSet<Node23<Integer>> hm = new HashSet<>();
        hm.add(branch(2,3));
        assertTrue(hm.contains(branch(2,3)));
        assertFalse(hm.contains(branch(2,3).reverse()));
        assertFalse(hm.contains(new Leaf<>(2)));
    }
}
