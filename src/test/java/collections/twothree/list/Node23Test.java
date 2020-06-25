package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Node23Test {
    private static <T> Node23<T> branch(T a, T b) {
        return new Branch<>(new Leaf<>(a),new Leaf<>(b));
    }
    private static <T> Node23<T> branch(T a, T b, T c) {
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
}
