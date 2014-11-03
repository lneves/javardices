package org.caudexorigo.jpt;

import java.util.Map;

public abstract class JptParentNode extends JptNode
{

	JptNode children[];
	int childCount;
	String actualBaseURI;

	JptParentNode()
	{
		childCount = 0;
	}

	public int getChildCount()
	{
		return childCount;
	}

	public void insertChild(JptNode child, int position)
	{
		_insertChild(child, position);
	}

	final void _insertChild(JptNode child, int position)
	{
		insertionAllowed(child, position);
		fastInsertChild(child, position);
	}

	void fastInsertChild(JptNode child, int position)
	{
		checkCapacity(childCount + 1);
		if (position < childCount)
			System.arraycopy(children, position, children, position + 1, childCount - position);
		children[position] = child;
		childCount++;
		child.setParent(this);
	}

	private void checkCapacity(int position)
	{
		if (children == null)
			children = new JptNode[1];
		else if (position >= children.length)
		{
			JptNode data[] = new JptNode[children.length * 2];
			System.arraycopy(children, 0, data, 0, children.length);
			children = data;
		}
	}

	void insertionAllowed(JptNode child, int position)
	{
		if (child == null)
			throw new NullPointerException("Tried to insert a null child in the document");
		if (child.getParent() != null)
			throw new IllegalStateException("Child already has a parent.");
		else
			return;
	}

	public void appendChild(JptNode child)
	{
		insertChild(child, childCount);
	}

	public JptNode getChild(int position)
	{
		/*
		 * if (children == null) throw new IndexOutOfBoundsException("This node has no children"); else
		 */
		return children[position];
	}

	public int indexOf(JptNode child)
	{
		if (children == null)
			return -1;
		for (int i = 0; i < childCount; i++)
			if (child == children[i])
				return i;
		return -1;
	}

	public JptNode removeChild(int position)
	{
		if (children == null)
			throw new IndexOutOfBoundsException("This node has no children");
		JptNode removed = children[position];
		int toCopy = childCount - position - 1;
		if (toCopy > 0)
			System.arraycopy(children, position + 1, children, position, toCopy);
		childCount--;
		children[childCount] = null;
		removed.setParent(null);
		return removed;
	}

	final boolean isParentNode()
	{
		return true;
	}
	
	protected void checkAllowed(Map<String, Object> context, String variable)
	{
		if (context.get(variable) != null)
		{
			throw new IllegalStateException(String.format("Variable '%s' is already in context", variable));
		}
	}
}