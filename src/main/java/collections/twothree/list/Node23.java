package collections.twothree.list;

interface Node23<E> {
	default E leafValue() {
	    throw new UnsupportedOperationException();
	}
	int size();
	default boolean isLeaf() {
	    return numBranches() == 0;
	}
	default Node23<E> b1() {
        throw new UnsupportedOperationException();
	}
	default int b1Size() {
        throw new UnsupportedOperationException();
    }
	default Node23<E> b2() {
        throw new UnsupportedOperationException();
    }
	default int b2Size() {
        throw new UnsupportedOperationException();
    }
    default Node23<E> b3() {
        throw new UnsupportedOperationException();
    }
	Node23<E> reverse();
	int numBranches();
}
