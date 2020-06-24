package collections.twothree.list;

import java.util.ListIterator;
import java.util.function.BiFunction;
import java.util.function.Function;

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
}
