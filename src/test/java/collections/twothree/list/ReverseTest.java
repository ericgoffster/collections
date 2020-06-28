package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;

public class ReverseTest {
    private static <T> Branch<T> branch(T a, T b) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b));
    }
    private static <T> Branch<T> branch(T a, T b, T c) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b),new Leaf<>(c));
    }
  
    @Test
    public void testIsValid() {
       assertTrue(branch("2","3").reverse().isValid(2));
       assertTrue(branch("1","2","3").reverse().isValid(2));
       assertFalse(new Branch<>(branch("2","3"), new Leaf<>("1"), new Leaf<>("4")).reverse().isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), branch("2","3"), new Leaf<>("4")).reverse().isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), new Leaf<>("4"), branch("2","3")).reverse().isValid(3));
       assertFalse(new Branch<>(branch("2","3"), new Leaf<>("1")).reverse().isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), branch("2","3")).reverse().isValid(3));
       assertTrue(new Branch<>(branch("2","3"), branch("4","5","6")).reverse().isValid(3));
    }
    @Test
    public void testHead() {
        assertEquals(branch("2","3").reverse().head(0), null);
        assertEquals(branch("2","3").reverse().head(1), new Leaf<>("3"));
        assertEquals(branch("2","3").reverse().head(2), branch("3","2"));

        assertEquals(branch("2","3","4").reverse().head(0), null);
        assertEquals(branch("2","3","4").reverse().head(1), new Leaf<>("4"));
        assertEquals(branch("2","3","4").reverse().head(2), branch("4","3"));
        assertEquals(branch("2","3","4").reverse().head(3), branch("4","3", "2"));

        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().head(0), null);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().head(1), new Leaf<>("6"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().head(2), branch("6", "5"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().head(3), branch("6", "5", "4"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().head(4), new Branch<>(branch("6","5"),branch("4","3")));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().head(5), new Branch<>(branch("6","5","4"),branch("3","2")));
    }
    @Test
    public void testTail() {
        assertEquals(branch("2","3").reverse().tail(2), null);
        assertEquals(branch("2","3").reverse().tail(1), new Leaf<>("2"));
        assertEquals(branch("2","3").reverse().tail(0), branch("3","2"));

        assertEquals(branch("2","3","4").reverse().tail(3), null);
        assertEquals(branch("2","3","4").reverse().tail(2), new Leaf<>("2"));
        assertEquals(branch("2","3","4").reverse().tail(1), branch("3","2"));
        assertEquals(branch("2","3","4").reverse().tail(0), branch("4","3", "2"));

        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().tail(5), null);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().tail(4), new Leaf<>("2"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().tail(3), branch("3", "2"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().tail(2), branch("4", "3", "2"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5", "6")).reverse().tail(1), new Branch<>(branch("5","4"),branch("3","2")));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().tail(0), new Branch<>(branch("6","5","4"),branch("3","2")));
    }
    @Test
    public void testFirst() {
        assertEquals(branch("2","3").reverse().first(), "3");
        assertEquals(branch("2","3","4").reverse().first(), "4");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().first(), "6");
    }
    @Test
    public void testLast() {
        assertEquals(branch("2","3").reverse().last(), "2");
        assertEquals(branch("2","3","4").reverse().last(), "2");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().last(), "2");
    }
    @Test
    public void testMap() {
        assertEquals(branch("2","3").reverse().map(s -> s+s), branch("33","22"));
        assertEquals(branch("2","3","4").reverse().map(s -> s+s), branch("44","33","22"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().map(s -> s+s), new Branch<>(branch("66","55","44"),branch("33","22")));
    }

    @Test
    public void testGet() {
        assertEquals(branch("2","3").reverse().get(1), "2");
        assertEquals(branch("2","3").reverse().get(0), "3");

        assertEquals(branch("2","3","4").reverse().get(2), "2");
        assertEquals(branch("2","3","4").reverse().get(1), "3");
        assertEquals(branch("2","3","4").reverse().get(0), "4");

        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().get(4), "2");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().get(3), "3");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().get(2), "4");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().get(1), "5");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().get(0), "6");
    }

    @Test
    public void testDepth() {
        assertEquals(branch("2","3").reverse().getDepth(), 2);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).reverse().getDepth(), 3);
    }
    
    @Test
    public void testNumBranches() {
        assertEquals(branch("2","3").reverse().numBranches(), 2);
        assertEquals(branch("2","3","4").reverse().numBranches(), 3);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).reverse().numBranches(), 2);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5"),branch("6","7")).reverse().numBranches(), 3);
    }
    
    @Test
    public void testGetBranch() {
        assertEquals(branch("2","3").reverse().getBranch(0), new Leaf<>("3"));
        assertEquals(branch("2","3").reverse().getBranch(1), new Leaf<>("2"));
        assertEquals(branch("2","3","4").reverse().getBranch(0), new Leaf<>("4"));
        assertEquals(branch("2","3","4").reverse().getBranch(1), new Leaf<>("3"));
        assertEquals(branch("2","3","4").reverse().getBranch(2), new Leaf<>("2"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).reverse().getBranch(0), branch("5","4"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).reverse().getBranch(1), branch("3","2"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5"),branch("6","7")).reverse().getBranch(0), branch("7","6"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5"),branch("6","7")).reverse().getBranch(1), branch("5","4"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5"),branch("6","7")).reverse().getBranch(2), branch("3","2"));
    }
    
    @Test
    public void testReverse() {
        assertEquals(branch("2","3").reverse().reverse(), branch("2","3"));
        assertEquals(branch("2","3","4").reverse().reverse(), branch("2","3","4"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5","6")).reverse().reverse(), new Branch<>(branch("2","3"),branch("4","5","6")));
    }

    @Test
    public void testBinarySearch() {
        assertEquals(branch(2,4).reverse().binarySearch(i -> -Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(branch(2,4).reverse().binarySearch(i -> -Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(branch(2,4).reverse().binarySearch(i -> -Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),0);
        
        assertEquals(branch(2,4,6).reverse().binarySearch(i -> -Integer.valueOf(1).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(branch(2,4,6).reverse().binarySearch(i -> -Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(branch(2,4,6).reverse().binarySearch(i -> -Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(branch(2,4,6).reverse().binarySearch(i -> -Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(branch(2,4,6).reverse().binarySearch(i -> -Integer.valueOf(5).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(branch(2,4,6).reverse().binarySearch(i -> -Integer.valueOf(6).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(branch(2,4,6).reverse().binarySearch(i -> -Integer.valueOf(7).compareTo(i), (e, i) -> i).intValue(),0);
       
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().binarySearch(i -> -Integer.valueOf(1).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().binarySearch(i -> -Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().binarySearch(i -> -Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().binarySearch(i -> -Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),3);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().binarySearch(i -> -Integer.valueOf(5).compareTo(i), (e, i) -> i).intValue(),3);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().binarySearch(i -> -Integer.valueOf(6).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().binarySearch(i -> -Integer.valueOf(7).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().binarySearch(i -> -Integer.valueOf(8).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().binarySearch(i -> -Integer.valueOf(9).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().binarySearch(i -> -Integer.valueOf(10).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().binarySearch(i -> -Integer.valueOf(11).compareTo(i), (e, i) -> i).intValue(),0);
    }
    @Test
    public void testStream() {
        assertEquals(branch(2,4).reverse().stream().collect(Collectors.toList()),Arrays.asList(4, 2));
        assertEquals(branch(2,4, 6).reverse().stream().collect(Collectors.toList()),Arrays.asList(6, 4, 2));
        assertEquals(new Branch<>(branch(2,4),branch(6,8,10)).reverse().stream().collect(Collectors.toList()),Arrays.asList(10, 8,6,4,2));
    }
}
