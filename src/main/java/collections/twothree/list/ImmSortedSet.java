package collections.twothree.list;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Predicate;

/**
 * Represents an Immutable ordered set of elements using a {@link ImmList} as a backing store.
 * Set membership and ordering is implemented with a comparator.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.  The object is not modified in any way.
 * <p>
 * Since operations on a List23 are log n, we can represent a set
 * relatively easily as a sorted list of elements, doing straightforward
 * binary searches.   subSet, head, tail, and exclude are also O(log n),
 * guaranteeing O(log n) operations on their results.
 * <p>
 * This version of a set is immutable.   All operations on a set, leave the original
 * set unchanged.
 *
 * @param <E> The type of the elements.
 */
public interface ImmSortedSet<E> extends ImmSet<E> {
    /**
     * Returns the set of all elements in this set in range &gt;= <code>element</code>.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).ge(2).asList().asCollection().equals(Arrays.asList(2, 3, 4));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).ge(4).asList().asCollection().equals(Arrays.asList(4));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).ge(0).asList().asCollection().equals(Arrays.asList(2, 3, 4));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).ge(5).asList().asCollection().equals(Arrays.asList());
     * </pre>
     * @param element The comparison element (inclusive)
     * @return The set of all elements in this set &gt;= element
     */
	ImmSortedSet<E> ge(E element);

    /**
     * Returns the set of all elements in this set &lt; <code>element</code>.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).lt(2).asList().asCollection().equals(Arrays.asList());
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).lt(4).asList().asCollection().equals(Arrays.asList(2, 3));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).lt(0).asList().asCollection().equals(Arrays.asList());
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).lt(5).asList().asCollection().equals(Arrays.asList(2, 3, 4));
     * </pre>
     * @param element The comparison element (exclusive)
     * @return The set of all elements in this set &lt; element
     */
	ImmSortedSet<E> lt(E element);

    /**
     * Returns the set of all elements not in range <code>[low, high)</code>.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).exclude(2, 3).asList().asCollection().equals(Arrays.asList(3, 4));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).exclude(2, 4).asList().asCollection().equals(Arrays.asList(4));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).exclude(0, 4).asList().asCollection().equals(Arrays.asList(4));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).exclude(0, 5).asList().asCollection().equals(Arrays.asList());
     * </pre>
     * @param low The low element (exclusive)
     * @param high The high element (inclusive)
     * @return The set of all elements in this set &lt; low or &gt;= high
     */
    ImmSortedSet<E> exclude(E low, E high);

    /**
     * Returns the set of all elements in this set in range <code>[low, high)</code>.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).subSet(2, 3).asList().asCollection().equals(Arrays.asList(2));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).subSet(2, 4).asList().asCollection().equals(Arrays.asList(2, 3));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).subSet(0, 4).asList().asCollection().equals(Arrays.asList(2, 3));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).subSet(0, 5).asList().asCollection().equals(Arrays.asList(2, 3, 4));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).subSet(3, 3).asList().asCollection().equals(Arrays.asList());
     * </pre>
     * @param low The low element (inclusive)
     * @param high The high element (exclusive)
     * @return The set of all elements in this set &lt; element
     */
	ImmSortedSet<E> subSet(E low, E high);

	/**
	 * Returns a set with <code>element</code> added.
     * <p>This operation is O(log n).
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).add(5).asList().asCollection().equals(Arrays.asList(2, 3, 4, 5));
     * </pre>
	 * @param element The element to add.
	 * @return A set with the given element added.
	 */
    @Override
	ImmSortedSet<E> add(E element);
	
    /**
     * Returns a set that is the union of this set with <code>other</code>.
     * <p>This operation is O(m * log n).
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).union(SortedSet23.of(Arrays.asList(5, 6))).asList().asCollection().equals(Arrays.asList(2, 3, 4, 5, 6));
     * </pre>
     * @param other The elements to remove.
     * @return A set with the given element removed.
     */
    @Override
	ImmSortedSet<E> union(ImmSet<E> other);

