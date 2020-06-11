package collections.twothree.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
public final class HashSet23<E> implements Iterable<E> {
	/**
	 * The list of elements
	 */
	final List23<E> elements;

	HashSet23(final List23<E> elements) {
		this.elements = Requirements.require(elements, Requirements.notNull(), () -> "elements");
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
    public static <E> HashSet23<E> of(final E element) {
        return new HashSet23<E>(List23.of(element));
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
    public static <E> HashSet23<E> empty() {
        return new HashSet23<E>(List23.empty());
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
    public static <E> HashSet23<E> of(final E ... elements) {
    	return of(Arrays.asList(elements));
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
    public static <E> HashSet23<E> of(final Iterable<E> elements) {
        List<E> l = new ArrayList<E>();
        for(E e: elements) {
            l.add(e);
        }
        Collections.sort(l, HashSet23::hashCompare);
        return new HashSet23<E>(List23.of(l));
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
	public HashSet23<E> add(final E element) {
	    if (contains(element)) {
	        return this;
	    }
	    return new HashSet23<>(elements.insertAt(naturalPosition(element), element));
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
	public HashSet23<E> union(HashSet23<E> other) {
	    if (size() == 0) {
	        return of(other);
	    }
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
     *     Set23.of(4, 2, 3).remove(2) == {3, 4}
     *     Set23.of(4, 2, 3).remove(5) == {2, 3, 4}
     * </pre>
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove
     * @return A set with the given element removed
     */
	public HashSet23<E> remove(final E element) {
	    int index = indexOf(element);
	    return index < 0 ? this : new HashSet23<>(elements.removeAt(index));
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
    public HashSet23<E> filter(final Predicate<E> filter) {
        return new HashSet23<>(elements.filter(filter));
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
    public HashSet23<E> intersection(final HashSet23<E> other) {
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
    public HashSet23<E> subtraction(final HashSet23<E> other) {
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
     *     Set23.of(4, 2, 3).asSet() == {2, 3, 4}
     * </pre>
     * @return the {@link SortedSet} view of this set
     */
	public Set<E> asSet() {
		return new HashSet23Set<>(this);
	}
	
    @Override
	public int hashCode() {
		return asSet().hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof HashSet23)) {
			return false;
		}
		HashSet23<?> other = (HashSet23<?>)obj;
		return asSet().equals(other.asSet());
	}
	
	@Override
	public String toString() {
		return asSet().toString();
	}
	
	@Override
	public Iterator<E> iterator() {
		return elements.iterator();
	}
    
    public Stream<E> stream() {
        return elements.stream();
    }

    int indexOf(final E element) {
        return elements.root == null ? -1 :
            visit(elements.root, element, 0, (leaf, i) -> hashCompare(element, leaf) == 0 ? i : -1);
    }

    static <E> E last(final Node23<E> node) {
    	return node.isLeaf() ? node.leafValue() : last(node.b_last());
    }

    static <E> E first(final Node23<E> node) {
    	return node.isLeaf() ? node.leafValue() : first(node.b1());
    }

    // Visits all leaves, returning an arbitrary result returned from leafVisitor
    static <T, E> T visit(final Node23<E> node, final E element, final int index, final BiFunction<E,Integer,T> leafVisitor) {
        return
                node.isLeaf() ? leafVisitor.apply(node.leafValue(), index) :
                    hashCompare(element, last(node.b1())) <= 0 ?
                        visit(node.b1(), element, index, leafVisitor):
                node.numBranches() < 3 || hashCompare(element, last(node.b2())) <= 0 ?
                        visit(node.b2(), element, index + node.b1Size(), leafVisitor):
                visit(node.b3(), element, index + node.b1Size() + node.b2Size(), leafVisitor);
    }

    // Returns the position where the element belongs
    int naturalPosition(final E element) {
    	Node23<E> n = elements.root;
    	return n == null ? 0:
    		   hashCompare(element, first(n)) < 0 ? 0:
    		       hashCompare(element, last(n)) > 0 ? size():
    		   visit(n, element, 0, (leaf, i) -> i);
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
