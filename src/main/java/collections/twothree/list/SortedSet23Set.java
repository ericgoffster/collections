package collections.twothree.list;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

final class SortedSet23Set<E> extends AbstractSet<E> implements SortedSet<E> {
	final SortedSet23<E> set;

	public SortedSet23Set(SortedSet23<E> set) {
		super();
		this.set = set;
	}

	@Override
	public Iterator<E> iterator() {
		return set.elements.asList().iterator();
	}

	@Override
	public int size() {
		return set.elements.size();
	}

	@Override
	public boolean contains(Object o) {
	    @SuppressWarnings("unchecked")
	    E element = (E)o;
	    return set.contains(element);
	}

	@Override
	public Comparator<? super E> comparator() {
		return set.comparator;
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
		return set.elements.getAt(0);
	}

	@Override
	public E last() {
        if (set.size() == 0) {
            throw new NoSuchElementException();
        }
		return set.elements.getAt(set.elements.size() - 1);
	}
}