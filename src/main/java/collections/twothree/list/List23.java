package collections.twothree.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Stream;

import org.granitesoft.requirement.Requirements;

/**
 * Represents a list as a 23-tree.
 * A 23-tree is a semi-balanced tree, each branch has 2 or 3 nodes.
 * The nodes are ordered such that the leftmost side of the list
 * is in the left-most branch, and the rightmost side of the list is in the right-most branch.
 * This version of a 23-tree is immutable.   All operations on a tree, leave the original
 * tree unchanged.  While other list implementations have bad insertion times (O(n)) into the middle of a list
 * all such operations here are O(log n) (given this is a balanced tree).
 * In addition, there are plenty of nice list manipulation methods not found in other list implementations.
 *
 * @param <E> The type of the elements.
 */
public final class List23<E> implements Iterable<E> {
    /**
     * The root of the tree.
     */
	final Node23<E> root;
	
	/**
	 * True if reversed.   If reversed, branches should
	 * be viewed in reverse order.
	 */
	final boolean reversed;

	List23(final Node23<E> root, boolean reversed) {
	    assert root == null || isValid(root, getDepth(root));
		this.root = root;
		this.reversed = reversed;
	}

    /**
     * The empty list.
     * This operation is O(1).
     * @param <E> The type of the elements.
     * @return The List23 representation of empty
     */
    public static <E> List23<E> empty() {
        return new List23<>(null, false);
    }

    /**
     * Construction of list with a single element.
     * This operation is O(1).
     * <pre>
     * Example:
     *     List23.of(5) == [5]
     * </pre>
     * @param <E> The type of the element.
     * @param element The element
     * @return The List23 representation of the element
     */
    public static <E> List23<E> of(final E element) {
        return new List23<>(new Leaf<>(element), false);
    }

	/**
	 * Easy construction of list.
	 * This operation is O(n log n).
     * <pre>
     * Example:
     *     List23.of() == []
     *     List23.of(5) == [5]
     *     List23.of(5, 7, 9) == [5, 7, 9]
     *     List23.of(5, 7, 5) == [5, 7, 5]
     * </pre>
     * @param <E> The type of the elements.
	 * @param elements The collections of elements
	 * @return The List23 representation of "elements"
	 */
	@SafeVarargs
    @SuppressWarnings("varargs")
	public static <E> List23<E> of(final E ... elements) {
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
		return of(Arrays.asList(elements));
	}

	/**
	 * Easy construction of list.
	 * This operation is O(n log n).
     * <pre>
     * Example:
     *     List23.of(Arrays.asList()) == []
     *     List23.of(Arrays.asList(5)) == [5]
     *     List23.of(Arrays.asList(5, 7, 9)) == [5, 7, 9]
     *     List23.of(Arrays.asList(5, 7, 5)) == [5, 7, 5]
     * </pre>
     * @param <E> The type of the elements.
	 * @param elements The array of elements
	 * @return The List23 representation of "elements"
	 */
	public static <E> List23<E> of(final Iterable<? extends E> elements) {
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
		List<Node23<E>> nodes = new ArrayList<>();
		for(E e: elements) {
			nodes.add(new Leaf<>(e));
		}
		if (nodes.isEmpty()) {
			return empty();
		}
		return quickConstruct(nodes);
	}
	
	/**
	 * Easy construction of sorted list.
	 * Creates a list23 represents of the elements sorted by a comparator.
	 * This operation is O(n log n).
     * <pre>
     * Example:
     *     List23.ofSorted(Integer::compare, Arrays.asList(6, 1, 6 , 8)) == [1, 6, 6, 8]
     * </pre>
     * @param <E> The type of the elements.
     * @param comparator The element comparator
	 * @param elements The collections of elements
	 * @return The sorted List23 representation of "elements"
	 */
	public static <E> List23<E> ofSorted(final Comparator<E> comparator, final Iterable<? extends E> elements) {
	    Requirements.require(comparator, Requirements.notNull(), () -> "comparator");
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
		List<Node23<E>> nodes = new ArrayList<>();
		for(E e: elements) {
			nodes.add(new Leaf<>(e));
		}
		if (nodes.isEmpty()) {
            return empty();
		}
		Collections.sort(nodes, (i,j) -> comparator.compare(i.leafValue(),j.leafValue()));
		return quickConstruct(nodes);
	}

