package collections.immutable;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

//
//Represents a hashset as a list of elements ordered by their hash.
//This is different than a standard hashset in that lookup's are O(log n).
//Therefore the only advantage of HashSet23 over TreeMap23 is that you can use
//elements for which a comparator is not trivial.
//
final class HashSet23<E> implements ImmSet<E> {
	final TreeList23<E> elements;

	HashSet23(final TreeList23<E> elements) {
	    assert elements != null;
		this.elements = elements;
	}
	
	static <E> HashSet23<E> singleton(final E element) {
        return new HashSet23<E>(TreeList23.singleton(element));
    }

    static <E> HashSet23<E> empty() {
        return new HashSet23<E>(TreeList23.empty());
    }

    static <E> HashSet23<E> of(final Iterable<? extends E> elements) {
        return new HashSet23<E>(TreeList23.ofSortedUnique(HashSet23::compare, elements));
    }

    @Override
	public int size() {
		return elements.size();
	}
	
    @Override
	public boolean contains(final E element) {
	    return elements.getIndexOf(e -> compare(element, e)) >= 0;
	}

    @Override
	public HashSet23<E> add(final E element) {
	    if (contains(element)) {
	        return this;
	    }
	    return new HashSet23<>(elements.insertAt(elements.naturalPosition(e -> compare(element, e)), element));
	}
	
    @Override
	public HashSet23<E> union(final ImmSet<E> other) {
	    HashSet23<E> s = this;
	    for(E e: other) {
	        s = s.add(e);
	    }
	    return s;
	}

    @Override
	public HashSet23<E> remove(final E element) {
        final int index = elements.getIndexOf(e -> compare(element, e));
	    return index < 0 ? this : new HashSet23<>(elements.removeAt(index));
	}
	
    @Override
    public HashSet23<E> filter(final Predicate<E> filter) {
        return new HashSet23<>(elements.filter(filter));
    }
	
    @Override
    public HashSet23<E> retain(final Iterable<? extends E> other) {
        final HashSet23<E> hs = HashSet23.of(other);
        return filter(hs::contains);
    }

    @Override
    public HashSet23<E> removeAllIn(final Iterable<? extends E> other) {
        HashSet23<E> m = this;
        for(E e: other) {
            m = m.remove(e);
        }
        return m;
    }
  
    @Override
	public Set<E> asCollection() {
		return new Set23Set<>(this);
	}
	
    @Override
	public int hashCode() {
		return asCollection().hashCode();
	}
	
	@Override
	public boolean equals(final Object otherObject) {
		if (!(otherObject instanceof ImmSet)) {
			return false;
		}
		final ImmSet<?> other = (ImmSet<?>)otherObject;
		return asCollection().equals(other.asCollection());
	}

	@Override
	public String toString() {
		return asCollection().toString();
	}
	
	@Override
	public Iterator<E> iterator() {
		return elements.iterator();
	}
    
    @Override
    public Spliterator<E> spliterator() {
        return elements.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    static <E> int compare(final E a, final E b) {
        int cmp = Integer.compare(Objects.hash(a), Objects.hash(b));
        if (cmp != 0) {
            return cmp;
        }
        if (Objects.equals(a, b)) {
            return 0;
        }
        return Integer.compare(System.identityHashCode(a), System.identityHashCode(b));
    }
}
