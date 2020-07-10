package collections.immutable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.junit.Test;

public class ReverseBranchTest {
    private static <T> Branch<T> branch(T a, T b) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b));
    }
    private static <T> Branch<T> branch(T a, T b, T c) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b),new Leaf<>(c));
    }
    private static <T> ReversedNode23<T> reverse(Node23<T> n) {
        return new ReversedNode23<T>(n);
    }
    private static <T> ReversedNode23<T>  reversedBranch(Node23<T> a, Node23<T> b) {
        return reverse(new Branch<>(a,b));
    }
    private static <T> ReversedNode23<T>  reversedBranch(Node23<T> a, Node23<T> b, Node23<T> c) {
        return reverse(new Branch<>(a,b, c));
    }
    private static <T> ReversedNode23<T>  reversedBranch2(T a, T b) {
        return reverse(branch(a,b));
    }
    private static <T> ReversedNode23<T>  reversedBranch3(T a, T b, T c) {
        return reverse(branch(a,b,c));
    }
  
    @Test
    public void testIsLeaf() {
        assertFalse(reversedBranch2("2","3").isLeaf());
    }
    @Test
    public void testIsValid() {
       assertTrue(reversedBranch2("2","3").isValid(2));
       assertTrue(reversedBranch3("1","2","3").isValid(2));
       assertFalse(reversedBranch(branch("2","3"), new Leaf<>("1"), new Leaf<>("4")).isValid(3));
       assertFalse(reversedBranch(new Leaf<>("1"), branch("2","3"), new Leaf<>("4")).isValid(3));
       assertFalse(reversedBranch(new Leaf<>("1"), new Leaf<>("4"), branch("2","3")).isValid(3));
       assertFalse(reversedBranch(branch("2","3"), new Leaf<>("1")).isValid(3));
       assertFalse(reversedBranch(new Leaf<>("1"), branch("2","3")).isValid(3));
       assertTrue(reversedBranch(branch("2","3"), branch("4","5","6")).isValid(3));
    }
    @Test
    public void testHead() {
        assertEquals(reversedBranch2("2","3").head(0), null);
        assertEquals(reversedBranch2("2","3").head(1), new Leaf<>("3"));
        assertEquals(reversedBranch2("2","3").head(2), branch("3","2"));

        assertEquals(reversedBranch3("2","3","4").head(0), null);
        assertEquals(reversedBranch3("2","3","4").head(1), new Leaf<>("4"));
        assertEquals(reversedBranch3("2","3","4").head(2), branch("4","3"));
        assertEquals(reversedBranch3("2","3","4").head(3), branch("4","3", "2"));

        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).head(0), null);
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).head(1), new Leaf<>("6"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).head(2), branch("6", "5"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).head(3), branch("6", "5", "4"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).head(4), new Branch<>(branch("6","5"),branch("4","3")));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).head(5), new Branch<>(branch("6","5","4"),branch("3","2")));
    }
    @Test
    public void testTail() {
        assertEquals(reversedBranch2("2","3").tail(2), null);
        assertEquals(reversedBranch2("2","3").tail(1), new Leaf<>("2"));
        assertEquals(reversedBranch2("2","3").tail(0), branch("3","2"));

        assertEquals(reversedBranch3("2","3","4").tail(3), null);
        assertEquals(reversedBranch3("2","3","4").tail(2), new Leaf<>("2"));
        assertEquals(reversedBranch3("2","3","4").tail(1), branch("3","2"));
        assertEquals(reversedBranch3("2","3","4").tail(0), branch("4","3", "2"));

        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).tail(5), null);
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).tail(4), new Leaf<>("2"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).tail(3), branch("3", "2"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).tail(2), branch("4", "3", "2"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5", "6")).tail(1), new Branch<>(branch("5","4"),branch("3","2")));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).tail(0), new Branch<>(branch("6","5","4"),branch("3","2")));
    }
    @Test
    public void testFirst() {
        assertEquals(reversedBranch2("2","3").first(), "3");
        assertEquals(reversedBranch3("2","3","4").first(), "4");
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).first(), "6");
    }
    @Test
    public void testLast() {
        assertEquals(reversedBranch2("2","3").last(), "2");
        assertEquals(reversedBranch3("2","3","4").last(), "2");
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).last(), "2");
    }
    @Test
    public void testMap() {
        assertEquals(reversedBranch2("2","3").map(s -> s+s), branch("33","22"));
        assertEquals(reversedBranch3("2","3","4").map(s -> s+s), branch("44","33","22"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).map(s -> s+s), new Branch<>(branch("66","55","44"),branch("33","22")));
    }

    @Test
    public void testGet() {
        assertEquals(reversedBranch2("2","3").get(1), "2");
        assertEquals(reversedBranch2("2","3").get(0), "3");

        assertEquals(reversedBranch3("2","3","4").get(2), "2");
        assertEquals(reversedBranch3("2","3","4").get(1), "3");
        assertEquals(reversedBranch3("2","3","4").get(0), "4");

        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).get(4), "2");
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).get(3), "3");
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).get(2), "4");
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).get(1), "5");
        assertEquals(reversedBranch(branch("2","3"),branch("4","5","6")).get(0), "6");
    }

    @Test
    public void testDepth() {
        assertEquals(reversedBranch2("2","3").getDepth(), 2);
        assertEquals(reversedBranch(branch("2","3"),branch("4","5")).getDepth(), 3);
    }
    
    @Test
    public void testNumBranches() {
        assertEquals(reversedBranch2("2","3").numBranches(), 2);
        assertEquals(reversedBranch3("2","3","4").numBranches(), 3);
        assertEquals(reversedBranch(branch("2","3"),branch("4","5")).numBranches(), 2);
        assertEquals(reversedBranch(branch("2","3"),branch("4","5"),branch("6","7")).numBranches(), 3);
    }
    
    @Test
    public void testGetBranch() {
        assertEquals(reversedBranch2("2","3").getBranch(0), new Leaf<>("3"));
        assertEquals(reversedBranch2("2","3").getBranch(1), new Leaf<>("2"));
        assertEquals(reversedBranch3("2","3","4").getBranch(0), new Leaf<>("4"));
        assertEquals(reversedBranch3("2","3","4").getBranch(1), new Leaf<>("3"));
        assertEquals(reversedBranch3("2","3","4").getBranch(2), new Leaf<>("2"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5")).getBranch(0), branch("5","4"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5")).getBranch(1), branch("3","2"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5"),branch("6","7")).getBranch(0), branch("7","6"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5"),branch("6","7")).getBranch(1), branch("5","4"));
        assertEquals(reversedBranch(branch("2","3"),branch("4","5"),branch("6","7")).getBranch(2), branch("3","2"));
    }
    
    @Test
    public void testReverse() {
        assertEquals(reversedBranch2("2","3").reverse(), branch("2","3"));
        assertEquals(reversedBranch3("2","3","4").reverse(), branch("2","3","4"));
        assertEquals(reverse(new Branch<>(branch("2","3"),branch("4","5","6"))).reverse(), new Branch<>(branch("2","3"),branch("4","5","6")));
    }

    @Test
    public void testBinarySearch() {
        assertEquals(reversedBranch2(2,4).binarySearch(i -> -Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(reversedBranch2(2,4).binarySearch(i -> -Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(reversedBranch2(2,4).binarySearch(i -> -Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),0);
        
        assertEquals(reversedBranch3(2,4,6).binarySearch(i -> -Integer.valueOf(1).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(reversedBranch3(2,4,6).binarySearch(i -> -Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(reversedBranch3(2,4,6).binarySearch(i -> -Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(reversedBranch3(2,4,6).binarySearch(i -> -Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(reversedBranch3(2,4,6).binarySearch(i -> -Integer.valueOf(5).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(reversedBranch3(2,4,6).binarySearch(i -> -Integer.valueOf(6).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(reversedBranch3(2,4,6).binarySearch(i -> -Integer.valueOf(7).compareTo(i), (e, i) -> i).intValue(),0);
       
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).binarySearch(i -> -Integer.valueOf(1).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).binarySearch(i -> -Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).binarySearch(i -> -Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).binarySearch(i -> -Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),3);
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).binarySearch(i -> -Integer.valueOf(5).compareTo(i), (e, i) -> i).intValue(),3);
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).binarySearch(i -> -Integer.valueOf(6).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).binarySearch(i -> -Integer.valueOf(7).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).binarySearch(i -> -Integer.valueOf(8).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).binarySearch(i -> -Integer.valueOf(9).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).binarySearch(i -> -Integer.valueOf(10).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).binarySearch(i -> -Integer.valueOf(11).compareTo(i), (e, i) -> i).intValue(),0);
    }
    @Test
    public void testStream() {
        assertEquals(reversedBranch2(2,4).stream().collect(Collectors.toList()),Arrays.asList(4, 2));
        assertEquals(reversedBranch3(2,4, 6).stream().collect(Collectors.toList()),Arrays.asList(6, 4, 2));
        assertEquals(reverse(new Branch<>(branch(2,4),branch(6,8,10))).stream().collect(Collectors.toList()),Arrays.asList(10, 8,6,4,2));
      
        ListIterator<Integer> iter = reversedBranch2(2,4).iterator();
        assertEquals(iter.nextIndex(), 0);
        assertEquals(iter.next().intValue(), 4);
        assertEquals(iter.nextIndex(), 1);
        assertEquals(iter.next().intValue(), 2);
        assertFalse(iter.hasNext());
        assertTrue(iter.hasPrevious());
        assertEquals(iter.previousIndex(), 1);
        assertEquals(iter.previous().intValue(), 2);
        assertEquals(iter.previousIndex(), 0);
        assertEquals(iter.previous().intValue(), 4);
        assertTrue(iter.hasNext());
        assertFalse(iter.hasPrevious());
    }
}
