package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class JptHolderNode extends JptParentNode
{
	private final boolean isInSlot;

	JptHolderNode(boolean isInSlot)
	{
		this.isInSlot = isInSlot;
	}

	@Override
	public void render(Map<String, Object> context, Writer out) throws IOException
	{
		int child_count = getChildCount();
		for (int i = 0; i < child_count; i++)
		{
			JptNode jpt_node = getChild(i);
			jpt_node.render(context, out);
		}
	}

	@Override
	public boolean isInSlot()
	{
		return isInSlot;
	}
}