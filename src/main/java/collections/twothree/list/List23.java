package collections.twothree.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.granitesoft.requirement.Requirements;

/**
 * Represents an Immutable list as a 23-tree.
 * A 23-tree is a semi-balanced tree, each branch has 2 or 3 nodes.
 * The nodes are ordered such that the leftmost side of the list
 * is in the left-most branch, and the rightmost side of the list is in the right-most branch.
 * <p>
 * This version of a 23-tree is immutable.   All operations on a tree leave the original
 * tree unchanged.
 * <p>
 * The following operations are all O(log n) worst case:
 * <ul>
 *     <li>{@link List23#insertAt(int, Object)}</li>
 *     <li>{@link List23#removeRange(int, int)}</li>
 *     <li>{@link List23#appendList(List23)}</li>
 *     <li>{@link List23#getAt(int)}</li>
 * </ul>
 * 
 * Which is significant, there is no need for a builder.
 * @param <E> The type of the elements.
 */
public final class List23<E> implements Collection23<E> {
    /**
     * The root of the tree.
     */
	final Node23<E> root;
	
	List23(final Node23<E> root) {
	    assert root == null || root.isValid(root.getDepth());
		this.root = root;
	}

    /**
     * Returns a new list with <code>function</code> applied to all elements of this list.
     * <p>This operation is O(1).
     * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(3, 4, 5).map(a -&gt; a + 1) == [4,5,6]
     * </pre>
     * @param <F> The new type of the elements.
     * @param function The mapping function
     * @return A new List23 representing the map of this one
     */
    public <F> List23<F> map(Function<E, F> function) {
        return root == null ? empty() : new List23<>(root.map(function));
    }

    /**
     * The empty list.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     List23.empty() == []
     * </pre>
     * @param <E> The type of the elements.
     * @return The List23 representation of empty
     */
    public static <E> List23<E> empty() {
        return new List23<>(null);
    }

    /**
     * Construction of a single list of <code>element</code>.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     List23.of(5) == [5]
     * </pre>
     * @param <E> The type of the element.
     * @param element The element
     * @return The List23 representation of the element
     */
    public static <E> List23<E> of(final E element) {
        return new List23<>(new Leaf<>(element));
    }

	/**
	 * Easy construction of list.
	 * <p>This operation is O(n log n) where n = |elements|.
     * <pre>
     * Example:
     *     List23.of(5, 7, 9) == [5, 7, 9]
     *     List23.of(5, 7, 5) == [5, 7, 5]
     * </pre>
     * @param <E> The type of the elements.
	 * @param elements The collections of elements
	 * @return The List23 representation of "elements"
	 */
	@SafeVarargs
    @SuppressWarnings("varargs")
	public static <E> List23<E> of(final E ... elements) {
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
		return of(new ArrayIterable<>(elements));
	}

	/**
	 * Easy construction of list.
     * <p>This operation is O(n log n) where n = |elements|.
     * <pre>
     * Example:
     *     List23.of(Arrays.asList(5, 7, 9)) == [5, 7, 9]
     *     List23.of(Arrays.asList(5, 7, 5)) == [5, 7, 5]
     * </pre>
     * @param <E> The type of the elements.
	 * @param elements The array of elements
	 * @return The List23 representation of "elements"
	 */
	public static <E> List23<E> of(final Iterable<? extends E> elements) {
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
        return quickConstruct(new LeafIterator<>(elements));
	}

	/**
	 * Construction of a list formed by filtering another list.
     * @param <E> The type of the elements.
	 * @param filter The filter
	 * @param elements The elements to filter
	 * @return The List23 representation of the filtered "elements"
	 */
    public static <E> List23<E> ofFiltered(final Predicate<E> filter, final Iterable<? extends E> elements) {
        Requirements.require(filter, Requirements.notNull(), () -> "filter");
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
        return quickConstruct(new FilteredIterator<>(new LeafIterator<>(elements), filter));
    }

    /**
     * Easy construction of a sorted list, with dups removed, using a custom comparator.
     * Creates a list23 represents of the elements sorted by a comparator.
     * <p>This operation is O(n log n) where n = |elements|.
     * <pre>
     * Example:
     *     List23.ofSortedUnique(Integer::compare, Arrays.asList(6, 1, 6, 8)) == [1, 6, 8]
     * </pre>
     * @param <E> The type of the elements.
     * @param comparator The element comparator
     * @param elements The collections of elements
     * @return The sorted List23 representation of "elements"
     */
    public static <E> List23<E> ofSortedUnique(Comparator<? super E> comparator,final Iterable<? extends E> elements) {
        Requirements.require(comparator, Requirements.notNull(), () -> "comparator");
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
        return quickConstruct(new RemoveDupsIterator<>(sortLeaves(comparator, new LeafIterator<>(elements)), comparator));
    }

