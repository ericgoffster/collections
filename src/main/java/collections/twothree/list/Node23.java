package collections.twothree.list;

import java.util.ListIterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

interface Node23<E> extends Iterable<E> {
	E leafValue();
	int size();
	boolean isLeaf();
    Node23<E> getBranch(int which);
    int getBranchSize(int which);
	Node23<E> reverse();
	int numBranches();
	int getDepth();
	ListIterator<E> iterator();
    E get(int index);
    boolean isValid(int depth);
    <T> T binarySearch(Function<? super E, Integer> comparator, int index, BiFunction<E,Integer,T> leafVisitor);
    <T> T reverseBinarySearch(Function<? super E, Integer> comparator, int index, BiFunction<E,Integer,T> leafVisitor);
    E last();
    E first(); 
    <F> Node23<F> map(Function<E, F> f);
    Node23<E> head(int index);
    Node23<E> tail(int index);
    Stream<E> stream();
    default int hc() {
        if (isLeaf()) {
            return Objects.hashCode(leafValue());
        } else {
            final int prime = 31;
            int result = 1;
            for(int i = 0; i < numBranches(); i++) {
                result = result * prime + Objects.hashCode(getBranch(i));
            }
            return result;            
        }
    }
    default public boolean eq(Object obj) {
        if (!(obj instanceof Node23)) {
            return false;
        }
        Node23<?> other = (Node23<?>)obj;
        if (other.numBranches() != numBranches()) {
            return false;
        }
        if (other.isLeaf()) {
            return Objects.equals(other.leafValue(), leafValue());
        }
        for(int i = 0; i < numBranches(); i++) {
            if (!Objects.equals(getBranch(i), other.getBranch(i))) {
                return false;
            }
        }
        return true;
    }
}