	/**
	 * Easy construction of a sorted list.
	 * Creates a List23 representation of elements sorted by a comparator.
	 * This operation is O(n log n).
     * <pre>
     * Example:
     *     List23.ofSorted(Integer::compare, 6, 1, 6 , 8) == [1, 6, 6, 8]
     * </pre>
     * @param <E> The type of the elements
     * @param comparator The element comparator
	 * @param elements The array of elements
	 * @return The sorted List23 representation of "elements"
	 */
	@SafeVarargs
    @SuppressWarnings("varargs")
	public static <E> List23<E> ofSorted(final Comparator<E> comparator, final E ... elements) {
        Requirements.require(comparator, Requirements.notNull(), () -> "comparator");
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
		return ofSorted(comparator, Arrays.asList(elements));
	}

	/**
     * Easy construction of a sorted list.
     * Creates a List23 representation of elements sorted by a comparator.
	 * This operation is O(n log n).
     * <pre>
     * Example:
     *     List23.ofSorted(Arrays.asList(6, 1, 6 , 8)) == [1, 6, 6, 8]
     * </pre>
     * @param <E> The type of the elements
	 * @param elements The collection of elements
	 * @return The sorted List23 representation of "elements"
	 */
	public static <E extends Comparable<E>> List23<E> ofSorted(final Iterable<? extends E> elements) {
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
		return ofSorted(List23::naturalCompare, elements);
	}

	/**
	 * Easy construction of a sorted list.
	 * Creates a List23 representation of elements sorted by their natural ordering.
	 * This operation is O(n log n).
     * <pre>
     * Example:
     *     List23.ofSorted(6, 1, 6 , 8) == [1, 6, 6, 8]
     * </pre>
     * @param <E> The type of the elements
	 * @param elements The array of elements
	 * @return The sorted List23 representation of "elements"
	 */
	@SafeVarargs
    @SuppressWarnings("varargs")
	public static <E extends Comparable<E>> List23<E> ofSorted(final E ... elements) {
        Requirements.require(elements, Requirements.notNull(), () -> "elements");
		return ofSorted(Arrays.asList(elements));
	}

	/**
	 * Returns a classic "read only java List" view of the list.
	 * This operation is O(1).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).asList() == [1, 6, 6, 8]
     * </pre>
	 * @return A classic "read only java List" view of the list
	 */
	public List<E> asList() {
		return new List23List<>(this);
	}
	
	/**
	 * Returns the number of elements in the list.
	 * This operation is O(1).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).size() == 4
     * </pre>
	 * @return The number of elements in the list
	 */
	public int size() {
		return root == null ? 0 : root.size();
	}
	
	/**
	 * Returns the element at the given index.
	 * This operation is O(log n).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).get(2) == 6
     * </pre>
	 * @param index The index to get. size() &gt; index &gt;= 0
	 * @return the element at the given index
	 * @throws IndexOutOfBoundsException if out of bounds
	 */
	public E get(final int index) {
		return get(root, verifyIndex(index, 0, size() - 1));
	}
	
	/**
	 * Returns true if we contain the given element.
	 * This is O(n)!!  Not fast!
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).contains(2) == false
     *     List23.of(6, 1, 6 , 8).contains(6) == true
     * </pre>
	 * @param element The element to look for
	 * @return true if we contain the given element
	 */
    public boolean contains(E element) {
        return indexOf(element) >= 0;
    }
    
