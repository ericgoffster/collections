package collections.twothree.list;

final class Branch1<E> implements Node23<E> {
	private final int size;
	private final Node23<E> b1;
	Branch1(Node23<E> b1) {
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
	public int b1Size() {
	    return b1.size();
	}
	@Override
	public Node23<E> b2() {
		return null;
	}
	@Override
	public int b2Size() {
	    return 0;
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
	public int numBranches() {
	    return 1;
	}
	
	@Override
	public Node23<E> b_last() {
		return b1();
	}

    @Override
    public boolean isLeaf() {
        return false;
    }
    @Override
    public String toString() {
        return "["+b1()+"]";
    }
    @Override
    public Node23<E> reverse() {
        return new RevBranch1<E>(b1);
    }
}
