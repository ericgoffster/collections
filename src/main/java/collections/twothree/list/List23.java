package collections.twothree.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.granitesoft.requirement.Requirements;

/**
 * Represents a list as a 23-tree.
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
	    assert isValid(root);
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
        return root == null ? empty() : new List23<>(new MappedNode23<E, F>(root, function));
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

    public static <E> List23<E> ofFiltered(final Predicate<E> filter, final Iterable<? extends E> elements) {
        Requirements.require(filter, Requirements.notNull(), () -> "filter");
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
        return quickConstruct(new FilteredIterator<>(new LeafIterator<>(elements), filter));
    }

    /**
     * Easy construction of a sorted list, with dups removed.
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
	 * Easy construction of a sorted list.
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
	public List<E> asList() {
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
		return get(root, verifyIndex("index", index, 0, size() - 1));
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
	    return root == null ? -1: find(root, element, 0);
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
	           reversed().headAt(size() - index).reversed();
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
		return index == size() ?  this : index == 0 ? empty(): new List23<>(head(root, index));
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
    	return asList().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
    	if (!(obj instanceof List23)) {
    		return false;
    	}
    	final List23<?> other = (List23<?>)obj;
    	return asList().equals(other.asList());
    }

    @Override
    public String toString() {
    	return asList().toString();
    }

    @Override
    public ListIterator<E> iterator() {
    	return new NodeIterator<E>(root);
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), 0);
    }

    /**
     * Returns the elements as a stream.
     * @return The elements as a stream
     */
    @Override
    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    static int verifyIndex(String name, int index, int low, int high) {
        if (index < low || index > high) {
            throw new IndexOutOfBoundsException(String.format("%s: %d, Low: %d, High %d", name, index, low, high));
        }
        return index;
    }

    /// Compares two elements, allowing for null.
    static <E> int unNaturalCompare(final E a, final E b) {
        if (a == null) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        
        @SuppressWarnings("unchecked")
        Comparable<? super E> ea = (Comparable<? super E>) a;
        return ea.compareTo(b);
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
	    Node23<E> b0 = nodes.next();
        if (!nodes.hasNext()) {
            return new List23<>(b0);
        }
	    return quickConstruct(new NodeConstructionIterator<E>(nodes, b0, nodes.next()));
	}

	// Search through the node for an element.
    // O(n log n)
	static <E> int find(final Node23<E> node, final E element, final int index) {
        if (node.isLeaf()) {
            return Objects.equals(node.leafValue(),element) ? index: -1;
        }
        int pos = 0;
        int j = 0;
        while(j < node.numBranches() - 1) {
            int i = find(node.getBranch(j), element, index + pos);
            if (i >= 0) {
                return i;
            }
            pos += node.getBranchSize(j++);
        }
        return find(node.getBranch(j), element, index + pos);
    }

    // Get the element at the given index.
    // O(log n)
    static <E> E get(final Node23<E> node, final int index) {
        assert index < node.size();
        if (node.isLeaf()) {
            return node.leafValue();
        }
        int pos = 0;
        int j = 0;
        while(j < node.numBranches() - 1 && index >= pos + node.getBranchSize(j)) {
            pos += node.getBranchSize(j++);
        }
        return get(node.getBranch(j), index - pos);
	}

	// Returns the concatenation of lhs and rhs.
    // The returned node will never be degenerate.
	// O(log max(m,n))
	static <E>  Node23<E> concat(final Node23<E> lhs, final Node23<E> rhs) {
	    assert lhs != null;
	    if (rhs == null) {
	        return lhs;
	    }
	    final NodePair<E> node = concat(lhs, rhs, getDepth(lhs) - getDepth(rhs));
        return node.rhs() == null? node.lhs(): new Branch<>(node.lhs(), node.rhs());
    }
	
	// Creates a branch representing the concatenation
	//   of 2 nodes.   Result will be a pair of branches, each of which are at the same depth
	//   of lhs and rhs.   However, a degenerate case may be returned
	//   of only 1 branch, which would mean that lhs was capable of
	//   absorbing rhs with a change of level.
    // O(log max(m,n))
	static <E> NodePair<E> concat(final Node23<E> lhs, final Node23<E> rhs, int depthDelta) {
        assert lhs != null;
        assert rhs != null;

        // for simplicity, make sure lhs is always the deeper branch.
	    if (depthDelta < 0) {
            return concat(rhs.reverse(), lhs.reverse(), -depthDelta).reverse();
            
        // Try to concatenate rhs onto the right most branch of lhs
	    } else if (depthDelta > 0) {
	        // Concatenate rhs to the last branch of lhs, and add back to this node.
            final NodePair<E> new_lhs_last = concat(lhs.getBranch(lhs.numBranches() - 1), rhs, depthDelta - 1);
	        if (lhs.numBranches() < 3) {
                return new_lhs_last.rhs() == null ?
	                new NodePair<>(new Branch<>(lhs.getBranch(0), new_lhs_last.lhs())):
	                new NodePair<>(new Branch<>(lhs.getBranch(0), new_lhs_last.lhs(), new_lhs_last.rhs()));
	        } else {
                return new_lhs_last.rhs() == null ?
	                new NodePair<>(new Branch<>(lhs.getBranch(0), lhs.getBranch(1), new_lhs_last.lhs())):
	                new NodePair<>(new Branch<>(lhs.getBranch(0), lhs.getBranch(1)), new Branch<>(new_lhs_last.lhs(), new_lhs_last.rhs()));
	        }
	        
	    // They are same depth, just create a pair.
	    } else {
	        return new NodePair<>(lhs, rhs);
	    }
	}

	// Returns a node where all indexes are < index, null if removing a leaf.
	// The general idea is this:
	//    if A = [B / C, D]
	//    where B is the sliced off portion, you want to return [C, D]
	//    the problem is what to do when C is no longer of the same depth of D, perhaps by a lot.
	// O(log n)
	static <E> Node23<E> head(final Node23<E> node, final int index) {
        assert index < node.size();
        return
           node.isLeaf() ? null:
           index < node.getBranchSize(0) ? head(node.getBranch(0), index):
           index - node.getBranchSize(0) < node.getBranchSize(1) ? concat(node.getBranch(0), head(node.getBranch(1), index - node.getBranchSize(0))):
           concat(new Branch<>(node.getBranch(0), node.getBranch(1)), head(node.getBranch(2), index - node.getBranchSize(0) - node.getBranchSize(1)));
	}

	// Returns depth of the node.
    // O(log n)
	static <E> int getDepth(Node23<E> node) {
        assert node != null;
		return node.isLeaf() ? 1 : (getDepth(node.getBranch(0)) + 1);
	}

	// Returns true, if the list is valid.
    // All branches are same height, and no 1 degenerate 1 branches.
    // O(n log n)
	boolean isValid() {
        return isValid(root);
    }

    // Returns true, if the node is valid.
    // All branches are same height, and no 1 degenerate 1 branches.
    // O(n log n)
    static <E> boolean isValid(Node23<E> n) {
        return n == null || isValid(n, getDepth(n));
    }

    // Returns true, if the node is valid.
    // All branches are same height, and no 1 degenerate 1 branches.
    // O(n log n)
    static <E> boolean isValid(Node23<E> n, int depth) {
        if (n.isLeaf()) {
            return depth == 1;
        }
        for(int i = 0; i < n.numBranches(); i++) {
            if (!isValid(n.getBranch(i), depth - 1)) {
                return false;
            }
        }
        return true;
    }
    
    static <E> E last(final Node23<E> node) {
        return node.isLeaf() ? node.leafValue() : last(node.getBranch(node.numBranches() - 1));
    }

    // Visits all leaves, returning an arbitrary result returned from leafVisitor
    // Warning, all elements in this list must follow order governed by this comparator
    static <T, E> T binarySearch(final Comparator<? super E> comparator, final Node23<E> node, final E element, final int index, final BiFunction<E,Integer,T> leafVisitor) {
        if (node.isLeaf()) {
            return leafVisitor.apply(node.leafValue(), index);
        }
        int pos = 0;
        int j = 0;
        while(j < node.numBranches() - 1 && comparator.compare(element, last(node.getBranch(j))) > 0) {
            pos += node.getBranchSize(j++);
        }
        return binarySearch(comparator, node.getBranch(j), element, index + pos, leafVisitor);
    }

    // Visits all leaves, returning an arbitrary result returned from leafVisitor
    // Warning, all elements in this list must follow order governed by this comparator
    <T> T binarySearch(final Comparator<? super E> comparator, final E element, final BiFunction<E,Integer,T> leafVisitor) {
        return root == null ? leafVisitor.apply(null, -1) : binarySearch(comparator, root, element, 0, leafVisitor);
    }
    
    // Warning, all elements in this list must follow order governed by this comparator
    int indexOf(final Comparator<? super E> comparator, final E element) {
        return binarySearch(comparator, element, (leaf, i) -> i < 0 ? -1 : comparator.compare(element, leaf) == 0 ? i : -1);
    }
    
    // Returns the position where the element belongs
    // Warning, all elements in this list must follow order governed by this comparator
    int naturalPosition(final Comparator<? super E> comparator, final E element) {
        return binarySearch(comparator, element, (leaf, i) -> i < 0 ? 0 : comparator.compare(element,leaf) > 0 ? (i + 1) : i);
    }

    static <E> Iterator<Leaf<E>> sortLeaves(final Comparator<? super E> comparator,
            final Iterator<Leaf<E>> elements) {
        final List<Leaf<E>> nodes = new ArrayList<>();
        elements.forEachRemaining(nodes::add);
        Collections.sort(nodes, (i,j) -> comparator.compare(i.leafValue(),j.leafValue()));
        return nodes.iterator();
    }

    @Override
    public Collection<E> asCollection() {
        return asList();
    }
}
