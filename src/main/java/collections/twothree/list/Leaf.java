package collections.twothree.list;

final class Leaf<E> implements Node23<E> {
	private final E element;
	public Leaf(E leaf) {
		super();
		this.element = leaf;
	}
	@Override
	public int numBranches() {
	    return 0;
	}
	@Override
	public int getDepth() {
	    return 1;
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
    public String toString() {
        return String.valueOf(element);
    }
    
    @Override
    public Node23<E> reverse() {
        return this;
    }

    @Override
    public Node23<E> getBranch(int which) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBranchSize(int which) {
        throw new UnsupportedOperationException();
    }

    public E get(final int index) {
        assert index < 1;
        return element;
    }
    
    @Override
    public boolean isValid(int depth) {
        return depth == 1;
    }
}
