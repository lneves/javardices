package org.caudexorigo.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Implements a list which can be divided into several pages. There are several methods like moveToPage(int page) or moveToLastPage() for page-oriented navigation in the list. The objects on the current page can be retrieved by calling iterator(). <br>
 * This class is very handy to implement resultlists, where users can page through a list of objects. <br>
 * NOTE: Page counting starts with zero. Zero is the first page!
 * 
 * @author ORCA Systems GmbH, Martin Schaefer
 * @author <a href="mailto:tresch@orcasys.ch">Anatole Tresch </a>
 * @created 15. November 2002
 * @version $Revision: 1.4 $ $Date: 2003/03/07 10:18:21 $
 */
public class PagedList<T> extends ArrayList<T>
{

	private static final long serialVersionUID = 3216432562834765287L;

	/**
	 * The default number of objects contained in one page *
	 */
	private static final int INITIAL_PAGESIZE = 10;

	/**
	 * The number of objects contained in one page *
	 */
	private int pageSize = INITIAL_PAGESIZE;

	/**
	 * The current number of pages *
	 */
	private int pageCount = 0;

	/**
	 * The number of the current page *
	 */
	private int currentPage = 0;

	/**
	 * Signal flag for list modifications *
	 */
	private boolean listModified = false;

	/**
	 * Flag to enable/disable paging *
	 */
	private boolean pagingEnabled = true;

	/**
	 * Creates a new empty list with a default page-size of 10 and paging enabled.
	 */
	public PagedList()
	{
		this(INITIAL_PAGESIZE);
	}

	/**
	 * Creates a new empty list with the given page-size and paging enabled.
	 * 
	 * @param pageSize
	 *            Page size to use for the list.
	 */
	public PagedList(int pageSize)
	{
		super();
		setPageSize(pageSize);
	}

	/**
	 * Creates a new empty list with the given page-size and initial capacity. Paging is enabled by default.
	 * 
	 * @param initialCapacity
	 *            The initial capacity of the list.
	 * @param pageSize
	 *            Page size to use for the list.
	 */
	public PagedList(int initialCapacity, int pageSize)
	{
		super(initialCapacity);
		setPageSize(pageSize);
	}

	/**
	 * Creates a new list containing the objects of the specified collection with a default page-size of 10 and paging enabled.
	 * 
	 * @param collection
	 *            The collection whose objects are to be placed into this list.
	 */
	public PagedList(Collection<T> collection)
	{
		this(collection, INITIAL_PAGESIZE);
	}

	/**
	 * Creates a new list containing the objects of the specified collection with a default page-size of 10 and paging enabled.
	 * 
	 * @param collection
	 *            The collection whose objects are to be placed into this list,
	 * @param pageSize
	 *            Page size to use for the list.
	 */
	public PagedList(Collection<T> collection, int pageSize)
	{
		super(collection);
		setPageSize(pageSize);
		listModified = true;
	}

	/**
	 * Calculates the internal values for paging. This method called on demand when objects have been added or removed to/from the list.
	 */
	protected void calculatePager()
	{
		if (listModified)
		{
			listModified = false;
			// Calculate the new page count
			pageCount = size() / pageSize;
			if ((size() % pageSize) != 0)
			{
				pageCount++;
			}
			// For zero page count reset current page
			if (pageCount == 0)
			{
				currentPage = 0;
			}
			// Make sure, the current page is inside the new range
			else if (currentPage >= pageCount)
			{
				currentPage = pageCount - 1;
			}
		}
	}

	/**
	 * Return an iterator for the objects contained on the current page. If paging is disabled, the iterator will contain all objects.
	 * 
	 * @return Iterator with objects
	 */
	public Iterator<T> iterator()
	{
		if (this.pagingEnabled)
		{
			calculatePager();
			ArrayList<T> pageList = new ArrayList<T>(pageSize);
			int max;
			int start = pageSize * currentPage;
			if (size() > start + pageSize)
			{
				max = start + pageSize;
			}
			else
			{
				max = size();
			}
			for (int i = start; i < max; i++)
			{
				pageList.add(get(i));
			}
			return pageList.iterator();
		}
		else
		{
			return super.iterator();
		}
	}

