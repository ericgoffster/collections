package collections.twothree.list;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Represents a generic collection of items backed by a {@link List23}.
 *
 * @param <E> The type of the elements contained by this set.
 */
public interface Collection23<E> extends Iterable<E> {
    /**
     * Returns the size of this set.
     * <p>This operation is O(1).
     * @return The size of this set
     */
    int size();
    
    /**
     * Returns true if the set contains <code>element</code>.
     * <p>This operation is O(log n) or O(n) depending in implementation, where n = |this|.
     * @param element The element to look for.
     * @return true if the set contains the given element
     */
    boolean contains(E element);

    /**
     * Returns a set with <code>element</code> added.
     * <p>This operation is O(log n), where n = |this|.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to add
     * @return A set with the given element added.
     */
    Collection23<E> add(E element);
    
    /**
     * Returns a set with <code>element</code> removed.
     * <p>This operation is O(log n) or O(n) depending in implementation, where n = |this|.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove
     * @return A set with the given element removed
     */
    Collection23<E> remove(E element);
    
    /**
     * Returns a set with only the elements that match <code>filter</code>.
     * <p>This operation is O(n * log n), where n = |this|.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param filter The filter to apply
     * @return A set with the given element removed
     */
    Collection23<E> filter(Predicate<E> filter);
    
    /**
     * Returns a set that is the intersection of this set with <code>other</code>.
     * <p>This operation is O(n * log n), where n = |this| + |other|.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The set to intersection with
     * @return A set with the given element removed
     */
    Collection23<E> retain(Iterable<E> other);

    /**
     * Returns a set that is the subtraction of this set with <code>other</code>.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The elements to remove
     * @return A set with the given element removed
     */
    Collection23<E> removeAllIn(Iterable<E> other);
  
    /**
     * Returns the read-only {@link Collection} view of this set.
     * <p>This operation is O(1).
     * @return the {@link Collection} view of this set
     */
    Collection<E> asCollection();
    
    /**
     * Streams all elements of this set.
     * <p>This operation is O(1).
     * @return A stream of elements in this set
     */
    Stream<E> stream();
}