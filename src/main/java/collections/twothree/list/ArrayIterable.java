package collections.twothree.list;

import java.util.ListIterator;

final class ArrayIterable<E> implements Iterable<E> {
    final E[] elements;

    @SafeVarargs
    public ArrayIterable(E ... elements) {
        super();
        this.elements = elements;
    }

    @Override
    public ListIterator<E> iterator() {
        return new ArrayIterator();
    }
    
    final class ArrayIterator implements ListIterator<E> {
        int i = 0;

        public ArrayIterator() {
            super();
        }
        
        @Override
        public boolean hasNext() {
            return i < elements.length;
        }

        @Override
        public E next() {
            return elements[i++];
        }

        @Override
        public boolean hasPrevious() {
            return i > 0;
        }

        @Override
        public E previous() {
            return elements[--i];
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

}
