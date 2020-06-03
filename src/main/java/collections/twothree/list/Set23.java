package collections.twothree.list;

import java.util.Arrays;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiFunction;
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
	final Comparator<E> comparator;

	/**
	 * The list of elements
	 */
	final List23<E> elements;

	Set23(final Comparator<E> comparator, final List23<E> keys) {
		this.elements = Requirements.require(keys, Requirements.notNull(), () -> "keys");
		this.comparator = Requirements.require(comparator, Requirements.notNull(), () -> "comparator");
	}
	
	/**
	 * Returns a set of exactly one element.
     * <p>This operation is O(1).
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
     * @param <E> The element type
     * @return An empty set.
     */
    public static <E extends Comparable<E>> Set23<E> empty() {
        return new Set23<E>(List23::naturalCompare, List23.empty());
    }

    /**
     * Returns a set containing an initial list of elements, using natural ordering.
     * <p>This operation is O(n log n).
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
     * Returns a set containing an initial list of elements, using custom ordering.
     * <p>This operation is O(n log n).
     * @param <E> The element type
     * @param comparator The comparator for elements
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <E> Set23<E> of(final Comparator<E> comparator, final E ... elements) {
    	return of(comparator, Arrays.asList(elements));
    }

    /**
     * Returns a set containing an initial list of elements, using natural ordering.
     * <p>This operation is O(n log n).
     * @param <E> The element type
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    public static <E extends Comparable<E>> Set23<E> of(final Iterable<E> elements) {
    	return of(List23::naturalCompare, elements);
    }

    /**
     * Returns a set containing an initial list of elements, using custom ordering.
     * <p>This operation is O(n log n).
     * @param <E> The element type
     * @param comparator The comparator of elements
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    public static <E> Set23<E> of(final Comparator<E> comparator, final Iterable<E> elements) {
        final TreeSet<E> list = new TreeSet<>(comparator);
    	for(E e: elements) {
    		list.add(e);
    	}
    	return new Set23<E>(comparator, List23.of(list));
    }

    /**
	 * Returns the size of this set.
     * <p>This operation is O(1).
	 * @return The size of this set
	 */
	public int size() {
		return elements.size();
	}
	
	/**
	 * Returns true if the set contains the given element.
     * <p>This operation is O(log n).
	 * @param element The element to look for.
	 * @return true if the set contains the given element
	 */
	public boolean contains(final E element) {
	    return indexOf(element) >= 0;
	}

    /**
     * Returns the index of the given element in the set.
     * <p>This operation is O(log n).
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
     * @param element The comparison element (inclusive)
     * @return The set of all elements in this set &gt;= element
     */
	public Set23<E> tailSet(final E element) {
		return new Set23<E>(comparator, elements.tail(findFirstElement(element)));
	}

    /**
     * Returns the set of all elements in this set &lt; element.
     * <p>This operation is O(log n).
     * @param element The comparison element (exclusive)
     * @return The set of all elements in this set &lt; element
     */
	public Set23<E> headSet(final E element) {
		return new Set23<E>(comparator, elements.head(findFirstElement(element)));
	}

    /**
     * Returns the set of all elements in this set &lt; low or &gt;= high.
     * <p>This operation is O(log n).
     * @param low The low element (exclusive)
     * @param high The high element (inclusive)
     * @return The set of all elements in this set &lt; low or &gt;= high
     */
    public Set23<E> exclude(final E low, final E high) {
        return new Set23<E>(comparator, elements.exclude(findFirstElement(low), findFirstElement(high)));
    }

    /**
     * Returns the set of all elements in this set &gt;= low and &lt; high.
     * <p>This operation is O(log n).
     * @param low The low element (inclusive)
     * @param high The high element (exclusive)
     * @return The set of all elements in this set &lt; element
     */
	public Set23<E> subSet(final E low, final E high) {
		return new Set23<E>(comparator, elements.subList(findFirstElement(low), findFirstElement(high)));
	}

	/**
	 * Returns a set with the given element added.
     * <p>This operation is O(log n).
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
	 * @param element The element to add.
	 * @return A set with the given element added.
	 */
	public Set23<E> add(final E element) {
	    Node23<E> n = elements.root;
	    return n == null ? new Set23<>(comparator, List23.of(element)) :
	        visit(comparator, n, element, 0, (leaf, i) -> {
	            int cmp = comparator.compare(element, leaf);
	            return cmp == 0 ? this :
	                   cmp < 0 ?
	                           new Set23<>(comparator, elements.insertAt(i, element)) :
	                           new Set23<>(comparator, elements.insertAt(i + 1, element));
	        });
	}

    /**
     * Returns a set with the elements reversed.
     * <p>This operation is O(1).
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @return A set with the elements reversed
     */
	public Set23<E> reversed() {
		return new Set23<E>(comparator.reversed(), elements.reversed());
	}

    /**
     * Returns a set with the given element removed.
     * <p>This operation is O(log n).
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove.
     * @return A set with the given element removed.
     */
	public Set23<E> remove(final E element) {
		Node23<E> n = elements.root;
		return n == null ? this:
			visit(comparator, n, element, 0, (leaf, i) -> comparator.compare(element, leaf) == 0 ?
						new Set23<>(comparator, elements.removeAt(i)) : this
			);
	}
	
	/**
	 * Return the element at the given index.
     * <p>This operation is O(log n).
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
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param index The index of the element to remove.
     * @return A set with the element at the given index removed
     */
    public Set23<E> removeAt(final int index) {
        return new Set23<E>(comparator, elements.removeAt(index));
    }

	/**
     * Returns the read-only {@link Set} view of this set.
     * @return the {@link Set} view of this set
     */
	public SortedSet<E> asSet() {
		return new Set23Set<>(this);
	}
	
    /**
     * Returns the {@link List23} view of this set.
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
    static <T, E> T visit(final Comparator<E> comparator, final Node23<E> node, final E element, final int index, final BiFunction<E,Integer,T> leafVisitor) {
        return
                node.isLeaf() ? leafVisitor.apply(node.leafValue(), index) :
                comparator.compare(element, last(node.b1())) <= 0 ?
                        visit(comparator, node.b1(), element, index, leafVisitor):
                node.numBranches() < 3 || comparator.compare(element, last(node.b2())) <= 0 ?
                        visit(comparator, node.b2(), element, index + node.b1Size(), leafVisitor):
                visit(comparator, node.b3(), element, index + node.b1Size() + node.b2Size(), leafVisitor);
    }

    // Returns first element <= element
    int findFirstElement(final E element) {
    	Node23<E> n = elements.root;
    	return n == null ? 0:
    		   comparator.compare(element, first(n)) < 0 ? 0:
    		   comparator.compare(element, last(n)) > 0 ? size():
    		   visit(comparator, n, element, 0, (leaf, i) -> i);
    }
}
