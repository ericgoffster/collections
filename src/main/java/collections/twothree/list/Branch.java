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
    public int indexOf(Function<? super E, Integer> comparator) {
        int pos = 0;
        int j = 0;
        while(j < nodes.length - 1 && comparator.apply(nodes[j].last()) > 0) {
            pos += nodes[j++].size();
        }
        int i = nodes[j].indexOf(comparator);
        return i < 0 ? -1 : i + pos;
    }
    
    @Override
    public <T> T binarySearch(Function<? super E, Integer> comparator,
            BiFunction<E, Integer, T> leafVisitor) {
        int pos = 0;
        int j = 0;
        while(j < nodes.length - 1 && comparator.apply(nodes[j].last()) > 0) {
            pos += nodes[j++].size();
        }
        final int p = pos;
        return nodes[j].binarySearch(comparator, (e, i) -> leafVisitor.apply(e, i + p));
    }
    
    @Override
    public <T> T reverseBinarySearch(Function<? super E, Integer> comparator,
            BiFunction<E, Integer, T> leafVisitor) {
        int pos = 0;
        int j = nodes.length - 1;
        while(j > 0 && comparator.apply(nodes[j].first()) > 0) {
            pos += nodes[j--].size();
        }
        final int p = pos;
        return nodes[j].reverseBinarySearch(comparator, (e, i) -> leafVisitor.apply(e, i + p));
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
        final int b1Index = index - nodes[0].size();
        if (b1Index < 0) {
            final Node23<E> lhs = nodes[0].tail(index);
            final Node23<E> rhs;
            if (nodes.length == 3) {
                rhs = new Branch<>(nodes[1], nodes[2]);
            } else {
                rhs = nodes[1];
            }
            return lhs == null ? rhs : List23.concat(lhs, rhs);
        }
        if (nodes.length == 2) {
            return nodes[1].tail(b1Index);
        }
        final int b2Index = b1Index - nodes[1].size();
        if (b2Index < 0) {
            final Node23<E> lhs = nodes[1].tail(b1Index);
            final Node23<E> rhs = nodes[2];
            return lhs == null ? rhs : List23.concat(lhs, rhs);
        } 
        return nodes[2].tail(b2Index);
    }
    
    @Override
    public Node23<E> head(int index) {
        final int b1Index = index - nodes[0].size();
        if (b1Index < 0) {
            return nodes[0].head(index);
        }
        final Node23<E> lhs;
        final Node23<E> rhs;
        final int b2Index = b1Index - nodes[1].size();
        if (b2Index < 0 || nodes.length == 2) {
            lhs = nodes[0];
            rhs = nodes[1].head(b1Index);
        } else {
            lhs = new Branch<>(nodes[0], nodes[1]);
            rhs = nodes[2].head(b2Index);
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
    @Override
    public int hashCode() {
        return hc();
    }
    @Override
    public boolean equals(Object obj) {
        return eq(obj);
    }
}
