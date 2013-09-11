package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class JptStaticFragment extends JptNode
{
	private char[] _value;

	public JptStaticFragment(String data)
	{
		_value = data.toCharArray();
	}

	public int getChildCount()
	{
		return 0;
	}

	public JptNode getChild(int position)
	{
		throw new IndexOutOfBoundsException("Static Fragments do not have children");
	}

	public void render(Map<String, Object> context, Writer out) throws IOException
	{
		out.write(_value, 0, _value.length);
	}

	boolean isStaticFragment()
	{
		return true;
	}

	public boolean isInSlot()
	{
		return false;
	}
}
