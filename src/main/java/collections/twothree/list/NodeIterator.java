package collections.twothree.list;

import java.util.ListIterator;

final class NodeIterator<E> implements ListIterator<E>{
    
    final Node23<E> root;
    NodeState<E> state;
    int i = 0;

    public NodeIterator(Node23<E> n) {
        super();
        this.root = n;
        this.state = n == null ? null : new NodeState<>(n).leftMost();
    }

    @Override
    public boolean hasNext() {
        return state != null;
    }

    @Override
    public E next() {
        E e = state.n.leafValue();
        state = state.next();
        i++;
        return e;
    }

    @Override
    public boolean hasPrevious() {
        return i > 0;
    }

    @Override
    public E previous() {
        if (state == null) {
            state = new NodeState<E>(root).rightMost();
        } else {
            state = state.prev();
        }
        i--;
        return state.n.leafValue();
    }

    @Override
    public int nextIndex() {
        return i;
    }

    @Override
    public int previousIndex() {
        return i - 1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(E e) {
        throw new UnsupportedOperationException();
    }
}
