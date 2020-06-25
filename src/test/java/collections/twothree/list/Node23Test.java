package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Node23Test {
    private static <T> Branch<T> branch(T a, T b) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b));
    }
    private static <T> Branch<T> branch(T a, T b, T c) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b),new Leaf<>(c));
    }
  
    @Test
    public void testIsValid() {
       assertTrue(new Leaf<>("1").isValid(1));
       assertTrue(branch("2","3").isValid(2));
       assertTrue(branch("1","2","3").isValid(2));
       assertFalse(new Branch<>(branch("2","3"), new Leaf<>("1"), new Leaf<>("4")).isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), branch("2","3"), new Leaf<>("4")).isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), new Leaf<>("4"), branch("2","3")).isValid(3));
       assertFalse(new Branch<>(branch("2","3"), new Leaf<>("1")).isValid(3));
       assertFalse(new Branch<>(new Leaf<>("1"), branch("2","3")).isValid(3));
       assertTrue(new Branch<>(branch("2","3"), branch("4","5")).isValid(3));
    }
    @Test
    public void testHead() {
        assertEquals(new Leaf<>("1").head(0), null);
        assertEquals(new Leaf<>("1").head(1), new Leaf<>("1"));

        assertEquals(branch("2","3").head(0), null);
        assertEquals(branch("2","3").head(1), new Leaf<>("2"));
        assertEquals(branch("2","3").head(2), branch("2","3"));

        assertEquals(branch("2","3","4").head(0), null);
        assertEquals(branch("2","3","4").head(1), new Leaf<>("2"));
        assertEquals(branch("2","3","4").head(2), branch("2","3"));
        assertEquals(branch("2","3","4").head(3), branch("2","3", "4"));

        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).head(0), null);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).head(1), new Leaf<>("2"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).head(2), branch("2", "3"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).head(3), branch("2", "3", "4"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).head(4), new Branch<>(branch("2","3"),branch("4","5")));
    }
    @Test
    public void testTail() {
        assertEquals(new Leaf<>("1").tail(0), new Leaf<>("1"));
        assertEquals(new Leaf<>("1").tail(1), null);

        assertEquals(branch("2","3").tail(2), null);
        assertEquals(branch("2","3").tail(1), new Leaf<>("3"));
        assertEquals(branch("2","3").tail(0), branch("2","3"));

        assertEquals(branch("2","3","4").tail(3), null);
        assertEquals(branch("2","3","4").tail(2), new Leaf<>("4"));
        assertEquals(branch("2","3","4").tail(1), branch("3","4"));
        assertEquals(branch("2","3","4").tail(0), branch("2","3", "4"));

        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).tail(4), null);
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).tail(3), new Leaf<>("5"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).tail(2), branch("4", "5"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).tail(1), branch("3", "4", "5"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).tail(0), new Branch<>(branch("2","3"),branch("4","5")));
    }
    @Test
    public void testFirst() {
        assertEquals(new Leaf<>("1").first(), "1");
        assertEquals(branch("2","3").first(), "2");
        assertEquals(branch("2","3","4").first(), "2");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).first(), "2");
    }
    @Test
    public void testLast() {
        assertEquals(new Leaf<>("1").last(), "1");
        assertEquals(branch("2","3").last(), "3");
        assertEquals(branch("2","3","4").last(), "4");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).last(), "5");
    }
    @Test
    public void testMap() {
        assertEquals(new Leaf<>("1").map(s -> s+s), new Leaf<>("11"));
        assertEquals(branch("2","3").map(s -> s+s), branch("22","33"));
        assertEquals(branch("2","3","4").map(s -> s+s), branch("22","33","44"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).map(s -> s+s), new Branch<>(branch("22","33"),branch("44","55")));
    }

    @Test
    public void testGet() {
        assertEquals(new Leaf<>("1").get(0), "1");

        assertEquals(branch("2","3").get(1), "3");
        assertEquals(branch("2","3").get(0), "2");

        assertEquals(branch("2","3","4").get(2), "4");
        assertEquals(branch("2","3","4").get(1), "3");
        assertEquals(branch("2","3","4").get(0), "2");

        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).get(3), "5");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).get(2), "4");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).get(1), "3");
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).get(0), "2");
    }
    @Test
    public void testReverse() {
        assertEquals(new Leaf<>("1").reverse(), new Leaf<>("1"));
        assertEquals(branch("2","3").reverse(), branch("3","2"));
        assertEquals(branch("2","3","4").reverse(), branch("4","3","2"));
        assertEquals(new Branch<>(branch("2","3"),branch("4","5")).reverse(), new Branch<>(branch("5","4"),branch("3","2")));
    }

    @Test
    public void testBinarySearch() {
        assertEquals(new Branch<>(branch(2,4),branch(6,8)).binarySearch(i -> Integer.valueOf(2).compareTo(i), (e, i) -> i).intValue(),0);
        assertEquals(new Branch<>(branch(2,4),branch(6,8)).binarySearch(i -> Integer.valueOf(3).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(new Branch<>(branch(2,4),branch(6,8)).binarySearch(i -> Integer.valueOf(4).compareTo(i), (e, i) -> i).intValue(),1);
        assertEquals(new Branch<>(branch(2,4),branch(6,8)).binarySearch(i -> Integer.valueOf(5).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(new Branch<>(branch(2,4),branch(6,8)).binarySearch(i -> Integer.valueOf(6).compareTo(i), (e, i) -> i).intValue(),2);
        assertEquals(new Branch<>(branch(2,4),branch(6,8)).binarySearch(i -> Integer.valueOf(7).compareTo(i), (e, i) -> i).intValue(),3);
        assertEquals(new Branch<>(branch(2,4),branch(6,8)).binarySearch(i -> Integer.valueOf(8).compareTo(i), (e, i) -> i).intValue(),3);
    }
}
