package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

public class MappedBranchTest {
    private static <T> Branch<T> branch(T a, T b) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b));
    }
    private static <T> Branch<T> branch(T a, T b, T c) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b),new Leaf<>(c));
    }
    private static <E, F> MappedNode23<E, F> map(Node23<E> n, Function<E, F> f) {
        return new MappedNode23<>(n, f);
    }
    private static <E, F> MappedNode23<E, F> mappedBranch(Node23<E> a, Node23<E> b, Function<E, F> f) {
        return map(new Branch<>(a,b), f);
    }
    private static <E, F> MappedNode23<E, F> mappedBranch(Node23<E> a, Node23<E> b, Node23<E> c, Function<E, F> f) {
        return map(new Branch<>(a,b, c), f);
    }
    private static <E, F> MappedNode23<E, F> mappedBranch2(E a, E b, Function<E, F> f) {
        return map(branch(a,b), f);
    }
    private static <E, F> MappedNode23<E, F> mappedBranch3(E a, E b, E c, Function<E, F> f) {
        return map(branch(a,b,c), f);
    }
  
    @Test
    public void testIsLeaf() {
        assertTrue(map(new Leaf<>("1"),i -> i).isLeaf());
        assertFalse(map(branch("2","3"),i -> i).isLeaf());
    }
    @Test
    public void testIsValid() {
       assertTrue(mappedBranch2("2","3", i -> i).isValid(2));
       assertTrue(mappedBranch3("1","2","3",i -> i).isValid(2));
       assertFalse(mappedBranch(branch("2","3"), new Leaf<>("1"), new Leaf<>("4"),i -> i).isValid(3));
       assertFalse(mappedBranch(new Leaf<>("1"), branch("2","3"), new Leaf<>("4"),i -> i).isValid(3));
       assertFalse(mappedBranch(new Leaf<>("1"), new Leaf<>("4"), branch("2","3"),i -> i).isValid(3));
       assertFalse(mappedBranch(branch("2","3"), new Leaf<>("1"),i -> i).isValid(3));
       assertFalse(mappedBranch(new Leaf<>("1"), branch("2","3"),i -> i).isValid(3));
       assertTrue(mappedBranch(branch("2","3"), branch("4","5","6"),i -> i).isValid(3));
    }
    @Test
    public void testHead() {
        assertEquals(mappedBranch2("2","3",i -> i).head(0), null);
        assertEquals(mappedBranch2("2","3",i -> i).head(1), new Leaf<>("2"));
        assertEquals(mappedBranch2("2","3",i -> i).head(2), branch("2","3"));

        assertEquals(mappedBranch3("2","3","4",i -> i).head(0), null);
        assertEquals(mappedBranch3("2","3","4",i -> i).head(1), new Leaf<>("2"));
        assertEquals(mappedBranch3("2","3","4",i -> i).head(2), branch("2","3"));
        assertEquals(mappedBranch3("2","3","4",i -> i).head(3), branch("2","3", "4"));

        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).head(0), null);
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).head(1), new Leaf<>("2"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).head(2), branch("2", "3"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).head(3), branch("2", "3", "4"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).head(4), new Branch<>(branch("2","3"),branch("4","5")));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).head(5), new Branch<>(branch("2","3"),branch("4","5","6")));
    }
    @Test
    public void testTail() {
        assertEquals(mappedBranch2("2","3",i -> i).tail(2), null);
        assertEquals(mappedBranch2("2","3",i -> i).tail(1), new Leaf<>("3"));
        assertEquals(mappedBranch2("2","3",i -> i).tail(0), branch("2","3"));

        assertEquals(mappedBranch3("2","3","4",i -> i).tail(3), null);
        assertEquals(mappedBranch3("2","3","4",i -> i).tail(2), new Leaf<>("4"));
        assertEquals(mappedBranch3("2","3","4",i -> i).tail(1), branch("3","4"));
        assertEquals(mappedBranch3("2","3","4",i -> i).tail(0), branch("2","3", "4"));

        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).tail(5), null);
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).tail(4), new Leaf<>("6"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).tail(3), branch("5", "6"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).tail(2), branch("4", "5", "6"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5", "6"),i -> i).tail(1), new Branch<>(branch("3","4"),branch("5","6")));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).tail(0), new Branch<>(branch("2","3"),branch("4","5","6")));
    }
    @Test
    public void testFirst() {
        assertEquals(mappedBranch2("2","3",i -> i).first(), "2");
        assertEquals(mappedBranch3("2","3","4",i -> i).first(), "2");
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).first(), "2");
    }
    @Test
    public void testLast() {
        assertEquals(mappedBranch2("2","3",i -> i).last(), "3");
        assertEquals(mappedBranch3("2","3","4",i -> i).last(), "4");
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).last(), "6");
    }
    @Test
    public void testMap() {
        assertEquals(mappedBranch2("2","3",i -> i).map(s -> s+s), branch("22","33"));
        assertEquals(mappedBranch3("2","3","4",i -> i).map(s -> s+s), branch("22","33","44"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).map(s -> s+s), new Branch<>(branch("22","33"),branch("44","55","66")));
    }

    @Test
    public void testGet() {
        assertEquals(mappedBranch2("2","3",i -> i).get(1), "3");
        assertEquals(mappedBranch2("2","3",i -> i).get(0), "2");

        assertEquals(mappedBranch3("2","3","4",i -> i).get(2), "4");
        assertEquals(mappedBranch3("2","3","4",i -> i).get(1), "3");
        assertEquals(mappedBranch3("2","3","4",i -> i).get(0), "2");

        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).get(4), "6");
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).get(3), "5");
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).get(2), "4");
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).get(1), "3");
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).get(0), "2");
    }

    @Test
    public void testDepth() {
        assertEquals(mappedBranch2("2","3",i -> i).getDepth(), 2);
        assertEquals(mappedBranch(branch("2","3"),branch("4","5"),i -> i).getDepth(), 3);
    }
    
    @Test
    public void testNumBranches() {
        assertEquals(mappedBranch2("2","3",i -> i).numBranches(), 2);
        assertEquals(mappedBranch3("2","3","4",i -> i).numBranches(), 3);
        assertEquals(mappedBranch(branch("2","3"),branch("4","5"),i -> i).numBranches(), 2);
        assertEquals(mappedBranch(branch("2","3"),branch("4","5"),branch("6","7"),i -> i).numBranches(), 3);
    }
    
    @Test
    public void testGetBranch() {
        assertEquals(mappedBranch2("2","3",i -> i).getBranch(0), new Leaf<>("2"));
        assertEquals(mappedBranch2("2","3",i -> i).getBranch(1), new Leaf<>("3"));
        assertEquals(mappedBranch3("2","3","4",i -> i).getBranch(0), new Leaf<>("2"));
        assertEquals(mappedBranch3("2","3","4",i -> i).getBranch(1), new Leaf<>("3"));
        assertEquals(mappedBranch3("2","3","4",i -> i).getBranch(2), new Leaf<>("4"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5"),i -> i).getBranch(0), branch("2","3"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5"),i -> i).getBranch(1), branch("4","5"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5"),branch("6","7"),i -> i).getBranch(0), branch("2","3"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5"),branch("6","7"),i -> i).getBranch(1), branch("4","5"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5"),branch("6","7"),i -> i).getBranch(2), branch("6","7"));
    }
    
    @Test
    public void testReverse() {
        assertEquals(mappedBranch2("2","3",i -> i).reverse(), branch("3","2"));
        assertEquals(mappedBranch3("2","3","4",i -> i).reverse(), branch("4","3","2"));
        assertEquals(mappedBranch(branch("2","3"),branch("4","5","6"),i -> i).reverse(), new Branch<>(branch("6","5","4"),branch("3","2")));
    }

    @Test
    public void testBinarySearch() {
        assertEquals(mappedBranch2(2,4,i -> i).binarySearch(i -> Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(mappedBranch2(2,4,i -> i).binarySearch(i -> Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(mappedBranch2(2,4,i -> i).binarySearch(i -> Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        
        assertEquals(mappedBranch3(2,4,6,i -> i).binarySearch(i -> Integer.valueOf(1).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(mappedBranch3(2,4,6,i -> i).binarySearch(i -> Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(mappedBranch3(2,4,6,i -> i).binarySearch(i -> Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(mappedBranch3(2,4,6,i -> i).binarySearch(i -> Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(mappedBranch3(2,4,6,i -> i).binarySearch(i -> Integer.valueOf(5).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(mappedBranch3(2,4,6,i -> i).binarySearch(i -> Integer.valueOf(6).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(mappedBranch3(2,4,6,i -> i).binarySearch(i -> Integer.valueOf(7).compareTo(i), (e, i) -> i).intValue(),2);
       
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).binarySearch(i -> Integer.valueOf(1).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).binarySearch(i -> Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).binarySearch(i -> Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).binarySearch(i -> Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).binarySearch(i -> Integer.valueOf(5).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).binarySearch(i -> Integer.valueOf(6).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).binarySearch(i -> Integer.valueOf(7).compareTo(i), (e, i) -> i).intValue(),3);
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).binarySearch(i -> Integer.valueOf(8).compareTo(i), (e, i) -> i).intValue(),3);
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).binarySearch(i -> Integer.valueOf(9).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).binarySearch(i -> Integer.valueOf(10).compareTo(i), (e, i) -> i).intValue(),4);
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).binarySearch(i -> Integer.valueOf(11).compareTo(i), (e, i) -> i).intValue(),4);
    }

    @Test
    public void testStream() {
        assertEquals(mappedBranch2(2,4,i -> i).stream().collect(Collectors.toList()),Arrays.asList(2, 4));
        assertEquals(mappedBranch3(2,4, 6,i -> i).stream().collect(Collectors.toList()),Arrays.asList(2, 4, 6));
        assertEquals(mappedBranch(branch(2,4),branch(6,8,10),i -> i).stream().collect(Collectors.toList()),Arrays.asList(2, 4, 6, 8, 10));
        
        ListIterator<Integer> iter = mappedBranch2(2,4,i -> i).iterator();
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
        hm.add(mappedBranch2(2,3,i -> i));
        assertTrue(hm.contains(mappedBranch2(2,3,i -> i)));
        assertFalse(hm.contains(mappedBranch2(2,3,i -> i + 1)));
        assertFalse(hm.contains(new Leaf<>(2)));
    }
}
