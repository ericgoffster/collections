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
public final class Set23<E> implements Iterable<E> {
    /**
     * The comparator for elements in the set.
     */
	final Comparator<E> keyComparator;

	/**
	 * The list of elements
	 */
	final List23<E> keys;

	/**
	 * True, if reversed.
	 */
	final boolean reversed;

	Set23(Comparator<E> keyComparator, List23<E> keys, boolean reversed) {
		this.keys = Requirements.require(keys, Requirements.notNull(), () -> "keys");
		this.keyComparator = Requirements.require(keyComparator, Requirements.notNull(), () -> "keyComparator");
		this.reversed = reversed;
	}
	
	/**
	 * Returns a set of exactly one element.
	 * @param <E> The element type
	 * @param element The singleton element
	 * @return A set of exactly one element
	 */
    public static <E extends Comparable<E>> Set23<E> of(E element) {
        return new Set23<E>(Set23::naturalCompare, List23.of(element), false);
    }

    /**
     * Returns the empty set.
     * @param <E> The element type
     * @return An empty set.
     */
    public static <E extends Comparable<E>> Set23<E> empty() {
        return new Set23<E>(Set23::naturalCompare, List23.empty(), false);
    }

    /**
     * Returns a set containing an initial list of elements, using natural ordering.
     * Elements are stored in natural order.
     * @param <E> The element type
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
	@SafeVarargs
    @SuppressWarnings("varargs")
    public static <E extends Comparable<E>> Set23<E> of(E ... elements) {
    	return of(Arrays.asList(elements));
    }

    /**
     * Returns a set containing an initial list of elements, using a custom order.
     * @param <E> The element type
     * @param keyComparator The comparator for elements
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <E> Set23<E> of(Comparator<E> keyComparator, E ... elements) {
    	return of(keyComparator, Arrays.asList(elements));
    }

    /**
     * Returns a set containing an initial list of elements, using natural ordering.
     * Elements are stored in natural order.
     * @param <E> The element type
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    public static <E extends Comparable<E>> Set23<E> of(Iterable<E> elements) {
    	return of(Set23::naturalCompare, elements);
    }

    /**
     * Returns a set containing an initial list of elements, using a custom order.
     * @param <E> The element type
     * @param keyComparator The comparator for elements
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    public static <E> Set23<E> of(Comparator<E> keyComparator, Iterable<E> elements) {
    	TreeSet<E> list = new TreeSet<>(keyComparator);
    	for(E e: elements) {
    		list.add(e);
    	}
    	return new Set23<E>(keyComparator, List23.of(list), false);
    }

    /**
	 * Returns the size of this set.
	 * @return the size of this set
	 */
	public int size() {
		return keys.size();
	}
	
	/**
	 * Returns true if the set contains the given element.
	 * @param element The element to look for.
	 * @return The element to look for.
	 */
	public boolean contains(final E element) {
	    return indexOf(element) >= 0;
	}

    /**
     * Returns the index of the given element in the set.
     * @param element The element to look for.
     * @return The index of the given element in the set, -1 of not found.
     */
    public int indexOf(final E element) {
        return keys.root == null ? -1 : visit(keys.root, element, 0, (leaf, i) -> compare(element, leaf) == 0 ? i : -1);
    }
    
    /**
     * Returns the set of all elements in this set &gt;= element
     * @param element The comparison element (inclusive)
     * @return the set of all elements in this set &gt;= element
     */
	public Set23<E> tailSet(E element) {
		return new Set23<E>(keyComparator, keys.tail(findFirstElement(element)), reversed);
	}

    /**
     * Returns the set of all elements in this set &lt; element
     * @param element The comparison element (exclusive)
     * @return the set of all elements in this set &lt; element
     */
	public Set23<E> headSet(E element) {
		return new Set23<E>(keyComparator, keys.head(findFirstElement(element)), reversed);
	}

    /**
     * Returns the set of all elements in this set &lt; from or &gt;= to
     * @param low The low element (exclusive)
     * @param high The high element (inclusive)
     * @return the set of all elements in this set &lt; from or &gt;= to
     */
    public Set23<E> exclude(E low, E high) {
        return new Set23<E>(keyComparator, keys.exclude(findFirstElement(low), findFirstElement(high)), reversed);
    }

    /**
     * Returns the set of all elements in this set &gt;= from and &lt; to
     * @param low The low element (inclusive)
     * @param high The high element (exclusive)
     * @return the set of all elements in this set &lt; element
     */
	public Set23<E> subSet(E low, E high) {
		return new Set23<E>(keyComparator, keys.subList(findFirstElement(low), findFirstElement(high)), reversed);
	}