    /**
     * Returns the index of the given element.
     * This is O(n)!!  Not fast!
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).indexOf(1) == 1
     *     List23.of(6, 1, 6 , 8).indexOf(6) == 0
     *     List23.of(6, 1, 6 , 8).indexOf(7) == -1
     * </pre>
     * @param element The element to look for
     * @return the index of the given element, -1 if not found
     */
	public int indexOf(E element) {
	    return root == null ? -1: find(root, element);
	}
	
    /**
     * Returns the last index of the given element.
     * This is O(n)!!  Not fast!
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).lastIndexOf(1) == 1
     *     List23.of(6, 1, 6 , 8).lastIndexOf(6) == 2
     *     List23.of(6, 1, 6 , 8).lastIndexOf(7) == -1
     * </pre>
     * @param element The element to look for
     * @return the index of the given element, -1 if not found
     */
    public int lastIndexOf(E element) {
        int pos = reverse().indexOf(element);
        return pos < 0 ? -1 : size() - 1 - pos;
    }
    
    /**
     * Returns a list with the first element matching the given element removed.
     * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * This is O(n)!!  Not fast!
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).remove(1) == [6,6,8]
     *     List23.of(6, 1, 6 , 8).remove(6) == [1,6,8]
     *     List23.of(6, 1, 6 , 8).remove(7) == [6,1,6,8]
     * </pre>
     * @param element The element to remove
     * @return A list with the given element removed
     */
    public List23<E> remove(final E element) {
        int index = indexOf(element);
        return index < 0 ? this : removeAt(index);
    }

	/**
	 * Returns a list with the given element added to the end.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is O(log n).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).add(9) == [6,1,6,8,9]
     *     List23.of(6, 1, 6 , 8).add(1) == [6,1,6,8,1]
     * </pre>
	 * @param element The element to add.
	 * @return A list with the given element added to the end.
	 */
	public List23<E> add(final E element) {
        return replace(size(), size(), List23.of(element));
	}

	/**
	 * Returns a list with the given element set at the specified index.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is O(log n).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).set(2, 3) == [6,1,3,8]
     * </pre>
     * @param index The index to set.  size() &gt; index &gt;= 0
	 * @param element The element to set
	 * @return A list with the given element set at the specified index.
     * @throws IndexOutOfBoundsException if out of bounds
	 */
	public List23<E> set(final int index, final E element) {
	    verifyIndex(index, 0, size() - 1);
	    return replace(index, index + 1, List23.of(element));
	}
	
	/**
	 * Returns a list with the given element inserted at a specified position.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is O(log n).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).insertAt(2, 3) == [6,1,3,6,8]
     * </pre>
     * @param index The index to insert.  size() &gt;= index &gt;= 0
	 * @param element The element to insert
	 * @return A list with the given element set at the specified index
     * @throws IndexOutOfBoundsException if out of bounds
	 */
	public List23<E> insertAt(final int index, final E element) {
        verifyIndex(index, 0, size());
        return replace(index, index, List23.of(element));
	}
	
	/**
	 * Returns a list with the given index removed.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is O(log n).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).removeAt(2) == [6,1,8]
     * </pre>
     * @param index The index to remove.  size() &gt; index &gt;= 0
	 * @return A list with the given index removed.
     * @throws IndexOutOfBoundsException if out of bounds
	 */
	public List23<E> removeAt(final int index) {
       verifyIndex(index, 0, size() - 1);
       return replace(index, index + 1, empty());
	}
	
    /**
     * Returns a list with the given range removed.
     * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * This operation is O(log n).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).removeRange(1,3) == [6,8]
     *     List23.of(6, 1, 6 , 8).removeRange(1,1) == [6, 1, 6 , 8]
     *     List23.of(6, 1, 6 , 8).removeRange(0,4) == []
     * </pre>
     * @param low The low index (inclusive)
     * @param high The high index (exclusive)
     * @return A list with the given list appended to the end.
     * @throws IndexOutOfBoundsException if out of bounds
     */
    public List23<E> removeRange(final int low, final int high) {
        return replace(low, high, empty());
    }
    
