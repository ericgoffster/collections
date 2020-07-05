package collections.twothree.list;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents an Immutable list as a 23-tree.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.  The object is not modified in any way.
 * <p>A 23-tree is a semi-balanced tree, each branch has 2 or 3 nodes.
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
public interface List23<E> extends ImmCollection<E> {
    /**
     * Returns a new list with <code>function</code> applied to all elements of this list.
     * <p>This operation is O(1).
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(3, 4, 5)).map(a -&gt; a + 1).asCollection().equals(Arrays.asList(4,5,6));
     * </pre>
     * @param <F> The new type of the elements.
     * @param function The mapping function
     * @return A new List23 representing the map of this one
     */
    public <F> List23<F> map(final Function<E, F> function);

	/**
	 * Returns a classic "read only java List" view of the list.
	 * <p>This operation is O(1).
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).asCollection().equals(Arrays.asList(6, 1, 6, 8));
     * </pre>
	 * @return A classic "read only java List" view of the list
	 */
	public List<E> asCollection();
	
	/**
	 * Returns <code>list[index]</code>.
	 * <p>This operation is O(log n) where n = |this|.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).getAt(2) == 6;
     * </pre>
     * @param index The index. Must be in range <code>[0, size - 1]</code>.
	 * @return <code>list[index]</code>
	 * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt;= size
	 */
	public E getAt(final int index);
	
    /**
     * Returns the index of the first occurrence of <code>element</code>.
     * <p>This operation is O(n) where n = |this|.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).indexOf(1) == 1;
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).indexOf(6) == 0;
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).indexOf(7) == -1;
     * </pre>
     * @param element The element to look for
     * @return The index of <code>element</code>, -1 if not found
     */
	public int indexOf(final E element);
	
    /**
     * Returns the index of the last occurrence of <code>element</code>.
     * <p>This operation is O(n) where n = |this|.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).lastIndexOf(1) == 1;
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).lastIndexOf(6) == 2;
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).lastIndexOf(7) == -1;
     * </pre>
     * @param element The element to look for
     * @return The last index of <code>element</code>, -1 if not found
     */
    public int lastIndexOf(final E element);
    
    /**
     * Returns a list with the first element matching <code>element</code> removed.
     * <p>This operation is O(n) where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).remove(1).asCollection().equals(Arrays.asList(6, 6, 8));
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).remove(6).asCollection().equals(Arrays.asList(1, 6, 8));
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).remove(7).asCollection().equals(Arrays.asList(6, 1, 6, 8));
     * </pre>
     * @param element The element to remove
     * @return A list with <code>element</code> removed
     */
    @Override
    public List23<E> remove(final E element);
    
    /**
     * Returns a list with only items that match <code>filter</code>.
     * <p>This operation is O(n log n + n * k) where n = |this| and k = O(filter.test).
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).filter(e -&gt; e != 6 ).asCollection().equals(Arrays.asList(1, 8));
     * </pre>
     * @param filter The filter to apply
     * @return A list with <code>filter</code> applied
     */
    @Override
    public List23<E> filter(final Predicate<E> filter);
   /**
     * Returns a list whose items also appear <code>other</code>.
     * <p>This operation is O(n log n) where n = |this| + |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).retain(Arrays.asList(6,1)).asCollection().equals(Arrays.asList(6, 1, 6));
     * </pre>
     * @param other The items to match.
     * @return a list whose items also appear in another list
     */
    @Override
    public List23<E> retain(final Iterable<? extends E> other);
    
    /**
     * Returns a list whose items don't appear <code>other</code>.
     * <p>This operation is O(n log n) where n = |this| + |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).removeAllIn(Arrays.asList(1)).asCollection().equals(Arrays.asList(6, 6, 8));
     * </pre>
     * @param other The items to match.
     * @return a list whose items don't appear in another list
     */
    @Override
    public List23<E> removeAllIn(final Iterable<? extends E> other);
    
    /**
	 * Returns a list with <code>element</code> added to the end.
	 * <p>This operation is O(log n) where n = |this|.
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).add(9).asCollection().equals(Arrays.asList(6, 1, 6, 8, 9));
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).add(1).asCollection().equals(Arrays.asList(6, 1, 6, 8, 1));
     * </pre>
	 * @param element The element to add.
	 * @return A list with <code>element</code> added to the end
	 */
    @Override
	public List23<E> add(final E element);
	
    /**
	 * Returns a new list with <code>list[index] == element</code>.
     * <p>This operation is O(log n) where n = |this|.
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).setAt(2, 3).asCollection().equals(Arrays.asList(6, 1, 3, 8));
     * </pre>
     * @param index The index. Must be in range <code>[0, size]</code>.
	 * @param element The element to set
	 * @return A new list with <code>list[index] == element</code>
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt;= size.
	 */
	public List23<E> setAt(final int index, final E element);
	
	/**
	 * Returns a list with <code>element</code> inserted at <code>index</code>.
     * <p>This operation is O(log n) where n = |this|.
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).insertAt(2, 3).asCollection().equals(Arrays.asList(6, 1, 3, 6, 8));
     * </pre>
     * @param index The index. Must be in range <code>[0, size]</code>.
	 * @param element The element to insert
	 * @return A list with the given element set at the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size.
	 */
	public List23<E> insertAt(final int index, final E element);
	
