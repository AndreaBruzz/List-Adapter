package myTest;

import myAdapter.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.*;

import java.util.Iterator;
import java.util.List;
//import java.util.NoSuchElementException;
import java.util.NoSuchElementException;
import java.util.function.IntUnaryOperator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class of {@link myAdapter.ListAdapter} methods
 * <p>
 * <p>
 * Summary: this class tests the functionality of all ListAdapter methods
 * <br><br>
 * Design test: in this class every method tests a different method of ListAdapter.
 * In every method the execution variables changes before calling the tested method in order to test all the possible conditions
 * <br><br>
 * Description: The ListAdapter class implements HList and HCollection, therefore the HList methods were tested using an HList as object
 * The TestList class deals with the testing for all the methods related to the main list and the list iterator, always of the main list.
 * For simplicity, the values entered are all Strings because the behaviour of this objects is well known
 * <br><br>
 * Preconditions:
 * <br>A new empty object of type ListAdapter must always be instantiated before each test and also a filled one is created
 * <br>All execution variables are never in an uninitialized state (i.e. list = null) unless explicitly desired
 * <br>Methods that take as parameters classes that implement HCollection are passed suitable objects for this interface, therefore they do not throw the exception ClassCastException
 * <br>All the elements contained in the list are known before the execution in order to allow the verification after the invocation of the tested methods
 * <br><br>
 * Postconditions: the methods implemented must always modify the list so that the elements contained are exactly those expected starting from their manual insertion
 * <br><br>
 * Execution record: each tested method is correct if all the tests that verify the correct functioning give positive results. Correct execution of the entire test method can then be considered the execution record
 * <br><br>
 * Execution variables:
 * <br>HList l1 - empty list on which methods common to HCollection and HList are tested
 * <br>HList l2 - non-empty list on which methods common to HCollection and HList are tested
 * <br>String[] argv - an array of strings used to fill the list in some situations
 * <br><br>
 *
 * @author Andrea Bruttomesso
 * @see myAdapter.HList
 * @see myAdapter.HIterator
 * @see myAdapter.HListIterator
 * @see myAdapter.HCollection
 */

public class TestList
{
	HList l1 = null, l2 = null;
	static String[] argv = {"pippo", "qui", "pluto", "paperino", "qui", "ciccio"};

	@Before
	public void setup()
	{
		System.out.println("Instantiate an empty List...");
		l1 = new ListAdapter();

		System.out.println("Instantiate and fill a List with {\"Ironman\", \"Thor\", \"Hulk\"}");
		l2 = new ListAdapter();
		l2.add("Ironman");
		l2.add("Thor");
		l2.add("Hulk");
	}

	@After
	public void cleanup()
	{
		System.out.println("Purge all remaining elements...");
		l1.clear();
		l2.clear();
		System.out.println();
	}

	/**
     * Test the {@link myAdapter.ListAdapter#subList(int, int)} method
     * <p>
     *
     * <br><br>Summary: 				Testing the sublist() method and how the father list changes after having invoked different methods
     * <br><br>Description: 			a sublist is created, some list's methods are invoked on it to check if the father lists correctly
	 * 									backs the changes on the son
	 * <br><br>Design test:				filling an empty list and creating a sublist from index 0 to half of the father list. Adding and removing
	 * 									an item from it and checking that the father size and elements change in the same way as the son.
	 * 									Cleaning the sublist content and seing that the father list content is affected by this change
     * <br><br>Preconditions:			add(), remove(), clean() should be correctly implemented for lists
     * <br><br>Postconditions: 			the father list must be affected by all the changes applied to the son list
     * <br><br>Expected results:		an item should be added and removed from both lists, their sizes must be increased and then decreased, when
	 * 									the sublist is cleaned the elements must also be deleted from the father list
     */	
	@Test
	public void testBacking()
	{
		System.out.println("----- TestBacking -----");
		
		System.out.println("Filling the list");
		for(int i=0;i<argv.length;i++)
		{
			l1.add(argv[i]);
		}

		int dl0, dl1, dli, dsl0, dsl1, dsli;

		iterate(l1.iterator());
		System.out.println("l1 " + l1.size());
		dl0 = l1.size();

		System.out.println("Creating a sublist from 0 to half of the length...");
		l2 = l1.subList(0, argv.length/2);
		dsl0 = l2.size();

		System.out.println("Adding element to the sublist");
		l2.add("pipperissimo");
		dli = l1.size();
		dsli = l2.size();

		assertEquals("\n*** sublist add is NOT backed correctly ***\n", dli, dl0+1);
		assertEquals("\n*** sublist add is NOT backed correctly ***\n", dsli, dsl0+1);
		System.out.println("Sizes of son and father updated correctly!");
		System.out.println("l1 " + l1.size());
		System.out.println("l2 " + l2.size());

		System.out.println("Removing element from the sublist");
		l2.remove("pipperissimo");
		assertEquals("\n*** list remove is NOT backed correctly ***\n", l1.size(), dl0);
		assertEquals("\n*** list remove is NOT backed correctly ***\n", l2.size(), dsl0);
		System.out.println("Sizes of son and father updated correctly!");
		System.out.println("l1 " + l1.size());
		System.out.println("l2 " + l2.size());

		System.out.println("Printing sublist through iterators");
		iterate(l2.iterator());
		System.out.println("l2 " + l2.size());

		System.out.println("Cleaning sublist");
		l2.clear();
		dl1 = l1.size();
		dsl1 = l2.size();
		System.out.println("l1 " + l1.size());
		iterate(l1.iterator());
		System.out.println("l2 " + l2.size());
		iterate(l2.iterator());

		System.out.println(dl0 + " " + dl1 + " " + dsl0 + " " + dsl1);
		assertEquals("\n*** sublist is NOT backed correctly ***\n", dsl0, (dl0/2));
		assertEquals("\n*** sublist is NOT backed correctly ***\n", dsl1, 0);
		assertEquals("\n*** sublist is NOT backed correctly ***\n", dl1, (dl0 - dsl0));

		System.out.println();
	}

	/**
     * Test the {@link myAdapter.ListAdapter#addAll(HCollection)} {@link myAdapter.ListAdapter#addAll(int, HCollection)},
	 * {@link myAdapter.ListAdapter#removeAll(HCollection)}, {@link myAdapter.ListAdapter#retainAll(HCollection)} methods on sublist
     * <p>
     *
     * <br><br>Summary: 				Testing the sublists' methods to add, remove, and retain collections and how the father list
	 * 									backs the changes on the son
	 * <br><br>Description: 			an empty sublis is created, some list's methods that use collections are invoked on it to check if the
	 * 									father list correctly backs the changes on the son
	 * <br><br>Design test:				creating an empty sublist of the pre-filled list in the middle of this one. Using addAll() and removeAll()
	 * 									methods to insert and remove the content of another list, it is checked that the son and father lists are
	 * 									correctly modified through the size() method and by printing their content. Then the elements previously
	 * 									added and removed are now added again, with one new element not in the parameter collection, and retained on
	 * 									the sublist, it's verified that the elements contained in the father list are not affected by the retain on the
	 * 									sublist
     * <br><br>Preconditions:			add(), size() and iterators should be correctly implemented for lists
     * <br><br>Postconditions: 			the father list must be affected by all the changes applied to the son list, when retainAll() is invoked
	 * 									the elements in the father list out of sublist index don't have to be modified
     * <br><br>Expected results:		a collection should be added to the sublist and the father list should be modified too, then it should be
	 * 									removed and added again with also one more element and only the element contained in the collection should be
	 * 									retained in the sublist, in the father the elements out of the sublist shouldn't be affected by this last
	 * 									invokation
     */	
	@Test
	public void testBacking2(){
		System.out.println("----- TestBacking2 -----");
		
		System.out.println("l2 size: " + l2.size());
		iterate(l2.iterator());

		System.out.println("Creating an empty sublist of l2 in the middle of it...");
		HList l3 = l2.subList(l2.size()/2, l2.size()/2);
		assertEquals("Void sublist should be of size 0", 0, l3.size());
		System.out.println("l3 size: " + l3.size());
	
		System.out.println("Filling another list...");
		l1.add("Spiderman");
		l1.add("Loki");
		l1.add("Thanos");

		int sz0, sz1, sz2, sz3;

		sz0 = l2.size();
		sz1 = l3.size();

		System.out.println("Ading all elements of the new list to the sublist...");
		l3.addAll(l1);
		sz2 = l2.size();
		sz3 = l3.size();
		assertEquals("Add all not working on sublists", sz0+l1.size(), sz2);
		assertEquals("Add all not working on sublists", sz1+l1.size(), sz3);
		System.out.println("Size of father and son correctly updated");
		System.out.println("l2 size: " + sz2);
		iterate(l2.iterator());
		System.out.println("l3 size: " + sz3);
		iterate(l3.iterator());

		System.out.println("Removing now all those elements from the sublist...");
		l3.removeAll(l1);
		sz2 = l2.size();
		sz3 = l3.size();
		assertEquals("Remove all not working on sublists", sz0, sz2);
		assertEquals("Remove all not working on sublists", sz1, sz3);
		System.out.println("Size of father and son correctly updated");
		System.out.println("l2 size: " + sz2);
		iterate(l2.iterator());
		System.out.println("l3 size: " + sz3);
		iterate(l3.iterator());

		System.out.println("Ading all elements again and a new element to the sublist...");
		l3.addAll(0, l1);
		l3.add(2, "Thor");
		sz2 = l2.size();
		sz3 = l3.size();
		assertEquals("Add all not working on sublists", sz0+l1.size()+1, sz2);
		assertEquals("Add all not working on sublists", sz1+l1.size()+1, sz3);
		System.out.println("Size of father and son correctly updated");
		System.out.println("l2 size: " + sz2);
		iterate(l2.iterator());
		System.out.println("l3 size: " + sz3);
		iterate(l3.iterator());

		System.out.println("Retaining now all those elements from the sublist...");
		l3.retainAll(l1);
		sz2 = l2.size();
		sz3 = l3.size();
		assertEquals("Retain all not working on sublists", sz0 + l1.size() , sz2);
		assertEquals("Retain all not working on sublists", sz1 + l1.size(), sz3);
		System.out.println("Size of father and son correctly updated");
		System.out.println("l2 size: " + sz2);
		iterate(l2.iterator());
		System.out.println("l3 size: " + sz3);
		iterate(l3.iterator());

		System.out.println("Only ements on the sublist got affected by the invokation on the son");
	}