    /**
     * Returns a list with the given range replaced with another list.
     * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * This operation is max(O(log m),O(log n)).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).replace(1,3,List23.of(7)) == [6,7,8]
     *     List23.of(6, 1, 6 , 8).replace(1,3,List23.of(7,5,3)) == [6,7,5,3,8]
     * </pre>
     * @param low The low index (inclusive)
     * @param high The high index (exclusive)
     * @param other The list to insert
     * @return A list with the given range replaced with another list
     * @throws IndexOutOfBoundsException if out of bounds
     */
    public List23<E> replace(final int low, final int high, final List23<E> other) {
        verifyIndex(high, 0, size());
        verifyIndex(low, 0, high);
        Requirements.require(other, Requirements.notNull(), () -> "other");
        if (low == high && other.size() == 0) {
            return this;
        }
        return head(low).append(other).append(tail(high));
    }
    
    /**
     * Returns a list with another list inserted at the specified index. 
     * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
     * This operation is max(O(log m),O(log n)).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).insertList(2,List23.of(7)) == [6,1,7,6,8]
     *     List23.of(6, 1, 6 , 8).insertList(2,List23.of(7,5,3)) == [6,1,7,5,3,6,8]
     * </pre>
     * @param index The index to insert at
     * @param other The list to insert
     * @return A list with another list inserted at the specified index
     */
    public List23<E> insertList(final int index, final List23<E> other) {
        verifyIndex(index, 0, size());
        Requirements.require(other, Requirements.notNull(), () -> "other");
        return replace(index, index, other);
    }
	
	/**
	 * Returns a list with the given list appended to the end.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is max(O(log m),O(log n)).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).append(List23.of(7)) == [6,1,6,8,7]
     *     List23.of(6, 1, 6 , 8).insertList(2,List23.of(7,5,3)) == [6,1,6,7,5,3]
     * </pre>
	 * @param other The list to append
	 * @return A list with the given list appended to the end
	 */
	public List23<E> append(final List23<E> other) {
		Requirements.require(other, Requirements.notNull(), () -> "other");
		return other.root == null ? this:
		       root == null ? other:
		       new List23<>(concat(root, other.root), reversed);
	}
	
	/**
	 * Returns a list with all indexes &gt;= the specified index.
	 * This operation is O(log n).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).tail(2) == [6,8,7]
     *     List23.of(6, 1, 6 , 8).tail(4) == []
     *     List23.of(6, 1, 6 , 8).tail(0) == [6, 1, 6 , 8]
     * </pre>
	 * @param index The chopping point (inclusive)
	 * @return a list with all indexes &gt;= the specified index
     * @throws IndexOutOfBoundsException if out of bounds
	 */
	public List23<E> tail(final int index) {
	    verifyIndex(index, 0, size());
	    return index == 0 ? this : index == size() ? empty() : reverse().head(size() - index).reverse();
	}
	
	/**
	 * Returns a list with all indexes &lt; the specified index.
	 * This operation is O(log n).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).head(2) == [6,1]
     *     List23.of(6, 1, 6 , 8).head(4) == [6, 1, 6 , 8]
     *     List23.of(6, 1, 6 , 8).tail(0) == []
     * </pre>
	 * @param index The chopping point (exclusive)
	 * @return a list with all indexes &lt; the specified index
     * @throws IndexOutOfBoundsException if out of bounds
	 */
	public List23<E> head(final int index) {
        verifyIndex(index, 0, size());
		return index == size() ?  this : index == 0 ? empty(): new List23<>(head(root, index), reversed);
	}
	
