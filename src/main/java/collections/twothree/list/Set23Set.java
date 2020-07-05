package collections.twothree.list;

import java.util.AbstractSet;
import java.util.Iterator;

final class Set23Set<E> extends AbstractSet<E> {
	final ImmSet<E> set;

	public Set23Set(ImmSet<E> set) {
		super();
		this.set = set;
	}

	@Override
	public Iterator<E> iterator() {
		return set.iterator();
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean contains(Object o) {
	    @SuppressWarnings("unchecked")
	    E element = (E)o;
	    return set.contains(element);
	}
}