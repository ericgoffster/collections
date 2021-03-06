package collections.immutable;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents an Immutable set.   There is no guarantee
 * on the order of entries (i.e. HashSet).
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.
 * Operations like add, remove return sets with the specified
 * operation applied, leaving the original set unmodified.   In addition,
 * these operations are *performant*, usually taking no more than O(log n).
 *
 * @param <E> The type of the elements contained by this set.
 */
public interface ImmSet<E> extends ImmCollection<E> {
    /**
     * Returns a set with <code>element</code> added.
     * The order of elements is not defined.
     * <p>This operation is O(log n), where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <p>Example:
     * <pre>{@code
     *     assert ImmCollections.asSet(4,2,3).add(5).asCollection().equals(new HashSet<>(Arrays.asList(2, 3, 4, 5)));
     *     assert ImmCollections.asSet(4, 2, 3, 5).add(5).asCollection().equals(new HashSet<>(Arrays.asList(2, 3, 4, 5)));
     * }</pre>
     * @param element The element to add
     * @return A set with the given element added
     */
    ImmSet<E> add(E element);
    
    /**
     * Returns true if the set contains <code>element</code>.
     * <p>This operation is O(log n), where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <p>Example:
     * <pre>{@code
     *     assert ImmCollections.asSet(4,2,3).contains(5) == false;
     *     assert ImmCollections.asSet(4,2,3).contains(4) == true;
     * }</pre>
     * @param element The element to add
     * @return A set with the given element added
     */
    boolean contains(E element);

    /**
     * Returns a set with <code>element</code> removed.
     * The order of elements is not defined.
     * <p>This operation is O(log n), where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <p>Example:
     * <pre>{@code
     *     assert ImmCollections.asSet(4,2,3).remove(2).asCollection().equals(new HashSet<>(Arrays.asList(3, 4)));
     *     assert ImmCollections.asSet(4,2,3).remove(5).asCollection().equals(new HashSet<>(Arrays.asList(2, 3, 4)));
     * }</pre>
     * @param element The element to remove
     * @return A set with the given element removed
     */
    ImmSet<E> remove(E element);
    
    /**
     * Returns a set that is the union of this set with <code>other</code>.
     * The order of elements is not defined.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <p>Example:
     * <pre>{@code
     *     assert ImmCollections.asSet(4,2,3).union(ImmCollections.asSet(4, 5, 6)).asCollection().equals(new HashSet<>(Arrays.asList(2, 3, 4, 5, 6)));
     * }</pre>
     * @param other The elements to remove
     * @return A set with the given element removed
     */
    ImmSet<E> union(ImmSet<E> other);

    /**
     * Returns a set with only the elements that match <code>filter</code>.
     * The order of elements should be retained.
     * <p>This operation is O(n * log n), where n = |this|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <p>Example:
     * <pre>{@code
     *     assert ImmCollections.asSet(4,2,3).filter(e -> e < 4).asCollection().equals(new HashSet<>(Arrays.asList(2, 3)));
     * }</pre>
     * @param filter The filter to apply
     * @return A set with the given element removed
     */
    ImmSet<E> filter(Predicate<E> filter);
    
    /**
     * Returns a set that is the intersection of this set with <code>other</code>.
     * The order of elements should be retained.
     * <p>This operation is O(n * log (n)), where n = |this| + |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <p>Example:
     * <pre>{@code
     *     ImmCollections.asSet(4,2,3).retain(ImmCollections.asSet(1,2,4)).asCollection().equals(new HashSet<>(Arrays.asList(4, 2)));
     * }</pre>
     * @param other The set to intersection with
     * @return A set with the given element removed
     */
    ImmSet<E> retain(Iterable<? extends E> other);

    /**
     * Returns a set that is the subtraction of this set with <code>other</code>.
     * The order of elements should be retained.
     * <p>This operation is O(m * log n), where n = |this| and m = |other|.
     * <p>THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * <p>Example:
     * <pre>{@code
     *     assert ImmCollections.asSet(4,2,3).removeAllIn(ImmCollections.asSet(2,4)).asCollection().equals(new HashSet<>(Arrays.asList(3)));
     * }</pre>
     * @param other The elements to remove
     * @return A set with the given element removed
     */
    ImmSet<E> removeAllIn(Iterable<? extends E> other);
  
    /**
     * Returns the read-only {@link Set} view of this set.
     * <p>This operation is O(1).
     * @return the {@link Set} view of this set
     * <p>Example:
     * <pre>{@code
     *     assert ImmCollections.asSet(4,2,3).asCollection().equals(new HashSet<>(Arrays.asList(2, 3, 4)));
     * }</pre>
     */
    Set<E> asCollection();
}