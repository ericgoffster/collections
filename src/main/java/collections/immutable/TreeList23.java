package collections.immutable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.granitesoft.requirement.Requirements;

/**
 * Represents an Immutable list as a 23-tree.
 * <p>*ALL OPERATIONS ARE IMMUTABLE*.  The object is not modified in any way.
 * <p>A 23-tree is a semi-balanced tree, each branch has 2 or 3 nodes.
 * The nodes are ordered such that the leftmost side of the list
 * is in the left-most branch, and the rightmost side of the list is in the right-most branch.
 * <p>
 * This version of a 23-tree is immutable.   All operations on a tree leave the original
 * tree unchanged.
 * <p>
 * The following operations are all O(log n) worst case:
 * <ul>
 *     <li>{@link TreeList23#insertAt(int, Object)}</li>
 *     <li>{@link TreeList23#removeRange(int, int)}</li>
 *     <li>{@link TreeList23#appendList(ImmList)}</li>
 *     <li>{@link TreeList23#getAt(int)}</li>
 * </ul>
 * 
 * Which is significant, there is no need for a builder.
 * @param <E> The type of the elements.
 */
final class TreeList23<E> implements ImmList<E> {
    /**
     * The root of the tree.
     */
	final Node23<E> root;
	
	TreeList23(final Node23<E> root) {
	    assert root == null || root.isValid(root.getDepth());
		this.root = root;
	}

    static <E> TreeList23<E> empty() {
        return new TreeList23<>(null);
    }

    static <E> TreeList23<E> singleton(final E element) {
        return new TreeList23<>(new Leaf<>(element));
    }

	static <E> TreeList23<E> of(final Iterable<? extends E> elements) {
	    if (elements instanceof TreeList23) {
	        @SuppressWarnings("unchecked")
            TreeList23<E> elements2 = (TreeList23<E>)elements;
            return elements2;
	    }
        if (elements instanceof TreeSet23) {
            @SuppressWarnings("unchecked")
            TreeList23<E> elements2 = ((TreeSet23<E>)elements).elements;
            return elements2;
        }
        if (elements instanceof HashSet23) {
            @SuppressWarnings("unchecked")
            TreeList23<E> elements2 = ((HashSet23<E>)elements).elements;
            return elements2;
        }
        return quickConstruct(new LeafIterator<>(elements));
	}

	static <E> TreeList23<E> ofFiltered(final Predicate<E> filter, final Iterable<? extends E> elements) {
        return quickConstruct(new FilteredIterator<>(new LeafIterator<>(elements), filter));
    }

    static <E> TreeList23<E> ofSortedUnique(final Comparator<? super E> comparator,final Iterable<? extends E> elements) {
        return quickConstruct(new RemoveDupsIterator<>(sortLeaves(comparator, new LeafIterator<>(elements)), comparator));
    }

    @Override
    public <F> TreeList23<F> map(final Function<E, F> function) {
        return root == null ? empty() : new TreeList23<>(root.map(function));
    }

    @Override
	public List<E> asCollection() {
		return new List23List<>(this);
	}
	
	@Override
	public int size() {
		return root == null ? 0 : root.size();
	}
	
    @Override
	public E getAt(final int index) {
		return root.get(validateIndex(index));
	}

    private int validateIndex(final int index) {
        return Requirements.require(index,  Requirements.and(Requirements.ge(0), Requirements.lt(size())), (i, p) -> new IndexOutOfBoundsException("index: " + p));
    }
	
    @Override
    public TreeList23<E> filter(final Predicate<E> filter) {
        return TreeList23.ofFiltered(Requirements.require(filter, Requirements.notNull(), () -> "filter"), this);
    }

    @Override
    public TreeList23<E> retain(final Iterable<? extends E> other) {
        final ImmSet<E> hs = ImmCollections.asSet(Requirements.require(other, Requirements.notNull(), () -> "other"));
        return filter(hs::contains);
    }
    
    @Override
    public TreeList23<E> removeAllIn(final Iterable<? extends E> other) {
        final ImmSet<E> hs = ImmCollections.asSet(Requirements.require(other, Requirements.notNull(), () -> "other"));
        return filter(e -> !hs.contains(e));
    }
    
    @Override
	public TreeList23<E> add(final E element) {
        return replaceRangeInt(size(), size(), TreeList23.singleton(element));
	}
	
    @Override
	public TreeList23<E> setAt(final int index, final E element) {
        validateIndex(index);
	    return replaceRangeInt(index, index + 1, TreeList23.singleton(element));
	}
	
