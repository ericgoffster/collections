package collections.twothree.list;

import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        return nodes[j].get(index - pos);
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
    public E last() {
        return nodes[nodes.length - 1].last();
    }
    
    @Override
    public E first() {
        return nodes[0].first();
    }
    
    @Override
    public <T> T binarySearch(Function<? super E, Integer> comparator, int index,
            BiFunction<E, Integer, T> leafVisitor) {
        int pos = 0;
        int j = 0;
        while(j < nodes.length - 1 && comparator.apply(nodes[j].last()) > 0) {
            pos += nodes[j++].size();
        }
        return nodes[j].binarySearch(comparator, index + pos, leafVisitor);
    }
    
    @Override
    public <T> T reverseBinarySearch(Function<? super E, Integer> comparator, int index,
            BiFunction<E, Integer, T> leafVisitor) {
        int pos = 0;
        int j = nodes.length - 1;
        while(j > 0 && comparator.apply(nodes[j].first()) > 0) {
            pos += nodes[j--].size();
        }
        return nodes[j].reverseBinarySearch(comparator, index + pos, leafVisitor);
    }
    
    @Override
    public Node23<E> reverse() {
        return new ReversedNode23<>(this);
    }
    
    @Override
    public boolean isLeaf() {
        return false;
    }
    
    @Override
    public E leafValue() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <F> Node23<F> map(Function<E, F> f) {
        return new MappedNode23<E, F>(this, f);
    }
    
    @Override
    public Node23<E> tail(int index) {
        final int b1Index = index - getBranchSize(0);
        if (b1Index < 0) {
            final Node23<E> lhs = getBranch(0).tail(index);
            final Node23<E> rhs;
            if (numBranches() == 3) {
                rhs = new Branch<>(getBranch(1), getBranch(2));
            } else {
                rhs = getBranch(1);
            }
            return lhs == null ? rhs : List23.concat(lhs, rhs);
        }
        if (numBranches() == 2) {
            return getBranch(1).tail(b1Index);
        }
        final int b2Index = b1Index - getBranchSize(1);
        if (b2Index < 0) {
            final Node23<E> lhs = getBranch(1).tail(b1Index);
            final Node23<E> rhs = getBranch(2);
            return lhs == null ? rhs : List23.concat(lhs, rhs);
        } 
        return getBranch(2).tail(b2Index);
    }
    
    @Override
    public Node23<E> head(int index) {
        final int b1Index = index - getBranchSize(0);
        if (b1Index < 0) {
            return getBranch(0).head(index);
        }
        final Node23<E> lhs;
        final Node23<E> rhs;
        final int b2Index = b1Index - getBranchSize(1);
        if (b2Index < 0) {
            lhs = getBranch(0);
            rhs = getBranch(1).head(b1Index);
        } else {
            lhs = new Branch<>(getBranch(0), getBranch(1));
            rhs = getBranch(2).head(b2Index);
        }
        return rhs == null ? lhs : List23.concat(lhs, rhs);
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), 0);
    }
    @Override
    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
