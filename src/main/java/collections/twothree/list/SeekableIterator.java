package collections.twothree.list;

import java.util.ListIterator;

public interface SeekableIterator<E> extends ListIterator<E> {
    public void toEnd();
}