    /**
     * Easy construction of a sorted list, with dups removed.
     * Creates a list23 represents of the elements sorted by the natural comparator of the elements.
     * <p>This operation is O(n log n) where n = |elements|.
     * <pre>
     * Example:
     *     List23.ofSortedUnique(Integer::compare, Arrays.asList(6, 1, 6, 8)) == [1, 6, 8]
     * </pre>
     * @param <E> The type of the elements.
     * @param elements The collections of elements
     * @return The sorted List23 representation of "elements"
     */
    public static <E extends Comparable<E>> List23<E> ofSortedUnique(final Iterable<? extends E> elements) {
        return ofSortedUnique(List23::naturalCompare, elements);
    }
	
	/**
	 * Easy construction of a sorted list, using a custom comparator.
	 * Creates a list23 represents of the elements sorted by a comparator.
     * <p>This operation is O(n log n) where n = |elements|.
     * <pre>
     * Example:
     *     List23.ofSorted(Integer::compare, Arrays.asList(6, 1, 6, 8)) == [1, 6, 6, 8]
     * </pre>
     * @param <E> The type of the elements.
     * @param comparator The element comparator
	 * @param elements The collections of elements
	 * @return The sorted List23 representation of "elements"
	 */
	public static <E> List23<E> ofSorted(final Comparator<E> comparator, final Iterable<? extends E> elements) {
	    Requirements.require(comparator, Requirements.notNull(), () -> "comparator");
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
        return quickConstruct(sortLeaves(comparator, new LeafIterator<>(elements)));
	}

	/**
     * Easy construction of a sorted list.
     * Creates a List23 representation of elements sorted by a comparator.
     * <p>This operation is O(n log n) where n = |elements|.
     * <pre>
     * Example:
     *     List23.ofSorted(Arrays.asList(6, 1, 6, 8)) == [1, 6, 6, 8]
     * </pre>
     * @param <E> The type of the elements
	 * @param elements The collection of elements
	 * @return The sorted List23 representation of "elements"
	 */
	public static <E extends Comparable<E>> List23<E> ofSorted(final Iterable<? extends E> elements) {
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
		return ofSorted(List23::naturalCompare, elements);
	}

	/**
	 * Easy construction of a sorted list.
	 * Creates a List23 representation of elements sorted by their natural ordering.
     * <p>This operation is O(n log n) where n = |elements|.
     * <pre>
     * Example:
     *     List23.ofSorted(6, 1, 6, 8) == [1, 6, 6, 8]
     * </pre>
     * @param <E> The type of the elements
	 * @param elements The array of elements
	 * @return The sorted List23 representation of "elements"
	 */
	@SafeVarargs
    @SuppressWarnings("varargs")
	public static <E extends Comparable<E>> List23<E> ofSorted(final E ... elements) {
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
		return ofSorted(new ArrayIterable<>(elements));
	}

	/**
	 * Returns a classic "read only java List" view of the list.
	 * <p> This operation is O(1).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).asList() == [6, 1, 6, 8]
     * </pre>
	 * @return A classic "read only java List" view of the list
	 */
	public List<E> asCollection() {
		return new List23List<>(this);
	}
	
	/**
	 * Returns the number of elements in the list.
	 * <p> This operation is O(1).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).size() == 4
     * </pre>
	 * @return The number of elements in the list
	 */
	@Override
	public int size() {
		return root == null ? 0 : root.size();
	}
	
	/**
	 * Returns <code>list[index]</code>.
	 * <p> This operation is O(log n) where n = |this|.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).getAt(2) == 6
     * </pre>
     * @param index The index. Must be in range <code>[0, size - 1]</code>.
	 * @return <code>list[index]</code>
	 * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt;= size
	 */
	public E getAt(final int index) {
		return root.get(verifyIndex("index", index, 0, size() - 1));
	}
	
    /**
	 * Returns true if this list contains <code>element</code>.
	 * <p> This operation is O(n) where n = |this|.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).contains(2) == false
     *     List23.of(6, 1, 6, 8).contains(6) == true
     * </pre>
	 * @param element The element to look for
	 * @return true if this list contains <code>element</code>
	 */
    @Override
    public boolean contains(E element) {
        return indexOf(element) >= 0;
    }
    
