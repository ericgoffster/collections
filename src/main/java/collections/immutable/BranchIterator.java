package collections.immutable;

import java.util.NoSuchElementException;

//Represents an iterator of elements in a branch.
final class BranchIterator<E> implements SeekableIterator<E>{
    
    final Node23<E> root;
    int which;
    SeekableIterator<E> curr;
    int index;

    BranchIterator(Node23<E> n) {
        super();
        this.root = n;
        this.which = 0;
        this.curr = n.getBranch(0).iterator();
        this.index = 0;
    }
    
    public void toEnd() {
        this.which = root.numBranches() - 1;
        this.curr = root.getBranch(root.numBranches() - 1).iterator();
        this.index = root.size() - 1;
        this.curr.toEnd();
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
            this.curr = root.getBranch(which).iterator();
            this.curr.toEnd();
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
            this.curr = root.getBranch(which).iterator();
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
