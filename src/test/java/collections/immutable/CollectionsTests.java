package collections.immutable;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

import org.junit.Test;

public class CollectionsTests {
    @Test
    public void testListInts() {
        assertEquals(ImmCollections.asList(1, 2, 3).asCollection(), Arrays.asList(1, 2, 3));
        {
            int[] arr = {1,2,3};
            assertEquals(ImmCollections.asList(arr).asCollection(), Arrays.asList(1, 2, 3));
        }
        assertEquals(ImmCollections.asList((short)1, (short)2, (short)3).asCollection(), Arrays.asList((short)1, (short)2, (short)3));
        assertEquals(ImmCollections.asList((byte)1, (byte)2, (byte)3).asCollection(), Arrays.asList((byte)1, (byte)2, (byte)3));
        assertEquals(ImmCollections.asList((float)1, (float)2, (float)3).asCollection(), Arrays.asList((float)1, (float)2, (float)3));
        assertEquals(ImmCollections.asList((double)1, (double)2, (double)3).asCollection(), Arrays.asList((double)1, (double)2, (double)3));
        assertEquals(ImmCollections.asList(true, false).asCollection(), Arrays.asList(true, false));
    }
    @Test
    public void testSetInts() {
        assertEquals(ImmCollections.asSet(1, 2, 3).asCollection(), new HashSet<>(Arrays.asList(1, 2, 3)));
        {
            int[] arr = {1,2,3};
            assertEquals(ImmCollections.asSet(arr).asCollection(), new HashSet<>(Arrays.asList(1, 2, 3)));
        }
        assertEquals(ImmCollections.asSet((short)1, (short)2, (short)3).asCollection(), new HashSet<>(Arrays.asList((short)1, (short)2, (short)3)));
        assertEquals(ImmCollections.asSet((byte)1, (byte)2, (byte)3).asCollection(), new HashSet<>(Arrays.asList((byte)1, (byte)2, (byte)3)));
        assertEquals(ImmCollections.asSet((float)1, (float)2, (float)3).asCollection(), new HashSet<>(Arrays.asList((float)1, (float)2, (float)3)));
        assertEquals(ImmCollections.asSet((double)1, (double)2, (double)3).asCollection(), new HashSet<>(Arrays.asList((double)1, (double)2, (double)3)));
        assertEquals(ImmCollections.asSet(true, false).asCollection(), new HashSet<>(Arrays.asList(true, false)));
    }
    @Test
    public void testSortedSetInts() {
        assertEquals(ImmCollections.asSortedSet(1, 2, 3).asCollection(), new TreeSet<>(Arrays.asList(1, 2, 3)));
        {
            int[] arr = {1,2,3};
            assertEquals(ImmCollections.asSortedSet(arr).asCollection(), new TreeSet<>(Arrays.asList(1, 2, 3)));
        }
        assertEquals(ImmCollections.asSortedSet((short)1, (short)2, (short)3).asCollection(), new TreeSet<>(Arrays.asList((short)1, (short)2, (short)3)));
        assertEquals(ImmCollections.asSortedSet((byte)1, (byte)2, (byte)3).asCollection(), new TreeSet<>(Arrays.asList((byte)1, (byte)2, (byte)3)));
        assertEquals(ImmCollections.asSortedSet((float)1, (float)2, (float)3).asCollection(), new TreeSet<>(Arrays.asList((float)1, (float)2, (float)3)));
        assertEquals(ImmCollections.asSortedSet((double)1, (double)2, (double)3).asCollection(), new TreeSet<>(Arrays.asList((double)1, (double)2, (double)3)));
        assertEquals(ImmCollections.asSortedSet(true, false).asCollection(), new TreeSet<>(Arrays.asList(true, false)));
    }

}