	/**
	 * Returns a list with all indexes &gt;= from and &lt; to
	 * This operation is O(log n).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).subList(1, 3) == [6,1]
     *     List23.of(6, 1, 6 , 8).head(4) == [6, 1, 6 , 8]
     *     List23.of(6, 1, 6 , 8).tail(0) == []
     * </pre>
	 * @param from The starting point, inclusive.
	 * @param to The end point, exclusive
	 * @return a list with all indexes &gt;= from and &lt; to
     * @throws IndexOutOfBoundsException if out of bounds
	 */
	public List23<E> subList(final int from, final int to) {
        verifyIndex(to, 0, size());
	    verifyIndex(from, 0, to);
		return tail(from).head(to - from);
	}
	
	/**
	 * Returns a list that is the original list reversed.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is O(1).
     * <pre>
     * Example:
     *     List23.of(6, 1, 6 , 8).reverse() == [8,6,1,6]
     * </pre>
	 * @return a list that is the original list reversed
	 */
	public List23<E> reverse() {
		return new List23<E>(root, !reversed);
	}

	int verifyIndex(int index, int low, int high) {
        if (index < low || index > high) {
            throw new IndexOutOfBoundsException(String.format("Index: %d, Low: %d, High %d", index, low, high));
        }
        return index;
    }

    /**
	 * Compares two elements, allowing for null.
	 * @param <E> The element type
	 * @param a left hand side
	 * @param b right hand side
	 * @return negative for less than, zero for equal, positive for greater than
	 */
	static <E extends Comparable<E>> int naturalCompare(final E a, final E b) {
		return a == null ?
				(b == null) ? 0 : -1:
				(b == null) ? 1 : a.compareTo(b);
	}

	/**
	 * Quickly constructs a list from a collection of nodes.
	 * @param <E> The element type
	 * @param nodes The list of nodes
	 * @return The 23 list
	 */
	static <E> List23<E> quickConstruct(final List<Node23<E>> nodes) {
		// Just one left?  It is the root
		if (nodes.size() == 1) {
			return new List23<E>(nodes.get(0), false);
		}
		
		List<Node23<E>> newNodes = new ArrayList<>(nodes.size() / 2);
		int i = 0;
		
		// We have >= 2 nodes.
		// If there are an odd number nodes, bring it down to even
		// with a 3 node.
	    if (nodes.size() % 2 == 1) {
            newNodes.add(new Branch3<E>(nodes.get(i), nodes.get(i + 1), nodes.get(i + 2)));
            i += 3;
	    }
	    
	    // We now have an even number of nodes >= 0, drain them down in pairs.
	    while(i < nodes.size()) {
	        newNodes.add(new Branch2<E>(nodes.get(i), nodes.get(i + 1)));
	        i += 2;
	    }
		
		// Create a tree from the new layer of nodes.
		return quickConstruct(newNodes);
	}

	/**
	 * Returns left most branch.   Will only be null if this is a leaf.
	 * @param node The node
	 * @return the left most branch.
	 */
	Node23<E> b1(final Node23<E> node) {
		return reversed ? node.r_b1() : node.b1();
	}

	/**
	 * Returns the second branch.
	 * Will be null if a leaf, or a 1-branch.
	 * @param node The node
	 * @return the second branch.
	 */
	Node23<E> b2(final Node23<E> node) {
		return reversed ? node.r_b2() : node.b2();
	}

	/**
	 * Returns the third branch.
	 * Will be null if a leaf, or not a 3-branch.
	 * @param node The node
	 * @return the third branch.
	 */
	Node23<E> b3(final Node23<E> node) {
		return reversed ? node.r_b3() : node.b3();
	}
	
	/**
	 * Returns the right most branch.  Will only be null if this is a leaf.
	 * @param node The node
	 * @return the right most branch.
	 */
	Node23<E> b_last(final Node23<E> node) {
		return reversed ? node.b1() : node.b_last();
	}
	
	static class Pair<E> {
	    final Node23<E> b1;
	    final Node23<E> b2;
	    
	    public Pair(Node23<E> b1, Node23<E> b2) {
            this.b1 = b1;
            this.b2 = b2;	            
        }
        
        public Pair(Node23<E> b1) {
            this.b1 = b1;
            this.b2 = null;               
        }
	}