    /**
     * Returns the index of the first occurrence of <code>element</code>.
     * <p> This operation is O(n) where n = |this|.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).indexOf(1) == 1
     *     List23.of(6, 1, 6, 8).indexOf(6) == 0
     *     List23.of(6, 1, 6, 8).indexOf(7) == -1
     * </pre>
     * @param element The element to look for
     * @return The index of <code>element</code>, -1 if not found
     */
	public int indexOf(E element) {
	    int i = 0;
	    for(E e: this) {
	        if (Objects.equals(e,element)) {
	            return i;
	        }
	        i++;
	    }
	    return -1;
	}
	
    /**
     * Returns the index of the last occurrence of <code>element</code>.
     * <p> This operation is O(n) where n = |this|.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).lastIndexOf(1) == 1
     *     List23.of(6, 1, 6, 8).lastIndexOf(6) == 2
     *     List23.of(6, 1, 6, 8).lastIndexOf(7) == -1
     * </pre>
     * @param element The element to look for
     * @return The last index of <code>element</code>, -1 if not found
     */
    public int lastIndexOf(E element) {
        int pos = reversed().indexOf(element);
        return pos < 0 ? -1 : size() - 1 - pos;
    }
    
    /**
     * Returns a list with the first element matching <code>element</code> removed.
     * <p> This operation is O(n) where n = |this|.
     * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).remove(1) == [6, 6, 8]
     *     List23.of(6, 1, 6, 8).remove(6) == [1, 6, 8]
     *     List23.of(6, 1, 6, 8).remove(7) == [6, 1, 6, 8]
     * </pre>
     * @param element The element to remove
     * @return A list with <code>element</code> removed
     */
    @Override
    public List23<E> remove(final E element) {
        int index = indexOf(element);
        return index < 0 ? this : removeAt(index);
    }
    
    /**
     * Returns a list with only items that match <code>filter</code>.
     * <p> This operation is O(n log n + n * k) where n = |this| and k = O(filter.test).
     * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).filter(e -&gt; e != 6 ) == [1, 8]
     * </pre>
     * @param filter The filter to apply
     * @return A list with <code>filter</code> applied
     */
    @Override
    public List23<E> filter(final Predicate<E> filter) {
        return List23.ofFiltered(filter, this);
    }

   /**
     * Returns a list whose items also appear <code>other</code>.
     * <p> This operation is O(n log n) where n = |this| + |other|.
     * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).retain(Arrays.asList(6,1)) == [6, 1, 6]
     * </pre>
     * @param other The items to match.
     * @return a list whose items also appear in another list
     */
    @Override
    public List23<E> retain(final Iterable<E> other) {
        final HashSet23<E> hs = HashSet23.of(other);
        return filter(hs::contains);
    }
    
    /**
     * Returns a list whose items don't appear <code>other</code>.
     * <p> This operation is O(n log n) where n = |this| + |other|.
     * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).removeAllIn(Arrays.asList(1)) == [6, 6, 8]
     * </pre>
     * @param other The items to match.
     * @return a list whose items don't appear in another list
     */
    @Override
    public List23<E> removeAllIn(final Iterable<E> other) {
        final HashSet23<E> hs = HashSet23.of(other);
        return filter(e -> !hs.contains(e));
    }
    
    /**
	 * Returns a list with <code>element</code> added to the end.
	 * <p> This operation is O(log n) where n = |this|.
	 * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).add(9) == [6, 1, 6, 8, 9]
     *     List23.of(6, 1, 6, 8).add(1) == [6, 1, 6, 8, 1]
     * </pre>
	 * @param element The element to add.
	 * @return A list with <code>element</code> added to the end
	 */
    @Override
	public List23<E> add(final E element) {
        return replaceRange(size(), size(), List23.of(element));
	}
	
    /**
	 * Returns a new list with <code>list[index] == element</code>.
     * <p> This operation is O(log n) where n = |this|.
	 * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).setAt(2, 3) == [6, 1, 3, 8]
     * </pre>
     * @param index The index. Must be in range <code>[0, size]</code>.
	 * @param element The element to set
	 * @return A new list with <code>list[index] == element</code>
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt;= size.
	 */
	public List23<E> setAt(final int index, final E element) {
	    verifyIndex("index", index, 0, size() - 1);
	    return replaceRange(index, index + 1, List23.of(element));
	}
	
