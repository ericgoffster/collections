package collections.twothree.list;

import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class Leaf<E> implements Node23<E> {
	private final E element;
	public Leaf(E leaf) {
		super();
		this.element = leaf;
	}
	@Override
	public int numBranches() {
	    return 0;
	}
	@Override
	public int getDepth() {
	    return 1;
	}
	@Override
	public int size() {
		return 1;
	}
	@Override
	public E leafValue() {
		return element;
	}
    @Override
    public String toString() {
        return String.valueOf(element);
    }
    
    @Override
    public Node23<E> reverse() {
        return this;
    }
    
    @Override
    public <T> T binarySearch(Function<? super E, Integer> comparator, int index,
            BiFunction<E, Integer, T> leafVisitor) {
        return leafVisitor.apply(element, index);
    }
    
    @Override
    public <T> T reverseBinarySearch(Function<? super E, Integer> comparator, int index,
            BiFunction<E, Integer, T> leafVisitor) {
        return leafVisitor.apply(element, index);
    }

    @Override
    public Node23<E> getBranch(int which) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBranchSize(int which) {
        throw new UnsupportedOperationException();
    }

    public E get(final int index) {
        assert index < 1;
        return element;
    }
    
    @Override
    public boolean isValid(int depth) {
        return depth == 1;
    }
    
    @Override
    public E last() {
        return element;
    }
    
    @Override
    public E first() {
        return element;
    }
    
    @Override
    public boolean isLeaf() {
        return true;
    }
    
    @Override
    public ListIterator<E> iterator() {
        return new SingletonIterator<>(element);
    }
    
    @Override
    public <F> Node23<F> map(Function<E, F> f) {
        return new Leaf<F>(f.apply(element));
    }
    
    @Override
    public Node23<E> head(int index) {
        return null;
    }
    
    @Override
    public Node23<E> tail(int index) {
        return index > 0 ? null : this;
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), 0);
    }
    @Override
    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
    @Override
    public int hashCode() {
        return hc();
    }
    @Override
    public boolean equals(Object obj) {
        return eq(obj);
    }
}
