package collections.twothree.list;

interface Node23<E> {
	E leafValue();
	int size();
	default boolean isLeaf() {
	    return numBranches() == 0;
	}
	Node23<E> b1();
	int b1Size();
	Node23<E> b2();
    int b2Size();
	Node23<E> b3();
	Node23<E> b_last();
	Node23<E> reverse();
	int numBranches();
}