    /**
     * Returns the index of the given element at the specified node.
     * @param node The starting node
     * @param element The element to find
     * @return The index of the given element at the specified node, -1 if not there
     */
    int find(final Node23<E> node, final E element) {
        if (node.isLeaf()) {
            return Objects.equals(node.leafValue(),element) ? 0: -1;
        }
        {
            int i = find(b1(node), element);
            if (i >= 0) {
                return i;
            }
        }
        {
            int i = find(b2(node), element);
            if (i >= 0) {
                return i + b1(node).size();
            }
        }
        if (b3(node) != null) {
            int i = find(b3(node), element);
            if (i >= 0) {
                return i + b1(node).size() + b2(node).size();
            }
        }
        return -1;
    }

	/**
	 * Returns the element at the given location starting at a node.
	 * It is assumed that "index" is already <= node.size.
	 * @param node The starting node
	 * @param index The index
	 * @return The leaf element at the index.
	 */
	E get(final Node23<E> node, final int index) {
        assert index < node.size();
        return node.isLeaf() ? node.leafValue():
		       index < b1(node).size() ? get(b1(node), index):
		       b3(node) == null || index - b1(node).size() < b2(node).size() ? get(b2(node), index - b1(node).size()):
		       get(b3(node), index - b1(node).size() - b2(node).size());
	}

	/**
	 * Creates a 1-branch.   Returns null if branch is null.
	 * @param b0 The singleton branch
	 * @return A 1-branch.
	 */
	Node23<E> makeBranch(final Node23<E> b0) {
		return b0 == null ? null : new Branch1<>(b0);
	}

	/**
	 * Creates a 2-branch.   Returns a 1-branch if one of the branches are null,
	 *    and null if both branches are null.
	 * @param b0 The left branch
	 * @param b1 The right branch
	 * @return A 2-branch.
	 */
	Node23<E> makeBranch(final Node23<E> b0, final Node23<E> b1) {
	    return b0 == null ? makeBranch(b1):
	           b1 == null ? makeBranch(b0):
	           reversed ? new Branch2<>(b1, b0): new Branch2<>(b0, b1);
	}

	/**
	 * Creates a 3-branch.   Returns a 2-branch if one of the branches are null,
	 *    and a 1-branch if two branched are null, and null, if all 3 are null.
	 * @param b0 The left branch
	 * @param b1 The middle branch
	 * @param b2 The right branch
	 * @return A 3-branch.
	 */
	Node23<E> makeBranch(final Node23<E> b0, final Node23<E> b1, final Node23<E> b2) {
	    return b0 == null ? makeBranch(b1, b2):
	           b1 == null ? makeBranch(b0, b2):
	           b2 == null ? makeBranch(b0, b1):
	           reversed ? new Branch3<>(b2, b1, b0): new Branch3<>(b0, b1, b2);
	}

    /**
     * Combines 3 branches into a Pair with one branch.
     * At least 2 branches must be non-null.
     * @param b0 branch 1
     * @param b1 branch 2
     * @param b2 branch 3
     * @return A Pair with one branch.
     */
    Pair<E> combine(Node23<E> b0, Node23<E> b1,  Node23<E> b2) {
        return new Pair<E>(makeBranch(b0,b1,b2));
    }
   
	/**
	 * Combines 4 branches into a Pair with one or two branches.
	 * At least 2 branches must be non-null.
	 * @param b0 branch 1
	 * @param b1 branch 2
	 * @param b2 branch 3
	 * @param b3 branch 4
     * @return A branch of 1 or 2 branches. (never 3)
	 */
    Pair<E> combine(Node23<E> b0, Node23<E> b1,  Node23<E> b2,  Node23<E> b3) {
        return b3 == null ? combine(b0,b1,b2):
               b2 == null ? combine(b0,b1,b3):
               b1 == null ? combine(b0,b2,b3):
               b0 == null ? combine(b1,b2,b3):
               new Pair<>(makeBranch(b0,b1), makeBranch(b2, b3));
    }
 
