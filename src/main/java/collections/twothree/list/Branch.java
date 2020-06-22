package collections.twothree.list;

import java.util.ListIterator;

final class Branch<E> implements Node23<E> {
	private final int size;
	private final Node23<E>[] nodes;
	Branch(Node23<E> b0, Node23<E> b1) {
		super();
		this.size = b0.size() + b1.size();	
		nodes = new Node23[] {b0, b1};
	}
    Branch(Node23<E> b0, Node23<E> b1, Node23<E> b2) {
        super();
        this.size = b0.size() + b1.size() + b2.size();  
        nodes = new Node23[] {b0, b1, b2};
    }
    
    @Override
    public int getDepth() {
        return nodes[0].getDepth() + 1;
    }
		
	@Override
	public int size() {
		return size;
	}

    @Override
    public Node23<E> getBranch(int which) {
        return nodes[which];
    }
    @Override
    public int getBranchSize(int which) {
        return nodes[which].size();
    }
	
    @Override
    public int numBranches() {
        return nodes.length;
    }
    
    @Override
    public String toString() {
        StringBuilder sb =  new StringBuilder("[");
        String delim = "";
        for(int i = 0; i < nodes.length; i++) {
            Node23<E> n = nodes[i];
            sb.append(delim).append(n.toString());
            delim = " ";
        }
        return sb.append("]").toString();
    }

    @Override
    public ListIterator<E> iterator() {
        return NodeIterator.atBeginning(this);
    }

    @Override
    public E get(final int index) {
        assert index < size;
        int pos = 0;
        int j = 0;
        while(j < nodes.length - 1 && index >= pos + nodes[j].size()) {
            pos += nodes[j++].size();
        }
        return getBranch(j).get(index - pos);
    }
    
    @Override
    public boolean isValid(final int depth) {
        for(int i = 0; i < nodes.length; i++) {
            if (!nodes[i].isValid(depth - 1)) {
                return false;
            }
        }
        return true;
    }
    

    @Override
    public Node23<E> reverse() {
        return new Node23<E>() {

            @Override
            public int size() {
                return size;
            }
            
            @Override
            public int getDepth() {
                return nodes[0].getDepth() + 1;
            }
                
            @Override
            public Node23<E> getBranch(int which) {
                return nodes[nodes.length - 1 - which].reverse();
            }
            
            @Override
            public int getBranchSize(int which) {
                return nodes[nodes.length - 1 - which].size();
            }

            @Override
            public Node23<E> reverse() {
                return Branch.this;
            }

            @Override
            public int numBranches() {
                return nodes.length;
            }
            @Override
            public String toString() {
                StringBuilder sb =  new StringBuilder("[");
                String delim = "";
                for(int i = nodes.length - 1; i >= 0; i--) {
                    Node23<E> n = nodes[i];
                    sb.append(delim).append(n.reverse().toString());
                    delim = " ";
                }
                return sb.append("]").toString();
            }

            @Override
            public ListIterator<E> iterator() {
                return NodeIterator.atBeginning(this);
            }
            
            @Override
            public E get(int index) {
                return Branch.this.get(size - index - 1);
            }
            
            @Override
            public boolean isValid(int depth) {
                return Branch.this.isValid(depth);
            }
        };
    }
}
