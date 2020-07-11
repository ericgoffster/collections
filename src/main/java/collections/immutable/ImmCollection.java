package collections.immutable;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Represents a generic Immutable collection of items.   There is no guarantee
 * of the order in which they are arranged.   (i.e. HashSet)
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.
 * Operations like add, remove, retain return collections with the specified
 * operation applied, leaving the original list unmodified.
 * 
 * @param <E> The type of the elements contained by this collection.
 */
public interface ImmCollection<E> extends Iterable<E> {
    /**
     * Returns the size of this collection.
     * <p>This operation is O(1).
     * @return The size of this collection
     */
    int size();
    
    /**
     * Returns a collection with <code>element</code> added.
     * <p>This operation is O(log n), where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original collection is left unchanged.
     * @param element The element to add
     * @return A collection with the given element added.
     */
    ImmCollection<E> add(E element);
    
    /**
     * Returns a collection with only the elements that match <code>filter</code>.
     * <p>This operation is O(n * log n), where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original collection is left unchanged.
     * @param filter The filter to apply
     * @return A collection with the given element removed
     */
    ImmCollection<E> filter(Predicate<E> filter);
    
    /**
     * Returns a collection that is the intersection of this collection with <code>other</code>.
     * <p>This operation is O(n * log n), where n = |this| + |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original collection is left unchanged.
     * @param other The collection to intersection with
     * @return A collection with the given element removed
     */
    ImmCollection<E> retain(Iterable<? extends E> other);

    /**
     * Returns a collection that is the subtraction of this collection with <code>other</code>.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original collection is left unchanged.
     * @param other The elements to remove
     * @return A collection with the given element removed
     */
    ImmCollection<E> removeAllIn(Iterable<? extends E> other);
  
    /**
     * Returns the read-only {@link Collection} view of this collection.
     * <p>This operation is O(1).
     * @return the {@link Collection} view of this collection
     */
    Collection<E> asCollection();
    
    /**
     * Streams all elements of this collection.
     * <p>This operation is O(1).
     * @return A stream of elements in this collection
     */
    Stream<E> stream();
}