    @Override
	public TreeList23<E> insertAt(final int index, final E element) {
        final int i = Requirements.require(index,  Requirements.and(Requirements.ge(0), Requirements.le(size())), (ind, p) -> new IndexOutOfBoundsException("index: " + p));
        return replaceRangeInt(i, i, TreeList23.singleton(element));
	}
	
    @Override
	public TreeList23<E> removeAt(final int index) {
       final int i = validateIndex(index);
       return replaceRangeInt(i, i + 1, empty());
	}
	
    @Override
    public TreeList23<E> removeRange(final int low, final int high) {
        return replaceRangeInt(validateRangeLow(low, high), validateRangeHigh(high), empty());
    }

    private int validateRangeLow(final int low, final int high) {
        return Requirements.require(low,  Requirements.and(Requirements.ge(0), Requirements.le(high)), (i, p) -> new IndexOutOfBoundsException("low: " + p));
    }

    private int validateRangeHigh(final int high) {
        return Requirements.require(high,  Requirements.and(Requirements.ge(0), Requirements.le(size())), (i, p) -> new IndexOutOfBoundsException("high: " + p));
    }
    
    @Override
    public TreeList23<E> replaceRange(final int low, final int high, final ImmList<E> other) {
        return replaceRangeInt(validateRangeLow(low, high), validateRangeHigh(high), Requirements.require(other, Requirements.notNull(), () -> "other"));
    }

    private TreeList23<E> replaceRangeInt(final int l, final int h, final ImmList<E> other) {
        return l == h && other.size() == 0 ?
            this:
            headAt(l).appendList(other).appendList(tailAt(h));
    }
    
    @Override
    public TreeList23<E> insertListAt(final int index, final ImmList<E> other) {
        final int i = Requirements.require(index,  Requirements.and(Requirements.ge(0), Requirements.le(size())), (i1, p) -> new IndexOutOfBoundsException("index: " + p));
        return replaceRange(i, i, Requirements.require(other, Requirements.notNull(), () -> "other"));
    }
	
    @Override
	public TreeList23<E> appendList(final ImmList<E> other) {
        final TreeList23<E> tother = TreeList23.of(Requirements.require(other, Requirements.notNull(), () -> "other"));
        return tother.root == null ? this:
            root == null ? tother:
            new TreeList23<>(concat(root, tother.root));
	}
	
    @Override
	public TreeList23<E> tailAt(final int index) {
        Requirements.require(index,  Requirements.and(Requirements.ge(0), Requirements.le(size())), (i, p) -> new IndexOutOfBoundsException("index: " + p));
	    return index == 0 ? this :
	           index == size() ? empty() :
	               new TreeList23<>(root.tail(index));
	}
	
    @Override
	public TreeList23<E> headAt(final int index) {
        Requirements.require(index,  Requirements.and(Requirements.ge(0), Requirements.le(size())), (i, p) -> new IndexOutOfBoundsException("index: " + p));
		return index == size() ?  this : index == 0 ? empty(): new TreeList23<>(root.head(index));
	}
	
    @Override
	public TreeList23<E> getRange(final int low, final int high) {
        validateRangeHigh(high);
        validateRangeLow(low, high);
		return tailAt(low).headAt(high - low);
	}
	
    @Override
	public TreeList23<E> reversed() {
	    if (size() < 2) {
	        return this;
	    }
	    
		return new TreeList23<E>(root.reverse());
	}

	@Override
    public int hashCode() {
    	return asCollection().hashCode();
    }

    @Override
    public boolean equals(final Object otherObject) {
    	if (!(otherObject instanceof TreeList23)) {
    		return false;
    	}
    	final TreeList23<?> other = (TreeList23<?>)otherObject;
    	return asCollection().equals(other.asCollection());
    }

    @Override
    public String toString() {
    	return asCollection().toString();
    }