	/**
     * Test the {@link myAdapter.ListAdapter#indexOf(Object)} {@link myAdapter.ListAdapter#lastIndexOf(Object)},
	 * {@link myAdapter.ListAdapter#add(int, Object)}, {@link myAdapter.ListAdapter#remove(int)}, {@link myAdapter.ListAdapter#set(int, Object)}
	 * methods on sublist 
     * <p>
	 * 									ATTENTION: in the test case documentation is not considered the situation in which the sublist goes from index
	 * 									0 to index size() of the father list. In this situation the test still works but the indexes if the list and sublist
	 * 									ar exactly the same
     * <br><br>Summary: 				Testing how the methods that require an index modify the father list and the sublist after an invokation
	 * 									on one of those
	 * <br><br>Description: 			a sublist of the pre-filled list is created, checking that indexOf() and lastIndexOf() return a different
	 * 									value if invoked on the list or sublist even if the searched element is the same, using add(), remove() and set()
	 * 									at a fixed index and checked that the father list is modified ad the right index
	 * <br><br>Design test:				creating a sublist of the pre-filled one, invoking indexOf() and lastIndexOf() on both lists and checking that
	 * 									the returned index is different even if the element passed is the same. Invoking add(), remove() and set() on
	 *									the sublist ad a fixed index and checking if the main list is modified at the right index
     * <br><br>Preconditions:			sublist() and iterator must be correctly implemented
     * <br><br>Postconditions: 			the index returned by indexOf() and lastIndexOf must be different and based on where the list starts even if
	 * 									the object requested to the lists is the same. When invoked add(), remove(), set() at specified index in the sublist
	 * 									the father list must be modified at his relative index
     * <br><br>Expected results:		two different indexes should be returned from the invokation of indexOf() and lastIndexOf() on both list and sublist
	 * 									when invoked the other methods the father list should be modified at an index which is different from the index
	 * 									passed to the method of the sublist.
	 * 									
     */	
	@Test
	public void testSublistsIndexing(){
		System.out.println("----- Test Indexing In Sublists -----");
		
		l2.add("CapitainAmerica");
		l2.add("Ironman");
		l2.add("Spiderman");
		System.out.println("l2 size: " + l2.size());
		iterate(l2.iterator());

		System.out.println("Making a sublist from index 2 to 4...");
		int from, to;
		from = 2;
		to = 5;
		l1 = l2.subList(from, to);
		System.out.println("Sublist size: " + l1.size());
		iterate(l1.iterator());

		assertEquals("Index Of method on list not working", 2, l2.indexOf("Hulk"));
		assertEquals("Index Of method on sublist not working", 0, l1.indexOf("Hulk"));
		System.out.println("Index of \"Hulk\" in l2: " + l2.indexOf("Hulk"));
		System.out.println("Index of \"Hulk\" in l1: " + l1.indexOf("Hulk"));
		System.out.println("Even if they're the same object!");

		System.out.println("Adding \"Hulk\" to sublist...");
		l1.add("Hulk");
		System.out.println("l2: ");
		iterate(l2.iterator());
		System.out.println("l1: ");
		iterate(l1.iterator());

		assertEquals("Index Of method on list not working", 5, l2.lastIndexOf("Hulk"));
		assertEquals("Index Of method on sublist not working", l1.size()-1, l1.lastIndexOf("Hulk"));
		System.out.println("Last index of \"Hulk\" in l2: " + l2.lastIndexOf("Hulk"));
		System.out.println("Last index of \"Hulk\" in l1: " + l1.lastIndexOf("Hulk"));
		System.out.println("But they're still the same element!");

		System.out.println("Setting an element at a fixed index in sublist...");
		l1.set(0, null);
		assertEquals("Object set in the wrong place in sublist", null, l1.toArray()[0]);
		assertEquals("Object set in the wrong place in list", null, l2.toArray()[from]);
		System.out.println("l2 after a set() at index = 0 in l1: ");
		iterate(l2.iterator());

		System.out.println("Removing an element at a fixed index in sublist...");
		Object[] arr1, arr2;
		arr1 = l1.toArray();
		arr2 = l2.toArray();
		Object obj = l1.remove(0);
		assertEquals("Object removed from the wrong place in sublist", obj, arr1[0]);
		assertEquals("Object removed from the wrong place in list", obj, arr2[from]);
		System.out.println("l2 after a remove at index = 0 in l1: ");
		iterate(l2.iterator());

		System.out.println("Adding an element at a fixed index in sublist...");
		l1.add(1, null);
		arr1 = l1.toArray();
		arr2 = l2.toArray();
		assertEquals("Object added in the wrong place in sublist", null, arr1[1]);
		assertEquals("Object added in the wrong place in list", null, arr2[from+1]);
		System.out.println("l2 after a add at index = 0 in l1: ");
		iterate(l2.iterator());
	}

	/**
     * Test the {@link myAdapter.ListAdapter#subList(int, int)} method invoked on another sublist
     * <p>
     *
     * <br><br>Summary: 				Testing the sublist() method when invoked on a sublist
     * <br><br>Description: 			a list is filled with elements, then the sublist(int, int) method is invoked at first on the list and then
	 * 									on the sublist obtained from the previous invocation and so on until the sublist size is 0
	 * <br><br>Design test:				filling an empty list and creating a sublist from index 1 to size()-1 of the father list. 
	 * 									Doing the same exact thing on the sublist obtained from the previous iteration and checking that the current
	 * 									sublist is of the correct size and the printing through iterator is correct
     * <br><br>Preconditions:			add() and size() should be correctly implemented
     * <br><br>Postconditions: 			the sublist's size must be decreased by two units at every iteration
     * <br><br>Expected results:		after every iteration the sublist's size should be decreased by two units, compared to the father size(), and
	 * 									the elements printed should be the same as the parent but without the first and last element
     */	
	@Test
	public void testRecursiveSublist()
	{
		System.out.println("---- TestRecursive SubListing ----");

		System.out.println("l1 size: " + l1.size());
		assertEquals("List Starts not empty", l1.size(), 0);
		int prev = l1.size();
		System.out.println("Filling list...");
		for(int i=0;i<argv.length;i++)
		{
			l1.add(argv[i]);
		}
		assertEquals("List add not working correctly", l1.size(), (prev + argv.length));
		System.out.println("l1 new size: " + l1.size());
		prev = l1.size();
		System.out.println("Adding same elements again...");
		for(int i=0;i<argv.length;i++)
		{
			l1.add(argv[i]);
		}
		assertEquals("List add not working correctly", l1.size(), (prev + argv.length));
		System.out.println("l1 new size: " + l1.size());
		System.out.println(l1.size());
		prev = l1.size();
		System.out.println("...and again...");
		for(int i=0;i<argv.length;i++)
		{
			l1.add(argv[i]);
		}
		assertEquals("List add not working correctly", l1.size(), (prev + argv.length));
		System.out.println("l1 new size: " + l1.size());
		iterate(l1.iterator());

		int after = 0;
		int count = 0;
		System.out.println("Making iterative sublists, from 1 to size()-1 ot the father list...");
		System.out.println("Basically shorting the list of one element at the beginning and at the end until \n getting the middle element");
		while(l1.size()>=2){
			count++;
			prev = l1.size();
			l1 = l1.subList(1, prev-1);
			after = l1.size();
			System.out.println("Sublist size: " + after);
			assertEquals("Iterative Sublisting not working at " + count + " iteration", after, (prev-2));
			iterate(l1.iterator());
		}
	}

