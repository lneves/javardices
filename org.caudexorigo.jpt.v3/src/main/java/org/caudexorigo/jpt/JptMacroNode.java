package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.mvel2.MVEL;

public class JptMacroNode extends JptParentNode
{

	private boolean _isInSlot;

	private String _ctx_object_type;

	private final Map<String, String> _macroParams;

	public JptMacroNode(String ctx_object_type, boolean isInSlot, Map<String, String> macroParams)
	{
		_ctx_object_type = ctx_object_type;
		_isInSlot = isInSlot;
		_macroParams = macroParams;
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
		System.out.println("JptMacroNode.render.context: " + context);

		Map<String, Object> macroContext = new HashMap<String, Object>();

		Set<Entry<String, String>> param_entries = _macroParams.entrySet();

		for (Entry<String, String> entry : param_entries)
		{
			System.out.println("JptMacroNode.render.paramName: " + entry.getKey());
			System.out.println("JptMacroNode.render.paramValue: " + entry.getValue());

			if (entry.getValue().startsWith("$this."))
			{
				Object value = MVEL.eval(entry.getValue(), context);
				System.out.println("JptMacroNode.parsedParamVvalue: " + value);
				macroContext.put(entry.getKey(), value);
			}
			else
			{
				macroContext.put(entry.getKey(), entry.getValue());
			}
		}

		try
		{
			Set<String> keys = context.keySet();

			System.out.println("JptMacroNode.render.keys: " + keys);
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

			System.out.println("JptMacroNode.render.macroContext: " + macroContext);
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
