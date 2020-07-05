package collections.twothree.list;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

final class SortedSet23Set<E> extends AbstractSet<E> implements SortedSet<E> {
	final ImmSortedSet<E> set;

	public SortedSet23Set(ImmSortedSet<E> set) {
		super();
		this.set = set;
	}

	@Override
	public Iterator<E> iterator() {
		return set.asList().iterator();
	}

	@Override
	public int size() {
		return set.asList().size();
	}

	@Override
	public boolean contains(Object o) {
	    @SuppressWarnings("unchecked")
	    E element = (E)o;
	    return set.contains(element);
	}

	@Override
	public Comparator<? super E> comparator() {
		return set.getComparator();
	}

	@Override
	public SortedSet<E> subSet(E fromElement, E toElement) {
		return set.subSet(fromElement, toElement).asCollection();
	}

	@Override
	public SortedSet<E> headSet(E toElement) {
		return set.lt(toElement).asCollection();
	}

	@Override
	public SortedSet<E> tailSet(E fromElement) {
		return set.ge(fromElement).asCollection();
	}

	@Override
	public E first() {
        if (set.size() == 0) {
            throw new NoSuchElementException();
        }
		return set.asList().getAt(0);
	}

	@Override
	public E last() {
        if (set.size() == 0) {
            throw new NoSuchElementException();
        }
		return set.asList().getAt(set.asList().size() - 1);
	}
}