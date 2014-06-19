package org.caudexorigo.jpt;

import java.util.HashMap;

import nu.xom.Document;
import nu.xom.ProcessingInstruction;

import org.caudexorigo.text.StringUtils;

public class ContextBuilder
{
	public ContextBuilder()
	{
	}

	public static Object buildObjectFromInstructions(Document doc)
	{
		String ctx_obj_class = objectNameFromInstructions(doc);
		return buildObjectFromName(ctx_obj_class);
	}

	public static String objectNameFromInstructions(Document doc)
	{
		HashMap<String, String> _processingInstructions = new HashMap<String, String>();
		for (int i = 0; i < doc.getChildCount(); i++)
		{
			nu.xom.Node child = doc.getChild(i);
			if (!(child instanceof ProcessingInstruction))
				continue;
			ProcessingInstruction pi = (ProcessingInstruction) child;
			if (!pi.getTarget().equals("jpt"))
				continue;
			String data[] = StringUtils.split(pi.getValue());
			for (int j = 0; j < data.length; j++)
			{
				String data_fragments[] = StringUtils.split(data[j], "=");
				_processingInstructions.put(data_fragments[0], StringUtils.remove(data_fragments[1], '"').trim());
			}
			break;
		}
		String ctx_obj_class = (String) _processingInstructions.get("template-class");
		if (ctx_obj_class == null)
			ctx_obj_class = "java.lang.Object";
		return ctx_obj_class;
	}

	public static Object buildObjectFromName(String ctx_obj_class)
	{

		Class<?> ctx_obj = null;
		Object ctx = null;
		try
		{
			ctx_obj = Class.forName(ctx_obj_class);
			ctx = ctx_obj.newInstance();
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException(e);
		}
		return ctx;
	}

	public static Class<?> buildClassFromName(String ctx_obj_class)
	{
		Class<?> ctx_obj = null;

		try
		{
			ctx_obj = Class.forName(ctx_obj_class);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException(e);
		}
		return ctx_obj;
	}
}
