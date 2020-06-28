package collections.twothree.list;

import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class ReversedNode23<E> implements Node23<E> {
    final Node23<E> other;

    public ReversedNode23(Node23<E> other) {
        super();
        this.other = other;
    }

    @Override
    public int size() {
        return other.size();
    }

    @Override
    public int getDepth() {
        return other.getDepth();
    }

    @Override
    public Node23<E> getBranch(int which) {
        return other.getBranch(numBranches() - which - 1).reverse();
    }

    @Override
    public int getBranchSize(int which) {
        return other.getBranchSize(numBranches() - which - 1);
    }

    @Override
    public Node23<E> reverse() {
        return other;
    }

    @Override
    public int numBranches() {
        return other.numBranches();
    }
    @Override
    public String toString() {
        if (isLeaf()) {
            return other.toString();
        }
        StringBuilder sb =  new StringBuilder("[");
        String delim = "";
        for(int i = 0; i < numBranches(); i++) {
            Node23<E> n = getBranch(i);
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
    public E get(int index) {
        return other.get(size() - index - 1);
    }

    @Override
    public boolean isValid(int depth) {
        return other.isValid(depth);
    }

    @Override
    public E last() {
        return other.first();
    }

    @Override
    public E first() {
        return other.last();
    }

    @Override
    public <T> T binarySearch(Function<? super E, Integer> comparator,
            BiFunction<E, Integer, T> leafVisitor) {
        return other.binarySearch(x -> -comparator.apply(x), (e,i) -> leafVisitor.apply(e, size() - i - 1));
    }
    
    @Override
    public boolean isLeaf() {
        return other.isLeaf();
    }
    
    @Override
    public E leafValue() {
        return other.leafValue();
    }

    @Override
    public <F> Node23<F> map(Function<E, F> f) {
        return new MappedNode23<E, F>(this, f);
    }
    
    @Override
    public Node23<E> head(int index) {
        final Node23<E> n = other.tail(size() - index);
        if (n == null) {
            return null;
        } else {
            return n.reverse();
        }
    }
    
    @Override
    public Node23<E> tail(int index) {
        final Node23<E> n = other.head(size() - index);
        if (n == null) {
            return null;
        } else {
            return n.reverse();
        }
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
