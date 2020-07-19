/**
 * A package for implementation and manipulation of immutable collections.
 * 
 * <p>What is immutability?
 * 
 * <p>Immutability is the foundation of functional programming.
 * 
 * A pure function is side-effect free.   Arguments and internal state
 * remain unchanged.   Side effects are a major source of bugs.
 * 
 * Immutable objects can be shared.   This *can* help with memory considerations.
 * 
 * Immutable objects are concurrent.   You pay no performance cost whatsoever
 * in sharing an object across many threads.
 * 
 * I will focus on one particular example to illustrate:
 * 
 * <pre>{@code
 *     ImmList l1 = ImmCollections.asList(1, 2, 3, 4, 5);
 *     ImmList l2 = ImmCollections.asList(6, 7, 8, 9, 10);
 *     ImmList l3 = l1.insertListAt(3, l2);
 * }</pre>
 *     
 *  At this point:
 *  <ul>
 *  <li>l1 == [1, 2, 3, 4, 5]</li>
 *  <li>l2 == [6, 7, 8, 9, 10]</li>
 *  <li>l3 == [1, 2, 3, 6, 7, 8, 9, 10, 4, 5]</li>
 *  </ul>
 *
 * Because of clever structure sharing, the insertListAt operation was also
 * fairly fast, even if the size of l1 or l2 o both were very large.
 * The insertListAt operation is O(log |l1|) + O(log |l2|).
 * 
 * There is no need at all for list "builders" as you might find in other immutable libraries such as guava.
 * 
 * Because all of these list operations are performant, we can implement things like immutable sets and maps
 * that use this immutable list.  These sets and maps are frequently fairly trivial.
 * <p>
 * Pros:
 * <ul>
 *      <li>Fast repeated operations that are normal not fast, insertAt, removeAt, exclude, replace, reverse</li>
 *      <li>Fast conversion to java collections</li>
 *      <li>Granite guarantee:
 *          <ul>
 *              <li>Compact library</li>
 *              <li>No breaking changes</li>
 *              <li>No reliance on third party libraries that do not have the granite guaranteed</li>
 *          </ul>
 *      </li>
 * </ul>
 *          
 * Cons:
 * <ul>
 *      <li>The space required to implement a list is O(n log n), not O(n)</li>
 *      <li>ImmList.get(i)  is O(log n), not O(1)</li>
 * </ul>
 *      
 */
package collections.immutable;