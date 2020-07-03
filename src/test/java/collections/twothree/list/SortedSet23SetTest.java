package collections.twothree.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import org.junit.Test;

public class SortedSet23SetTest {

    @Test
    public void testEmpty() {
        assertEquals(SortedSet23.empty().asCollection(), Collections.emptySet());
        assertEquals(SortedSet23.of().asCollection(), Collections.emptySet());
    }
	
	@Test
	public void testAsSet() {
		SortedSet23<String> l1 = SortedSet23.of("1","2","3","4","5",null);
		assertEquals(l1.asCollection(), new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", null)));
		assertEquals(l1.asCollection(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5", null)));
		assertNotEquals(l1.asCollection(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5")));
		assertTrue(l1.asCollection().contains("5"));
		assertFalse(l1.asCollection().contains("6"));
		
		assertEquals(SortedSet23.of("1","2","3","4","5").asCollection(), new TreeSet<>(Arrays.asList("1","2","3","4","5")));
        assertEquals(SortedSet23.of("1","2","3","4","5").asCollection().subSet("2", "5"), new TreeSet<>(Arrays.asList("1","2","3","4","5")).subSet("2", "5"));
        assertEquals(SortedSet23.of("1","2","3","4","5").asCollection().headSet("3"), new TreeSet<>(Arrays.asList("1","2","3","4","5")).headSet("3"));
        assertEquals(SortedSet23.of("1","2","3","4","5").asCollection().tailSet("3"), new TreeSet<>(Arrays.asList("1","2","3","4","5")).tailSet("3"));
        assertEquals(SortedSet23.of("1","2","3","4","5").asCollection().first(), "1");
        assertEquals(SortedSet23.of("1","2","3","4","5").asCollection().last(), "5");
        assertThrows(NoSuchElementException.class, ()->SortedSet23.of().asCollection().first());
        assertThrows(NoSuchElementException.class, ()->SortedSet23.of().asCollection().last());
	}
}
