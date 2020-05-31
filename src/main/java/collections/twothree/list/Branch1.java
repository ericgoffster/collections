package collections.twothree.list;

final class Branch1<E> implements Node23<E> {
	private final int size;
	private final Node23<E> b1;
	public Branch1(Node23<E> b1) {
		super();
		this.size = b1.size();
		this.b1 = b1;
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
		return null;
	}
	@Override
	public Node23<E> b3() {
		return null;
	}
	@Override
	public E leafValue() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Node23<E> r_b1() {
		return b1;
	}
	
	@Override
	public Node23<E> b_last() {
		return b1;
	}

    @Override
    public Node23<E> r_b2() {
        return null;
    }

    @Override
    public Node23<E> r_b3() {
        return null;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
    @Override
    public String toString() {
        return "["+b1+"]";
    }
}
