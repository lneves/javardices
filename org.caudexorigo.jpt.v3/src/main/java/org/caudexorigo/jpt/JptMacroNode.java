package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JptMacroNode extends JptParentNode
{
	private static final Logger log = LoggerFactory.getLogger(JptMacroNode.class);

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
		Map<String, Object> macroContext = new HashMap<String, Object>();

		Set<Entry<String, String>> param_entries = _macroParams.entrySet();

		for (Entry<String, String> entry : param_entries)
		{
			if ("macro".equals(entry.getKey()))
			{
				continue;
			}

			// System.out.println("JptMacroNode.render.paramName: " + entry.getKey());
			// System.out.println("JptMacroNode.render.paramValue: " + entry.getValue());

			try
			{
				Object value = MVEL.eval(entry.getValue().toString(), context);
				// System.out.println("JptMacroNode.parsedParamVvalue: " + value);
				macroContext.put(entry.getKey(), value);
			}
			catch (org.mvel2.PropertyAccessException t)
			{
				log.warn(String.format("PropertyAccessException for parameter value: '%s'. Using the value as string", entry.getValue()));
				// System.out.printf("PropertyAccessException for parameter value: '%s'. Using the value as string%n", entry.getValue());
				macroContext.put(entry.getKey(), entry.getValue());
			}
		}

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

		try
		{
			Object oCtx = Class.forName(_ctx_object_type).newInstance();
			macroContext.put("$this", oCtx);
			// System.out.println("JptMacroNode.render.macroContext: " + macroContext);
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
