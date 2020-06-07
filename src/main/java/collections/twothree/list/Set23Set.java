package collections.twothree.list;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

final class Set23Set<E> extends AbstractSet<E> implements SortedSet<E> {
	final Set23<E> set;

	public Set23Set(Set23<E> set) {
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
		try {
			@SuppressWarnings("unchecked")
			E element = (E)o;
			return set.contains(element);
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public Comparator<? super E> comparator() {
		return set.comparator;
	}

	@Override
	public SortedSet<E> subSet(E fromElement, E toElement) {
		return set.subSet(fromElement, toElement).asSet();
	}

	@Override
	public SortedSet<E> headSet(E toElement) {
		return set.lt(toElement).asSet();
	}

	@Override
	public SortedSet<E> tailSet(E fromElement) {
		return set.ge(fromElement).asSet();
	}

	@Override
	public E first() {
		return set.elements.getAt(0);
	}

	@Override
	public E last() {
		return set.elements.getAt(set.elements.size() - 1);
	}
}