	/**
	 * Returns a list with <code>element</code> inserted at <code>index</code>.
     * <p> This operation is O(log n) where n = |this|.
	 * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).insertAt(2, 3) == [6, 1, 3, 6, 8]
     * </pre>
     * @param index The index. Must be in range <code>[0, size]</code>.
	 * @param element The element to insert
	 * @return A list with the given element set at the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size.
	 */
	public List23<E> insertAt(final int index, final E element) {
        verifyIndex("index", index, 0, size());
        return replaceRange(index, index, List23.of(element));
	}
	
	/**
	 * Returns a list with <code>index</code> removed.
     * <p> This operation is O(log n) where n = |this|.
	 * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).removeAt(2) == [6, 1, 8]
     * </pre>
     * @param index The index. Must be in range <code>[0, size - 1]</code>
	 * @return A list with the given index removed
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt;= size.
	 */
	public List23<E> removeAt(final int index) {
       verifyIndex("index", index, 0, size() - 1);
       return replaceRange(index, index + 1, empty());
	}
	
    /**
     * Returns a list with range <code>[low, high - 1]</code> removed.
     * <p> This operation is O(log n) where n = |this|.
     * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).removeRange(1,3) == [6, 8]
     *     List23.of(6, 1, 6, 8).removeRange(1,1) == [6, 1, 6, 8]
     *     List23.of(6, 1, 6, 8).removeRange(0,4) == []
     * </pre>
     * @param low The low index (inclusive).   Must be in range <code>[0, high]</code>
     * @param high The high index (exclusive).   Must be in range <code>[0, size]</code>
     * @return A list with the given list appended to the end
     * @throws IndexOutOfBoundsException if low &lt; 0 or low &gt; high or high &gt; size
     */
    public List23<E> removeRange(final int low, final int high) {
        verifyIndex("high", high, 0, size());
        verifyIndex("log", low, 0, high);
        return replaceRange(low, high, empty());
    }
    
    /**
     * Returns a list with range <code>[low, high - 1]</code> replaced with <code>other</code>.
     * <p> This operation is O(log n) where n = |this| + |other|.
     * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).replaceRange(1,3,List23.of(7)) == [6, 7, 8]
     *     List23.of(6, 1, 6, 8).replaceRange(1,3,List23.of(7,5,3)) == [6, 7, 5, 3, 8]
     * </pre>
     * @param low The low index (inclusive).   Must be in range <code>[0, high]</code>
     * @param high The high index (exclusive).   Must be in range <code>[0, size]</code>
     * @param other The list to insert
     * @return A list with the given range replaced with another list
     * @throws IndexOutOfBoundsException if low &lt; 0 or low &gt; high or high &gt; size
     */
    public List23<E> replaceRange(final int low, final int high, final List23<E> other) {
        verifyIndex("high", high, 0, size());
        verifyIndex("low", low, 0, high);
        Requirements.require(other, Requirements.notNull(), () -> "other");
        return low == high && other.size() == 0 ?
            this:
            headAt(low).appendList(other).appendList(tailAt(high));
    }
    
    /**
     * Returns a list with <code>other</code> inserted at <code>index</code>. 
     * <p> This operation is O(log n) where n = |this| + |other|.
     * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).insertListAt(2,List23.of(7)) == [6, 1, 7, 6, 8]
     *     List23.of(6, 1, 6, 8).insertListAt(2,List23.of(7,5,3)) == [6, 1, 7, 5, 3, 6, 8]
     * </pre>
     * @param index The index.   Must be in range <code>[0, size]</code>
     * @param other The list to insert
     * @return A list with another list inserted at the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size.
     */
    public List23<E> insertListAt(final int index, final List23<E> other) {
        verifyIndex("index", index, 0, size());
        Requirements.require(other, Requirements.notNull(), () -> "other");
        return replaceRange(index, index, other);
    }
	
	/**
	 * Returns a list with <code>other</code> appended to the end.
     * <p> This operation is O(log n) where n = |this| + |other|.
	 * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).appendList(List23.of(7)) == [6, 1, 6, 8, 7]
     *     List23.of(6, 1, 6, 8).appendList(List23.of(7,5,3)) == [6, 1, 6, 8, 7, 5, 3]
     * </pre>
	 * @param other The list to append
	 * @return A list with the given list appended to the end
	 */
	public List23<E> appendList(final List23<E> other) {
		Requirements.require(other, Requirements.notNull(), () -> "other");
		return other.root == null ? this:
		       root == null ? other:
		       new List23<>(concat(root, other.root));
	}
	