	/**
     * Test the {@link myAdapter.ListAdapter#add(Object)}, {@link myAdapter.ListAdapter#add(int, Object)}, {@link myAdapter.ListAdapter#remove(Object)},
	 * {@link myAdapter.ListAdapter#remove(int)} method invoked on a sublist of a sublist
     * <p>
     *
     * <br><br>Summary: 				Testing add() and remove() methods invoked on a sublist of a sublist
     * <br><br>Description: 			a sublist of the main list is created, then a sublist of the other one is created, add() and remove() methods
	 * 									are invoked on the deeper one. Verified that the main list and the higher sublist are correclty modified
	 * <br><br>Design test:				invoking the sublist() method on the pre-filled list from indx 1 to size()-1, then the same exact method is invokated
	 * 									on the sublist, in order to have a sublist of the sublist. It is added an element to the deepest sublist and it
	 * 									is verified that the fathers are correctly modified, this happens for both add() and remove() methods with and
	 * 									without index as a parameter
     * <br><br>Preconditions:			sublist() and iterators should be correctly implemented
     * <br><br>Postconditions: 			in all the three lists an item must be insert, than deleted, than insert again ad a specified index, and at the
	 * 									and deleted again, their sizes must correctly increase and decrease  
     * <br><br>Expected results:		after the first add a null object should be insert in all the lists and their sizes should increase by one, then
	 * 									the element should be removed and the sizes decreased. This exact behaviour is expected also with the add() and
	 * 									remove() methods with a specified index
     */	
	@Test
	public void testDeepRecursiveSublist(){
		System.out.println("---- Test Deep Recursive SubListing ----");
		
		l2.add("CapitainAmerica");
		l2.add("Ironman");
		l2.add("Spiderman");
		l2.add("Thor");
		System.out.println("l2 size: " + l2.size());
		iterate(l2.iterator());

		System.out.println("Making a sublist of main list from index 1 to size()-1");
		HList l3 = l2.subList(1, l2.size()-1);
		System.out.println("l3 size: " + l3.size());
		iterate(l3.iterator());

		System.out.println("Making a sublist of the sublist from index 1 to size()-1");
		HList l4 = l3.subList(1, l3.size()-1);
		System.out.println("l4 size: " + l4.size());
		iterate(l4.iterator());

		int sz2, sz3, sz4;
		sz2 = l2.size();
		sz3 = l3.size();
		sz4 = l4.size();

		System.out.println("Adding an element to the deeper sublist...");
		l4.add(null);
		assertEquals("Add not working correctly", sz4+1, l4.size());
		assertEquals("Add don't increase correctly father's size", sz3+1, l3.size());
		assertEquals("Add don't increase correctly father's father size", sz2+1, l2.size());
		System.out.println("l4 size: " + l4.size());
		iterate(l4.iterator());
		System.out.println("l3 size: " + l3.size());
		iterate(l3.iterator());
		System.out.println("l2 size: " + l2.size());
		iterate(l2.iterator());

		System.out.println("Removing that element from the deeper sublist...");
		l4.remove(null);
		assertEquals("remove not working correctly", sz4, l4.size());
		assertEquals("remove don't increase correctly father's size", sz3, l3.size());
		assertEquals("remove don't increase correctly father's father size", sz2, l2.size());
		System.out.println("l2 size: " + l2.size());
		iterate(l2.iterator());
		System.out.println("l3 size: " + l3.size());
		iterate(l3.iterator());
		System.out.println("l4 size: " + l4.size());
		iterate(l4.iterator());	

		System.out.println("Adding an element to the deeper sublist at a fixed index...");
		l4.add(1, null);
		assertEquals("Add not working correctly", sz4+1, l4.size());
		assertEquals("Add don't increase correctly father's size", sz3+1, l3.size());
		assertEquals("Add don't increase correctly father's father size", sz2+1, l2.size());
		System.out.println("l2 size: " + l2.size());
		iterate(l2.iterator());
		System.out.println("l3 size: " + l3.size());
		iterate(l3.iterator());
		System.out.println("l4 size: " + l4.size());
		iterate(l4.iterator());		
		

		System.out.println("Removing that element from the deeper sublist...");
		l4.remove(1);
		assertEquals("remove not working correctly", sz4, l4.size());
		assertEquals("remove don't increase correctly father's size", sz3, l3.size());
		assertEquals("remove don't increase correctly father's father size", sz2, l2.size());
		System.out.println("l2 size: " + l2.size());
		iterate(l2.iterator());
		System.out.println("l3 size: " + l3.size());
		iterate(l3.iterator());
		System.out.println("l4 size: " + l4.size());
		iterate(l4.iterator());	
	}

	/**
     * Test the {@link myAdapter.ListAdapter.Iterator} class
     * <p>
     *
     * <br><br>Summary: 				Testing all methods of the Iterator class
     * <br><br>Description: 			an iterator on a list is created, the list is printed and cleaned through the iterator
	 * <br><br>Design test:				creating the iterator on the pre-filled list, scanning the list with the hasNext() method and printing the elements
	 * 									with next(), the correct order of the elements is checked using the toArray() method of the list.
	 * 									Then creating a new iterator and deleting the content of the list using the hasNext() and next() methods
	 * 									to scan it and delete() method to delete the current element
     * <br><br>Preconditions:			toArray() and size() should be correctly implemented
     * <br><br>Postconditions: 			the next() method should return the element after the current position of the iterator, hasNext() must return fals
	 * 									only if the iterator is at the end of the list, true otherwise, remove() must delete the element that has been returned
	 * 									by the previous next() call.
     * <br><br>Expected results:		after the first cycle the entire content of the list should be printed, after the second one the list should
	 * 									be empty.
     */	
	@Test
	public void testIterator(){
		System.out.println("---- Test Iterator ----");

		Object[] arr = l2.toArray();
		System.out.println("Printing list through iterator...");
		HIterator it = l2.iterator();
		
		int i=0;
		while(it.hasNext()){
			Object tmp = it.next();
			assertEquals("Iterator now working", arr[i], tmp);
			System.out.print(tmp + " ");
			i++;
		}
		System.out.println();

		System.out.println("Deleting list through iterator...");
		it = l2.iterator();
		while(it.hasNext()){
			it.next();
			it.remove();
		}
		assertTrue(l2.isEmpty());
		System.out.println("l2 size: " + l2.size());
		iterate(l2.iterator());
	}

	/**
     * Test the {@link myAdapter.ListAdapter.Iterator} class when Exceptions are thrown
     * <p>
     *
     * <br><br>Summary: 				Testing methods of the Iterator class when they should throw an exception
     * <br><br>Description: 			an iterator on a list is created, iterated until the end of the list and tried to call next() then tried to
	 * 									use delete() twice in a row
	 * <br><br>Design test:				creating the iterator on the pre-filled list, scanning the list with the hasNext() method and calling next() 
	 * 									method when the iterator is already at the end of the list, then trying to call twice in a row the delete()
	 * 									method
     * <br><br>Preconditions:			
     * <br><br>Postconditions: 			after calling the last next() an error must be thrown, after calling the second remove() and error must be
	 * 									thrown too
     * <br><br>Expected results:		NoSuchElementException should be thrown after calling next() while iterator is at the end of the list, IllegalStateException
	 * 									should be thrown when remove() is called twice in a row
     */	
	@Test
	public void testIteratorExceptions(){
		System.out.println("---- Test Iterator Exceptions ----");

		HIterator it = l2.iterator();
		while(it.hasNext()) it.next();

		System.out.println("Calling next() when iterator is a the end of the list...");
		try{
			it.next();
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Went out of bounds", NoSuchElementException.class, e.getClass()); 
			System.out.println("NoSuchElementException has been thrown correctly!");
		}

		System.out.println("Calling remove() twice in a row...");	
		it.remove();	
		try{
			it.remove();
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Removed twice", IllegalStateException.class, e.getClass()); 
			System.out.println("IllegalStateException has been thrown correctly!");
		}
	}

	/**
     * Test the {@link myAdapter.ListAdapter.ListIterator} class
     * <p>
     *
     * <br><br>Summary: 				Testing methods of the ListIterator class 
     * <br><br>Description: 			a listiterator on a list is created, iterated until the end of the list and then iterated back to the beginning
	 * 									printing the list backwards. a listiterator is created in the middel of the list and an element is added there
	 *									then another listiterator is created to set the entire list at null objects. a last iterator is created to clean
	 									the content of the list
	 * <br><br>Design test:				creating the iterator on the pre-filled list, scanning the list with the hasNext() method until the end of it and
	 * 									printing it backwards with hasPrevious() and previous() methods. creating a listiterator in the middle of the list
	 * 									and invoking add() to insert a null object there, then setting all the elements of the list at null and cleaning
	 * 									it still through iterators.
     * <br><br>Preconditions:			toArray(), size() and isEmpty() methods of ListAdapter must be well implemented
     * <br><br>Postconditions: 			the first iterator must travell to the end of the list and then print it backwards, previous() must return the element
	 * 									placed before the iterator. the second iterator must add an element in the position before it. the next one must travel
	 * 									to the end of the list and set all the elements at null. the last one must travell to the end of the list too and delete
	 * 									all the elements.
     * <br><br>Expected results:		after the first iteration the list should be printed backwards, then an element should be added in the middle of the list
	 * 									and the list size should increase, after this the iterator should put at null all the elements and at the end the list
	 * 									should be cleaned.
     */	
	@Test
	public void testListIterator(){
		
		System.out.println("---- Test List Iterator ----");

		System.out.println("Adding some elements to the list...");
		l2.add("Hulk");
		l2.add("Thanos");
		l2.add("Loki");
		l2.add("CapitainAmerica");

		System.out.println("Creating the list iterator from beginning...");
		HListIterator it = l2.listIterator();
		System.out.println("l2 size: " + l2.size());
		iterate(it);
		int sz = l2.size();

		while(it.hasNext()) it.next();

		Object[] arr = l2.toArray();
		System.out.println("Printing list backwards through list iterator...");
		int i = arr.length - 1;
		while(it.hasPrevious()){
			Object tmp = it.previous();
			assertEquals("Previous returned wrong element", arr[i], tmp);
			System.out.print(tmp + " ");
			i--;
		}
		System.out.println();

		System.out.println("Creating a list iterator in the middle of the list...");
		it = l2.listIterator(l2.size()/2);
		assertEquals("Iterator in the wrong place", l2.size()/2 + 1 , it.nextIndex());
		assertEquals("Iterator in the wrong place", l2.size()/2 - 1 , it.previousIndex());

		System.out.println("Adding an element through the iterator...");
		it.add(null);	
		it = l2.listIterator();
		assertEquals("Size not updated", sz+1, l2.size());

		System.out.println("Creating another list iterator at the beginning and setting the entire list at null objects...");
		it = l2.listIterator();
		it.next();
		while(it.hasNext()){
			it.set(null);
			it.next();
		}
		iterate(l2.listIterator());

		System.out.println("Creating another list iterator to clean the list...");
		it = l2.listIterator();
		while(it.hasNext()){
			it.next();
			it.remove();
		}
		assertTrue("List hasn't been cleared", l2.isEmpty());
		iterate(l2.listIterator());

	}

