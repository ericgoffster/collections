package collections.twothree.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.granitesoft.requirement.Requirements;

/**
 * Represents an ordered set of elements using a {@link List23} as a backing store.
 * Set membership and ordering is implemented with a comparator.
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
public final class SortedSet23<E> implements Iterable<E> {
    /**
     * The comparator for elements in the set.   Defines the ordering.
     */
	final Comparator<? super E> comparator;

	/**
	 * The list of elements
	 */
	final List23<E> elements;

	SortedSet23(final Comparator<? super E> comparator, final List23<E> elements) {
		this.elements = Requirements.require(elements, Requirements.notNull(), () -> "elements");
		this.comparator = Requirements.require(comparator, Requirements.notNull(), () -> "comparator");
	}
	
	/**
	 * Returns a single set of <code>element</code>.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     Set23.of(6) == {6}
     * </pre>
	 * @param <E> The element type
	 * @param element The singleton element
	 * @return A set of exactly one element
	 */
    public static <E extends Comparable<E>> SortedSet23<E> of(final E element) {
        return new SortedSet23<E>(List23::naturalCompare, List23.of(element));
    }

    /**
     * Returns the empty set, using a custom ordering.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     Set23.empty(Integer::compare) == {}
     * </pre>
     * @param comparator The comparator which defines ordering.
     * @param <E> The element type
     * @return An empty set.
     */
    public static <E> SortedSet23<E> empty(Comparator<E> comparator) {
        return new SortedSet23<E>(comparator, List23.empty());
    }

    /**
     * Returns the empty set.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     Set23.empty() == {}
     * </pre>
     * @param <E> The element type
     * @return An empty set.
     */
    public static <E extends Comparable<E>> SortedSet23<E> empty() {
        return empty(List23::naturalCompare);
    }

    /**
     * Returns a set containing an initial list of elements, using natural ordering.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3) == {2, 3, 4}
     * </pre>
     * @param <E> The element type
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
	@SafeVarargs
    @SuppressWarnings("varargs")
    public static <E extends Comparable<E>> SortedSet23<E> of(final E ... elements) {
    	return of(Arrays.asList(elements));
    }

    /**
     * Returns a set containing an initial list of elements, using natural ordering.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     Set23.of(Arrays.asList(4, 2, 3)) == {2, 3, 4}
     * </pre>
     * @param <E> The element type
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    public static <E extends Comparable<E>> SortedSet23<E> of(final Iterable<E> elements) {
    	return of(List23::naturalCompare, elements);
    }
    
    /**
     * Returns a set containing an initial list of elements from <code>other</code>.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     Set23.of(Set23.of(4, 2, 3).asSet()) == {2, 3, 4}
     * </pre>
     * @param <E> The element type
     * @param other The set of elements
     * @return A set containing an initial list of elements
     */
    public static <E> SortedSet23<E> ofSorted(final SortedSet<E> other) {
        Comparator<? super E> comparator = other.comparator();
        if (comparator == null) {
            comparator = List23::unNaturalCompare;
        }
        return new SortedSet23<>(comparator, List23.of(other));
    }

    /**
     * Returns a set containing an initial list of elements, using custom ordering.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     Set23.of(Integer::compare, Arrays.asList(4, 2, 3)) == {2, 3, 4}
     *     Set23.of(Integer::compare.reversed(), Arrays.asList(4, 2, 3)) == {4, 3, 2}
     * </pre>
     * @param <E> The element type
     * @param comparator The comparator of elements
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    public static <E> SortedSet23<E> of(final Comparator<? super E> comparator, final Iterable<E> elements) {
        List<E> l = new ArrayList<E>();
        for(E e: elements) {
            l.add(e);
        }
        Collections.sort(l, comparator);
    	return new SortedSet23<E>(comparator, List23.of(l));
    }

    /**
	 * Returns the size of this set.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).size() == 3
     * </pre>
	 * @return The size of this set
	 */
	public int size() {
		return elements.size();
	}
	
	/**
	 * Returns true if the set contains <code>element</code>.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).contains(2) == true
     *     Set23.of(4, 2, 3).contains(5) == false
     * </pre>
	 * @param element The element to look for.
	 * @return true if the set contains the given element
	 */
	public boolean contains(final E element) {
	    return indexOf(element) >= 0;
	}

    /**
     * Returns the index of <code>element</code> in the set.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).indexOf(2) == 0
     *     Set23.of(4, 2, 3).indexOf(4) == 2
     *     Set23.of(4, 2, 3).indexOf(5) == -1
     * </pre>
     * @param element The element to look for.
     * @return The index of the given element in the set, -1 of not found.
     */
    public int indexOf(final E element) {
        return elements.root == null ? -1 :
            visit(comparator, elements.root, element, 0, (leaf, i) -> comparator.compare(element, leaf) == 0 ? i : -1);
    }
    
    /**
     * Returns the set of all elements in this set in range &gt;= <code>element</code>.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).ge(2) == {2, 3, 4}
     *     Set23.of(4, 2, 3).ge(4) == {4}
     *     Set23.of(4, 2, 3).ge(0) == {2, 3, 4}
     *     Set23.of(4, 2, 3).ge(5) == {}
     * </pre>
     * @param element The comparison element (inclusive)
     * @return The set of all elements in this set &gt;= element
     */
	public SortedSet23<E> ge(final E element) {
		return new SortedSet23<E>(comparator, elements.tailAt(naturalPosition(element)));
	}

    /**
     * Returns the set of all elements in this set &lt; <code>element</code>.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).lt(2) == {}
     *     Set23.of(4, 2, 3).lt(4) == {2, 3}
     *     Set23.of(4, 2, 3).lt(0) == {}
     *     Set23.of(4, 2, 3).lt(5) == {2, 3, 4}
     * </pre>
     * @param element The comparison element (exclusive)
     * @return The set of all elements in this set &lt; element
     */
	public SortedSet23<E> lt(final E element) {
		return new SortedSet23<E>(comparator, elements.headAt(naturalPosition(element)));
	}

    /**
     * Returns the set of all elements not in range <code>[low, high)</code>.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).exclude(2, 3) == {3, 4}
     *     Set23.of(4, 2, 3).exclude(2, 4) == {4}
     *     Set23.of(4, 2, 3).exclude(0, 4) == {4}
     *     Set23.of(4, 2, 3).exclude(0, 5) == {}
     * </pre>
     * @param low The low element (exclusive)
     * @param high The high element (inclusive)
     * @return The set of all elements in this set &lt; low or &gt;= high
     */
    public SortedSet23<E> exclude(final E low, final E high) {
        int cmp = comparator.compare(low, high);
        if (cmp > 0) {
            throw new IllegalArgumentException("low must be <= high");
        }
        if (comparator.compare(low, high) == 0) {
            return this;
        }
        return new SortedSet23<E>(comparator, elements.removeRange(naturalPosition(low), naturalPosition(high)));
    }

    /**
     * Returns the set of all elements in this set in range <code>[low, high)</code>.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).subSet(2, 3) == {2}
     *     Set23.of(4, 2, 3).subSet(2, 4) == {2, 3}
     *     Set23.of(4, 2, 3).subSet(0, 4) == {2, 3}
     *     Set23.of(4, 2, 3).subSet(0, 5) == {2, 3, 4}
     *     Set23.of(4, 2, 3).subSet(3, 3) == {}
     * </pre>
     * @param low The low element (inclusive)
     * @param high The high element (exclusive)
     * @return The set of all elements in this set &lt; element
     */
	public SortedSet23<E> subSet(final E low, final E high) {
        int cmp = comparator.compare(low, high);
        if (cmp > 0) {
            throw new IllegalArgumentException("low must be <= high");
        }
        if (comparator.compare(low, high) == 0) {
            return new SortedSet23<E>(comparator, List23.empty());
        }
		return new SortedSet23<E>(comparator, elements.getRange(naturalPosition(low), naturalPosition(high)));
	}

	/**
	 * Returns a set with <code>element</code> added.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).add(5) == {2, 3, 4, 5}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
	 * @param element The element to add.
	 * @return A set with the given element added.
	 */
	public SortedSet23<E> add(final E element) {
	    if (contains(element)) {
	        return this;
	    }
	    return new SortedSet23<>(comparator, elements.insertAt(naturalPosition(element), element));
	}
	
    /**
     * Returns a set that is the union of this set with <code>other</code>.
     * <p>This operation is O(m * log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).union(Set23.of(5, 6)) == {2, 3, 4, 5, 6}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The elements to remove.
     * @return A set with the given element removed.
     */
	public SortedSet23<E> union(SortedSet23<E> other) {
	    if (size() == 0) {
	        return of(comparator, other);
	    }
	    SortedSet23<E> s = this;
	    for(E e: other) {
	        s = s.add(e);
	    }
	    return s;
	}

    /**
     * Returns a set with the elements in reverse order.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).reversed() == {4, 3, 2}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @return A set with the elements reversed
     */
	public SortedSet23<E> reversed() {
		return new SortedSet23<E>(comparator.reversed(), elements.reversed());
	}

    /**
     * Returns a set with <code>element</code> removed.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).remove(2) == {3, 4}
     *     Set23.of(4, 2, 3).remove(5) == {2, 3, 4}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove
     * @return A set with the given element removed
     */
	public SortedSet23<E> remove(final E element) {
	    int index = indexOf(element);
	    return index < 0 ? this : new SortedSet23<>(comparator, elements.removeAt(index));
	}
	
    /**
     * Returns a set with only the elements that match <code>filter</code>.
     * <p>This operation is O(n * log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).filter(e -&gt; e &lt; 4) == {2, 3}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param filter The filter to apply
     * @return A set with the given element removed
     */
    public SortedSet23<E> filter(final Predicate<E> filter) {
        return new SortedSet23<>(comparator, elements.filter(filter));
    }
	
    /**
     * Returns a set that is the intersection of this set with <code>other</code>.
     * <p>This operation is O((m + n) * log (m + n)).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).intersection(Set.of(1,2,4)) == {2, 4}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The set to intersection with
     * @return A set with the given element removed
     */
    public SortedSet23<E> intersection(final SortedSet23<E> other) {
        return filter(other::contains);
    }
    /**
     * Returns a set that is the subtraction of this set with <code>other</code>.
     * <p>This operation is O(m * log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).subtraction(Set.of(2,4)) == {3}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param other The elements to remove.
     * @return A set with the given element removed.
     */
    public SortedSet23<E> subtraction(final SortedSet23<E> other) {
        SortedSet23<E> m = this;
        for(E e: other) {
            m = m.remove(e);
        }
        return m;
    }
  
	/**
	 * Return the element at the given index.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).getAt(0) == 2
     *     Set23.of(4, 2, 3).getAt(2) == 4
     * </pre>
	 * @param index The index.
	 * @return The element at the given index.
     * @throws IndexOutOfBoundsException if out of bounds
	 */
    public E getAt(final int index) {
        return elements.getAt(index);
    }
    
    /**
     * Returns a set with the element at the given index removed.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).removeAt(0) == {3, 4}
     *     Set23.of(4, 2, 3).removeAt(2) == {2, 3}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param index The index of the element to remove.
     * @return A set with the element at the given index removed
     */
    public SortedSet23<E> removeAt(final int index) {
        return new SortedSet23<E>(comparator, elements.removeAt(index));
    }

	/**
     * Returns the read-only {@link Set} view of this set.
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).asSet() == {2, 3, 4}
     * </pre>
     * @return the {@link SortedSet} view of this set
     */
	public SortedSet<E> asSet() {
		return new SortedSet23Set<>(this);
	}
	
    /**
     * Returns the {@link List23} view of this set.
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).asList() == [2, 3, 4]
     * </pre>
     * @return the {@link List23} view of this set
     */
	public List23<E> asList() {
		return elements;
	}
	
    @Override
	public int hashCode() {
		return asSet().hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof SortedSet23)) {
			return false;
		}
		SortedSet23<?> other = (SortedSet23<?>)obj;
		return asSet().equals(other.asSet());
	}
	
	@Override
	public String toString() {
		return asSet().toString();
	}
	
	@Override
	public ListIterator<E> iterator() {
		return elements.iterator();
	}
    
    public Stream<E> stream() {
        return elements.stream();
    }

    static <E> E last(final Node23<E> node) {
    	return node.isLeaf() ? node.leafValue() : last(node.b_last());
    }

    static <E> E first(final Node23<E> node) {
    	return node.isLeaf() ? node.leafValue() : first(node.b1());
    }

    // Visits all leaves, returning an arbitrary result returned from leafVisitor
    static <T, E> T visit(final Comparator<? super E> comparator, final Node23<E> node, final E element, final int index, final BiFunction<E,Integer,T> leafVisitor) {
        return
                node.isLeaf() ? leafVisitor.apply(node.leafValue(), index) :
                comparator.compare(element, last(node.b1())) <= 0 ?
                        visit(comparator, node.b1(), element, index, leafVisitor):
                node.numBranches() < 3 || comparator.compare(element, last(node.b2())) <= 0 ?
                        visit(comparator, node.b2(), element, index + node.b1Size(), leafVisitor):
                visit(comparator, node.b3(), element, index + node.b1Size() + node.b2Size(), leafVisitor);
    }

    // Returns the position where the element belongs
    int naturalPosition(final E element) {
    	Node23<E> n = elements.root;
    	return n == null ? 0:
    		   comparator.compare(element, first(n)) < 0 ? 0:
    		   comparator.compare(element, last(n)) > 0 ? size():
    		   visit(comparator, n, element, 0, (leaf, i) -> i);
    }
}
