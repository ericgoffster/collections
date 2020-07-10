package collections.immutable;

import java.util.ListIterator;

interface SeekableIterator<E> extends ListIterator<E> {
    void toEnd();
}