	/**
     * Test the {@link myAdapter.ListAdapter.ListIterator} class when exceptions are thrown
     * <p>
     *
     * <br><br>Summary: 				Testing methods of the ListIterator class that navigate the list and throw Exceptions
     * <br><br>Description: 			trying to create a listiterator starting at illegal indexes, then an iterator is created at the beggining of the
	 * 									list and previous() method is called, another iterator is created at the end of the list and next() is called
	 * <br><br>Design test:				trying to create an iterator at a negative index, then trying to do the same at an index bigger than the list size
	 * 									creating an iterator at the begginning of the list and invoking previous() than doing the same at the end of the list
	 * 									with next()
     * <br><br>Preconditions:			size() method of ListAdapter must be well implemented
     * <br><br>Postconditions: 			all the four iterations must throw an exception
     * <br><br>Expected results:		the two constructors should throw IndexOutOfBoundException than the two other iterations should throw NoSuchElementException
     */	
	@Test
	public void testListIteratorExceptions(){

		System.out.println("---- Test List Iterator Exceptions 1 ----");

		System.out.println("Trying to create an iterator out of the boundarues of the list...");
		HListIterator it;
		try{
			it = l2.listIterator(-1);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Iterator accepted a negative index", IndexOutOfBoundsException.class, e.getClass()); 
			System.out.println("IndexOutOfBoundsException has been thrown correctly for a negative index!");
		}
		try{
			it = l2.listIterator(l2.size() + 1);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Iterator accepted an index bigger than size", IndexOutOfBoundsException.class, e.getClass()); 
			System.out.println("IndexOutOfBoundsException has been thrown correctly for an index bigger than size!");
		}

		System.out.println("Iterator created at the beginning of the list");
		it = l2.listIterator();
		try{
			it.previous();
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Iterator went before index 0", NoSuchElementException.class, e.getClass()); 
			System.out.println("NoSuchElementException has been thrown correctly when triyng to move before index 0!");
		}

		System.out.println("Iterator created at the end of the list");
		it = l2.listIterator(l2.size());
		try{
			it.next();
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Iterator went after end of the list", NoSuchElementException.class, e.getClass()); 
			System.out.println("NoSuchElementException has been thrown correctly when triyng to move beyond end of the list!");
		}

	}
	/**
     * Test the {@link myAdapter.ListAdapter.ListIterator} class when exceptions are thrown
     * <p>
     *
     * <br><br>Summary: 				Testing methods of the ListIterator class that modify the list and throw Exceptions
     * <br><br>Description: 			creating an iterator and trying to modify the list without any further iteration, then calling two times the methods
	 * 									that modify the list without any iteration between them
	 * <br><br>Design test:				creating an iterator and calling remove() and set() methods without any previous iteration. calling add() and then
	 * 									invoking set() and remove() without any iteration in between. calling remove() and the invoking again set() and remove()
	 * 									without any iteration in between
     * <br><br>Preconditions:			
     * <br><br>Postconditions: 			all the methods must throw an exception
     * <br><br>Expected results:		all the methods invokated that modify the list should throw IllegalStateException
     */
	@Test
	public void testListIteratorExceptions2(){
		System.out.println("---- Test List Iterator Exceptions 2 ----");
		
		System.out.println("Creating a list iterator...");
		HListIterator it = l2.listIterator();

		System.out.println("Trying to call remove/set without any iteration before...");

		try{
			it.remove();
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Remove worked without any iteration", IllegalStateException.class, e.getClass()); 
			System.out.println("IllegalStateException has been thrown correctly by remove()!");
		}

		try{
			it.set(null);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Set worked without any iteration", IllegalStateException.class, e.getClass()); 
			System.out.println("IllegalStateException has been thrown correctly by set()!");
		}

		System.out.println("Trying to call two methods that modify the list without any iteration between...");
		System.out.println("Called add()...");
		l1.add(null);
		try{
			it.remove();
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Add and remove worked with no iterations between", IllegalStateException.class, e.getClass()); 
			System.out.println("IllegalStateException has been thrown correctly by remove()!");
		}

		System.out.println("Trying to remove and add without any iteration between...");
		try{
			it.set(null);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Add and set worked with no iterations between", IllegalStateException.class, e.getClass()); 
			System.out.println("IllegalStateException has been thrown correctly by set()!");
		}

		System.out.println("Called remove()...");
		it.next();
		it.remove();
		try{
			it.set(null);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Remove and set worked with no iterations between", IllegalStateException.class, e.getClass()); 
			System.out.println("IllegalStateException has been thrown correctly by set()!");
		}

		try{
			it.remove();
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Remove worked twice with no iterations between", IllegalStateException.class, e.getClass()); 
			System.out.println("IllegalStateException has been thrown correctly by add()!");
		}




	}

	/**
     * Test the default constructor of the ListAdapter class
     * <p>
     *
     * <br><br>Summary: 			test to verify the correct creation of an object of type ListAdapter
     * <br><br>Design test: 		invoking the default costructor and checking if an empty object is created
     * <br><br>Preconditions:		the size() method works correctly
     * <br><br>Postconditions: 		the created object must be empty
     * <br><br>Expected results: 	the size of the created object is zero
     */
	@Test
	public void testDefaultConstructor(){
		System.out.println("----- Testing Constructor With Parameters: -----");

		ListAdapter la = new ListAdapter();
		System.out.println("Created a new empty list");
		assertEquals(0, la.size());
		System.out.println("Size: " + la.size());

	}

	/**
     * Test the constructor with parameter of the ListAdapter class
     * <p>
     *
     * <br><br>Summary: 			test to verify the correct creation of an object of type ListAdapter
	 * 								that contains the same values as the collection passed as a parameter
     * <br><br>Design test: 		using a pre-filled List, the constructor is invoked passing the collection containing
	 * 								the elements as a parameter and the respective arrays obtained with toArray() are compared. 
	 * 								Then testing the case a null object is passed
     * <br><br>Preconditions:		The toArray () method must be working correctly
	 * <br>							Iterators must be working correctly
     * <br><br>Postconditions: 		the created object must be the same as the object passed as a parameter, the constructor with
	 * 								a null pointer as a parameter should throw NUllPointerException.
     * <br><br>Expected results: 	the same elements of the test collection must be present in the main test collection. When passed 
	 * 								a null collection NullPointerException should be thrown
     */
	@Test
	public void testConstructorWithParameters(){
		System.out.println("----- Testing Constructor With Parameters: -----");

		ListAdapter	list2 = new ListAdapter(l2);
		System.out.println("The list based on the previously filled list has been created");
		assertEquals("\n*** constructor with parameter not working ***\n", l2.size(), list2.size());
		System.out.println("Size of new list: " + list2.size());
		assertArrayEquals(l2.toArray(), list2.toArray());
		System.out.println("Pre-filled list: ");
		iterate(l2.iterator());
		System.out.println("Newly created list: ");
		iterate(list2.iterator());

		System.out.println("Trying to pass a null pointer as a parameter...");
		try{
			list2 = new ListAdapter(null);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("\n*** constructor considering null as valid parameter ***\n", NullPointerException.class, e.getClass()); 
			System.out.println("Constructor didn't accept null");
		}
	}
	
	/**
     * Test the {@link myAdapter.ListAdapter#add(Object)} , {@link myAdapter.ListAdapter#add(int, Object)} methods
     * <p>
     *
     * <br><br>Summary: 			test to verify the correct execution of the add() methods
     * <br><br>Design test: 		adding to the empty list the same elements contained in the pre-filled list in the same order,
	 * 								checking if toArray() and size() invoked on both lists return the same results then printing the array
	 * 								obtained the list that has been modified. Adding another element at the previous list at a fixed position
	 * 								and checking if the list has been correclty updated through size() and toArray(), than printing the result
     * <br><br>Preconditions:		toArray() and size() must be working correctly
     * <br><br>Postconditions: 		the obtained array from the modified should contain the same objects in the same order as stored in the array
	 * 								obtained from the pre-filled list, then an item should be added to the first at a fixed index
     * <br><br>Expected results: 	at first the arrays obtained from the two lists must be equals, then the modified one should have one more
	 * 								element at a fixed position
     */
	@Test
	public void testAdd(){
		System.out.println("----- Testing Add -----");

		l1.add("Ironman");
		l1.add("Thor");
		l1.add("Hulk");

		Object[] arr1 = l1.toArray();
		Object[] arr2 = l2.toArray();
		assertArrayEquals("add not working", arr1, arr2);
		assertEquals("add isn't incrementing to", l2.size(), l1.size());

		int s1 = l1.size();
		System.out.println("l1 size: " + s1);
		System.out.print("l1 contains:");
		for(int i=0; i<arr1.length; i++) System.out.print(arr1[i] + " ");
		System.out.println("\n same as l2");

		System.out.println("Adding \"CapitainAmerica\" at index 1");
		l1.add(1, "CapitainAmerica");
		arr1 = l1.toArray();
		int s2 = l1.size();
		assertEquals("add with index not working, wrong element at selected index","CapitainAmerica", arr1[1]);
		assertEquals("size hasn't been updated", s1+1, s2);
		System.out.println("l1 size: " + s2);
		System.out.print("l1 now contains:");
		for(int i=0; i<arr1.length; i++) System.out.print(arr1[i] + " ");
		System.out.println();
	}

	/**
     * Test the {@link myAdapter.ListAdapter#addAll(HCollection)} , {@link myAdapter.ListAdapter#addAll(int, HCollection)} methods
     * <p>
     *
     * <br><br>Summary: 			test to verify the correct execution of the addAll() methods
     * <br><br>Design test: 		adding to the empty list the the pre-filled list and checking if they're equals with the equals() method,
	 * 								also checking if the size has been correctly updated.
	 * 								Then adding another list to the previous one at a fixed index and checking that the parameters have been
	 * 								correctly updated
     * <br><br>Preconditions:		equals(), size() and iterators must be working correctly
     * <br><br>Postconditions: 		after the first addAll() the modified list should contain the same elements as the pre-filled one,
	 * 								then the former should have two more elements inserted at a fixed index.
     * <br><br>Expected results: 	the two lists must be equals after the method is invoked the first tome, then the modified one should have
	 * 								some more elements inserted at a fixed index
     */
	@Test
	public void testAddAll(){

		System.out.println("----- Testing addAll -----");

		System.out.println("Adding the entire pre-filled list to the empty one:");
		l1.addAll(l2);
		assertTrue("addAll not working", l1.equals(l2));
		assertEquals("size hasn't been updated", l2.size(), l1.size());
		System.out.println("Now the lists have the same elements.");
		iterate(l1.iterator());

		System.out.println("Creating a new list containing {\"CapitainAmerica\", \"BlackWidow\"}");
		ListAdapter l3 = new ListAdapter();
		l3.add("CapitainAmerica");
		l3.add("BlackWidow");
		int s1 = l1.size();
		System.out.println("Adding this new list to l1 at index 2");
		l1.addAll(1, l3);
		assertEquals("Size hasn't been updated properly" ,5, s1 + l3.size());
		assertArrayEquals("addAll with index not working", new Object[]{"Ironman", "CapitainAmerica", "BlackWidow", "Thor", "Hulk"}, l1.toArray());
		System.out.println("Now l1 contains " + l1.size() + " elements: ");
		iterate(l1.iterator());
	}

