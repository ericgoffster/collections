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
		return set.keys.asList().iterator();
	}

	@Override
	public int size() {
		return set.keys.size();
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
		return set::compare;
	}

	@Override
	public SortedSet<E> subSet(E fromElement, E toElement) {
		return set.subSet(fromElement, toElement).asSet();
	}

	@Override
	public SortedSet<E> headSet(E toElement) {
		return set.headSet(toElement).asSet();
	}

	@Override
	public SortedSet<E> tailSet(E fromElement) {
		return set.tailSet(fromElement).asSet();
	}

	@Override
	public E first() {
		return set.keys.get(0);
	}

	@Override
	public E last() {
		return set.keys.get(set.keys.size() - 1);
	}
}