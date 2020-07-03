package collections.twothree.list;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.granitesoft.requirement.Requirements;

/**
 * Represents a hash ordered set of elements using a {@link List23} as a backing store.
 * Set membership and ordering is implemented using standard java hashCode/equals semantics.
 * <p>
 * Since operations on a List23 are log n, we can represent a set
 * relatively easily as a sorted list of elements, doing straightforward
 * binary searches.
 * <p>
 * This version of a set is immutable.   All operations on a set, leave the original
 * set unchanged.
 *
 * @param <E> The type of the elements.
 */
public final class HashSet23<E> implements Set23<E> {
	/**
	 * The list of elements
	 */
	final List23<E> elements;

	HashSet23(final List23<E> elements) {
	    assert elements != null;
		this.elements = elements;
	}
	
	/**
	 * Returns a single set of <code>element</code>.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     HashSet23.of(6) == {6}
     * </pre>
	 * @param <E> The element type
	 * @param element The singleton element
	 * @return A set of exactly one element
	 */
    public static <E> HashSet23<E> of(final E element) {
        return new HashSet23<E>(List23.of(element));
    }

    /**
     * Returns the empty hashset.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     HashSet23.empty() == {}
     * </pre>
     * @param <E> The element type
     * @return An empty set.
     */
    public static <E> HashSet23<E> empty() {
        return new HashSet23<E>(List23.empty());
    }

    /**
     * Returns a hashset containing an initial list of elements.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     HashSet23.of(4, 2, 3) == {2, 3, 4}
     * </pre>
     * @param <E> The element type
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
	@SafeVarargs
    @SuppressWarnings("varargs")
    public static <E> HashSet23<E> of(final E ... elements) {
    	return of(new ArrayIterable<>(Requirements.require(elements, Requirements.notNull(), () -> "elements")));
    }

    /**
     * Returns a hashset containing an initial list of elements.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     HashSet23.of(Arrays.asList(4, 2, 3)) == {2, 3, 4}
     * </pre>
     * @param <E> The element typer
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    public static <E> HashSet23<E> of(final Iterable<E> elements) {
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
        return new HashSet23<E>(List23.ofSortedUnique(HashSet23::compare, elements));
    }

    /**
	 * Returns the size of this set.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     HashSet23.of(4, 2, 3).size() == 3
     * </pre>
	 * @return The size of this set
	 */
    @Override
	public int size() {
		return elements.size();
	}
	
	/**
	 * Returns true if the set contains <code>element</code>.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     HashSet23.of(4, 2, 3).contains(2) == true
     *     HashSet23.of(4, 2, 3).contains(5) == false
     * </pre>
	 * @param element The element to look for.
	 * @return true if the set contains the given element
	 */
    @Override
	public boolean contains(final E element) {
	    return elements.getIndexOf(e -> compare(element, e)) >= 0;
	}

    /**
	 * Returns a set with <code>element</code> added.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     HashSet23.of(4, 2, 3).add(5) == {2, 3, 4, 5}
     *     HashSet23.of(4, 2, 3, 5).add(5) == {2, 3, 4, 5}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
	 * @param element The element to add.
	 * @return A set with the given element added.
	 */
    @Override
	public HashSet23<E> add(final E element) {
	    if (contains(element)) {
	        return this;
	    }
	    return new HashSet23<>(elements.insertAt(elements.naturalPosition(e -> compare(element, e)), element));
	}
	
    /**
     * Returns a set that is the union of this set with <code>other</code>.
     * <p>This operation is O(m * log n).
     * <pre>
     * Example:
     *     HashSet23.of(4, 2, 3).union(HashSet23.of(4, 5, 6)) == {2, 3, 4, 5, 6}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The elements to remove.
     * @return A set with the given element removed.
     */
    @Override
	public HashSet23<E> union(Set23<E> other) {
	    HashSet23<E> s = this;
	    for(E e: other) {
	        s = s.add(e);
	    }
	    return s;
	}

    /**
     * Returns a set with <code>element</code> removed.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     HashSet23.of(4, 2, 3).remove(2) == {3, 4}
     *     HashSet23.of(4, 2, 3).remove(5) == {2, 3, 4}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove
     * @return A set with the given element removed
     */
    @Override
	public HashSet23<E> remove(final E element) {
	    int index = elements.getIndexOf(e -> compare(element, e));
	    return index < 0 ? this : new HashSet23<>(elements.removeAt(index));
	}
	
    /**
     * Returns a set with only the elements that match <code>filter</code>.
     * <p>This operation is O(n * log n).
     * <pre>
     * Example:
     *     HashSet23.of(4, 2, 3).filter(e -&gt; e &lt; 4) == {2, 3}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param filter The filter to apply
     * @return A set with the given element removed
     */
    @Override
    public HashSet23<E> filter(final Predicate<E> filter) {
        return new HashSet23<>(elements.filter(filter));
    }
	
    /**
     * Returns a set that is the intersection of this set with <code>other</code>.
     * <p>This operation is O((m + n) * log (m + n)).
     * <pre>
     * Example:
     *     HashSet23.of(4, 2, 3).retain(Set.of(1,2,4)) == {4, 2}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The set to intersection with
     * @return A set with the given element removed
     */
    @Override
    public HashSet23<E> retain(final Iterable<E> other) {
        final HashSet23<E> hs = HashSet23.of(other);
        return filter(hs::contains);
    }
    /**
     * Returns a set that is the subtraction of this set with <code>other</code>.
     * <p>This operation is O(m * log n).
     * <pre>
     * Example:
     *     HashSet23.of(4, 2, 3).removeAllIn(Set.of(2,4)) == {3}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The elements to remove.
     * @return A set with the given element removed.
     */
    @Override
    public HashSet23<E> removeAllIn(final Iterable<E> other) {
        HashSet23<E> m = this;
        for(E e: other) {
            m = m.remove(e);
        }
        return m;
    }
  
	/**
     * Returns the read-only {@link Set} view of this set.
     * <pre>
     * Example:
     *     HashSet23.of(4, 2, 3).asSet() == {2, 3, 4}
     * </pre>
     * @return the {@link SortedSet} view of this set
     */
    @Override
	public Set<E> asCollection() {
		return new Set23Set<>(this);
	}
	
    @Override
	public int hashCode() {
		return asCollection().hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Set23)) {
			return false;
		}
		Set23<?> other = (Set23<?>)obj;
		return asCollection().equals(other.asCollection());
	}

	@Override
	public String toString() {
		return asCollection().toString();
	}
	
	@Override
	public Iterator<E> iterator() {
		return elements.iterator();
	}
    
    @Override
    public Spliterator<E> spliterator() {
        return elements.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    static <E> int compare(E a, E b) {
        int cmp = Integer.compare(Objects.hash(a), Objects.hash(b));
        if (cmp != 0) {
            return cmp;
        }
        if (Objects.equals(a, b)) {
            return 0;
        }
        return Integer.compare(System.identityHashCode(a), System.identityHashCode(b));
    }
}
