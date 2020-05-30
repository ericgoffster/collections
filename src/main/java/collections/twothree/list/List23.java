package collections.twothree.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import org.granitesoft.requirement.Requirements;

/**
 * Represents a list as a 23-tree.
 * A 23-tree is a semi-balanced tree, each branch has 2 or 3 nodes.
 * The nodes are ordered such that the leftmost side of the list
 * is in the left-most branch, and the rightmost side of the list is in the right-most branch.
 * 
 * All operations here are at worst O(log n).
 *
 * @param <E> The type of the elements.
 */
final class List23<E> implements Iterable<E> {
	final Node23<E> root;
	final boolean reversed;

	List23(final Node23<E> root, boolean reversed) {
		this.root = root;
		this.reversed = reversed;
	}

	/**
	 * Fast construction of list.
	 * This operation is O(n log n).
     * @param <E> The type of the elements.
	 * @param elements The collections of elements
	 * @return The List23 representation of "elements".
	 */
	@SafeVarargs
    @SuppressWarnings("varargs")
	public static <E> List23<E> of(final E ... elements) {
		return of(Arrays.asList(elements));
	}

	/**
	 * Fast construction of list.
	 * This operation is O(n log n).
     * @param <E> The type of the elements.
	 * @param elements The array of elements
	 * @return The List23 representation of "elements".
	 */
	public static <E> List23<E> of(final Iterable<? extends E> elements) {
		List<Node23<E>> nodes = new ArrayList<>();
		for(E e: elements) {
			nodes.add(new Leaf<>(e));
		}
		if (nodes.isEmpty()) {
			return new List23<>(null, false);
		}
		return quickConstruct(nodes);
	}
	
	/**
	 * Fast construction of sorted list.
	 * Creates a list23 represents of the elements sorted by a comparator.
	 * This operation is O(n log n).
     * @param <E> The type of the elements.
     * @param comparator The element comparator
	 * @param elements The collections of elements
	 * @return The sorted List23 representation of "elements".
	 */
	public static <E> List23<E> ofSorted(final Comparator<E> comparator, final Iterable<? extends E> elements) {
		List<Node23<E>> nodes = new ArrayList<>();
		for(E e: elements) {
			nodes.add(new Leaf<>(e));
		}
		if (nodes.isEmpty()) {
			return new List23<>(null, false);
		}
		Collections.sort(nodes, (i,j) -> comparator.compare(i.leafValue(),j.leafValue()));
		return quickConstruct(nodes);
	}

	/**
	 * Fast construction of sorted list.
	 * Creates a list23 represents of the elements sorted by a comparator.
	 * This operation is O(n log n).
     * @param <E> The type of the elements.
     * @param comparator The element comparator
	 * @param elements The array of elements
	 * @return The sorted List23 representation of "elements".
	 */
	@SafeVarargs
    @SuppressWarnings("varargs")
	public static <E> List23<E> ofSorted(final Comparator<E> comparator, final E ... elements) {
		return ofSorted(comparator, Arrays.asList(elements));
	}

	/**
	 * Fast construction of sorted list.
	 * Creates a list23 represents of the elements sorted by their natural ordering.
	 * This operation is O(n log n).
     * @param <E> The type of the elements.
	 * @param elements The collection of elements
	 * @return The sorted List23 representation of "elements".
	 */
	public static <E extends Comparable<E>> List23<E> ofSorted(final Iterable<? extends E> elements) {
		return ofSorted(List23::naturalCompare, elements);
	}

	/**
	 * Fast construction of sorted list.
	 * Creates a list23 represents of the elements sorted by their natural ordering.
	 * This operation is O(n log n).
     * @param <E> The type of the elements.
	 * @param elements The array of elements
	 * @return The sorted List23 representation of "elements".
	 */
	@SafeVarargs
    @SuppressWarnings("varargs")
	public static <E extends Comparable<E>> List23<E> ofSorted(final E ... elements) {
		return ofSorted(Arrays.asList(elements));
	}

	/**
	 * Returns a classic "read only java List" view of the list.
	 * This operation is O(1).
	 * @return A classic "read only java List" view of the list.
	 */
	public List<E> asList() {
		return new List23List<>(this);
	}
	
	/**
	 * Returns the number of elements in the list.
	 * This operation is O(1).
	 * @return The number of elements in the list
	 */
	public int size() {
		return root == null ? 0 : root.size();
	}
	
