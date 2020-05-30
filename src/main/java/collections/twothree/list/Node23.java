package collections.twothree.list;

interface Node23<E> {
	E leafValue();
	int size();
	default Node23<E> b1() {
		return null;
	}
	default Node23<E> b2() {
		return null;
	}
	default Node23<E> b3() {
		return null;
	}
	default Node23<E> b_last() {
		return null;
	}
	default Node23<E> r_b1() {
		return null;
	}
	default Node23<E> r_b2() {
		return null;
	}
	default Node23<E> r_b3() {
		return null;
	}
}