	/**
	 * Returns a list with <code>index</code> removed.
     * <p>This operation is O(log n) where n = |this|.
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).removeAt(2).asCollection().equals(Arrays.asList(6, 1, 8));
     * </pre>
     * @param index The index. Must be in range <code>[0, size - 1]</code>
	 * @return A list with the given index removed
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt;= size.
	 */
	public List23<E> removeAt(final int index);
	
    /**
     * Returns a list with range <code>[low, high - 1]</code> removed.
     * <p>This operation is O(log n) where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).removeRange(1,3).asCollection().equals(Arrays.asList(6, 8));
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).removeRange(1,1).asCollection().equals(Arrays.asList(6, 1, 6, 8));
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).removeRange(0,4).asCollection().equals(Arrays.asList());
     * </pre>
     * @param low The low index (inclusive).   Must be in range <code>[0, high]</code>
     * @param high The high index (exclusive).   Must be in range <code>[0, size]</code>
     * @return A list with the given list appended to the end
     * @throws IndexOutOfBoundsException if low &lt; 0 or low &gt; high or high &gt; size
     */
    public List23<E> removeRange(final int low, final int high);
    
    /**
     * Returns a list with range <code>[low, high - 1]</code> replaced with <code>other</code>.
     * <p>This operation is O(log n) where n = |this| + |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).replaceRange(1,3,List23.singleton(7)).asCollection().equals(Arrays.asList(6, 7, 8));
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).replaceRange(1,3,List23.of(Arrays.asList(7,5,3))).asCollection().equals(Arrays.asList(6, 7, 5, 3, 8));
     * </pre>
     * @param low The low index (inclusive).   Must be in range <code>[0, high]</code>
     * @param high The high index (exclusive).   Must be in range <code>[0, size]</code>
     * @param other The list to insert
     * @return A list with the given range replaced with another list
     * @throws IndexOutOfBoundsException if low &lt; 0 or low &gt; high or high &gt; size
     */
    public List23<E> replaceRange(final int low, final int high, final List23<E> other);
    
    /**
     * Returns a list with <code>other</code> inserted at <code>index</code>. 
     * <p>This operation is O(log n) where n = |this| + |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).insertListAt(2,List23.singleton(7)).asCollection().equals(Arrays.asList(6, 1, 7, 6, 8));
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).insertListAt(2,List23.of(Arrays.asList(7,5,3))).asCollection().equals(Arrays.asList(6, 1, 7, 5, 3, 6, 8));
     * </pre>
     * @param index The index.   Must be in range <code>[0, size]</code>
     * @param other The list to insert
     * @return A list with another list inserted at the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size.
     */
    public List23<E> insertListAt(final int index, final List23<E> other);
	
	/**
	 * Returns a list with <code>other</code> appended to the end.
     * <p>This operation is O(log n) where n = |this| + |other|.
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).appendList(List23.singleton(7)).asCollection().equals(Arrays.asList(6, 1, 6, 8, 7));
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).appendList(List23.of(Arrays.asList(7,5,3))).asCollection().equals(Arrays.asList(6, 1, 6, 8, 7, 5, 3));
     * </pre>
	 * @param other The list to append
	 * @return A list with the given list appended to the end
	 */
	public List23<E> appendList(final List23<E> other);
	
	/**
	 * Returns a list with all indexes &gt;= <code>index</code>.
	 * <p>This operation is O(log n) where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE. The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).tailAt(2).asCollection().equals(Arrays.asList(6,8));
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).tailAt(4).asCollection().equals(Arrays.asList());
     *     assert List23.of(Arrays.asList(6, 1, 6,  8)).tailAt(0).asCollection().equals(Arrays.asList(6, 1, 6, 8));
     * </pre>
	 * @param index The chopping point (inclusive).  Must be in range <code>[0, size]</code>
	 * @return A list with all indexes &gt;= the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size
	 */
	public List23<E> tailAt(final int index);
	
	/**
	 * Returns a list with all indexes &lt; <code>index</code>.
     * <p>This operation is O(log n) where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE. The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).headAt(2).asCollection().equals(Arrays.asList(6, 1));
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).headAt(4).asCollection().equals(Arrays.asList(6, 1, 6, 8));
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).headAt(0).asCollection().equals(Arrays.asList());
     * </pre>
	 * @param index The chopping point (exclusive).  Must be in range <code>[0, size]</code>
	 * @return A list with all indexes &lt; the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size
	 */
	public List23<E> headAt(final int index);
	
	/**
	 * Returns a list with all indexes that fall in range <code>[low, high - 1]</code>.
     * <p>This operation is O(log n) where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE. The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).getRange(1, 3).asCollection().equals(Arrays.asList(1, 6));
     * </pre>
     * @param low The low index (inclusive).   Must be in range <code>[0, high]</code>
     * @param high The high index (exclusive).   Must be in range <code>[0, size]</code>
	 * @return A list with all indexes &gt;= from and &lt; to
     * @throws IndexOutOfBoundsException if low &lt; 0 or low &gt; high or high &gt; size
	 */
	public List23<E> getRange(final int low, final int high);
	
	/**
	 * Returns a list that is the original list reversed.
	 * <p>This operation is O(1).
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert List23.of(Arrays.asList(6, 1, 6, 8)).reversed().asCollection().equals(Arrays.asList(8, 6, 1, 6));
     * </pre>
	 * @return A list that is the original list reversed
	 */
	public List23<E> reversed();
	
	@Override
	ListIterator<E> iterator();
}