	/**
     * Test the {@link myAdapter.ListAdapter#add(Object)} , {@link myAdapter.ListAdapter#add(int, Object)} methods in critical
	 * situations
     * <p>
     *
     * <br><br>Summary: 			Test that add(Object) and add(int,Object) works fine with a null object and throws an exception when the index
	 * 								is out of bounds
     * <br><br>Design test: 		passing null to both add() methods and checking that everything works, passing illegal indexes and seeing if
	 * 								the method throws the expected exceptions
     * <br><br>Postconditions: 		null should be added do the list in both cases, when using indexes out of bound IndexOutOfBoundException should
	 * 								be thrown
     * <br><br>Expected results: 	the list must have a null element at the specified index and at last index, the other methods should
	 * 								throw IndexOutOfBOundException
     */
	@Test
	public void testAddCriticals(){

		System.out.println("----- Testing Add with critical parameters ----");

		Object obj = null;
		String s = "Loki";
		int big_indx = l2.size();
		int small_indx = -1;

		System.out.println("Trying to pass a null as a parameter to add()...");
		l2.add(obj);
		assertEquals("Add isn't working with null", null, l2.get(l2.size()-1));
		System.out.println("Null has been added correctly");

		System.out.println("Trying to pass a null as a parameter to add() at index 1...");
		l2.add(1, obj);
		assertEquals("Add with index isn't working with null", null, l2.get(1));
		System.out.println("Null has been added correctly at index 1");
		System.out.println("l2 contains:");
		iterate(l2.iterator());

		System.out.println("Trying to pass an index too big...");
		try{
			l1.add(big_indx, s);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Considering an index > size + 1 as valid parameter...", IndexOutOfBoundsException.class, e.getClass()); 
			System.out.println("Correctly, failed.");
		}

		System.out.println("Trying to pass an index too small...");
		try{
			l1.add(small_indx, s);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Considering an index < 0 as valid parameter...", IndexOutOfBoundsException.class, e.getClass()); 
			System.out.println("Correctly, failed.");
		}


	}

	/**
     * Test the {@link myAdapter.ListAdapter#addAll(HCollection)} , {@link myAdapter.ListAdapter#addAll(int, HCollection)} methods in critical
	 * situations
     * <p>
     *
     * <br><br>Summary: 				Testing that addAll(Hcollection) and addAll(int, HCollection) throw the right exceptions
     * <br><br>Design test:				passing a null collection to addAll() with valid indexes, then passing a valid HCollection but with invalid
	 * 									indexes
     * <br><br>Preconditions:
     * <br><br>Postconditions: 			first two cases should throw NullPointerException, the last two should throw IndexOutOfBoundException
     * <br><br>Expected results:		all test cases should throw exceptions, nothing should change in the list
     */	
	@Test
	public void testAddAllCriticals(){
		System.out.println("----- Testing AddAll with critical parameters ----");
		
		HList tmp = null;
		System.out.println("Trying to pass a null pointer as a parameter to addAll()...");
		try{
			l1.addAll(tmp);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Considering null as valid parameter...", NullPointerException.class, e.getClass()); 
			System.out.println("Correctly, failed.");
		}

		System.out.println("Trying to pass a null pointer as a parameter to addAll() with a valid index...");
		try{
			l1.addAll(0, tmp);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Considering null as valid parameter...", NullPointerException.class, e.getClass()); 
			System.out.println("Correctly, failed.");
		}

		int big_indx = l2.size() + 1;
		int small_indx = -1;

		System.out.println("Trying to pass an index too big...");
		try{
			l1.addAll(big_indx, l2);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Considering an index > size + 1 as valid parameter...", IndexOutOfBoundsException.class, e.getClass()); 
			System.out.println("Correctly, failed.");
		}

		System.out.println("Trying to pass an index too small...");
		try{
			l1.addAll(small_indx, l2);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Considering an index > 0 as valid parameter...", IndexOutOfBoundsException.class, e.getClass()); 
			System.out.println("Correctly, failed.");
		}

	}

	/**
     * Test the {@link myAdapter.ListAdapter#clear()} method
     * <p>
     *
     * <br><br>Summary: 			Verify that the method makes the list empty
     * <br><br>Design test:			printing initial size and elements of the list, invoking clear on that list, verify that it makes the list of
	 * 								size 0 and empty
     * <br><br>Preconditions: 		the iterators and size() are implemented correctly
     * <br><br>Postconditions: 		after the call size() should return 0 and iterate() method should print an empty list
     * <br><br>Expected results: 	size of the list must be 0 and and empty list must be printed
     */	
	@Test
	public void testClear(){
		System.out.println("----- Clear Testing -----");

		System.out.println("l2 initial size: " + l2.size());
		System.out.println("l2 contains: ");
		iterate(l2.iterator());

		System.out.println("Cleaning list...");
		l2.clear();

		assertEquals("Clear not working", 0, l2.size());
		System.out.println("l2 actual size: " + l2.size());
		System.out.println("l2 now contains: ");
		iterate(l2.iterator());
	}

	/**Test of {@link myAdapter.ListAdapter#contains(Object)}
     * <p>	
     * <br><br>Summary: 				Verifies that the method returns true if and only if the container contains the specified element
     * <br><br>Design test: 			using a pre-filled list, verification of the existence of an element present, verification of the existence
	 * 									of an element not present. Check with an instance of an object and a null.
     * <br><br>Description: 			adding a null object to the list, printing the liste elements for a better visualisation of che collection,
	 * 									Verify that the method returns true using one of the elements printed as a parameter, false using an element
	 * 									that does not exist in the collection as a parameter, then checking if null is considered a valid parameter
     * <br><br>Preconditions: 			the add(Object) method is correctly implemented
     * <br><br>Postconditions: 			the method must return true if the element passed is actually contained, false otherwise, it should accept a null object
     * <br><br>Expected results: 		true with already presente data and null object, false with data not present
	 */
	@Test
	public void testContains(){
		System.out.println("----- Contains Test -----");

		System.out.println("l2 contains: ");
		iterate(l2.iterator());

		System.out.print("Checking if it contains \"Hulk\": ");
		boolean check = l2.contains("Hulk");
		assertEquals("Contains not working", true, check);
		System.out.println(check);
	
		System.out.print("Checking if it contains \"Loki\": ");
		check = l2.contains("Loki");
		assertEquals("Contains not working", false, check);
		System.out.println(check);
	}

	/**
     * Test of {@link myAdapter.ListAdapter#containsAll(HCollection)}
     * <p>
     * <br><br>Summary: 					the test verifies that the main collection contains all the elements of the collection passed as a parameter
     * <br><br>Design test: 				created a new list with elements of the pre-filled one, added some new elements to the latter and verificated that
	 * 										one contains the other but not vice versa, tested the case of a null parameter
     * <br><br>Description:					a new list is created using the pre-filled list, some elements are added to the pre-filled list, is verified
	 * 										that the methos returns true if the method is invoked on the just created list if the parameter is the pre-filled list, but if invoked
	 * 										vice versa the method returns false, passed a null collection to the method and verified that throws an exception
     * <br><br>Preconditions: 				The add(Object) method must be correctly implemented
     * <br><br>Postconditions: 				the method must return true if the collection contains all the elements present in the collection passed as a parameter
	 * 										and throw an exception when a null collection is passed 
     * <br><br>Expected results: 			If the list contains all the elements contained in the parameter true is returned, if the collection is null
	 * 										NUllPointerException is thrown
     */
    @Test
    public void testContainsAll() {
       
		System.out.println("----- ContainsAll Test -----");

		System.out.println("Creating a new list (l3), based on the pre-filled list (l2)");
        HCollection l3 = new ListAdapter(l2);

		l2.add("Blackwidow");
		l2.add("CapitainAmerica");
		System.out.println("Added {\"Blackwidow\", \"CapitainAmerica\"} to l2");

		System.out.print("Checking if l2 contains all l3... ");
        boolean flag = l2.containsAll(l3);
		assertTrue("Contains all not working", flag);
		System.out.println(flag);

		System.out.print("Checking if l3 contains all l2... ");
        flag = l3.containsAll(l2);
		assertFalse("Contains all not working", flag);
		System.out.println(flag);

		System.out.println("Trying to pass a null collection...");
		try{
			l2.containsAll(null);
			throw new Exception();
		}catch (Exception e) {
            assertEquals("Considering null as a valid parameter", NullPointerException.class, e.getClass()); 
			System.out.println("Correctly, failed.");
		}
    }