	/**
	 * Return an iterator cintaining all objects from the list.
	 * 
	 * @return Iterator with objects
	 */
	public Iterator iteratorAll()
	{
		return super.iterator();
	}

	/**
	 * Appends the specified object to the end of the list. All paging values are adjusted according to the new list size.
	 * 
	 * @param object
	 *            The object to add
	 * @return Description of the Return Value
	 */
	public boolean add(T object)
	{
		boolean flag = super.add(object);
		listModified = true;
		return flag;
	}

	/**
	 * Inserts the specified object at the specified position in the list. All paging values are adjusted according to the new list size.
	 * 
	 * @param index
	 *            The position to insert the object
	 * @param object
	 *            The object to insert
	 */
	public void add(int index, T object)
	{
		super.add(index, object);
		listModified = true;
	}

	/**
	 * Appends all of the objects in the specified collection to the end of the list. All paging values are adjusted according to the new list size.
	 * 
	 * @param collection
	 *            The collectioen containing the objects to append.
	 * @return Description of the Return Value
	 */
	public boolean addAll(Collection<? extends T> collection)
	{
		boolean flag = super.addAll(collection);
		listModified = true;
		return flag;
	}

	/**
	 * Inserts all of the objects in the specified collection beginning at the specified position. All paging values are adjusted according to the new list size.
	 * 
	 * @param index
	 *            The position to insert the objects
	 * @param collection
	 *            The collectioen containing the objects to append.
	 * @return Description of the Return Value
	 */
	public boolean addAll(int index, Collection<? extends T> collection)
	{
		boolean flag = super.addAll(index, collection);
		listModified = true;
		return flag;
	}

	/**
	 * Remove an object
	 * 
	 * @param index
	 *            Position of the object to be removed
	 * @return The object removed or null
	 */
	public T remove(int index)
	{
		T removed = super.remove(index);
		listModified = true;
		return removed;
	}

	/**
	 * // * Remove a range of objects // * // * @param fromIndex // * start index // * @param toIndex // * end index //
	 */
	// protected void removeRange(int fromIndex, int toIndex)
	// {
	// removeRange(fromIndex, toIndex);
	// listModified = true;
	// }

	/**
	 * Remove the given object
	 * 
	 * @param object
	 *            The object to be removed
	 * @return TRUE if the objects was removed.
	 */
	public boolean remove(Object object)
	{
		boolean flag = super.remove(object);
		listModified = true;
		return flag;
	}

	/**
	 * Remove all objects contained within the given collection
	 * 
	 * @param collection
	 * @return TRUE if the objects were removed
	 */
	public boolean removeAll(Collection collection)
	{
		boolean flag = super.remove(collection);
		listModified = true;
		return flag;
	}

	/**
	 * Remove all objects but the given ones
	 * 
	 * @param collection
	 *            The objects to be retained
	 * @return TRUE if the operation succeeded
	 */
	public boolean retainAll(Collection collection)
	{
		boolean flag = super.retainAll(collection);
		listModified = true;
		return flag;
	}

	/**
	 * Clear all data from the list
	 */
	public void clear()
	{
		super.clear();
		this.currentPage = 0;
		this.pageCount = 0;
	}

	/**
	 * Sets the page size. <br>
	 * NOTE: The counting starts with zero. Zero is the first page!
	 * 
	 * @param pageSize
	 *            The new pageSize value
	 */
	public void setPageSize(int pageSize)
	{
		if (pageSize > 0)
		{
			this.pageSize = pageSize;
			listModified = true;
		}
	}

	/**
	 * Returns the current page-size, that means the number of lines per page.
	 * 
	 * @return The nubmer of pages
	 */
	public int getPageSize()
	{
		return pageSize;
	}

	/**
	 * Returns true, if the current page is the last page
	 * 
	 * @return TRUE if paging is enabled
	 */
	public boolean isPagingEnabled()
	{
		return this.pagingEnabled;
	}

	/**
	 * Enables/disables the paging mode.
	 * 
	 * @param pagingEnabled
	 *            Flag to enabled or disable the paging mode
	 */
	public void setPagingEnabled(boolean pagingEnabled)
	{
		this.pagingEnabled = pagingEnabled;
	}

