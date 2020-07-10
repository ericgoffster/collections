package collections.immutable;

import java.util.Iterator;

//Fast iterable of an array.
final class ArrayIterable<E> implements Iterable<E> {
    final E[] elements;

    public ArrayIterable(E[] elements) {
        super();
        this.elements = elements;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator();
    }
    
    final class ArrayIterator implements Iterator<E> {
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
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
