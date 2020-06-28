package collections.twothree.list;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Collection23<E> extends Iterable<E> {
    /**
     * Returns the size of this set.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     SortedSet23.of(4, 2, 3).size() == 3
     * </pre>
     * @return The size of this set
     */
    int size();
    
    /**
     * Returns true if the set contains <code>element</code>.
     * <p>This operation is O(log n) or O(n) depending in implementation.
     * <pre>
     * Example:
     *     SortedSet23.of(4, 2, 3).contains(2) == true
     *     SortedSet23.of(4, 2, 3).contains(5) == false
     * </pre>
     * @param element The element to look for.
     * @return true if the set contains the given element
     */
    boolean contains(final E element);

    /**
     * Returns a set with <code>element</code> added.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     SortedSet23.of(4, 2, 3).add(5) == {2, 3, 4, 5}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to add.
     * @return A set with the given element added.
     */
    Collection23<E> add(final E element);
    
    /**
     * Returns a set with <code>element</code> removed.
     * <p>This operation is O(log n) or O(n) depending in implementation.
     * <pre>
     * Example:
     *     SortedSet23.of(4, 2, 3).remove(2) == {3, 4}
     *     SortedSet23.of(4, 2, 3).remove(5) == {2, 3, 4}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove
     * @return A set with the given element removed
     */
    Collection23<E> remove(final E element);
    
    /**
     * Returns a set with only the elements that match <code>filter</code>.
     * <p>This operation is O(n * log n).
     * <pre>
     * Example:
     *     SortedSet23.of(4, 2, 3).filter(e -&gt; e &lt; 4) == {2, 3}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param filter The filter to apply
     * @return A set with the given element removed
     */
    Collection23<E> filter(final Predicate<E> filter);
    
    /**
     * Returns a set that is the intersection of this set with <code>other</code>.
     * <p>This operation is O((m + n) * log (m + n)).
     * <pre>
     * Example:
     *     SortedSet23.of(4, 2, 3).retain(Set.of(1,2,4)) == {2, 4}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The set to intersection with
     * @return A set with the given element removed
     */
    Collection23<E> retain(final Iterable<E> other);

    /**
     * Returns a set that is the subtraction of this set with <code>other</code>.
     * <p>This operation is O(m * log n).
     * <pre>
     * Example:
     *     SortedSet23.of(4, 2, 3).subtraction(Set.of(2,4)) == {3}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The elements to remove.
     * @return A set with the given element removed.
     */
    Collection23<E> removeAllIn(final Iterable<E> other);
  
    /**
     * Returns the read-only {@link Set} view of this set.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     SortedSet23.of(4, 2, 3).asSet() == {2, 3, 4}
     * </pre>
     * @return the {@link SortedSet} view of this set
     */
    Collection<E> asCollection();
    
    /**
     * Streams all elements of this set.
     * <p>This operation is O(1).
     * @return A stream of elements in this set.
     */
    Stream<E> stream();
}