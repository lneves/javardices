package org.caudexorigo.ds;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Convenient implementation of a Last In First Out (LIFO) stack. This implementation differs from the one in java.util.Stack in two ways.
 * 
 * First, like most of the collection APIs, it is unsynchronized for better performance when synchronization is not required. If a synchronized stack is required, you can use the {@link java.util.Collections#synchronizedCollection(java.util.Collection) Collections.synchronizedCollection()} method to retrieve a synchronized instance.
 * 
 * Second, it does not expose its internal implementation via its superclass. Extending <tt>AbstractCollection</tt> instead of <tt>Vector</tt> allows objects of this class to be used interchangably with other collection framework classes without exposing its internal implementation.
 * 
 */
public final class Stack<E> extends AbstractCollection<E>
{
	private ArrayList<E> _list;

	/**
	 * Construct a stack with no additional arguments.
	 */
	public Stack()
	{
	}

	/**
	 * Construct a stack initialized to contain all the items in the specified collection. The items will be pushed on to the stack in their order in the collection.
	 * 
	 * @param c
	 *            a collection of items to push onto the stack.
	 */
	public Stack(Collection<E> c)
	{
		addAll(c);
	}

	/**
	 * Gets (or lazily instantiates) the list implementation used by this stack.
	 * 
	 * @return a list.
	 */
	private List<E> getList()
	{
		if (_list == null)
		{
			_list = new ArrayList<E>();
		}
		return _list;
	}

	/**
	 * Gets whether there are more elements on the stack.
	 * 
	 * @return true if there are no more elements on the stack.
	 */
	public boolean isEmpty()
	{
		return _list == null || _list.isEmpty();
	}

	/**
	 * Pushes an element onto the stack.
	 * 
	 * @param o
	 *            an element to push onto the stack
	 * @return true if the stack changed as a result of this operation.
	 */
	public boolean push(E o)
	{
		return getList().add(o);
	}

	/**
	 * Obtains the top element on the stack without removing it.
	 * 
	 * @return the top element on the stack.
	 */
	public E peek()
	{
		if (isEmpty())
		{
			throw new EmptyStackException();
		}
		List<E> l = getList();
		return l.get(l.size() - 1);
	}

	/**
	 * Replaces the top of the stack with the specified object.
	 * 
	 * @param o
	 *            the object to replace the top of the stack with.
	 */
	public void replace(E o)
	{
		if (isEmpty())
		{
			throw new EmptyStackException();
		}

		List<E> l = getList();
		l.set(l.size() - 1, o);
	}

	/**
	 * Pops the top element off the stack and returns it.
	 * 
	 * @return the old top element
	 */
	public E pop()
	{
		if (isEmpty())
		{
			throw new EmptyStackException();
		}
		List<E> l = getList();
		int lastIndex = l.size() - 1;

		E theValue = l.get(lastIndex);
		l.remove(lastIndex);

		if (l.isEmpty())
		{
			_list = null;
		}

		return theValue;
	}

	/**
	 * Gets an iterator for elements on the stack. This iterator starts at the bottom of the stack and proceeds to the top.
	 * 
	 * @return an iterator over stack elements.
	 */
	public Iterator<E> reverseIterator()
	{
		if (isEmpty())
		{
			return Collections.EMPTY_LIST.iterator();
		}
		return new ReverseListIterator<E>(_list);
	}

	// java.util.Collection implementation

	public boolean add(E o)
	{
		return push(o);
	}

	public void clear()
	{
		getList().clear();
	}

	/**
	 * Gets an iterator for elements on the stack. The iterator starts at the top of the stack and proceeds to the bottom of the stack.
	 * 
	 * @return an iterator over stack elements.
	 */
	public Iterator<E> iterator()
	{
		if (isEmpty())
		{
			return Collections.EMPTY_LIST.iterator();
		}
		return _list.iterator();
	}

	public int size()
	{
		return getList().size();
	}

	/**
	 * Iterator that traverses a list in reverse order. It does this by just adapting the ListIterator of the list.
	 */
	private final static class ReverseListIterator<E> implements Iterator<E>
	{
		private final ListIterator<E> _listIterator;

		public ReverseListIterator(List<E> list)
		{
			_listIterator = list.listIterator(list.size());
		}

		public boolean hasNext()
		{
			return _listIterator.hasPrevious();
		}

		public E next()
		{
			return _listIterator.previous();
		}

		public void remove()
		{
			_listIterator.remove();
		}

	}
}
