package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;

public class LeafTest {
    @Test
    public void testIsValid() {
       assertTrue(new Leaf<>("1").isValid(1));
    }
    @Test
    public void testHead() {
        assertEquals(new Leaf<>("1").head(0), null);
        assertEquals(new Leaf<>("1").head(1), new Leaf<>("1"));
    }
    @Test
    public void testTail() {
        assertEquals(new Leaf<>("1").tail(0), new Leaf<>("1"));
        assertEquals(new Leaf<>("1").tail(1), null);
    }
    @Test
    public void testFirst() {
        assertEquals(new Leaf<>("1").first(), "1");
    }
    @Test
    public void testLast() {
        assertEquals(new Leaf<>("1").last(), "1");
    }
    @Test
    public void testMap() {
        assertEquals(new Leaf<>("1").map(s -> s+s), new Leaf<>("11"));
    }

    @Test
    public void testGet() {
        assertEquals(new Leaf<>("1").get(0), "1");
    }
  
    @Test
    public void testDepth() {
        assertEquals(new Leaf<>("1").getDepth(), 1);
    }
    
    @Test
    public void testSize() {
        assertEquals(new Leaf<>("1").size(), 1);
    }
    
    @Test
    public void testLeafValue() {
        assertEquals(new Leaf<>("1").leafValue(), "1");
    }
    
    @Test
    public void testReverse() {
        assertEquals(new Leaf<>("1").reverse(), new Leaf<>("1"));
    }

    @Test
    public void testBinarySearch() {
        assertEquals(new Leaf<>("1").binarySearch("0"::compareTo, (e, i) -> i).intValue(),0);
        assertEquals(new Leaf<>("1").binarySearch("1"::compareTo, (e, i) -> i).intValue(),0);
        assertEquals(new Leaf<>("1").binarySearch("2"::compareTo, (e, i) -> i).intValue(),0);
    }
    @Test
    public void testStream() {
        assertEquals(new Leaf<>("1").stream().collect(Collectors.toList()),Arrays.asList("1"));
    }
}
