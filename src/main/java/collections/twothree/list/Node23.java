package collections.twothree.list;

import java.util.ListIterator;

interface Node23<E> extends Iterable<E> {
	default E leafValue() {
	    throw new UnsupportedOperationException();
	}
	int size();
	default boolean isLeaf() {
	    return numBranches() == 0;
	}
    Node23<E> getBranch(int which);
    int getBranchSize(int which);
	Node23<E> reverse();
	int numBranches();
	int getDepth();
	default ListIterator<E> iterator() {
	    return NodeIterator.atBeginning(this);
	}
}
