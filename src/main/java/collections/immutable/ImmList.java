package collections.immutable;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents an Immutable list.   Elements are arranged as they
 * are in an array, indexed by integer.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.
 * Operations like add, remove, insertListAt return lists with the specified
 * operation applied, leaving the original list unmodified.   In addition,
 * these operations are *performant*, usually taking no more than O(log n).
 * 
 * @param <E> The type of the elements.
 */
public interface ImmList<E> extends ImmCollection<E> {
    /**
     * Returns a new list with <code>function</code> applied to all elements of this list.
     * <p>This operation is O(1).
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(3, 4, 5).map(a -&gt; a + 1).asCollection().equals(Arrays.asList(4,5,6));
     * </pre>
     * @param <F> The new type of the elements.
     * @param function The mapping function
     * @return A new List23 representing the map of this one
     */
    public <F> ImmList<F> map(final Function<E, F> function);

	/**
	 * Returns a classic "read only java List" view of the list.
	 * <p>This operation is O(1).
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).asCollection().equals(Arrays.asList(6, 1, 6, 8));
     * </pre>
	 * @return A classic "read only java List" view of the list
	 */
	public List<E> asCollection();
	
	/**
	 * Returns <code>list[index]</code>.
	 * <p>This operation is O(log n) where n = |this|.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).getAt(2) == 6;
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
     *     assert ImmCollections.asList(6, 1, 6, 8).indexOf(1) == 1;
     *     assert ImmCollections.asList(6, 1, 6, 8).indexOf(6) == 0;
     *     assert ImmCollections.asList(6, 1, 6, 8).indexOf(7) == -1;
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
     *     assert ImmCollections.asList(6, 1, 6, 8).lastIndexOf(1) == 1;
     *     assert ImmCollections.asList(6, 1, 6, 8).lastIndexOf(6) == 2;
     *     assert ImmCollections.asList(6, 1, 6, 8).lastIndexOf(7) == -1;
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
     *     assert ImmCollections.asList(6, 1, 6, 8).remove(1).asCollection().equals(Arrays.asList(6, 6, 8));
     *     assert ImmCollections.asList(6, 1, 6, 8).remove(6).asCollection().equals(Arrays.asList(1, 6, 8));
     *     assert ImmCollections.asList(6, 1, 6, 8).remove(7).asCollection().equals(Arrays.asList(6, 1, 6, 8));
     * </pre>
     * @param element The element to remove
     * @return A list with <code>element</code> removed
     */
    @Override
    public ImmList<E> remove(final E element);
    
    /**
     * Returns a list with only items that match <code>filter</code>.
     * <p>This operation is O(n log n + n * k) where n = |this| and k = O(filter.test).
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).filter(e -&gt; e != 6 ).asCollection().equals(Arrays.asList(1, 8));
     * </pre>
     * @param filter The filter to apply
     * @return A list with <code>filter</code> applied
     */
    @Override
    public ImmList<E> filter(final Predicate<E> filter);
   /**
     * Returns a list whose items also appear <code>other</code>.
     * <p>This operation is O(n log n) where n = |this| + |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).retain(Arrays.asList(6,1)).asCollection().equals(Arrays.asList(6, 1, 6));
     * </pre>
     * @param other The items to match.
     * @return a list whose items also appear in another list
     */
    @Override
    public ImmList<E> retain(final Iterable<? extends E> other);
    
    /**
     * Returns a list whose items don't appear <code>other</code>.
     * <p>This operation is O(n log n) where n = |this| + |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).removeAllIn(Arrays.asList(1)).asCollection().equals(Arrays.asList(6, 6, 8));
     * </pre>
     * @param other The items to match.
     * @return a list whose items don't appear in another list
     */
    @Override
    public ImmList<E> removeAllIn(final Iterable<? extends E> other);
    
    /**
	 * Returns a list with <code>element</code> added to the end.
	 * <p>This operation is O(log n) where n = |this|.
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).add(9).asCollection().equals(Arrays.asList(6, 1, 6, 8, 9));
     *     assert ImmCollections.asList(6, 1, 6, 8).add(1).asCollection().equals(Arrays.asList(6, 1, 6, 8, 1));
     * </pre>
	 * @param element The element to add.
	 * @return A list with <code>element</code> added to the end
	 */
    @Override
	public ImmList<E> add(final E element);
	
    /**
	 * Returns a new list with <code>list[index] == element</code>.
     * <p>This operation is O(log n) where n = |this|.
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).setAt(2, 3).asCollection().equals(Arrays.asList(6, 1, 3, 8));
     * </pre>
     * @param index The index. Must be in range <code>[0, size]</code>.
	 * @param element The element to set
	 * @return A new list with <code>list[index] == element</code>
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt;= size.
	 */
	public ImmList<E> setAt(final int index, final E element);
	
	/**
	 * Returns a list with <code>element</code> inserted at <code>index</code>.
     * <p>This operation is O(log n) where n = |this|.
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).insertAt(2, 3).asCollection().equals(Arrays.asList(6, 1, 3, 6, 8));
     * </pre>
     * @param index The index. Must be in range <code>[0, size]</code>.
	 * @param element The element to insert
	 * @return A list with the given element set at the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size.
	 */
	public ImmList<E> insertAt(final int index, final E element);
	
	/**
	 * Returns a list with <code>index</code> removed.
     * <p>This operation is O(log n) where n = |this|.
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).removeAt(2).asCollection().equals(Arrays.asList(6, 1, 8));
     * </pre>
     * @param index The index. Must be in range <code>[0, size - 1]</code>
	 * @return A list with the given index removed
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt;= size.
	 */
	public ImmList<E> removeAt(final int index);
	
