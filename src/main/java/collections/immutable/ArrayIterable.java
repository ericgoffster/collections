package collections.immutable;

import java.lang.reflect.Array;
import java.util.Iterator;

//Fast iterable of an array.
final class ArrayIterable<E> implements Iterable<E> {
    final Object array;

    public ArrayIterable(Object array) {
        super();
        this.array = array;
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
            return i < Array.getLength(array);
        }

        @Override
        public E next() {
            @SuppressWarnings("unchecked")
            E e = (E)Array.get(array, i++);
            return e;
        }


        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
