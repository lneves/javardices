package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class JptDocument extends JptParentNode
{
	private String _ctx_object_type;

	JptDocument(String ctx_object_type)
	{
		_ctx_object_type = ctx_object_type;
	}

	public void render(Map<String, Object> context, Writer out) throws IOException
	{
		int child_count = getChildCount();
		for (int i = 0; i < child_count; i++)
		{
			JptNode jpt_node = getChild(i);

			jpt_node.render(context, out);

		}
	}

	boolean isDocument()
	{
		return true;
	}

	public boolean isInSlot()
	{
		return false;
	}

	public String getCtxObjectType()
	{
		return _ctx_object_type;
	}
}