	/**
	 * Returns a list with all indexes &gt;= <code>index</code>.
	 * <p> This operation is O(log n) where n = |this|.
     * THIS OPERATION IS IMMUTABLE. The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).tailAt(2) == [6,8 ]
     *     List23.of(6, 1, 6, 8).tailAt(4) == []
     *     List23.of(6, 1, 6,  8).tailAt(0) == [6, 1, 6, 8]
     * </pre>
	 * @param index The chopping point (inclusive).  Must be in range <code>[0, size]</code>
	 * @return A list with all indexes &gt;= the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size
	 */
	public List23<E> tailAt(final int index) {
	    verifyIndex("index", index, 0, size());
	    return index == 0 ? this :
	           index == size() ? empty() :
	               new List23<>(root.tail(index));
	}
	
	/**
	 * Returns a list with all indexes &lt; <code>index</code>.
     * <p> This operation is O(log n) where n = |this|.
     * THIS OPERATION IS IMMUTABLE. The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).headAt(2) == [6, 1]
     *     List23.of(6, 1, 6, 8).headAt(4) == [6, 1, 6, 8]
     *     List23.of(6, 1, 6, 8).headAt(0) == []
     * </pre>
	 * @param index The chopping point (exclusive).  Must be in range <code>[0, size]</code>
	 * @return A list with all indexes &lt; the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size
	 */
	public List23<E> headAt(final int index) {
        verifyIndex("index", index, 0, size());
		return index == size() ?  this : index == 0 ? empty(): new List23<>(root.head(index));
	}
	
	/**
	 * Returns a list with all indexes that fall in range <code>[low, high - 1]</code>.
     * <p> This operation is O(log n) where n = |this|.
     * THIS OPERATION IS IMMUTABLE. The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).getRange(1, 3) == [1, 6]
     * </pre>
     * @param low The low index (inclusive).   Must be in range <code>[0, high]</code>
     * @param high The high index (exclusive).   Must be in range <code>[0, size]</code>
	 * @return A list with all indexes &gt;= from and &lt; to
     * @throws IndexOutOfBoundsException if low &lt; 0 or low &gt; high or high &gt; size
	 */
	public List23<E> getRange(final int low, final int high) {
        verifyIndex("high", high, 0, size());
	    verifyIndex("low", low, 0, high);
		return tailAt(low).headAt(high - low);
	}
	
	/**
	 * Returns a list that is the original list reversed.
	 * <p> This operation is O(1).
	 * <p> THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     List23.of(6, 1, 6, 8).reversed() == [8, 6, 1, 6]
     * </pre>
	 * @return A list that is the original list reversed
	 */
	public List23<E> reversed() {
	    if (size() < 2) {
	        return this;
	    }
	    
		return new List23<E>(root.reverse());
	}

	@Override
    public int hashCode() {
    	return asCollection().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
    	if (!(obj instanceof List23)) {
    		return false;
    	}
    	final List23<?> other = (List23<?>)obj;
    	return asCollection().equals(other.asCollection());
    }

    @Override
    public String toString() {
    	return asCollection().toString();
    }

    @Override
    public ListIterator<E> iterator() {
        if (root == null) {
            return Collections.emptyListIterator();
        }
    	return root.iterator();
    }
    
    @Override
    public Spliterator<E> spliterator() {
        if (root == null) {
            return Spliterators.emptySpliterator();
        }
        return root.spliterator();
    }

    /**
     * Returns the elements as a stream.
     * @return The elements as a stream
     */
    @Override
    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    static int verifyIndex(final String name, final int index, final int low, final int high) {
        if (index < low || index > high) {
            throw new IndexOutOfBoundsException(String.format("%s: %d, Low: %d, High %d", name, index, low, high));
        }
        return index;
    }

    /// Compares two elements, allowing for null.
	static <E extends Comparable<E>> int naturalCompare(final E a, final E b) {
		return a == null ?
				((b == null) ? 0 : -1):
				(b == null) ? 1 : a.compareTo(b);
	}

	// Quickly constructs a list from a collection of nodes.
	// O(n log n)
	static <E> List23<E> quickConstruct(final Iterator<? extends Node23<E>> nodes) {
	    if (!nodes.hasNext()) {
	        return List23.empty();
	    }
	    final Node23<E> b0 = nodes.next();
        if (!nodes.hasNext()) {
            return new List23<>(b0);
        }
	    return quickConstruct(new NodeConstructionIterator<E>(nodes, b0, nodes.next()));
	}