	/**
     * Test of {@link myAdapter.ListAdapter#equals(Object)}
     * <p>
     * <br><br>Summary: 					the equals method must return true only if the two collections contain the same elments and in the same order 
     * <br><br>Design test: 				the empty list is filled with the same elements of the pre-filled list.
	 * 										the reflective property is tested for equal lists, non-equal lists are compared and the case with a non-collection object is tested
     * <br><br>Description: 				after the fill of the second list with the same elements contained in the first one they're compared.
	 *										then an element is added to one of them and they're compared again. the list is cleared and re-filled with the same elements as the other list
	 *										but in a different order and they're compared again. it's also tested the case a non-collection object is passed as a parameter
     * <br><br>Preconditions: 				the add(Object) and clear() methods must be correctly implemented
     * <br><br>Postconditions: 				the method must return true if the two containers have the same elements arranged in the same order and must return false
	 * 										if the two objects are incompatible for a comparison
     * <br><br>Expected results: 			true only in the first case described and false for the others
     */
	@Test
	public void testEquals(){

		System.out.println("----- Equals Test -----");
		System.out.println("Adding to l1 same elements contained in l2 in same order...");
		l1.add("Ironman");
		l1.add("Thor");
		l1.add("Hulk");
		boolean flag = l2.equals(l1);
		System.out.print("Checking if l2 equals l1: ");
		assertTrue("Equals not working", flag);
		System.out.println(flag);
		flag = l1.equals(l2);
		System.out.print("Checking if l1 equals l2: ");
		assertTrue("Equals not working", flag);
		System.out.println(flag);
		System.out.println();

		System.out.println("Adding an item to l1...");
		l1.add("Thanos");

		flag = l2.equals(l1);
		System.out.print("Checking if l2 equals l1 after the update: ");
		assertFalse("Equals not working", flag);
		System.out.println(flag);
		System.out.println();

		System.out.println("Clearing l1, the adding the same elements of l2 but in a different order...");
		l1.clear();
		l1.add("Thor");
		l1.add("Hulk");
		l1.add("Ironman");
		flag = l2.equals(l1);
		System.out.print("Checking if l2 equals l1: ");
		assertFalse("Equals not working", flag);
		System.out.println(flag);

		System.out.println();
		System.out.print("Checking if l1 equals to an object which is not a collection: ");
		String s = "Loki";
		flag = l2.equals(s);
		assertFalse("Equals not working", flag);
		System.out.println(flag);

	}

	/**
     * Test of {@link myAdapter.ListAdapter#get(int)}
     * <p>
     * <br><br>Summary: 				test to verify the correct return parameter of the get function
     * <br><br>Design test: 			in the pre-filled list check that each element is reachable thorough its index, testing also the case an
	 * 									illegal index is passed
     * <br><br>Description: 			the list is printed to display it's content, a for loop is called to verify that each element is correctly returned
	 * 									by the get method, then two illegal indexes are passed to see the method behaviour
     * <br><br>Preconditions: 			iterators and toArray() method are correctly implemented
     * <br><br>Postconditions: 			the element returned must be the one contained in the i-th position of the list, and index too big or to small
	 * 									should make the method throw in exception
     * <br><br>Expected results: 		the for loop ends correctly and prints the content of the list, the illegal indexes should make the method
	 * 									throw IndexOutOfBoundException
     */
	@Test
	public void testGet(){
		
		System.out.println("----- Get Test -----");

		System.out.println("l2 contains: ");
		iterate(l2.iterator());

		for(int i=0; i<l2.size(); i++){
			System.out.print("Element at " + i + ": ");
			assertEquals("get not working...", l2.toArray()[i], l2.get(i));
			System.out.println(l2.get(i));
		}

		System.out.println("Getting the element at index = size()...");
		try{
			l2.get(l2.size());
			throw new Exception();
		} catch(Exception e){
			assertEquals("Accepting an illegal index...", IndexOutOfBoundsException.class, e.getClass());
			System.out.println("Error, correct!");
		}

		System.out.println("Getting the element at index = -1 ...");
		try{
			l2.get(-1);
			throw new Exception();
		} catch(Exception e){
			assertEquals("Accepting an illegal index...", IndexOutOfBoundsException.class, e.getClass());
			System.out.println("Error, correct!");
		}
	}

	/**
     * Test of {@link myAdapter.ListAdapter#hashCode()}
     * <p>
     * <br><br>Summary: 				Test the hashcode method
     * <br><br>Design test: 			is tested the case of an empty list, than the same elements of the pre-filled list are added to the empty one and this case
	 * 									is tested, at last the order of the elements are changed and so should be the hashcode
     * <br><br>Description: 			after having entered the data contained in the pre-filled list in the empty list the calculated hash must be the same.
	 * 									the hash calculation of the list in which has been changed the order must be different
     * <br><br>Preconditions: 			the hash calculation must be the one described in HList, add(Object) method must work correctly
     * <br><br>Postconditions:			only if the compared objects have the same elements arranged in the same order, the hash must be identical
     * <br><br>Expected results: 		the cases in which the hash of the two objects match is exactly when the content and order of the elements of the
	 * 									two objects are the same
     */
	@Test
	public void testHashCode(){
		System.out.println("----- HashCode Test -----");

		System.out.print("HashCode of l1, which is void: ");
		assertEquals("hashcode not working", 1, l1.hashCode());
		System.out.println(l1.hashCode());
		
		System.out.println("Adding same elements contained in the pre-filled list l2:");
		l1.clear();
		l1.add("Ironman");
		l1.add("Thor");
		l1.add("Hulk");
		assertEquals("Equals list but differen hashcode...",l1.hashCode(),l2.hashCode());
		System.out.println("l1 hashcode: " + l1.hashCode());
		System.out.println("l2 hashcode: " + l2.hashCode());
		int h1 = l1.hashCode();

		System.out.println("Cleaning l1 and adding same elements but in a different order:");
		l1.clear();
		l1.add("Ironman");
		l1.add("Hulk");
		l1.add("Thor");
		assertNotEquals("hash code should have changed", h1, l1.hashCode());
		System.out.println("l1 hashcode: " + l1.hashCode());
		
	}

	/**
     * Test of {@link myAdapter.ListAdapter#indexOf(Object)}
     * <p>
     * <br><br>Summary:						Test for the method that looks for the position of the first element corresponding to the element passed as a parameter
     * <br><br>Design test: 				looking for a non-existing element, an existing element, a duplicated element, a null elemente. Search also with empty
	 * 										list
     * <br><br>Description: 				at first looking for a non-existing element, than for an existing one. duplicating that element and looking for it again,
	 * 										testing a null object and than testing on a void and a null list.
     * <br><br>Preconditions:				the list object must not be null (but can be empty)
     * <br><br>Postconditions: 				the index of the first matching element found is returned, otherwise -1
     * <br><br>Expected results: 			-1 for empty list and element not found; index of the position of the single element, searched; index of the position of the
	 * 										first element of the list found. NullPointerException for a null list.
     */
	@Test
	public void testIndexOf(){
		System.out.println("----- Index Of Test -----");

		System.out.println("l2 contains: ");
		iterate(l2.iterator());

		System.out.println("Looking for \"Loki\"...");
		assertEquals(-1, l2.indexOf("Loki"));
		System.out.println("Loki is at index " + l2.indexOf("Loki") + " so he isn't here");

		System.out.println("Looking for \"Thor\"...");
		assertEquals(1, l2.indexOf("Thor"));
		System.out.println("Thor is at index " + l2.indexOf("Thor"));
		int i = l2.indexOf("Thor");

		System.out.println("Adding another \"Thor\" to the list");
		l2.add("Thor");
		System.out.println("Looking for \"Thor\" again...");
		assertEquals(i, l2.indexOf("Thor"));
		System.out.println("Thor is still at index " + l2.indexOf("Thor"));

		System.out.println("Testing a null object");
		assertEquals(-1, l2.indexOf(null));
		System.out.println("Correctly not found");

		System.out.println("Trying to find \"Thor\" on the empty list l1...");
		assertEquals(-1, l1.indexOf("Thor"));
		System.out.println("Correctly not found here");

		System.out.println("Trying to find \"Thor\" on a null list...");
		ListAdapter l3 = null;
		try{
			l3.indexOf("Thor");
			throw new Exception();
		} catch(Exception e){
			assertEquals("Accepting an illegal index...", NullPointerException.class, e.getClass());
		}
		System.out.println("Error, correct!");
	}

	/**
     * Test of {@link myAdapter.ListAdapter#isEmpty()}
     * <p>
     * <br><br>Summary: 				Testing the method isEmpty()
     * <br><br>Design test: 			testing on an empty list, then adding an element and testing it again, the list is cleared and the method called
	 * 									another time
     * <br><br>Description: 			at first it's invoked on an empty list, after adding a data item it is verified that the container is not indicated
	 * 									as empty, then after the clearing it is verified that the container is correctly considered empty
     * <br><br>Preconditions: 			the add(Object) and clear() methods must be properly implemented and working
     * <br><br>Postconditions: 			the method must return true if the container is empty and therefore does not contain any elements
     * <br><br>Expected results: 		false after insert, true at first and after the cleaning
     */
	@Test
	public void testIsEmpty(){
		System.out.println("----- isEmpty Test -----");

		System.out.println("Invoking method on the empty list...");
		assertTrue("isEmpty not working", l1.isEmpty());
		System.out.println("l1 is empty: " + l1.isEmpty());

		System.out.println("Adding an element and then invoking it again...");
		l1.add("Hulk");
		assertFalse("isEmpty not working", l1.isEmpty());
		System.out.println("l1 is empty: " + l1.isEmpty());
		
		System.out.println("Clearing the list and invoking it another time...");
		l1.clear();
		assertTrue("isEmpty not working", l1.isEmpty());
		System.out.println("l1 is empty: " + l1.isEmpty());
	}

