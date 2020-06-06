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
    public F leafValue() {
        return f.apply(e.leafValue());
    }

    @Override
    public int size() {
        return e.size();
    }
    
    Node23<F> xform(Node23<E> b) {
        return b == null ? null : new MappedNode23<>(b, f);
    }

    @Override
    public Node23<F> b1() {
        return xform(e.b1()) ;
    }

    @Override
    public int b1Size() {
        return e.b1Size();
    }

    @Override
    public Node23<F> b2() {
        return xform(e.b2()) ;
    }

    @Override
    public int b2Size() {
        return e.b2Size();
    }

    @Override
    public Node23<F> b3() {
        return xform(e.b3()) ;
    }

    @Override
    public Node23<F> b_last() {
        return xform(e.b_last()) ;
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
