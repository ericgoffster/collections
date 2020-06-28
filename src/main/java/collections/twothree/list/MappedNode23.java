package collections.twothree.list;

import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class MappedNode23<E, F> implements Node23<F> {
    final Node23<E> e;
    final Function<E, F> f;

    MappedNode23(Node23<E> e, Function<E, F> f) {
        super();
        this.e = e;
        this.f = f;
    }
    
    @Override
    public F get(int index) {
        return f.apply(e.get(index));
    }

    @Override
    public int getDepth() {
        return e.getDepth();
    }
        
    @Override
    public F leafValue() {
        return f.apply(e.leafValue());
    }

    @Override
    public int size() {
        return e.size();
    }
    
    @Override
    public Node23<F> getBranch(int which) {
        return e.getBranch(which).map(f);
    }
    
    @Override
    public int getBranchSize(int which) {
        return e.getBranchSize(which);
    }

    @Override
    public Node23<F> reverse() {
        return e.reverse().map(f);
    }
    
    @Override
    public <T> T binarySearch(Function<? super F, Integer> comparator,
            BiFunction<F, Integer, T> leafVisitor) {
        return e.binarySearch(e -> comparator.apply(f.apply(e)), (e, i) -> leafVisitor.apply(f.apply(e), i));
    }
    
    @Override
    public F last() {
        return f.apply(e.last());
    }
    
    @Override
    public F first() {
        return f.apply(e.first());
    }

    @Override
    public int numBranches() {
        return e.numBranches();
    }  
    
    @Override
    public boolean isValid(int depth) {
        return e.isValid(depth);
    }
    
    @Override
    public ListIterator<F> iterator() {
        return new MappedIterator<>(e.iterator(), f);
    }
    
    @Override
    public boolean isLeaf() {
        return e.isLeaf();
    }
    
    @Override
    public <G> Node23<G> map(Function<F, G> f) {
        return new MappedNode23<F, G>(this, f);
    }
    
    @Override
    public Node23<F> head(int index) {
        final Node23<E> head = e.head(index);
        if (head == null) {
            return null;
        }
        return head.map(f);
    }
    
    @Override
    public Node23<F> tail(int index) {
        final Node23<E> tail = e.tail(index);
        if (tail == null) {
            return null;
        }
        return tail.map(f);
    }
    
    @Override
    public Spliterator<F> spliterator() {
        return Spliterators.spliterator(iterator(), size(), 0);
    }

    @Override
    public Stream<F> stream() {
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
