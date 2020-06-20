package collections.twothree.list;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class RemoveDupsIterator<E> implements Iterator<Leaf<E>> {
    final Iterator<Leaf<E>> iterator;
    final Comparator<? super E> comparator;
    Leaf<E> node; 

    public RemoveDupsIterator(Iterator<Leaf<E>> iterator, Comparator<? super E> comparator) {
        super();
        this.iterator = iterator;
        this.comparator = comparator;
        node = iterator.hasNext() ? iterator.next() : null;
    }
    
    private static <E> Leaf<E> advance(Iterator<Leaf<E>> iterator, Leaf<E> prev, Comparator<? super E> comparator) {
        while(iterator.hasNext()) {
            Leaf<E> n = iterator.next();
            if (comparator.compare(n.leafValue(), prev.leafValue()) != 0) {
                return n;
            }
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        return node != null;
    }

    @Override
    public Leaf<E> next() {
        if (node == null) {
            throw new NoSuchElementException();
        }
        Leaf<E> n = node;
        node = advance(iterator, n, comparator);
        return n;
    }
}
