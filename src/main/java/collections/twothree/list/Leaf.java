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
}
