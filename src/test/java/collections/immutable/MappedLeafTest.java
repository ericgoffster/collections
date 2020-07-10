package collections.immutable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

public class MappedLeafTest {
    private static <E, F> MappedNode23<E, F> map(E n, Function<E, F> f) {
        return new MappedNode23<>(new Leaf<>(n), f);
    }
    @Test
    public void testIsLeaf() {
       assertTrue(map("1",i -> i).isLeaf());
    }
    @Test
    public void testIsValid() {
       assertTrue(map("1",i -> i).isValid(1));
    }
    @Test
    public void testHead() {
        assertEquals(map("1",i -> i).head(0), null);
        assertEquals(map("1",i -> i).head(1), new Leaf<>("1"));
    }
    @Test
    public void testTail() {
        assertEquals(map("1",i -> i).tail(0), new Leaf<>("1"));
        assertEquals(map("1",i -> i).tail(1), null);
    }
    @Test
    public void testFirst() {
        assertEquals(map("1",i -> i).first(), "1");
    }
    @Test
    public void testLast() {
        assertEquals(map("1",i -> i).last(), "1");
    }
    @Test
    public void testMap() {
        assertEquals(map("1",i -> i).map(s -> s+s), new Leaf<>("11"));
    }

    @Test
    public void testGet() {
        assertEquals(map("1",i -> i).get(0), "1");
    }
  
    @Test
    public void testDepth() {
        assertEquals(map("1",i -> i).getDepth(), 1);
    }
    
    @Test
    public void testSize() {
        assertEquals(map("1",i -> i).size(), 1);
    }
    
    @Test
    public void testLeafValue() {
        assertEquals(map("1",i -> i).leafValue(), "1");
    }
    
    @Test
    public void testReverse() {
        assertEquals(map("1",i -> i).reverse(), new Leaf<>("1"));
    }

    @Test
    public void testBinarySearch() {
        assertEquals(map("1",i -> i).binarySearch("0"::compareTo, (e, i) -> i).intValue(),0);
        assertEquals(map("1",i -> i).binarySearch("1"::compareTo, (e, i) -> i).intValue(),0);
        assertEquals(map("1",i -> i).binarySearch("2"::compareTo, (e, i) -> i).intValue(),0);
    }
    @Test
    public void testStream() {
        assertEquals(map("1",i -> i).stream().collect(Collectors.toList()),Arrays.asList("1"));
        
        ListIterator<Integer> iter = new Leaf<>(1).reverse().iterator();
        assertEquals(iter.nextIndex(), 0);
        assertEquals(iter.next().intValue(), 1);
        assertFalse(iter.hasNext());
        assertTrue(iter.hasPrevious());
        assertEquals(iter.previousIndex(), 0);
        assertEquals(iter.previous().intValue(), 1);
        assertTrue(iter.hasNext());
        assertFalse(iter.hasPrevious());
    }
    @Test
    public void testHash() {
        HashSet<Node23<Integer>> hm = new HashSet<>();
        hm.add(map(1,i -> i));
        assertTrue(hm.contains(new Leaf<>(1)));
        assertTrue(hm.contains(new Leaf<>(1).reverse()));
        assertFalse(hm.contains(new Leaf<>(2)));
    }
}
