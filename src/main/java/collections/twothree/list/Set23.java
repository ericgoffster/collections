package collections.twothree.list;

import java.util.Arrays;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.granitesoft.requirement.Requirements;

/**
 * Represents a set of elements using a {@link List23} as a backing store.
 * Set membership is implemented with a comparator.
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
public final class Set23<E> implements Iterable<E> {
    /**
     * The comparator for elements in the set.
     */
	final Comparator<? super E> comparator;

	/**
	 * The list of elements
	 */
	final List23<E> elements;

	Set23(final Comparator<? super E> comparator, final List23<E> elements) {
		this.elements = Requirements.require(elements, Requirements.notNull(), () -> "elements");
		this.comparator = Requirements.require(comparator, Requirements.notNull(), () -> "comparator");
	}
	
	/**
	 * Returns a set of exactly one element.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     Set23.of(6) == {6}
     * </pre>
	 * @param <E> The element type
	 * @param element The singleton element
	 * @return A set of exactly one element
	 */
    public static <E extends Comparable<E>> Set23<E> of(final E element) {
        return new Set23<E>(List23::naturalCompare, List23.of(element));
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
    public static <E extends Comparable<E>> Set23<E> empty() {
        return new Set23<E>(List23::naturalCompare, List23.empty());
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
    public static <E extends Comparable<E>> Set23<E> of(final E ... elements) {
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
    public static <E extends Comparable<E>> Set23<E> of(final Iterable<E> elements) {
    	return of(List23::naturalCompare, elements);
    }
    
    /**
     * Returns a set containing an initial list of elements from another sorted set.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     Set23.of(Set23.of(4, 2, 3).asSet()) == {2, 3, 4}
     * </pre>
     * @param <E> The element type
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    public static <E> Set23<E> ofSorted(final SortedSet<E> items) {
        Comparator<? super E> comparator = items.comparator();
        if (comparator == null) {
            comparator = List23::unNaturalCompare;
        }
        return new Set23<>(comparator, List23.of(items));
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
    public static <E> Set23<E> of(final Comparator<? super E> comparator, final Iterable<E> elements) {
        final TreeSet<E> list = new TreeSet<>(comparator);
    	for(E e: elements) {
    		list.add(e);
    	}
    	return new Set23<E>(comparator, List23.of(list));
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
	 * Returns true if the set contains the given element.
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
     * Returns the index of the given element in the set.
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
     * Returns the set of all elements in this set &gt;= element
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).tailSet(2) == {2, 3, 4}
     *     Set23.of(4, 2, 3).tailSet(4) == {4}
     *     Set23.of(4, 2, 3).tailSet(0) == {2, 3, 4}
     *     Set23.of(4, 2, 3).tailSet(5) == {}
     * </pre>
     * @param element The comparison element (inclusive)
     * @return The set of all elements in this set &gt;= element
     */
	public Set23<E> tailSet(final E element) {
		return new Set23<E>(comparator, elements.tail(naturalPosition(element)));
	}

    /**
     * Returns the set of all elements in this set &lt; element.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).headSet(2) == {}
     *     Set23.of(4, 2, 3).headSet(4) == {2, 3}
     *     Set23.of(4, 2, 3).headSet(0) == {}
     *     Set23.of(4, 2, 3).headSet(5) == {2, 3, 4}
     * </pre>
     * @param element The comparison element (exclusive)
     * @return The set of all elements in this set &lt; element
     */
	public Set23<E> headSet(final E element) {
		return new Set23<E>(comparator, elements.head(naturalPosition(element)));
	}

    /**
     * Returns the set of all elements in this set &lt; low or &gt;= high.
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
    public Set23<E> exclude(final E low, final E high) {
        if (comparator.compare(low, high) > 0) {
            throw new IllegalArgumentException("low must be <= high");
        }
        if (comparator.compare(low, high) == 0) {
            return this;
        }
        return new Set23<E>(comparator, elements.exclude(naturalPosition(low), naturalPosition(high)));
    }

    /**
     * Returns the set of all elements in this set &gt;= low and &lt; high.
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
	public Set23<E> subSet(final E low, final E high) {
        if (comparator.compare(low, high) > 0) {
            throw new IllegalArgumentException("low must be <= high");
        }
        if (comparator.compare(low, high) == 0) {
            return new Set23<E>(comparator, List23.empty());
        }
		return new Set23<E>(comparator, elements.subList(naturalPosition(low), naturalPosition(high)));
	}

	/**
	 * Returns a set with the given element added.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).add(5) == {2, 3, 4, 5}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
	 * @param element The element to add.
	 * @return A set with the given element added.
	 */
	public Set23<E> add(final E element) {
	    if (contains(element)) {
	        return this;
	    }
	    return new Set23<>(comparator, elements.insertAt(naturalPosition(element), element));
	}
	
    /**
     * Returns a set with the given elements added.
     * <p>This operation is O(m * log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).union(Set23.of(5, 6)) == {2, 3, 4, 5, 6}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove.
     * @return A set with the given element removed.
     */
	public Set23<E> union(Set23<E> elements) {
	    if (size() == 0) {
	        return of(comparator, elements);
	    }
	    Set23<E> s = this;
	    for(E e: elements) {
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
	public Set23<E> reversed() {
		return new Set23<E>(comparator.reversed(), elements.reversed());
	}

    /**
     * Returns a set with the given element removed.
     * <p>This operation is O(log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).remove(2) == {3, 4}
     *     Set23.of(4, 2, 3).remove(5) == {2, 3, 4}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove.
     * @return A set with the given element removed.
     */
	public Set23<E> remove(final E element) {
	    int index = indexOf(element);
	    return index < 0 ? this : new Set23<>(comparator, elements.removeAt(index));
	}
	
    /**
     * Returns a set with only the elements that match the filter.
     * <p>This operation is O(n * log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).filter(e -> e < 4) == {2, 3}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove.
     * @return A set with the given element removed.
     */
    public Set23<E> filter(final Predicate<E> filter) {
        return new Set23<>(comparator, elements.filter(filter));
    }
	
    /**
     * Returns a set that is the intersection with the given set.
     * <p>This operation is O((m + n) * log (m + n)).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).intersection(Set.of(1,2,4)) == {2, 4}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove.
     * @return A set with the given element removed.
     */
    public Set23<E> intersection(final Set23<E> elements) {
        return filter(elements::contains);
    }
    /**
     * Returns a set with the given elements retain.
     * <p>This operation is O(m * log n).
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).subtraction(Set.of(2,4)) == {3}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove.
     * @return A set with the given element removed.
     */
    public Set23<E> subtraction(final Set23<E> elements) {
        Set23<E> m = this;
        for(E e: elements) {
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
        return elements.get(index);
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
    public Set23<E> removeAt(final int index) {
        return new Set23<E>(comparator, elements.removeAt(index));
    }

	/**
     * Returns the read-only {@link Set} view of this set.
     * <pre>
     * Example:
     *     Set23.of(4, 2, 3).asSet() == {2, 3, 4}
     * </pre>
     * @return the {@link Set} view of this set
     */
	public SortedSet<E> asSet() {
		return new Set23Set<>(this);
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
		if (!(obj instanceof Set23)) {
			return false;
		}
		Set23<?> other = (Set23<?>)obj;
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
    
    static <E> int hashCompare(E a, E b) {
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
