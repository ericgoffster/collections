package collections.twothree.list;

import java.util.function.Function;

class MappedNode23<E, F> implements Node23<F> {
    final Node23<E> e;
    final Function<E, F> f;

    public MappedNode23(Node23<E> e, Function<E, F> f) {
        super();
        this.e = e;
        this.f = f;
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
    
    Node23<F> xform(Node23<E> b) {
        return new MappedNode23<>(b, f);
    }
    
    @Override
    public Node23<F> getBranch(int which) {
        return xform(e.getBranch(which));
    }
    
    @Override
    public int getBranchSize(int which) {
        return e.getBranchSize(which);
    }

    @Override
    public Node23<F> reverse() {
        return xform(e.reverse()) ;
    }

    @Override
    public int numBranches() {
        return e.numBranches();
    }    
}
