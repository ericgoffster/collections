package collections.twothree.list;

import java.util.ListIterator;
import java.util.function.BiFunction;
import java.util.function.Function;

final class MappedNode23<E, F> implements Node23<F> {
    final Node23<E> e;
    final Function<E, F> f;

    public static <E, F>  Node23<F> map(Node23<E> e, Function<E, F> f) {
        if (e == null) {
            return null;
        }
        if (e.isLeaf()) {
            return new Leaf<F>(f.apply(e.leafValue()));
        } else {
            return new MappedNode23<>(e, f);
        }
    }

    private MappedNode23(Node23<E> e, Function<E, F> f) {
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
        return map(e.getBranch(which), f);
    }
    
    @Override
    public int getBranchSize(int which) {
        return e.getBranchSize(which);
    }

    @Override
    public Node23<F> reverse() {
        return map(e.reverse(), f);
    }
    
    @Override
    public <T> T binarySearch(Function<? super F, Integer> comparator, int index,
            BiFunction<F, Integer, T> leafVisitor) {
        return e.binarySearch(e -> comparator.apply(f.apply(e)), index, (e, i) -> leafVisitor.apply(f.apply(e), i));
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
        return new MappedIterator<E, F>(e.iterator(), f);
    }
}
