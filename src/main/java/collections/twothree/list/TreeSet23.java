package collections.twothree.list;

import java.util.Comparator;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.granitesoft.requirement.Requirements;

/**
 * Represents an Immutable ordered set of elements using a {@link ImmList} as a backing store.
 * Set membership and ordering is implemented with a comparator.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.  The object is not modified in any way.
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
public final class TreeSet23<E> implements ImmSortedSet<E> {
    /**
     * The comparator for elements in the set.   Defines the ordering.
     */
	final Comparator<? super E> comparator;

	/**
	 * The list of elements
	 */
	final TreeList23<E> elements;

	TreeSet23(final Comparator<? super E> comparator, final TreeList23<E> elements) {
		this.elements = Requirements.require(elements, Requirements.notNull(), () -> "elements");
		this.comparator = Requirements.require(comparator, Requirements.notNull(), () -> "comparator");
	}
	
	/**
	 * Returns a single set of <code>element</code>.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     assert TreeSet23.singleton(6).asList().asCollection().equals(Arrays.asList(6));
     * </pre>
	 * @param <E> The element type
	 * @param element The singleton element
	 * @return A set of exactly one element
	 */
    public static <E extends Comparable<E>> TreeSet23<E> singleton(final E element) {
        return new TreeSet23<E>(TreeList23::naturalCompare, TreeList23.singleton(element));
    }

    /**
     * Returns the empty set, using a custom ordering.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     assert TreeSet23.empty(Integer::compare).asList().asCollection().equals(Arrays.asList());
     * </pre>
     * @param comparator The comparator which defines ordering.
     * @param <E> The element type
     * @return An empty set.
     */
    public static <E> TreeSet23<E> empty(Comparator<? super E> comparator) {
        return new TreeSet23<E>(comparator, TreeList23.empty());
    }

    /**
     * Returns the empty set.
     * <p>This operation is O(1).
     * <pre>
     * Example:
     *     assert TreeSet23.empty().asList().asCollection().equals(Arrays.asList());
     * </pre>
     * @param <E> The element type
     * @return An empty set.
     */
    public static <E extends Comparable<E>> TreeSet23<E> empty() {
        return empty(TreeList23::naturalCompare);
    }

    /**
     * Returns a set containing an initial list of elements, using natural ordering.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     assert TreeSet23.of(Arrays.asList(4, 2, 3)).asList().asCollection().equals(Arrays.asList(2, 3, 4));
     * </pre>
     * @param <E> The element type
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    public static <E extends Comparable<E>> TreeSet23<E> of(final Iterable<? extends E> elements) {
    	return of(TreeList23::naturalCompare, elements);
    }
    
    /**
     * Returns a set containing an initial list of elements from <code>sortedSet</code>.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     assert TreeSet23.of(TreeSet23.of(Arrays.asList(4, 2, 3)).asCollection()).asList().asCollection().equals(Arrays.asList(2, 3, 4));
     * </pre>
     * @param <E> The element type
     * @param sortedSet The set of elements
     * @return A set containing an initial list of elements
     */
    public static <E> TreeSet23<E> ofSorted(final SortedSet<E> sortedSet) {
        return new TreeSet23<>(getComparator(sortedSet), TreeList23.of(sortedSet));
    }

    /**
     * Returns a set containing an initial list of elements, using custom ordering.
     * <p>This operation is O(n log n).
     * <pre>
     * Example:
     *     Comparator&lt;Integer&gt; comp = Integer::compare;
     *     assert TreeSet23.of(comp, Arrays.asList(4, 2, 3)).asList().asCollection().equals(Arrays.asList(2, 3, 4));
     *     assert TreeSet23.of(comp.reversed(), Arrays.asList(4, 2, 3)).asList().asCollection().equals(Arrays.asList(4, 3, 2));
     * </pre>
     * @param <E> The element type
     * @param comparator The comparator of elements
     * @param elements The array of elements
     * @return A set containing an initial list of elements
     */
    public static <E> TreeSet23<E> of(final Comparator<? super E> comparator, final Iterable<? extends E> elements) {
    	return new TreeSet23<E>(comparator, TreeList23.ofSortedUnique(comparator, elements));
    }
    
    @Override
	public int size() {
		return elements.size();
	}
	
    @Override
	public boolean contains(final E element) {
	    return indexOf(element) >= 0;
	}

    @Override
    public int indexOf(final E element) {
        return elements.getIndexOf(e -> comparator.compare(element, e));
    }
    
    @Override
	public TreeSet23<E> ge(final E element) {
		return new TreeSet23<E>(comparator, elements.tailAt(elements.naturalPosition(e -> comparator.compare(element, e))));
	}

    @Override
	public TreeSet23<E> lt(final E element) {
		return new TreeSet23<E>(comparator, elements.headAt(elements.naturalPosition(e -> comparator.compare(element, e))));
	}

    @Override
    public TreeSet23<E> exclude(final E low, final E high) {
        int cmp = comparator.compare(low, high);
        if (cmp > 0) {
            throw new IllegalArgumentException("low must be <= high");
        }
        if (comparator.compare(low, high) == 0) {
            return this;
        }
        return new TreeSet23<E>(comparator, elements.removeRange(
                elements.naturalPosition(e -> comparator.compare(low, e)),
                elements.naturalPosition(e -> comparator.compare(high, e))));
    }

    @Override
	public TreeSet23<E> subSet(final E low, final E high) {
        int cmp = comparator.compare(low, high);
        if (cmp > 0) {
            throw new IllegalArgumentException("low must be <= high");
        }
        if (comparator.compare(low, high) == 0) {
            return new TreeSet23<E>(comparator, TreeList23.empty());
        }
		return new TreeSet23<E>(comparator, elements.getRange(
		        elements.naturalPosition(e -> comparator.compare(low, e)),
		        elements.naturalPosition(e -> comparator.compare(high, e))));
	}

    @Override
	public TreeSet23<E> add(final E element) {
	    if (contains(element)) {
	        return this;
	    }
	    return new TreeSet23<>(comparator, elements.insertAt(elements.naturalPosition(e -> comparator.compare(element, e)), element));
	}
	
    @Override
	public TreeSet23<E> union(final ImmSet<E> other) {
	    TreeSet23<E> s = this;
	    for(E e: other) {
	        s = s.add(e);
	    }
	    return s;
	}

    @Override
	public TreeSet23<E> reversed() {
		return new TreeSet23<E>(comparator.reversed(), elements.reversed());
	}

    @Override
	public TreeSet23<E> remove(final E element) {
	    int index = indexOf(element);
	    return index < 0 ? this : new TreeSet23<>(comparator, elements.removeAt(index));
	}
	
    @Override
    public TreeSet23<E> filter(final Predicate<E> filter) {
        return new TreeSet23<>(comparator, elements.filter(filter));
    }
	
    @Override
    public TreeSet23<E> retain(final Iterable<? extends E> other) {
        final HashSet23<E> hs = HashSet23.of(other);
        return filter(hs::contains);
    }

    @Override
    public TreeSet23<E> removeAllIn(final Iterable<? extends E> other) {
        TreeSet23<E> m = this;
        for(E e: other) {
            m = m.remove(e);
        }
        return m;
    }
  
    @Override
    public E getAt(final int index) {
        return elements.getAt(index);
    }
    
    @Override
    public TreeSet23<E> removeAt(final int index) {
        return new TreeSet23<E>(comparator, elements.removeAt(index));
    }

    @Override
	public SortedSet23Set<E> asCollection() {
		return new SortedSet23Set<>(this);
	}
	
    @Override
	public TreeList23<E> asList() {
		return elements;
	}
	
    @Override
	public int hashCode() {
		return asCollection().hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof ImmSet)) {
			return false;
		}
		ImmSet<?> other = (ImmSet<?>)obj;
		return asCollection().equals(other.asCollection());
	}
	
	@Override
	public String toString() {
		return asCollection().toString();
	}
	
	@Override
	public ListIterator<E> iterator() {
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
    
    @Override
    public Comparator<? super E> getComparator() {
        return comparator;
    }

    static <E> Comparator<? super E> getComparator(final SortedSet<E> sortedSet) {
        final Comparator<? super E> comparator = sortedSet.comparator();
        if (comparator == null) {
            return TreeSet23::unNaturalCompare;
        }
        return comparator;
    }

    /// Compares two elements, allowing for null.
    static <E> int unNaturalCompare(final E a, final E b) {
        if (a == null) {
            return (b == null) ? 0 : -1;
        }
        if (b == null) {
            return 1;
        }
        
        @SuppressWarnings("unchecked")
        final Comparable<? super E> ea = (Comparable<? super E>) a;
        return ea.compareTo(b);
    }
}
