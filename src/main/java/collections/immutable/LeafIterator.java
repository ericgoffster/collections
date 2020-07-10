package collections.immutable;

import java.util.Iterator;

//Represents an iterator of leaves from an iterator of elements.
final class LeafIterator<E> implements Iterator<Leaf<E>> {
    final Iterator<? extends E> iterator;    

    public LeafIterator(Iterable<? extends E> iterable) {
        super();
        this.iterator = iterable.iterator();
    }
    
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Leaf<E> next() {
        return new Leaf<>(iterator.next());
    }
}
