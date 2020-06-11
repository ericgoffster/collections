package collections.twothree.list;

import java.util.AbstractSet;
import java.util.Iterator;

final class HashSet23Set<E> extends AbstractSet<E> {
	final HashSet23<E> set;

	public HashSet23Set(HashSet23<E> set) {
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
}