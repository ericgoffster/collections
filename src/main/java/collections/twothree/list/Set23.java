package collections.twothree.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.function.BiFunction;

import org.granitesoft.requirement.Requirements;

final class Set23<E> implements Iterable<E> {
	final Comparator<E> keyComparator;
	final List23<E> keys;
	final boolean reversed;
	Set23(Comparator<E> keyComparator, List23<E> keys, boolean reversed) {
		this.keys = Requirements.require(keys, Requirements.notNull(), () -> "keys");
		this.keyComparator = Requirements.require(keyComparator, Requirements.notNull(), () -> "keyComparator");
		this.reversed = reversed;
	}
	
	public int size() {
		return keys.size();
	}
	
	public boolean contains(final E element) {
		return keys.root != null && visit(keys.root, element, 0, (leaf, i) -> compare(element, leaf) == 0);
	}
	
	public int compare(E a, E b) {
		return reversed ? keyComparator.compare(b, a) : keyComparator.compare(a, b);
	}
	
	E last(final Node23<E> node) {
		return keys.isLeaf(node) ? node.leafValue() : last(keys.b_last(node));
	}

	E first(final Node23<E> node) {
		return keys.isLeaf(node) ? node.leafValue() : first(keys.b1(node));
	}

	<T> T visit(Node23<E> n, E element, int i, BiFunction<E,Integer,T> leafVisitor) {
		return keys.isLeaf(n) ? leafVisitor.apply(n.leafValue(), i) :
			compare(element, last(keys.b1(n))) <= 0 ? visit(keys.b1(n), element, i, leafVisitor):
				keys.b3(n) == null || compare(element, last(keys.b2(n))) <= 0 ? visit(keys.b2(n), element, i + keys.b1(n).size(), leafVisitor):
					visit(keys.b3(n), element, i + keys.b1(n).size() + keys.b2(n).size(), leafVisitor);
	}

	
	int findFirstElement(final E element) {
		Node23<E> n = keys.root;
		return n == null ? 0:
			compare(element, first(n)) < 0 ? 0:
				compare(element, last(n)) > 0 ? size():
					visit(n, element, 0, (leaf, i) -> i);
	}
	
	public Set23<E> tailSet(E element) {
		return new Set23<E>(keyComparator, keys.tail(findFirstElement(element)), reversed);
	}

	public Set23<E> headSet(E element) {
		return new Set23<E>(keyComparator, keys.head(findFirstElement(element)), reversed);
	}

	public Set23<E> subSet(E from, E to) {
		return new Set23<E>(keyComparator, keys.subList(findFirstElement(from), findFirstElement(to)), reversed);
	}

	public Set23<E> add(E element) {
		Node23<E> n = keys.root;
		return n == null ? new Set23<>(keyComparator, keys.insert(0, element), reversed) :
			visit(n, element, 0, (leaf, i) -> {
				int cmp = compare(element, leaf);
				return cmp == 0 ? this : cmp < 0 ? new Set23<>(keyComparator, keys.insert(i, element), reversed) :
					new Set23<>(keyComparator, keys.insert(i + 1, element), reversed);
			});
	}

	public Set23<E> reverse() {
		return new Set23<E>(keyComparator, keys.reverse(), !reversed);
	}

	public Set23<E> remove(E element) {
		Node23<E> n = keys.root;
		return n == null ? this:
			visit(n, element, 0, (leaf, i) -> {
				return compare(element, leaf) == 0 ?
						new Set23<>(keyComparator, keys.remove(i), reversed) : this;
			});
	}

	static <E extends Comparable<E>> int naturalCompare(E a, E b) {
		return a == null ?
				(b == null) ? 0 : -1:
				(b == null) ? 1 : a.compareTo(b);
	}
	
	static <E extends Comparable<E>> Set23<E> natural() {
		return new Set23<E>(Set23::naturalCompare, new List23<E>(null, false), false);
	}

	SortedSet<E> asSet() {
		return new Set23Set<>(this);
	}
	
	List23<E> asList() {
		return keys;
	}
	
	@Override
	public int hashCode() {
		return asSet().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Set23)) {
			return false;
		}
		Set23<?> other = (Set23<?>)obj;
		return asSet().equals(other.asSet());
	}
	
	@Override
	public String toString() {
		return asSet().toString();
	}
	
	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <E extends Comparable<E>> Set23<E> of(E ... elements) {
		return of(Arrays.asList(elements));
	}

	@SafeVarargs
    @SuppressWarnings("varargs")
	public static <E> Set23<E> of(Comparator<E> keyComparator, E ... elements) {
		return of(keyComparator, Arrays.asList(elements));
	}

	public static <E extends Comparable<E>> Set23<E> of(Iterable<E> elements) {
		return of(Set23::naturalCompare, elements);
	}

	public static <E> Set23<E> of(Comparator<E> keyComparator, Iterable<E> elements) {
		List<E> list = new ArrayList<E>();
		for(E e: elements) {
			list.add(e);
		}
		return new Set23<E>(keyComparator, List23.of(sortUnique(keyComparator, list)), false);
	}

	private static <E> List<E> sortUnique(Comparator<E> keyComparator, List<E> list) {
		Collections.sort(list, keyComparator);
		int i = 0;
		int j = 0;
		while(j < list.size()) {
			while(j + 1 < list.size() && Objects.equals(list.get(j), list.get(j + 1))) {
				j++;
			}
			if (i == j) {
				i++;
				j++;
			} else {
				list.set(i++, list.get(j++));
			}
		}
		return list.subList(0, i);
	}

	@Override
	public ListIterator<E> iterator() {
		return keys.iterator();
	}
}