	/**
     * Test of {@link myAdapter.ListAdapter#lastIndexOf(Object)}
     * <p>
     * <br><br>Summary: 				Test for the method that looks for the position of the last element corresponding to the element passed as a parameter
     * <br><br>Design test: 			looking for a non-existing element, an existing element, a duplicated element, a null elemente. Search also with empty
	 * 									list
     * <br><br>Description: 			at first looking for a non-existing element, than for an existing one. duplicating that element and looking for it again,
	 * 									adding two null objects and testing that situation and than testing on a void and a null list.
     * <br><br>Preconditions: 			the list object must not be null (but can be empty)
     * <br><br>Postconditions: 			the index of the last matching element found is returned, otherwise -1
     * <br><br>Expected results:		-1 for empty list and element not found; index of the position of the single element, searched; index of the position of the
	 * 									last element of the list found. NullPointerException for a null list.
     */
	@Test
	public void testLastIndexOf(){
		System.out.println("----- Last Index Of Test -----");

		System.out.println("l2 contains: ");
		iterate(l2.iterator());

		System.out.println("Looking for \"Loki\"...");
		assertEquals(-1, l2.lastIndexOf("Loki"));
		System.out.println("Loki is at index" + l2.indexOf("Loki") + "so he isn't here");

		System.out.println("Looking for \"Thor\"...");
		assertEquals(1, l2.lastIndexOf("Thor"));
		System.out.println("Thor's last appearance is at index " + l2.lastIndexOf("Thor"));
		int i = l2.indexOf("Thor");

		System.out.println("Adding another \"Thor\" to the list");
		l2.add("Thor");
		System.out.println("Looking for \"Thor\" again...");
		assertEquals(l2.size()-1, l2.lastIndexOf("Thor"));
		System.out.println("Thor's last appearance is now at index " + l2.lastIndexOf("Thor"));

		System.out.println("Adding two null objects at the end");
		l2.add(null);
		l2.add(null);
		assertEquals(l2.size()-1, l2.lastIndexOf(null));
		System.out.println("Works also with null objects!");

		System.out.println("Trying to find \"Thor\" on the empty list l1...");
		assertEquals(-1, l1.indexOf("Thor"));
		System.out.println("Correctly not found here");

		System.out.println("Trying to find \"Thor\" on a null list...");
		ListAdapter l3 = null;
		try{
			l3.indexOf("Thor");
			throw new Exception();
		} catch(Exception e){
			assertEquals("Accepting an illegal index...", NullPointerException.class, e.getClass());
		}
		System.out.println("Error, correct!");
	}


 	/**
     * Test of {@link myAdapter.ListAdapter#remove(Object)}
     * <p>
     * <br><br>Summary: 					Verifies that the tested method removes the specified element or returns false if it is not present
     * <br><br>Description: 				both with empty collection and with non-existent element, the tested method is invoked verifying that it returns
	 * 										the expected result.
     * <br><br>Design test: 				removing an existing element and checkin if the parameters are correctly modified, removing a non-existing element from
	 * 										the list then deleting the same element from the empty list, added and removed a null object from the list
	 * <br><br>Preconditions: 				the add(Object obj) method and the iterators method must be correctly implemented
     * <br><br>Postconditions: 				after invoking the method, the collection must not contain the data entered as a parameter, returning true,
	 * 										if the collection is not modified it returns false
     * <br><br>Expected results: 			after the removal of the specified element, the size of the collection must be decreased by one unit,
	 * 										the first element corresponding to the entered parameter must have been removed and true must be returned, with data not present,
	 * 										however, false
     */
	@Test
	public void testRemoveObject(){
		System.out.println("----- Remove Object Test -----");
		System.out.println("l2 contains: ");
		iterate(l2.iterator());
		int sz = l2.size();

		System.out.println("Removing \"Thor\" from the list");
		assertTrue("remove not working", l2.remove("Thor"));
		assertEquals("Size not updated", sz-1, l2.size());
		System.out.println("Now l2 contains " + l2.size() + "elements");

		System.out.println("Removing \"Loki\" from the list");
		assertFalse("remove not working", l2.remove("Loki"));
		System.out.println("Nothing changed because \"Loki\" isn't in the list");

		System.out.println("Removing \"Loki\" from the empty list...");
		assertFalse("remove not working well", l1.remove("Loki"));
		System.out.println("Nothing changed because \"Loki\" isn't in the list");

		System.out.println("Adding and removing a null object from the list");
		l2.add(null);
		assertTrue("remove not working", l2.remove(null));
		System.out.println("Everything went okay");
	}

	/**
     * Test of {@link myAdapter.ListAdapter#remove(int)}
     * <p>
     * <br><br>Summary:						Verifies that the tested method removes and returns the element at the specified index
     * <br><br>Description: 				both with empty collection and with non-existent element, the tested method is invoked verifying that it returns
	 * 										the expected result.
     * <br><br>Design test: 				removing all the elements in a list and printing it in the correct order, while the list is empty the method is invoked
	 * 										again with an index too small and one too big
	 * <br><br>Preconditions: 				the get(int) method and the iterators method must be correctly implemented
     * <br><br>Postconditions: 				after invoking the method, the collection must not contain the data at the specified index, returning the remove element,
     * <br><br>Expected results: 			after the removal of the specified index, the relvative element must be returned and the size must be decreased by one
	 * 										if the index is out of bounds an error should be thrown
     */
	@Test
	public void testRemoveIndex(){
		System.out.println("----- Remove At Index Test -----");
		System.out.println("l2 contains: ");
		iterate(l2.iterator());
		
		System.out.println("Cleaning and printing the list through remove(int) method");
		int sz = l2.size();
		for(int i=0; i<sz; i++){
			Object tmp = l2.get(0);
			assertEquals("remove didn't work as expected", tmp, l2.remove(0));
			System.out.println(i + ": " + tmp);
		}
		assertEquals("Size not updated", 0, l2.size());
		System.out.println("Now l2 has " + l2.size() + " elements");


		System.out.println("Trying to use an index too big...");
		try{
			l2.remove(l2.size());
			throw new Exception();
		} catch(Exception e){
			assertEquals("Accepting an illegal index...", IndexOutOfBoundsException.class, e.getClass());
		}
		System.out.println("Error, correct!");

		System.out.println("Trying to use a negative index...");
		try{
			l2.remove(-1);
			throw new Exception();
		} catch(Exception e){
			assertEquals("Accepting an illegal index...", IndexOutOfBoundsException.class, e.getClass());
		}
		System.out.println("Error, correct!");
	}

	/**
     * Test of {@link myAdapter.ListAdapter#removeAll(HCollection)}
     * <p>
     * <br><br>Summary: 					the method that removes all the elements contained in the collection passed as parameter from the main collection is tested
	 * <br><br>Description:					tested the situation in which a null collection is passed, after verification of correct operation using empty collection,
	 * 										some elements of the pre-filled collection are added to the prameter one and another element not present. After checking that the returned
	 * 										value is true, the same exact method is called again in now it returns false. In the pre-filled list a duplicated element is added to see
	 * 										if the method removes all the instances of a specified element.
     * <br><br>Design test: 				check that if a null collection is passed an exception is thrown, see that an empty collection doesn't modify the list,
	 *										see if the collection contains some elements of the list the method returns true and it deletes all the instances of an object and then testing
	 *										that the method returns false if the list doesn't contain any of the elems in the collection.
	 * <br><br> Preconditions: 				The add(Object) and toArray() methods must be correctly implemented
     * <br><br>Postconditions: 				in the main collection there must be no elements contained in the collection passed as a parameter, an exception should be
	 * 										thrown in the case of a null collection as a parameter
     * <br><br>Expected results: 			false if an empty collection is passed or if the collection doesn't contain any element of the list,
	 *										true if at least one element is removed
     */
	@Test
	public void testRemoveAll(){
		System.out.println("----- RemoveAll Test -----");

		System.out.println("Trying to pass a null collection...");
        try {
            l2.removeAll(null);
            throw new Exception();
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
			System.out.println("Error, correct!");
        }

		l2.add("Hulk");

		System.out.println("l2 contains: ");
		iterate(l2.iterator());

		System.out.println("Passing an empty collection...");
		boolean flag = l2.removeAll(l1);
		assertFalse("remove all didn't work", flag);
		assertArrayEquals(new Object[]{"Ironman", "Thor", "Hulk", "Hulk"}, l2.toArray());
		System.out.println("l2 changed: " + flag);

		System.out.println("Adding {\"Ironman\", \"Hulk\", \"Loki\"} to l1...");
		l1.add("Ironman");
		l1.add("Hulk");
		l1.add("Loki");

		System.out.println("Removing all elements from l1 in l2.");
		flag = l2.removeAll(l1);
		assertTrue("remove all didn't work", flag);
		assertArrayEquals(new Object[]{"Thor"}, l2.toArray());
		System.out.println("l2 changed: " + flag);

		System.out.println("Invoking the same method again...");
		flag = l2.removeAll(l1);
		assertFalse("remove all didn't work", flag);
		assertArrayEquals(new Object[]{"Thor"}, l2.toArray());
		System.out.println("l2 changed: " + flag);
	}

	/**
     * Test of {@link myAdapter.ListAdapter#retainAll(HCollection)}
     * <p>
     * <br><br>Summary: 					the method that retains all the elements contained in the collection passed as parameter from the main collection is tested
	 * <br><br>Description: 				tested the situation in which a null collection is passed, verificating that if the same exact collection is passe ad a
	 * 										parameter nothing is modified, than testing when the parameter collection contains some of the main list elements and one that is not contained,
	 * 										testing the case of an empty list.
     * <br><br>Design test: 				check that if a null collection is passed an exception is thrown, see that if the parameter has the same elements of the 
	 * 										main list that doesn't modify this, see if the collection contains some elements of the list the method returns true and it retains all the
	 * 										instances of an object and then testing that the list gets cleared when an empty list is passed.
	 * <br><br> Preconditions: 				The add(Object), toArray() and isEmpty() methods must be correctly implemented
     * <br><br>Postconditions: 				in the main collection there must all the elements contained in the collection passed as a parameter, an exception should be
	 * 										thrown in the case of a null collection as a parameter
     * <br><br>Expected results: 			false only if the parameter collection is equals to the main collection, an exception should be thrown if a null coll
	 * is passed
     */
	@Test
	public void testRetainAll(){
		System.out.println("----- RetainAll Test -----");

		System.out.println("Trying to pass a null collection...");
        try {
            l2.retainAll(null);
            throw new Exception();
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
			System.out.println("Error, correct!");
        }

		l2.add("Hulk");

		System.out.println("l2 contains: ");
		iterate(l2.iterator());

		System.out.println("Creating a list l3 that is equal to l2...");
		ListAdapter l3 = new ListAdapter(l2);

		System.out.println("Retaining elements from l3 in l2.");
		boolean flag = l2.retainAll(l3);
		assertFalse("retain all didn't work", flag);
		assertArrayEquals(new Object[]{"Ironman", "Thor", "Hulk", "Hulk"}, l2.toArray());
		System.out.println("l2 changed: " + flag);

		System.out.println("Adding {\"Ironman\", \"Hulk\", \"Loki\"} to l1...");
		l1.add("Ironman");
		l1.add("Hulk");
		l1.add("Loki");

		System.out.println("Retaining elements from l1 in l2.");
		flag = l2.retainAll(l1);
		assertTrue("retain all didn't work", flag);
		assertArrayEquals(new Object[]{"Ironman", "Hulk", "Hulk"}, l2.toArray());
		System.out.println("l2 changed: " + flag);
		System.out.println("l2 contains: ");
		iterate(l2.iterator());

		l1.clear();
		System.out.println("Passing an empty collection...");
		flag = l2.retainAll(l1);
		assertTrue("remove all didn't work", flag);
		assertArrayEquals(new Object[]{}, l2.toArray());
		System.out.println("l2 changed: " + flag);

		System.out.println("Now l2 should be empty: ");
		assertTrue(l2.isEmpty());
		System.out.println("l2 contains: ");
		iterate(l2.iterator());
	}

