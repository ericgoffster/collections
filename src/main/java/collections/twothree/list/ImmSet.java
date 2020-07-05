package collections.twothree.list;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents an Immutable set of items backed by a {@link ImmList}.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.  The object is not modified in any way.
 *
 * @param <E> The type of the elements contained by this set.
 */
public interface ImmSet<E> extends ImmCollection<E> {
    /**
     * Returns a set with <code>element</code> added.
     * The order of elements is not defined.
     * <p>This operation is O(log n), where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to add
     * @return A set with the given element added
     */
    ImmSet<E> add(E element);
    
    /**
     * Returns a set with <code>element</code> removed.
     * The order of elements is not defined.
     * <p>This operation is O(log n), where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove
     * @return A set with the given element removed
     */
    ImmSet<E> remove(E element);
    
    /**
     * Returns a set that is the union of this set with <code>other</code>.
     * The order of elements is not defined.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The elements to remove
     * @return A set with the given element removed
     */
    ImmSet<E> union(ImmSet<E> other);

    /**
     * Returns a set with only the elements that match <code>filter</code>.
     * The order of elements should be retained.
     * <p>This operation is O(n * log n), where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param filter The filter to apply
     * @return A set with the given element removed
     */
    ImmSet<E> filter(Predicate<E> filter);
    
    /**
     * Returns a set that is the intersection of this set with <code>other</code>.
     * The order of elements should be retained.
     * <p>This operation is O(n * log (n)), where n = |this| + |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The set to intersection with
     * @return A set with the given element removed
     */
    ImmSet<E> retain(Iterable<? extends E> other);

    /**
     * Returns a set that is the subtraction of this set with <code>other</code>.
     * The order of elements should be retained.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The elements to remove
     * @return A set with the given element removed
     */
    ImmSet<E> removeAllIn(Iterable<? extends E> other);
  
    /**
     * Returns the read-only {@link Set} view of this set.
     * <p>This operation is O(1).
     * @return the {@link Set} view of this set
     */
    Set<E> asCollection();
}