	/**
	 * Returns the concatenation of lhs and rhs
	 * @param lhs left hand side
	 * @param rhs right hand side
	 * @return the concatenation of lhs and rhs
	 */
	Node23<E> concat(final Node23<E> lhs, final Node23<E> rhs) {
	    return rhs == null ? lhs:
	           lhs == null ? rhs:
	           collapse(concat(lhs, rhs, getDepth(lhs) - getDepth(rhs)));
	}

    Node23<E> collapse(Pair<E> node) {
        return node.b2 == null ? node.b1: makeBranch(node.b1, node.b2);
    }

	/**
	 * Creates a 2-level branch representing the concatenation
	 *   of 2 nodes.
	 * @param lhs The left hand side
	 * @param rhs The right hand side
	 * @param depthDelta The delta between the depths
	 * @return a 2-level branch representing the concatenation
	 */
	Pair<E> concat(final Node23<E> lhs, final Node23<E> rhs, int depthDelta) {
	    if (depthDelta < 0) {
	        Pair<E> new_rhs_b1 = concat(lhs, b1(rhs), depthDelta + 1);
	        return combine(new_rhs_b1.b1, new_rhs_b1.b2, b2(rhs), b3(rhs));
	    } else if (depthDelta > 0) {
	        if (b3(lhs) == null) {
	            Pair<E> new_lhs_b2 = concat(b2(lhs), rhs, depthDelta - 1);
	            return combine(b1(lhs), new_lhs_b2.b1, new_lhs_b2.b2);
	        } else {
	            Pair<E> new_lhs_b3 = concat(b3(lhs), rhs, depthDelta - 1);
	            return combine(b1(lhs), b2(lhs), new_lhs_b3.b1, new_lhs_b3.b2);
	        }
	    } else {
	        return new Pair<>(lhs, rhs);
	    }
	}

	/**
	 * Returns a node where all indexes are < index.
	 * Returns null if there are no such elements.
	 * @param node The node to get a head of
	 * @param index The chopping point
	 * @return a node where all indexes are < index
	 */
	Node23<E> head(final Node23<E> node, final int index) {
        assert index < node.size();
		if (node.isLeaf()) {
			return null;
		}
		final Node23<E> b1 = b1(node);
		final Node23<E> b2 = b2(node);
		final Node23<E> b3 = b3(node);
		return index < b1.size() ? head(b1, index):
		       b3 == null || index - b1.size() < b2.size() ? concat(b1, head(b2, index - b1.size())):
		       concat(makeBranch(b1, b2), head(b3, index - b1.size() - b2.size()));
	}

	/**
	 * Returns depth of the node.
	 * @param node The node to get a depth of.
	 * @return depth of the node.
	 */
	static <E> int getDepth(Node23<E> node) {
		return node.isLeaf() ? 1 : (getDepth(node.b1()) + 1);
	}

	@Override
	public int hashCode() {
		return asList().hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof List23)) {
			return false;
		}
		final List23<?> other = (List23<?>)obj;
		return asList().equals(other.asList());
	}
	
	@Override
	public String toString() {
		return asList().toString();
	}
	
	@Override
	public ListIterator<E> iterator() {
		return asList().listIterator();
	}
    
    public Stream<E> stream() {
        return asList().stream();
    }
    
    boolean isValid() {
        return root == null || isValid(root, getDepth(root));
    }

    static <E> boolean isValid(Node23<E> n, int depth) {
        if (n == null) {
            return false;
        }
        if (n.isLeaf()) {
            return depth == 1;
        }
        if (!isValid(n.b1(), depth - 1)) {
            return false;
        }
        if (!isValid(n.b2(), depth - 1)) {
            return false;
        }
        if (n.b3() != null && !isValid(n.b3(), depth - 1)) {
            return false;
        }
        return true;
    }
}