	/**
     * Test of {@link myAdapter.ListAdapter#set(int, Object)}
     * <p>
     * <br><br>Summary: 					The method must correctly set all elements of the list and return the substituted element
	 * <br><br>Description: 				an object and a null object are insert in the pre-filled list and the correctness of the insertion is checked controlling
	 * 										that the element in the array returned by the toArray() method matches the object past to the method, the same thing, using the array obtained
	 *  									calling toArray() before the set(Object) method, is done to check if the previously contained element is correclty returned. Is also tested the
	 * 										Exceptions throwing in case of an illegal index
     * <br><br>Design test: 				after the invocation  of set method the list must contain a different element at the specified index and the previously
	 * 										contained one must be returned, this situation is also tested with a null object. Then two illegal indexes are passed and en exception should
	 * 										be thrown. 
     * <br><br>Preconditions: 				The implementation of the add(Object) and toArray() methods is correct
     * <br><br>Postconditions:				the main list is different than it was before the invocation of the set method and the previously contained element is
	 * 										returned ar every invocation
     * <br><br>Expected results: 			the element in the obtained array of the list must matche the element set by the method and the returned object must
	 * 										match the element in the obtained array of the list at that index
     */
	@Test
	public void testSet(){
		System.out.println("----- Set Test -----");

		System.out.println("l2 contains: ");
		iterate(l2.iterator());

		Object[] old = l2.toArray();
		System.out.println("Setting \"Thor\" at index 0");
		Object obj = l2.set(0, "Thor");
		assertEquals("Set not working as expected", "Thor", l2.toArray()[0]);
		assertEquals("Returned wrong element", old[0], obj);

		old = l2.toArray();
		System.out.println("Now trying with null object at index 2");
		obj = l2.set(2, null);
		assertEquals("Set not working as expected", null, l2.toArray()[2]);
		assertEquals("Returned wrong element", old[2], obj);

		System.out.println("Trying to use a negative index...");
		try{
			l2.set(-1, "Thor");
			throw new Exception();
		} catch(Exception e){
			assertEquals("Accepting an illegal index...", IndexOutOfBoundsException.class, e.getClass());
			System.out.println("Error, correct!");
		}

		System.out.println("Trying to use an index = size()...");
		try{
			l2.set(l2.size(), "Thor");
			throw new Exception();
		} catch(Exception e){
			assertEquals("Accepting an illegal index...", IndexOutOfBoundsException.class, e.getClass());
			System.out.println("Error, correct!");
		}

	}

	/**
     * Test of {@link myAdapter.ListAdapter#size()}
     * <p>
     * <br><br>Summary: 					Method test that returns the current size of the Collection
     * <br><br>Design test: 				test of the size of the collection before and after adding an element and its removal,
     * 										increasing and decreasing the value of the size of the collection, then checking that it don't change after invoking set method. Tested the case
	 * 										of an empty list
     * <br><br>Description: 				The size method is invoked after adding an element, after removing an element and after the set method. In order the size
	 * 										should increase, then decrease and at the end should not change. Tested that an empty list's size is 0
     * <br><br>Preconditions: 				the add(), remove() and set() methods must be properly implemented
     * <br><br>Postconditions: 				the returned value must correspond to the number of elements contained in the collection
     * <br><br>Expected results:			0 if collection is empty, otherwise the number of elements present in the collection.
     * 										The size of the collection must vary consistently as the insertion/removal elements vary
     */
	@Test
	public void testSize(){
		System.out.println("----- Size Test -----");
		
		System.out.println("l2 contains: ");
		iterate(l2.iterator());

		assertEquals("Size not working", 3, l2.size());
		System.out.println("l2 size: " + l2.size());
		
		System.out.println("Adding an element to the list..");
		int sz = l2.size();
		l2.add(null);
		assertEquals("Size didn't update", sz+1, l2.size());
		System.out.println("l2 size: " + l2.size());
		System.out.println("Size correctly updated!");	

		System.out.println("Removing an element from the list..");
		sz = l2.size();
		l2.remove(null);
		assertEquals("Size didn't update", sz-1, l2.size());
		System.out.println("l2 size: " + l2.size());
		System.out.println("Size correctly updated!");

		System.out.println("Invoking set on the list..");
		sz = l2.size();
		l2.set(0, null);
		assertEquals("Size modified", sz, l2.size());
		System.out.println("l2 size: " + l2.size());
		System.out.println("Size correctly didn't change!");

		System.out.println("Test on an empty list..");
		assertEquals("Size don't work", 0, l1.size());
		System.out.println("l2 size: " + l2.size());

	}

	/**
     * Test the {@link myAdapter.ListAdapter#toArray()} method
     * <p>
     *
     * <br><br>Summary: 					test to verify the correct execution of the toArray() method
     * <br><br>Design test: 				using a pre-filled List, toArray() is invoked on it, the obtained array is compared
	 * 										to the expected result and then is printed, through the iterator, the actual content of the list
     * <br><br>Preconditions:				Iterators must be working correctly
     * <br><br>Postconditions: 				the obtained array must contain the same objects in the same order as stored in the list 
     * <br><br>Expected results: 			the array printed is exactly the same as the list printed with the iterator
     */
	@Test
	public void testToArray(){
		System.out.println("----- Testing toArray() -----");

		Object[] arr = l2.toArray();
		assertArrayEquals(new Object[]{"Ironman", "Thor", "Hulk"}, arr);
		System.out.print("Printing array: \n{");
		for(int i=0; i<arr.length; i++) System.out.print(arr[i] + "; ");
		System.out.println( "}");

		System.out.println("Printing with iterator: ");
		iterate(l2.iterator());

	}

	/**
     * Test of {@link myAdapter.ListAdapter#toArray(Object[])}
     * <p>
     * <br><br>Summary: 						comparable test with the toArray () method with the difference that, if it is large enough, the array passed as a parameter is used
     * <br><br>Design Test: 					invokation on an empty list and an array is passed as a parameter, some elements are inserted into the collection and an array with
	 * 											of the same size is passed as a parameter,some elements are inserted into the collection and a partially occupied array with a smaller size is passed as a parameter.
	 * 											Also test of the correct throw of the exception with null parameter
     * <br><br>Description:						for all three situations indicated by the design test, it is verified with the use of a manually created array that the tested method returns an
	 * 											array of Object, filled with null the greater the difference between the size of the array passed as parameter and the size of the collection, exactly the elements of the collection, or a new array always with the elements of the collection
     * <br><br>Preconditions: 					The add(Object) method must be correctly implemented
     * <br><br>Postconditions: 					the returned array must contain all the elements present in the collection on which the method is invoked and in the order with
	 * which they appear in the collection
     * <br><br>Expected results: 				the method must return the array passed as a parameter with the objects of the collection, if large enough, otherwise
	 * 											return another one with a size equal to the number of objects in the collection. If collection empty, the array supplied as a parameter is not
	 * 											modified
     */
	@Test
	public void testToArrayWithParameters(){
		
		System.out.println("----- Testing toArray with Parameters -----");

		Object[] arr = null;
		System.out.println("Using a null array as a parameter");
		try{
			l1.toArray(arr);
			throw new Exception();
		} catch(Exception e){
			assertEquals("Accepting a null array", NullPointerException.class, e.getClass());
			System.out.println("Error, correct!");
		}

		System.out.println("Invoking method on an empty list...");
		arr = new Object[]{"A", "B", "C"};
		arr = l1.toArray(arr);
		assertArrayEquals("to array not working", new Object[]{null,null,null}, arr);
		System.out.println("The array now contains: ");
		for(int i = 0; i<arr.length; i++) System.out.println(arr[i] + " ");

		System.out.println("Invoking method on a list as big as the array...");
		arr = l2.toArray(arr);
		assertArrayEquals("to array not working", new Object[]{"Ironman", "Thor", "Hulk"}, arr);
		System.out.println("The array now contains: ");
		for(int i = 0; i<arr.length; i++) System.out.println(arr[i] + " ");

		l2.add("Blackwidow");
		System.out.println("Invoking method on a list bigger than the array...");
		arr = l2.toArray(arr);
		assertArrayEquals("to array not working", new Object[]{"Ironman", "Thor", "Hulk", "Blackwidow"}, arr);
		System.out.println("The array now contains: ");
		for(int i = 0; i<arr.length; i++) System.out.println(arr[i] + " ");
	}

	public static void iterate(HIterator iter)
	{
		System.out.print("{");
		while(iter.hasNext())
		{
			System.out.print(iter.next() + "; ");
		}
		System.out.println("}");
	}
}
