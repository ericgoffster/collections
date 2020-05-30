package collections.twothree.list;

final class Branch3<E> implements Node23<E> {
	private final int size;
	private final Node23<E> b1;
	private final Node23<E> b2;
	private final Node23<E> b3;
	public Branch3(Node23<E> b1, Node23<E> b2, Node23<E> b3) {
		super();
		this.size = b1.size() + b2.size() + b3.size();			
		this.b1 = b1;
		this.b2 = b2;
		this.b3 = b3;
	}
		
	@Override
	public int size() {
		return size;
	}
	@Override
	public Node23<E> b1() {
		return b1;
	}
	@Override
	public Node23<E> b2() {
		return b2;
	}
	@Override
	public Node23<E> b3() {
		return b3;
	}
	@Override
	public E leafValue() {
		throw new UnsupportedOperationException();
	}
	@Override
	public Node23<E> r_b1() {
		return b3;
	}
	@Override
	public Node23<E> r_b2() {
		return b2;
	}
	@Override
	public Node23<E> r_b3() {
		return b1;
	}
	
	@Override
	public Node23<E> b_last() {
		return b3;
	}
}