    /**
     * Returns a list with range <code>[low, high - 1]</code> removed.
     * <p>This operation is O(log n) where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).removeRange(1,3).asCollection().equals(Arrays.asList(6, 8));
     *     assert ImmCollections.asList(6, 1, 6, 8).removeRange(1,1).asCollection().equals(Arrays.asList(6, 1, 6, 8));
     *     assert ImmCollections.asList(6, 1, 6, 8).removeRange(0,4).asCollection().equals(Arrays.asList());
     * </pre>
     * @param low The low index (inclusive).   Must be in range <code>[0, high]</code>
     * @param high The high index (exclusive).   Must be in range <code>[0, size]</code>
     * @return A list with the given list appended to the end
     * @throws IndexOutOfBoundsException if low &lt; 0 or low &gt; high or high &gt; size
     */
    public ImmList<E> removeRange(final int low, final int high);
    
    /**
     * Returns a list with range <code>[low, high - 1]</code> replaced with <code>other</code>.
     * <p>This operation is O(log n) where n = |this| + |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).replaceRange(1,3,ImmCollections.asList(7)).asCollection().equals(Arrays.asList(6, 7, 8));
     *     assert ImmCollections.asList(6, 1, 6, 8).replaceRange(1,3,ImmCollections.asList(7,5,3)).asCollection().equals(Arrays.asList(6, 7, 5, 3, 8));
     * </pre>
     * @param low The low index (inclusive).   Must be in range <code>[0, high]</code>
     * @param high The high index (exclusive).   Must be in range <code>[0, size]</code>
     * @param other The list to insert
     * @return A list with the given range replaced with another list
     * @throws IndexOutOfBoundsException if low &lt; 0 or low &gt; high or high &gt; size
     */
    public ImmList<E> replaceRange(final int low, final int high, final ImmList<E> other);
    
    /**
     * Returns a list with <code>other</code> inserted at <code>index</code>. 
     * <p>This operation is O(log n) where n = |this| + |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).insertListAt(2,ImmCollections.asList(7)).asCollection().equals(Arrays.asList(6, 1, 7, 6, 8));
     *     assert ImmCollections.asList(6, 1, 6, 8).insertListAt(2,ImmCollections.asList(7,5,3)).asCollection().equals(Arrays.asList(6, 1, 7, 5, 3, 6, 8));
     * </pre>
     * @param index The index.   Must be in range <code>[0, size]</code>
     * @param other The list to insert
     * @return A list with another list inserted at the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size.
     */
    public ImmList<E> insertListAt(final int index, final ImmList<E> other);
	
	/**
	 * Returns a list with <code>other</code> appended to the end.
     * <p>This operation is O(log n) where n = |this| + |other|.
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).appendList(ImmCollections.asList(7)).asCollection().equals(Arrays.asList(6, 1, 6, 8, 7));
     *     assert ImmCollections.asList(6, 1, 6, 8).appendList(ImmCollections.asList(7,5,3)).asCollection().equals(Arrays.asList(6, 1, 6, 8, 7, 5, 3));
     * </pre>
	 * @param other The list to append
	 * @return A list with the given list appended to the end
	 */
	public ImmList<E> appendList(final ImmList<E> other);
	
	/**
	 * Returns a list with all indexes &gt;= <code>index</code>.
	 * <p>This operation is O(log n) where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE. The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).tailAt(2).asCollection().equals(Arrays.asList(6,8));
     *     assert ImmCollections.asList(6, 1, 6, 8).tailAt(4).asCollection().equals(Arrays.asList());
     *     assert ImmCollections.asList(Arrays.asList(6, 1, 6,  8)).tailAt(0).asCollection().equals(Arrays.asList(6, 1, 6, 8));
     * </pre>
	 * @param index The chopping point (inclusive).  Must be in range <code>[0, size]</code>
	 * @return A list with all indexes &gt;= the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size
	 */
	public ImmList<E> tailAt(final int index);
	
	/**
	 * Returns a list with all indexes &lt; <code>index</code>.
     * <p>This operation is O(log n) where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE. The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).headAt(2).asCollection().equals(Arrays.asList(6, 1));
     *     assert ImmCollections.asList(6, 1, 6, 8).headAt(4).asCollection().equals(Arrays.asList(6, 1, 6, 8));
     *     assert ImmCollections.asList(6, 1, 6, 8).headAt(0).asCollection().equals(Arrays.asList());
     * </pre>
	 * @param index The chopping point (exclusive).  Must be in range <code>[0, size]</code>
	 * @return A list with all indexes &lt; the specified index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size
	 */
	public ImmList<E> headAt(final int index);
	
	/**
	 * Returns a list with all indexes that fall in range <code>[low, high - 1]</code>.
     * <p>This operation is O(log n) where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE. The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).getRange(1, 3).asCollection().equals(Arrays.asList(1, 6));
     * </pre>
     * @param low The low index (inclusive).   Must be in range <code>[0, high]</code>
     * @param high The high index (exclusive).   Must be in range <code>[0, size]</code>
	 * @return A list with all indexes &gt;= from and &lt; to
     * @throws IndexOutOfBoundsException if low &lt; 0 or low &gt; high or high &gt; size
	 */
	public ImmList<E> getRange(final int low, final int high);
	
	/**
	 * Returns a list that is the original list reversed.
	 * <p>This operation is O(1).
	 * <p>THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * <pre>
     * Example:
     *     assert ImmCollections.asList(6, 1, 6, 8).reversed().asCollection().equals(Arrays.asList(8, 6, 1, 6));
     * </pre>
	 * @return A list that is the original list reversed
	 */
	public ImmList<E> reversed();
	
	@Override
	ListIterator<E> iterator();
}
