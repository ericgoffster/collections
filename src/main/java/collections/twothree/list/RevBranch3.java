package collections.twothree.list;

final class RevBranch3<E> implements Node23<E> {
	private final int size;
	private final Node23<E> b1;
	private final Node23<E> b2;
	private final Node23<E> b3;
	RevBranch3(Node23<E> b1, Node23<E> b2, Node23<E> b3) {
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
        return b3.reverse();
	}
    @Override
    public int b1Size() {
        return b3.size();
    }
	@Override
	public Node23<E> b2() {
        return b2.reverse();
	}
    @Override
    public int b2Size() {
        return b2.size();
    }
	@Override
	public Node23<E> b3() {
        return b1.reverse();
	}
	@Override
	public E leafValue() {
		throw new UnsupportedOperationException();
	}
    @Override
    public int numBranches() {
        return 3;
    }
	
	@Override
	public Node23<E> b_last() {
		return b3();
	}

    @Override
    public boolean isLeaf() {
        return false;
    }
    
    @Override
    public String toString() {
        return "["+b1()+" "+b2()+" "+b3()+"]";
    }
    
    @Override
    public Node23<E> reverse() {
        return new Branch3<>(b1, b2, b3);
    }
}
