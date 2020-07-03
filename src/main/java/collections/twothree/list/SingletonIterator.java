package collections.twothree.list;

import java.util.NoSuchElementException;

final class SingletonIterator<E> implements SeekableIterator<E> {
    final E element;
    int i = 0;
    
    @Override
    public void toEnd() {
        i = 1;
    }

    public SingletonIterator(E element) {
        super();
        this.element = element;
    }

    @Override
    public boolean hasNext() {
        return i < 1;
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        i++;
        return element;
    }

    @Override
    public boolean hasPrevious() {
        return i > 0;
    }

    @Override
    public E previous() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        i--;
        return element;
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