	/**
	 * Returns a set with the given element added.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
	 * @param element The element to add.
	 * @return a set with the given element added.
	 */
	public Set23<E> add(E element) {
	    Node23<E> n = keys.root;
	    return n == null ? new Set23<>(keyComparator, keys.add(element), reversed) :
	        visit(n, element, 0, (leaf, i) -> {
	            int cmp = compare(element, leaf);
	            return cmp == 0 ? this :
	                   cmp < 0 ? new Set23<>(keyComparator, keys.insertAt(i, element), reversed) :
	                   new Set23<>(keyComparator, keys.insertAt(i + 1, element), reversed);
	        });
	}

    /**
     * Returns a set with the elements reversed.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @return A set with the elements reversed
     */
	public Set23<E> reverse() {
		return new Set23<E>(keyComparator, keys.reverse(), !reversed);
	}

    /**
     * Returns a set with the given element removed.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param element The element to remove.
     * @return a set with the given element removed.
     */
	public Set23<E> remove(E element) {
		Node23<E> n = keys.root;
		return n == null ? this:
			visit(n, element, 0, (leaf, i) -> {
				return compare(element, leaf) == 0 ?
						new Set23<>(keyComparator, keys.removeAt(i), reversed) : this;
			});
	}
	
	/**
	 * Return the element at the given index.
	 * @param index The index.
	 * @return the element at the given index.
     * @throws IndexOutOfBoundsException if out of bounds
	 */
    public E getAt(int index) {
        return keys.get(index);
    }
    
    /**
     * Returns a set with the element at the given index removed.
     * THIS OPERATION IS IMMUTABLE.  The original set is left unchanged.
     * @param index The index of the element to remove.
     * @return a set with the element at the given index removed
     */
    public Set23<E> removeAt(int index) {
        return new Set23<E>(keyComparator, keys.removeAt(index), reversed);
    }

    /**
     * Compares two elements, taking reversing into account.
     * @param a left
     * @param b right
     * @return negative if a &lt; b zero if a == b, positive otherwise.
     */
	int compare(E a, E b) {
    	return reversed ? keyComparator.compare(b, a) : keyComparator.compare(a, b);
    }

	/**
	 * Returns the last element of a node.
	 * @param node The starting point.
	 * @return the last element of a node.
	 */
    E last(final Node23<E> node) {
    	return node.isLeaf() ? node.leafValue() : last(node.b_last());
    }

    /**
     * Returns the first element of a node.
     * @param node The starting point.
     * @return the last element of a node.
     */
    E first(final Node23<E> node) {
    	return node.isLeaf() ? node.leafValue() : first(node.b1());
    }

    /**
     * visits a leaf, calling a function at that point.
     * @param <T> The type of the object being returned
     * @param node The node to start from
     * @param element The element to compare to
     * @param index The index we are starting from
     * @param leafVisitor The visit to call
     * @return The return from the leafVisitor
     */
    <T> T visit(final Node23<E> node, final E element, final int index, final BiFunction<E,Integer,T> leafVisitor) {
    	return node.isLeaf() ? leafVisitor.apply(node.leafValue(), index) :
    		compare(element, last(node.b1())) <= 0 ? visit(node.b1(), element, index, leafVisitor):
    			node.b3() == null || compare(element, last(node.b2())) <= 0 ? visit(node.b2(), element, index + node.b1().size(), leafVisitor):
    				visit(node.b3(), element, index + node.b1().size() + node.b2().size(), leafVisitor);
    }

    /**
     * Finds the first element at or before the given element.
     * @param element The element to compare to
     * @return the index of first element at or before the given element
     */
    int findFirstElement(final E element) {
    	Node23<E> n = keys.root;
    	return n == null ? 0:
    		   compare(element, first(n)) < 0 ? 0:
    		   compare(element, last(n)) > 0 ? size():
    		   visit(n, element, 0, (leaf, i) -> i);
    }

    /**
     * Natural compare of two elements.
     * @param <E> The type of the objects being compared
     * @param a left
     * @param b right
     * @return negative if a &lt; b, zero if a == b positive otherwise
     */
    static <E extends Comparable<E>> int naturalCompare(E a, E b) {
		return a == null ?
				(b == null) ? 0 : -1:
				(b == null) ? 1 : a.compareTo(b);
	}

    /**
     * Returns the read-only {@link Set} view of this set.
     * @return the {@link Set} view of this set
     */
	SortedSet<E> asSet() {
		return new Set23Set<>(this);
	}
	
    /**
     * Returns the {@link List23} view of this set.
     * @return the {@link List23} view of this set
     */
	List23<E> asList() {
		return keys;
	}
	
    @Override
	public int hashCode() {
		return asSet().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
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
		return keys.iterator();
	}
    
    public Stream<E> strean() {
        return keys.stream();
    }
}
