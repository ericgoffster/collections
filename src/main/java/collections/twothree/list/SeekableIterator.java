package collections.twothree.list;

import java.util.ListIterator;

interface SeekableIterator<E> extends ListIterator<E> {
    void toEnd();
}
