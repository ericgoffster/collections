package collections.twothree.list;

import java.util.ListIterator;
import java.util.NoSuchElementException;

final class NodeIterator<E> implements ListIterator<E>{
    
    final Node23<E> root;
    int which;
    ListIterator<E> curr;
    int index;

    private NodeIterator(Node23<E> n, int which, ListIterator<E> curr, int index) {
        super();
        this.root = n;
        this.which = which;
        this.curr = curr;
        this.index = index;
    }
    
    public static <E> ListIterator<E> atBeginning(Node23<E> n) {
        assert n != null;
        if (n.isLeaf()) {
            return new SingletonIterator<>(n.leafValue());
        }
        return new NodeIterator<>(n, 0, atBeginning(n.getBranch(0)), 0);
    }
    
    public static <E> ListIterator<E> atEnd(Node23<E> n) {
        assert n != null;
        if (n.isLeaf()) {
            ListIterator<E> iterator = new SingletonIterator<>(n.leafValue());
            iterator.next();
            return iterator;
        }
        return new NodeIterator<>(n, n.numBranches() - 1, atEnd(n.getBranch(n.numBranches() - 1)), n.size() - 1);
    }

    @Override
    public boolean hasNext() {
        if (curr.hasNext()) {
            return true;
        }
        if (which >= root.numBranches() - 1) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean hasPrevious() {
        if (curr.hasPrevious()) {
            return true;
        }
        if (which == 0) {
            return false;
        }
        return true;
    }

    @Override
    public E previous() {
        if (!curr.hasPrevious()) {
            if (which == 0) {
                throw new NoSuchElementException();
            }
            which--;
            this.curr = atEnd(root.getBranch(which));
        }
        index--;
        return curr.previous();
    }

    @Override
    public E next() {
        if (!curr.hasNext()) {
            if (which >= root.numBranches() - 1) {
                throw new NoSuchElementException();
            }
            which++;
            this.curr = atBeginning(root.getBranch(which));
        }
        index++;
        return curr.next();
    }
    
    @Override
    public int nextIndex() {
        return index;
    }
    
    @Override
    public int previousIndex() {
        return index - 1;
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
