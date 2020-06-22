package collections.twothree.list;

import java.util.ListIterator;
import java.util.function.Function;

final class MappedIterator<E, F> implements ListIterator<F> {
    final ListIterator<E> iterator;
    final Function<E, F> f;

    public MappedIterator(ListIterator<E> iterator, Function<E, F> f) {
        this.iterator = iterator;
        this.f = f;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public F next() {
        return f.apply(iterator.next());
    }

    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    @Override
    public F previous() {
        return f.apply(iterator.previous());
    }

    @Override
    public int nextIndex() {
        return iterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return iterator.previousIndex();
    }

    @Override
    public void remove() {
        iterator.remove();
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(F e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(F e) {
        throw new UnsupportedOperationException();
    }
}
