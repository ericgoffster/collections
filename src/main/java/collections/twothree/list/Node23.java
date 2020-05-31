package collections.twothree.list;

interface Node23<E> {
	E leafValue();
	int size();
	boolean isLeaf();
	Node23<E> b1();
	Node23<E> b2();
	Node23<E> b3();
	Node23<E> b_last();
	Node23<E> r_b1();
	Node23<E> r_b2();
	Node23<E> r_b3();
}
