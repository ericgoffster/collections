package collections.twothree.list;

final class Leaf<E> implements Node23<E> {
	private final E element;
	public Leaf(E leaf) {
		super();
		this.element = leaf;
	}
	@Override
	public boolean isLeaf() {
	    return true;
	}
	@Override
	public int numBranches() {
	    return 0;
	}
	@Override
	public int size() {
		return 1;
	}
	@Override
	public E leafValue() {
		return element;
	}
    @Override
    public Node23<E> b1() {
        throw new UnsupportedOperationException();
    }
    @Override
    public int b1Size() {
        return 0;
    }
    @Override
    public int b2Size() {
        return 0;
    }
    @Override
    public Node23<E> b2() {
        throw new UnsupportedOperationException();
    }
    @Override
    public Node23<E> b3() {
        throw new UnsupportedOperationException();
    }
    @Override
    public Node23<E> b_last() {
        throw new UnsupportedOperationException();
    }
    @Override
    public String toString() {
        return String.valueOf(element);
    }
    
    @Override
    public Node23<E> reverse() {
        return this;
    }
}
