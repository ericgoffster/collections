package collections.twothree.list;

final class Branch2<E> implements Node23<E> {
	private final int size;
	private final Node23<E> b1;
	private final Node23<E> b2;
	Branch2(Node23<E> b1, Node23<E> b2) {
		super();
		this.size = b1.size() + b2.size();		
		this.b1 = b1;
		this.b2 = b2;
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
        return b2;
	}
    @Override
    public int b2Size() {
        return b2.size();
    }
	
    @Override
    public int numBranches() {
        return 2;
    }
    
    @Override
    public String toString() {
        return "["+b1()+" "+b2()+"]";
    }
    @Override
    public Node23<E> reverse() {
        return new Node23<E>() {

            @Override
            public int size() {
                return size;
            }

            @Override
            public Node23<E> b1() {
                return b2.reverse();
            }

            @Override
            public int b1Size() {
                return b2.size();
            }

            @Override
            public Node23<E> b2() {
                return b1.reverse();
            }

            @Override
            public int b2Size() {
                return b1.size();
            }

            @Override
            public Node23<E> reverse() {
                return Branch2.this;
            }

            @Override
            public int numBranches() {
                return 2;
            }
            @Override
            public String toString() {
                return "["+b1()+" "+b2()+"]";
            }
        };
    }
}
