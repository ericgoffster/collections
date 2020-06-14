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
        assertEquals(SortedSet23.empty().asSet(), Collections.emptySet());
        assertEquals(SortedSet23.of().asSet(), Collections.emptySet());
    }
	
	@Test
	public void testAsSet() {
		SortedSet23<String> l1 = SortedSet23.of("1","2","3","4","5",null);
		assertEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", null)));
		assertEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5", null)));
		assertNotEquals(l1.asSet(), new HashSet<>(Arrays.asList("1", "2", "4", "3", "5")));
		assertTrue(l1.asSet().contains("5"));
		assertFalse(l1.asSet().contains("6"));
		
		assertEquals(SortedSet23.of("1","2","3","4","5").asSet(), new TreeSet<>(Arrays.asList("1","2","3","4","5")));
        assertEquals(SortedSet23.of("1","2","3","4","5").asSet().subSet("2", "5"), new TreeSet<>(Arrays.asList("1","2","3","4","5")).subSet("2", "5"));
        assertEquals(SortedSet23.of("1","2","3","4","5").asSet().headSet("3"), new TreeSet<>(Arrays.asList("1","2","3","4","5")).headSet("3"));
        assertEquals(SortedSet23.of("1","2","3","4","5").asSet().tailSet("3"), new TreeSet<>(Arrays.asList("1","2","3","4","5")).tailSet("3"));
        assertEquals(SortedSet23.of("1","2","3","4","5").asSet().first(), "1");
        assertEquals(SortedSet23.of("1","2","3","4","5").asSet().last(), "5");
        assertThrows(NoSuchElementException.class, ()->SortedSet23.of().asSet().first());
        assertThrows(NoSuchElementException.class, ()->SortedSet23.of().asSet().last());
	}
}
