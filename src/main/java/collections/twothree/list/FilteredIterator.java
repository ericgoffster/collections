package collections.twothree.list;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

final class FilteredIterator<E> implements Iterator<Leaf<E>> {
    final Iterator<Leaf<E>> iterator;
    final Predicate<E> filter;
    Leaf<E> node;
    

    public FilteredIterator(Iterator<Leaf<E>> iterator, Predicate<E> filter) {
        super();
        this.iterator = iterator;
        this.filter = filter;
        node = advance(iterator, filter);
    }
    
    private static <E> Leaf<E> advance(Iterator<Leaf<E>> iterator, Predicate<E> filter) {
        while(iterator.hasNext()) {
            Leaf<E> n = iterator.next();
            if (filter.test(n.leafValue())) {
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
        node = advance(iterator, filter);
        return n;
    }
}