    /**
     * Returns a set with the elements in reverse order.
     * <p>This operation is O(1).
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).reversed().asList().asCollection().equals(Arrays.asList(4, 3, 2));
     * </pre>
     * @return A set with the elements reversed
     */
	ImmSortedSet<E> reversed();

    /**
     * Returns a set with <code>element</code> removed.
     * <p>This operation is O(log n).
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).remove(2).asList().asCollection().equals(Arrays.asList(3, 4));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).remove(5).asList().asCollection().equals(Arrays.asList(2, 3, 4));
     * </pre>
     * @param element The element to remove
     * @return A set with the given element removed
     */
    @Override
	ImmSortedSet<E> remove(E element);
	
    /**
     * Returns a set with only the elements that match <code>filter</code>.
     * <p>This operation is O(n * log n).
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).filter(e -&gt; e &lt; 4).asList().asCollection().equals(Arrays.asList(2, 3));
     * </pre>
     * @param filter The filter to apply
     * @return A set with the given element removed
     */
    @Override
    ImmSortedSet<E> filter(Predicate<E> filter);
	
    /**
     * Returns a set that is the intersection of this set with <code>other</code>.
     * <p>This operation is O((m + n) * log (m + n)).
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).retain(SortedSet23.of(Arrays.asList(1,2,4))).asList().asCollection().equals(Arrays.asList(2, 4));
     * </pre>
     * @param other The set to intersection with
     * @return A set with the given element removed
     */
    @Override
    ImmSortedSet<E> retain(Iterable<? extends E> other);
    /**
     * Returns a set that is the subtraction of this set with <code>other</code>.
     * <p>This operation is O(m * log n).
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).removeAllIn(SortedSet23.of(Arrays.asList(2,4))).asList().asCollection().equals(Arrays.asList(3));
     * </pre>
     * @param other The elements to remove.
     * @return A set with the given element removed.
     */
    @Override
    ImmSortedSet<E> removeAllIn(Iterable<? extends E> other);
  
    /**
     * Returns a set with the element at the given index removed.
     * <p>This operation is O(log n).
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).removeAt(0).asList().asCollection().equals(Arrays.asList(3, 4));
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).removeAt(2).asList().asCollection().equals(Arrays.asList(2, 3));
     * </pre>
     * @param index The index of the element to remove.
     * @return A set with the element at the given index removed
     */
    ImmSortedSet<E> removeAt(int index);

    /**
     * Return the element at the given index.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).getAt(0) == 2;
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).getAt(2) == 4;
     * </pre>
     * @param index The index.
     * @return The element at the given index.
     * @throws IndexOutOfBoundsException if out of bounds
     */
    E getAt(int index);

	/**
     * Returns the read-only {@link Set} view of this set.
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).asList().asCollection().equals(Arrays.asList(2, 3, 4));
     * </pre>
     * @return the {@link SortedSet} view of this set
     */
    @Override
	SortedSet<E> asCollection();
    
    /**
     * Returns the index of <code>element</code> in the set.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).indexOf(2) == 0;
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).indexOf(4) == 2;
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).indexOf(5) == -1;
     * </pre>
     * @param element The element to look for.
     * @return The index of the given element in the set, -1 of not found.
     */
    int indexOf(E element);
	
    /**
     * Returns the {@link ImmList} view of this set.
     * <pre>
     * Example:
     *     assert SortedSet23.of(Arrays.asList(4, 2, 3)).asList().asCollection().equals(Arrays.asList(2, 3, 4));
     * </pre>
     * @return the {@link ImmList} view of this set
     */
	ImmList<E> asList();

	/**
	 * Returns the {@link Comparator} associated with this set.
	 * @return the {@link Comparator} associated with this set
	 */
    Comparator<? super E> getComparator();
}
