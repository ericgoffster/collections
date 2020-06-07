package collections.twothree.list;

import java.util.AbstractList;

class List23List<E> extends AbstractList<E> {
	private final List23<E> list23;

	public List23List(List23<E> list23) {
		super();
		this.list23 = list23;
	}
	
	@Override
	public E get(int index) {
		return list23.getAt(index);
	}

	@Override
	public int size() {
		return list23.size();
	}
}
