package collections.immutable;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Takes an iterator of nodes, and returns an iterator of resulting branches
 * that is exactly half the size of the original.
 * If there are an even number of nodes, then this will return a list of 2 branches
 * If there are an odd number of nodes, then this will return a list of 2 branches followed by one 3 branch.
 * 
 * @param <E> The element type
 */
final class NodeConstructionIterator<E> implements Iterator<Node23<E>> {
    final Iterator<? extends Node23<E>> iterator;
    
    // The next 4 nodes.  Null means it is not there.
    Node23<E> b0;
    Node23<E> b1;
    Node23<E> b2;
    Node23<E> b3;  

    public NodeConstructionIterator(Iterator<? extends Node23<E>> iterator, Node23<E> b0, Node23<E> b1) {
        super();
        assert b0 != null;
        assert b1 != null;
        this.iterator = iterator;
        this.b0 = b0;        
        this.b1 = b1;        
        b2 = advance(iterator);        
        b3 = advance(iterator);        
    }

    private Node23<E> advance(Iterator<? extends Node23<E>> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public boolean hasNext() {
        return b1 != null;
    }

    @Override
    public Node23<E> next() {
        // If there are 4 or more nodes, then retrieve the top 2, and shift left by 2
        if (b3 != null) {
            Branch<E> branch2 = new Branch<E>(b0, b1);
            b0 = b2;
            b1 = b3;
            b2 = advance(iterator);
            b3 = advance(iterator);
            return branch2;
        }
        // If there are 3 nodes, then we are done.
        if (b2 != null) {
            Branch<E> branch3 = new Branch<E>(b0, b1, b2);
            b0 = null;
            b1 = null;
            b2 = null;
            return branch3;
        }
        // If there are 2 nodes, then we are done.
        if (b1 != null) {
            Branch<E> branch2 = new Branch<E>(b0, b1);
            b0 = null;
            b1 = null;
            return branch2;
        }
        throw new NoSuchElementException();
    }
}