	/**
	 * Returns the number of the current page
	 * 
	 * @return The number of the current page
	 */
	public int getCurrentPage()
	{
		calculatePager();
		return currentPage;
	}

	/**
	 * Returns the total number of pages.
	 * 
	 * @return The number of pages.
	 */
	public int getPageCount()
	{
		calculatePager();
		return pageCount;
	}

	/**
	 * Sets the current page. NOTE: Page counting starts with zero. Zero is the first page!
	 * 
	 * @param pageNumber
	 *            Description of the Parameter
	 * @return true if the page was moved. false if the page was not moved because it would be outside the valid range.
	 */
	public boolean moveToPage(int pageNumber)
	{
		calculatePager();
		if (pageNumber < pageCount && pageNumber >= 0)
		{
			this.currentPage = pageNumber;
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Moves to the next page.
	 * 
	 * @return true if the page was moved. false if the page was not moved because it would be after the last page.
	 */
	public boolean moveToNextPage()
	{
		return moveToPage(currentPage + 1);
	}

	/**
	 * Moves to the previous page.
	 * 
	 * @return true if the page was moved. false if the page was not moved because it would be before the first page.
	 */
	public boolean moveToPreviousPage()
	{
		return moveToPage(currentPage - 1);
	}

	/**
	 * Moves to the first page
	 * 
	 * @return true if the page was moved. false if the page was not moved because it was already on the first page.
	 */
	public boolean moveToFirstPage()
	{
		calculatePager();
		if (pageCount < 2)
		{
			return false;
		}
		if (currentPage != 0)
		{
			currentPage = 0;
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Moves to the last page.
	 * 
	 * @return true if the page was moved. false if the page was not moved because it was already on the last page.
	 */
	public boolean moveToLastPage()
	{
		calculatePager();
		if (pageCount < 2)
		{
			return false;
		}
		int oldPage = currentPage;
		currentPage = pageCount - 1;
		if (currentPage < 0)
		{
			currentPage = 0;
		}
		return (currentPage != oldPage);
	}

	/**
	 * Returns true, if the current page is the first page.
	 * 
	 * @return The onFirstPage value
	 */
	public boolean isOnFirstPage()
	{
		calculatePager();
		return (currentPage == 0 || pageCount < 2);
	}

	/**
	 * Returns true, if the current page is the last page.
	 * 
	 * @return The onLastPage value
	 */
	public boolean isOnLastPage()
	{
		calculatePager();
		return (currentPage == (pageCount - 1) || pageCount < 2);
	}

	/**
	 * Test and example app
	 * 
	 * @param args
	 *            The command line arguments
	 */

	/*
	 * public static void main(String[] args) { System.out.println(">>>>>> Start Test"); // Create the list with given page size PagedList pagedList = new PagedList(5); // Add some objects int i = 0; while (i < 12) { pagedList.add(new Integer(i++)); } System.out.println("List Size:................." + pagedList.size()); System.out.println("Page Count:................" + pagedList.getPageCount()); pagedList.moveToPage(1); System.out.println("Items on page 1:..........."); Iterator iterator = pagedList.iterator(); while (iterator.hasNext()) { Object item = iterator.next(); System.out.println(item.toString()); }
	 * 
	 * pagedList.moveToFirstPage(); System.out.println("Items on first page:......."); iterator = pagedList.iterator(); while (iterator.hasNext()) { Object item = iterator.next(); System.out.println(item.toString()); }
	 * 
	 * pagedList.moveToLastPage(); System.out.println("Items on last page:........"); iterator = pagedList.iterator(); while (iterator.hasNext()) { Object item = iterator.next(); System.out.println(item.toString()); } // Add more objects while (i < 21) { pagedList.add(new Integer(i++)); } System.out.println("List Size:................." + pagedList.size()); System.out.println("Page Count:................" + pagedList.getPageCount());
	 * 
	 * pagedList.moveToLastPage(); System.out.println("Items on last page:........"); iterator = pagedList.iterator(); while (iterator.hasNext()) { Object item = iterator.next(); System.out.println(item.toString()); } System.out.println(">>>>>> End Test"); }
	 */

}