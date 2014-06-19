package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class JptIncludeNode extends JptNode
{
	private String _path;

	private boolean _isInSlot;

	JptIncludeNode(String path, boolean isInSlot)
	{
		_isInSlot = isInSlot;
		_path = path;
	}

	public int getChildCount()
	{
		return 0;
	}

	public JptNode getChild(int i)
	{
		throw new IndexOutOfBoundsException("Includes do not have children");
	}

	public void render(Map<String, Object> context, Writer out) throws IOException
	{
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}

	public String getPath()
	{
		return _path;
	}
}
