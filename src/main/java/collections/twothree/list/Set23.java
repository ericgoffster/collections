package collections.twothree.list;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents an Immutable set of items backed by a {@link List23}.
 *
 * @param <E> The type of the elements contained by this set.
 */
public interface Set23<E> extends Collection23<E> {
    /**
     * Returns a set with <code>element</code> added.
     * The order of elements is not defined.
     * <p>This operation is O(log n), where n = |this|.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to add
     * @return A set with the given element added
     */
    Set23<E> add(E element);
    
    /**
     * Returns a set with <code>element</code> removed.
     * The order of elements is not defined.
     * <p>This operation is O(log n), where n = |this|.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove
     * @return A set with the given element removed
     */
    Set23<E> remove(E element);
    
    /**
     * Returns a set that is the union of this set with <code>other</code>.
     * The order of elements is not defined.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The elements to remove
     * @return A set with the given element removed
     */
    Set23<E> union(Set23<E> other);

    /**
     * Returns a set with only the elements that match <code>filter</code>.
     * The order of elements should be retained.
     * <p>This operation is O(n * log n), where n = |this|.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param filter The filter to apply
     * @return A set with the given element removed
     */
    Set23<E> filter(Predicate<E> filter);
    
    /**
     * Returns a set that is the intersection of this set with <code>other</code>.
     * The order of elements should be retained.
     * <p>This operation is O(n * log (n)), where n = |this| + |other|.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The set to intersection with
     * @return A set with the given element removed
     */
    Set23<E> retain(Iterable<E> other);

    /**
     * Returns a set that is the subtraction of this set with <code>other</code>.
     * The order of elements should be retained.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The elements to remove
     * @return A set with the given element removed
     */
    Set23<E> removeAllIn(Iterable<E> other);
  
    /**
     * Returns the read-only {@link Set} view of this set.
     * <p>This operation is O(1).
     * @return the {@link Set} view of this set
     */
    Set<E> asCollection();
}