	// Returns the concatenation of lhs and rhs.
    // The returned node will never be degenerate.
	// O(log max(m,n))
	static <E> Node23<E> concat(final Node23<E> lhs, final Node23<E> rhs) {
	    assert lhs != null;
        assert rhs != null;
        final int depthDelta = lhs.getDepth() - rhs.getDepth();
        if (depthDelta == 0) {
            return new Branch<>(lhs, rhs);
        }
        @SuppressWarnings("rawtypes")
        final Node23[] nodes = new Node23[2];
        @SuppressWarnings("unchecked")
        final Node23<E>[] nodes2 = nodes;
        final int nodelen;
	    if (depthDelta >= 0) {
	        nodelen = append(lhs, rhs, depthDelta, nodes2, 0);
	    } else {
	        nodelen = prepend(lhs, rhs, -depthDelta, nodes2);
	    }
	    return nodelen == 1 ? nodes2[0] : new Branch<>(nodes2[0], nodes2[1]);
    }

	// Combines 2-4 nodes into a list of one or 2 nodes.
    private static <E> int combine(final Node23<E>[] arr, final int arrlen, final Node23<E>[] nodes, final int pos) {
        switch(arrlen) {
        case 2: {
            nodes[pos] = new Branch<>(arr[0], arr[1]);
            return pos + 1;
        }
        case 3: {
            nodes[pos] = new Branch<>(arr[0], arr[1], arr[2]);
            return pos + 1;
        }
        case 4: {
            nodes[pos] = new Branch<>(arr[0], arr[1]);
            nodes[pos + 1] = new Branch<>(arr[2], arr[3]);
            return pos + 2;
        }
	    default: throw new IllegalStateException();
	    }
    }

	static <E> int prepend(final Node23<E> lhs, final Node23<E> rhs, final int depthDelta, final Node23<E>[] result) {
	    assert lhs != null;
	    assert rhs != null;
	    assert depthDelta >= 0;

	    if (depthDelta == 0) {
	        result[0] = lhs;
            result[1] = rhs;
            return 2;
	    }

        @SuppressWarnings("rawtypes")
        final Node23[] arr = new Node23[4];
        @SuppressWarnings("unchecked")
        final Node23<E>[] arr2 = arr;
	    int arrlen = prepend(lhs, rhs.getBranch(0), depthDelta - 1, arr2);
        for(int i = 1; i < rhs.numBranches(); i++) {
            arr2[arrlen++] = rhs.getBranch(i);
        }
	    return combine(arr2, arrlen, result, 0);            
	}

	static <E> int append(final Node23<E> lhs, final Node23<E> rhs, final int depthDelta, final Node23<E>[] result, final int pos) {
        assert lhs != null;
        assert rhs != null;
        assert depthDelta >= 0;

        if (depthDelta == 0) {
            result[pos] = lhs;
            result[pos + 1] = rhs;
            return pos + 2;
        }

        @SuppressWarnings("rawtypes")
        final Node23[] arr = new Node23[4];
        @SuppressWarnings("unchecked")
        final Node23<E>[] arr2 = arr;
        int arrlen = 0;
        for(int i = 0; i < lhs.numBranches() - 1; i++) {
            arr2[arrlen++] = lhs.getBranch(i);
        }
        arrlen = append(lhs.getBranch(lhs.numBranches() - 1), rhs, depthDelta - 1, arr2, arrlen);
        return combine(arr2, arrlen, result, pos);            
	}

    // Warning, all elements in this list must follow order governed by this comparator
    int getIndexOf(final Function<? super E, Integer> comparator) {
        if (root == null) {
            return -1;
        }
        return root.binarySearch(comparator, (leaf, i) -> comparator.apply(leaf) == 0 ? i : -1);
    }
    
    // Returns the position where the element belongs
    // Warning, all elements in this list must follow order governed by this comparator
    int naturalPosition(final Function<? super E, Integer> comparator) {
        if (root == null) {
            return 0;
        }
        return root.binarySearch(comparator, (leaf, i) -> comparator.apply(leaf) > 0 ? (i + 1) : i);
    }

    static <E> Iterator<Leaf<E>> sortLeaves(final Comparator<? super E> comparator,
            final Iterator<Leaf<E>> elements) {
        final List<Leaf<E>> nodes = new ArrayList<>();
        elements.forEachRemaining(nodes::add);
        Collections.sort(nodes, (i,j) -> comparator.compare(i.leafValue(),j.leafValue()));
        return nodes.iterator();
    }
}