	/**
	 * Returns the element at the given index.
	 * This operation is O(log n).
	 * @param index The index to get.
	 * @return the element at the given index
	 * @throws IndexOutOfBoundsException, if out of bounds
	 */
	public E get(final int index) {
		if (root == null || index < 0 || index >= root.size()) {
			throw new IndexOutOfBoundsException(outofBounds(index));
		}
		return get(root, index);
	}
	
	/**
	 * Returns a list with the given element added to the end.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is O(log n).
	 * @param element The element to add.
	 * @return A list with the given element added to the end.
	 */
	public List23<E> add(final E element) {
		return insert(size(), element);
	}

	/**
	 * Returns a list with the given element set at the specified index.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is O(log n).
	 * @param element The element to set.
	 * @return A list with the given element set at the specified index.
	 */
	public List23<E> set(final int index, final E element) {
		return remove(index).insert(index, element);
	}
	
	/**
	 * Returns a list with the given element inserted at a specified position.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is O(log n).
	 * @param element The element to insert.
	 * @return A list with the given element set at the specified index.
	 */
	public List23<E> insert(final int index, final E element) {
		if (root == null) {
			if (index != 0) {
				throw new IndexOutOfBoundsException(outofBounds(index));
			}
			return new List23<>(new Leaf<>(element), reversed);
		}
		if (index < 0 || index > root.size()) {
			throw new IndexOutOfBoundsException(outofBounds(index));
		}
		final Node23<E> split = insert(root, index, element);
		if (b2(split) == null) {
			return new List23<>(b1(split), reversed);
		} else {
			return new List23<>(split, reversed);
		}
	}
	
	/**
	 * Returns a list with the given index removed.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is O(log n).
	 * @param index The index to remove.
	 * @return A list with the given index removed.
	 */
	public List23<E> remove(final int index) {
		if (root == null || index < 0 || index >= root.size()) {
			throw new IndexOutOfBoundsException(outofBounds(index));
		}
		final Node23<E> newRoot = remove(root, index);
		if (newRoot == null) {
			return new List23<>(null, reversed);
		}
		// if we are left with a 1-branch, then we shorten the height
		if (b2(newRoot) == null) {
			return new List23<>(b1(newRoot), reversed);
		}
		return new List23<>(newRoot, reversed);
	}
	
	/**
	 * Returns a list with the given list appended to the end.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is max(O(log m),O(log n)).
	 * @param other The list to append.
	 * @return A list with the given list appended to the end.
	 */
	public List23<E> append(final List23<E> other) {
		Requirements.require(other, Requirements.notNull(), () -> "other");
		if (other.root == null) {
			return this;
		}
		if (root == null) {
			return other;
		}
		// newRoot may be taller than the original,
		// so here we are not really shortening, but leaving it the same height.
		final Node23<E> newRoot = concat(root, other.root);
		if (b2(newRoot) == null) {
			return new List23<>(b1(newRoot), reversed);
		} else {
			return new List23<>(newRoot, reversed);
		}
	}
	
