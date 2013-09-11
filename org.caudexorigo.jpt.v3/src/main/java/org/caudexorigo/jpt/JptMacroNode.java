package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JptMacroNode extends JptParentNode
{

	private boolean _isInSlot;

	private String _ctx_object_type;

	public JptMacroNode(String ctx_object_type, boolean isInSlot)
	{
		_ctx_object_type = ctx_object_type;
		_isInSlot = isInSlot;
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}

	boolean isMacroNode()
	{
		return true;
	}

	public void render(Map<String, Object> context, Writer out) throws IOException
	{

		Map<String, Object> macroContext = new HashMap<String, Object>();
		try
		{
			Set<String> keys = context.keySet();
			for (String key : keys)
			{
				if (key.equals("$this"))
				{
					macroContext.put("$parent", context.get(key));
				}
				else
				{
					macroContext.put(key, context.get(key));
				}

			}

			Object oCtx = Class.forName(_ctx_object_type).newInstance();
			macroContext.put("$this", oCtx);
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}

		int child_count = getChildCount();
		for (int i = 0; i < child_count; i++)
		{
			JptNode jpt_node = getChild(i);
			if (jpt_node.isInSlot())
				jpt_node.render(context, out);
			else
				jpt_node.render(macroContext, out);
		}
	}
}
