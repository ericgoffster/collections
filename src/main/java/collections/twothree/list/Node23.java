package collections.twothree.list;

interface Node23<E> {
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
}