    @Override
    public ListIterator<E> iterator() {
        return root == null ? Collections.emptyListIterator() : root.iterator();
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return root == null ? Spliterators.emptySpliterator() : root.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /// Compares two elements, allowing for null.
	static <E extends Comparable<E>> int naturalCompare(final E a, final E b) {
		return a == null ?
				((b == null) ? 0 : -1):
				(b == null) ? 1 : a.compareTo(b);
	}

	// Quickly constructs a list from a collection of nodes.
	// O(n log n)
	static <E> TreeList23<E> quickConstruct(final Iterator<? extends Node23<E>> nodes) {
	    if (!nodes.hasNext()) {
	        return TreeList23.empty();
	    }
	    final Node23<E> b0 = nodes.next();
        if (!nodes.hasNext()) {
            return new TreeList23<>(b0);
        }
	    return quickConstruct(new NodeConstructionIterator<E>(nodes, b0, nodes.next()));
	}

	// Returns the concatenation of lhs and rhs.
    // The returned node will never be degenerate.
	// O(log max(m,n))
	static <E> Node23<E> concat(final Node23<E> lhs, final Node23<E> rhs) {
	    assert lhs != null;
        assert rhs != null;
        final int depthDelta = lhs.getDepth() - rhs.getDepth();
        if (depthDelta == 0) {
            return new Branch<>(lhs, rhs);
        }
        @SuppressWarnings("rawtypes")
        final Node23[] nodes = new Node23[2];
        @SuppressWarnings("unchecked")
        final Node23<E>[] nodes2 = nodes;
        final int nodelen;
	    if (depthDelta >= 0) {
	        nodelen = append(lhs, rhs, depthDelta, nodes2, 0);
	    } else {
	        nodelen = prepend(lhs, rhs, -depthDelta, nodes2);
	    }
	    return nodelen == 1 ? nodes2[0] : new Branch<>(nodes2[0], nodes2[1]);
    }

	// Combines 2-4 nodes into a list of one or 2 nodes.
    private static <E> int combine(final Node23<E>[] arr, final int arrlen, final Node23<E>[] nodes, final int pos) {
        switch(arrlen) {
        case 2: {
            nodes[pos] = new Branch<>(arr[0], arr[1]);
            return pos + 1;
        }
        case 3: {
            nodes[pos] = new Branch<>(arr[0], arr[1], arr[2]);
            return pos + 1;
        }
        case 4: {
            nodes[pos] = new Branch<>(arr[0], arr[1]);
            nodes[pos + 1] = new Branch<>(arr[2], arr[3]);
            return pos + 2;
        }
	    default: throw new IllegalStateException();
	    }
    }

	static <E> int prepend(final Node23<E> lhs, final Node23<E> rhs, final int depthDelta, final Node23<E>[] result) {
	    assert lhs != null;
	    assert rhs != null;
	    assert depthDelta >= 0;

	    if (depthDelta == 0) {
	        result[0] = lhs;
            result[1] = rhs;
            return 2;
	    }

        @SuppressWarnings("rawtypes")
        final Node23[] arr = new Node23[4];
        @SuppressWarnings("unchecked")
        final Node23<E>[] arr2 = arr;
	    int arrlen = prepend(lhs, rhs.getBranch(0), depthDelta - 1, arr2);
        for(int i = 1; i < rhs.numBranches(); i++) {
            arr2[arrlen++] = rhs.getBranch(i);
        }
	    return combine(arr2, arrlen, result, 0);            
	}

	static <E> int append(final Node23<E> lhs, final Node23<E> rhs, final int depthDelta, final Node23<E>[] result, final int pos) {
        assert lhs != null;
        assert rhs != null;
        assert depthDelta >= 0;

        if (depthDelta == 0) {
            result[pos] = lhs;
            result[pos + 1] = rhs;
            return pos + 2;
        }

        @SuppressWarnings("rawtypes")
        final Node23[] arr = new Node23[4];
        @SuppressWarnings("unchecked")
        final Node23<E>[] arr2 = arr;
        int arrlen = 0;
        for(int i = 0; i < lhs.numBranches() - 1; i++) {
            arr2[arrlen++] = lhs.getBranch(i);
        }
        arrlen = append(lhs.getBranch(lhs.numBranches() - 1), rhs, depthDelta - 1, arr2, arrlen);
        return combine(arr2, arrlen, result, pos);            
	}

    // Warning, all elements in this list must follow order governed by this comparator
    int getIndexOf(final Function<? super E, Integer> comparator) {
        return root == null ? -1 : root.binarySearch(comparator, (leaf, i) -> comparator.apply(leaf) == 0 ? i : -1);
    }
    
    // Returns the position where the element belongs
    // Warning, all elements in this list must follow order governed by this comparator
    int naturalPosition(final Function<? super E, Integer> comparator) {
        return root == null ? 0 : root.binarySearch(comparator, (leaf, i) -> comparator.apply(leaf) > 0 ? (i + 1) : i);
    }

    static <E> Iterator<Leaf<E>> sortLeaves(final Comparator<? super E> comparator,
            final Iterator<Leaf<E>> elements) {
        final List<Leaf<E>> nodes = new ArrayList<>();
        elements.forEachRemaining(nodes::add);
        Collections.sort(nodes, (i,j) -> comparator.compare(i.leafValue(),j.leafValue()));
        return nodes.iterator();
    }
}
