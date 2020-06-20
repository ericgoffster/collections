package collections.twothree.list;

import java.util.ListIterator;

final class NodeIterator<E> implements ListIterator<E>{
    
    final Node23<E> root;
    NodeState<E> state;
    int i = 0;

    public NodeIterator(Node23<E> n) {
        super();
        this.root = n;
        this.state = n == null ? null : new NodeState<E>(null, n).leftMost();
    }

    private static class NodeState<E> {
        final NodeState<E> parent;
        final Node23<E> n;
        public NodeState(NodeState<E> parent, Node23<E> n) {
            super();
            this.parent = parent;
            this.n = n;
        }
        private NodeState<E> next() {
            if (parent == null) {
                return null;
            }
            if (n == parent.n.b1()) {
                return new NodeState<>(parent, parent.n.b2()).leftMost();
            }
            if (n == parent.n.b2()) {
                if (parent.n.numBranches() == 2) {
                    return parent.next();
                }
                return new NodeState<>(parent, parent.n.b3()).leftMost();
            }
            return parent.next();
        }
        private NodeState<E> prev() {
            if (parent == null) {
                return null;
            }
            if (n == parent.n.b1()) {
                return parent.prev();
            }
            if (n == parent.n.b2()) {
                return new NodeState<>(parent, parent.n.b1()).rightMost();
            }
            return new NodeState<>(parent, parent.n.b2()).rightMost();
        }
        
        private NodeState<E> leftMost() {
            if (n.isLeaf()) {
                return this;
            }
            return new NodeState<E>(this, n.b1()).leftMost();
        }
        private NodeState<E> rightMost() {
            if (n.isLeaf()) {
                return this;
            }
            if (n.numBranches() == 3) {
                return new NodeState<E>(this, n.b3()).rightMost();
            }
            return new NodeState<E>(this, n.b2()).rightMost();
        }
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
            state = new NodeState<E>(null, root).rightMost();
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