	/**
	 * Returns a list with all indexes >= the specified index.
	 * This operation is O(log n).
	 * @param index The chopping point (inclusive)
	 * @return a list with all indexes >= the specified index.
	 */
	public List23<E> tail(final int index) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException(outofBounds(index));
		}
		if (index == 0) {
			return this;
		}
		return new List23<>(tail(root, index - 1), reversed);
	}
	
	/**
	 * Returns a list with all indexes < the specified index.
	 * This operation is O(log n).
	 * @param index The chopping point (exclusive)
	 * @return a list with all indexes < the specified index.
	 */
	public List23<E> head(final int index) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException(outofBounds(index));
		}
		if (index == size()) {
			return this;
		}
		return new List23<>(head(root, index), reversed);
	}
	
	/**
	 * Returns a list with all indexes >= from and < to
	 * This operation is O(log n).
	 * @param from The starting point, inclusive.
	 * @param to The end point, exclusive.
	 * @return a list with all indexes >= from and < to
	 */
	public List23<E> subList(final int from, final int to) {
		if (from < 0 || from > size()) {
			throw new IndexOutOfBoundsException(outofBounds(from));
		}
		if (to < 0 || to > size()) {
			throw new IndexOutOfBoundsException(outofBounds(to));
		}
		if (from > to) {
			throw new IndexOutOfBoundsException("from must not be greater than to");
		}
		return tail(from).head(to - from);
	}
	
	/**
	 * Returns a list that is the original list reversed.
	 * THIS OPERATION IS IMMUTABLE.  The original list is left unchanged.
	 * This operation is O(1).
	 * @return a list that is the original list reversed.
	 */
	public List23<E> reverse() {
		return new List23<E>(root, !reversed);
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
		Requirements.require(nodes, Requirements.notNull(), () -> "nodes");
		// Just one left?  It is the root
		if (nodes.size() == 1) {
			return new List23<E>(nodes.get(0), false);
		}
		
		// Group all of the nodes into 3 branches and 2 branches.
		List<Node23<E>> newNodes = new ArrayList<>();
		int i = 0;
		while(nodes.size() - i >= 5) {
			newNodes.add(new Branch3<E>(nodes.get(i), nodes.get(i + 1), nodes.get(i + 2)));
			i += 3;
		}
		switch(nodes.size() - i) {
		case 2:
			newNodes.add(new Branch2<E>(nodes.get(i), nodes.get(i + 1)));
			break;
		case 3:
			newNodes.add(new Branch3<E>(nodes.get(i), nodes.get(i + 1), nodes.get(i + 2)));
			break;
		case 4:
			newNodes.add(new Branch2<E>(nodes.get(i), nodes.get(i + 1)));
			newNodes.add(new Branch2<E>(nodes.get(i + 2), nodes.get(i + 3)));
			break;
		default:
			throw new IllegalStateException();
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
	 * Returns the right most branch.
	 * Will be null if a leaf.
	 * @param node The node
	 * @return the right most branch.
	 */
	Node23<E> b_last(final Node23<E> node) {
		return reversed ? node.b1() : node.b_last();
	}

	/**
	 * Returns the element at the given location starting at a node.
	 * It is assumed that "index" is already <= node.size.
	 * @param node The starting node
	 * @param index The index
	 * @return The leaf element at the index.
	 */
	E get(final Node23<E> node, final int index) {
		Requirements.require(node, Requirements.notNull(), () -> "node");
		Requirements.require(index, Requirements.le(node.size()), () -> "index");
		if (isLeaf(node)) {
			return node.leafValue();
		}
		if (index < b1(node).size()) {
			return get(b1(node), index);
		} 
		final int b2Index = index - b1(node).size();
		if (b3(node) == null || b2Index < b2(node).size()) {
			return get(b2(node), b2Index);
		}
		return get(b3(node), b2Index - b2(node).size());
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
	 * Creates a 2-branch.   Returns a 1-branch if one of the branches was null,
	 *    and null if both branches are null.
	 * @param b0 The left branch
	 * @param b1 The right branch
	 * @return A 2-branch.
	 */
	Node23<E> makeBranch(final Node23<E> b0, final Node23<E> b1) {
		return b0 == null ? makeBranch(b1):
					b1 == null ? makeBranch(b0):
						reversed ? new Branch2<>(b1, b0) : new Branch2<>(b0, b1);
	}

	/**
	 * Creates a 3-branch.   Returns a 2-branch if one of the branches was null,
	 *    and a 1-branch if one branch was null, and null, if all 3 are null.
	 * @param b0 The left branch
	 * @param b2 The middle branch
	 * @param b1 The right branch
	 * @return A 3-branch.
	 */
	Node23<E> makeBranch(final Node23<E> b0, final Node23<E> b1, final Node23<E> b2) {
		return b0 == null ? makeBranch(b1, b2):
               b1 == null ? makeBranch(b0, b2):
               b2 == null ? makeBranch(b0, b1):
            	   reversed ? new Branch3<>(b2, b1, b0):
            		   new Branch3<>(b0, b1, b2);
	}

	/**
	 * Removes branch 1 from a node.
	 * @param node The node to remove from.
	 * @return A node with b1 removed
	 */
	Node23<E> remove1(final Node23<E> node) {
		return makeBranch(b2(node), b3(node)); 
	}

	/**
	 * Removes branch 2 from a node.
	 * @param node The node to remove from.
	 * @return A node with b2 removed
	 */
	Node23<E> remove2(final Node23<E> node) {
		return makeBranch(b1(node), b3(node)); 
	}

	/**
	 * Removes branch 3 from a node.
	 * @param node The node to remove from.
	 * @return A node with b3 removed
	 */
	Node23<E> remove3(final Node23<E> node) {
		return makeBranch(b1(node), b2(node)); 
	}

	/**
	 * Returns a 3-branch with a branch prepended.
	 * @param node The node to operate on
	 * @return A 3-branch with a branch prepended
	 */
	Node23<E> prepend(final Node23<E> node, final Node23<E> b0) {
		return makeBranch(b0, b1(node), b2(node)); 
	}

	/**
	 * Returns a 3-branch with a branch appended.
	 * @param node The node to operate on
	 * @return A 3-branch with a branch appended
	 */
	Node23<E> append(final Node23<E> node, final Node23<E> b0) {
		return makeBranch(b1(node), b2(node), b0);
	}

	/**
	 * Combines 2-4 nodes into a 2 level branch structure.
	 * @param a branch 1
	 * @param b branch 2
	 * @param c branch 3
	 * @param d branch 4
	 * @return A 2 level branch.
	 */
	Node23<E> combine(Node23<E> a, Node23<E> b,  Node23<E> c,  Node23<E> d) {
		if (c == null) {
			c = d;
			d = null;
		}
		if (b == null) {
			Requirements.require(c, Requirements.notNull(), () -> "c");
			b = c;
			c = d;
			d = null;
		}
		if (a == null) {
			a = b;
			b = c;
			c = d;
			d = null;
		}
		if (c == null) {
			return makeBranch(makeBranch(a,b));
		}
		if (d == null) {
			return makeBranch(makeBranch(a,b,c));
		}
		return makeBranch(makeBranch(a,b), makeBranch(c, d));
	}

	/**
	 * Creates a 2-level branch representing the concatenation
	 *   of 2 nodes.
	 * @param lhs The left hand side
	 * @param rhs The right hand side
	 * @return a 2-level branch representing the concatenation
	 */
	Node23<E> concat(final Node23<E> lhs, final Node23<E> rhs) {
		if (rhs == null) {
			return makeBranch(lhs);
		}
		if (lhs == null) {
			return makeBranch(rhs);
		}
		return concat(lhs, rhs, getDepth(lhs) - getDepth(rhs));
	}

	/**
	 * Creates a 2-level branch representing the concatenation
	 *   of 2 nodes.
	 * @param lhs The left hand side
	 * @param rhs The right hand side
	 * @param depthDelta The delta between the depths
	 * @return a 2-level branch representing the concatenation
	 */
	Node23<E> concat(final Node23<E> lhs, final Node23<E> rhs, int depthDelta) {
		if (depthDelta < 0) {
			Node23<E> new_rhs_b1 = concat(lhs, b1(rhs), depthDelta + 1);
			return combine(b1(new_rhs_b1), b2(new_rhs_b1), b2(rhs), b3(rhs));
		} else if (depthDelta > 0) {
			if (b3(lhs) == null) {
				Node23<E> new_lhs_b2 = concat(b2(lhs), rhs, depthDelta - 1);
				return combine(b1(lhs), b1(new_lhs_b2), b2(new_lhs_b2), null);
			} else {
				Node23<E> new_lhs_b3 = concat(b3(lhs), rhs, depthDelta - 1);
				return combine(b1(lhs), b2(lhs), b1(new_lhs_b3), b2(new_lhs_b3));
			}
		} else {
			return makeBranch(lhs, rhs);
		}
	}

	Node23<E> head(final Node23<E> node, final int index) {
		if (isLeaf(node)) {
			return null;
		}
		final Node23<E> b1 = b1(node);
		final Node23<E> b2 = b2(node);
		final Node23<E> b3 = b3(node);
		if (index < b1.size()) {
			return head(b1, index);
		}
		final int b2Index = index - b1.size();
		if (b3 == null || b2Index < b2.size()) {
			final Node23<E> new_b2 = head(b2, b2Index);
			final Node23<E> concat = concat(b1, new_b2);
			return b2(concat) == null ? b1(concat) : concat;
		}
		final Node23<E> new_b3 = head(b3, b2Index - b2.size());
		final Node23<E> concat = concat(makeBranch(b1, b2), new_b3);
		return b2(concat) == null ? b1(concat) : concat;
	}

	Node23<E> tail(final Node23<E> node, final int index) {
		if (isLeaf(node)) {
			return null;
		}
		final Node23<E> b1 = b1(node);
		final Node23<E> b2 = b2(node);
		final Node23<E> b3 = b3(node);
		if (index < b1.size()) {
			final Node23<E> new_b1 = tail(b1, index);
			if (b3 == null) {
				final Node23<E> concat = concat(new_b1, b2);
				return b2(concat) == null ? b1(concat) : concat;
			} else {
				final Node23<E> concat = concat(new_b1, makeBranch(b2, b3));
				return b2(concat) == null ? b1(concat) : concat;
			}
		}
		final int b2Index = index - b1.size();
		if (b3 == null) {
			return tail(b2, b2Index);
		}
		if (b2Index < b2.size()) {
			final Node23<E> new_b2 = tail(b2, b2Index);
			final Node23<E> concat = concat(new_b2, b3);
			return b2(concat) == null ? b1(concat) : concat;
		}
		return tail(b3, b2Index - b2.size());
	}

	int getDepth(Node23<E> lhs) {
		if (isLeaf(lhs)) {
			return 1;
		}
		return getDepth(b1(lhs)) + 1;
	}

	private Node23<E> remove(final Node23<E> node, final int index) {
		if (isLeaf(node)) {
			return null;
		}
		final Node23<E> b1 = b1(node);
		final Node23<E> b2 = b2(node);
		final Node23<E> b3 = b3(node);
		if (index < b1.size()) {
			final Node23<E> new_b1 = remove(b1, index);
			return new_b1 == null || b2(new_b1) != null ?
					makeBranch(new_b1, b2, b3):
						b3(b2) == null ?
								makeBranch(prepend(b2, b1(new_b1)), b3):
									makeBranch(
											append(new_b1,b1(b2)),
											remove1(b2), b3);
		}
		final int b2Index = index - b1.size();
		if (b3 == null || b2Index < b2.size()) {
			Node23<E> new_b2 = remove(b2, b2Index);
			return new_b2 == null || b2(new_b2) != null ?
					makeBranch(b1, new_b2, b3) :
						b3(b1) == null ?
								makeBranch(append(b1, b1(new_b2)), b3):
									makeBranch(
											remove3(b1),
											prepend(new_b2, b3(b1)),
											b3);
		}
		{
			final Node23<E> new_b3 = remove(b3, b2Index - b2.size());
			return new_b3 == null || b2(new_b3) != null ?
					makeBranch(b1, b2, new_b3):
						b3(b2) == null ?
								makeBranch(b1, append(b2, b1(new_b3))):
									makeBranch(
											b1,
											remove3(b2),
											prepend(new_b3, b3(b2)));
		}
	}

	private Node23<E> insert(final Node23<E> node, final int index, final E element) {
		if (isLeaf(node)) {
			return index == 0 ?
					makeBranch(new Leaf<>(element), node): 
						makeBranch(node, new Leaf<>(element));
		}
		final Node23<E> b1 = b1(node);
		final Node23<E> b2 = b2(node);
		final Node23<E> b3 = b3(node);
		if (index <= b1.size()) {
			Node23<E> split = insert(b1, index, element);
			return b3 == null ? makeBranch(append(split, b2), null):
				b2(split) == null ?
						makeBranch(makeBranch(b1(split), b2, b3), null):
							makeBranch(split, remove1(node));
		}
		final int b2Index = index - b1.size();
		if (b3 == null || b2Index <= b2.size()) {
			Node23<E> split = insert(b2, b2Index, element);
			return b3 == null ? makeBranch(prepend(split, b1), null):
				b2(split) == null ?
						makeBranch(makeBranch(b1, b1(split), b3), null):
							makeBranch(prepend(remove2(split), b1), append(remove1(split), b3));
		}
		{
			Node23<E> split = insert(b3, b2Index - b2.size(), element);
			return b2(split) == null ?
					makeBranch(makeBranch(b1, b2, b1(split)), null):
						makeBranch(remove3(node), split);
		}
	}

	private String outofBounds(final int index) {
	    return "Index: "+index+", Size: "+size();
	}

	boolean isLeaf(final Node23<E> node) {
		return b1(node) == null;
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
}
