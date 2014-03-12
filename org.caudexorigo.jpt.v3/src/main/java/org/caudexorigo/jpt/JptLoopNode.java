package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.caudexorigo.ErrorAnalyser;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

public class JptLoopNode extends JptParentNode
{
	private boolean _isInSlot;

	private RepeatElements _repeatElements;

	private Serializable _compiled_exp;

	JptLoopNode(RepeatElements repeatElements, boolean isInSlot)
	{
		_repeatElements = repeatElements;
		_isInSlot = isInSlot;
	}

	public void render(Map<String, Object> context, Writer out) throws IOException
	{
		if (_compiled_exp == null)
		{
			ParserContext parser_context = ParserContext.create();

			Set<Entry<String, Object>> ctx_entries = context.entrySet();

			for (Entry<String, Object> entry : ctx_entries)
			{
				parser_context.addInput(entry.getKey(), entry.getValue().getClass());
			}
			// Compile the expression.
			_compiled_exp = MVEL.compileExpression(_repeatElements.getLoopSourceExpression(), parser_context);
		}

		int child_count = getChildCount();
		int increment = _repeatElements.getLoopIncrement();
		String loopVar = _repeatElements.getLoopVar();
		String pad = _repeatElements.getPadding();

		try
		{
			Object collection = MVEL.executeExpression(_compiled_exp, context);

			if (collection == null)
			{
				arrayLoop(context, out, new Object[0], child_count, increment, loopVar, pad);
				return;
			}

			if (collection.getClass().isArray())
			{
				arrayLoop(context, out, (Object[]) collection, child_count, increment, loopVar, pad);
				return;
			}

			if (collection instanceof Collection)
			{
				collectionLoop(context, out, (Collection) collection, child_count, increment, loopVar, pad);
				return;
			}

			if (collection instanceof Iterable)
			{
				iterableLoop(context, out, (Iterable) collection, child_count, increment, loopVar, pad);
				return;
			}

			if (collection instanceof Iterator)
			{
				iteratorLoop(context, out, (Iterator) collection, child_count, increment, loopVar, pad);
				return;
			}

			arrayLoop(context, out, new Object[0], child_count, increment, loopVar, pad);
		}
		catch (Throwable t)
		{
			Throwable r = ErrorAnalyser.findRootCause(t);
			throw new RuntimeException(String.format("Error processing JptLoopNode:%nexpression: '%s';%ncontext: %s;%nmessage: '%s'", loopVar, context, r.getMessage()));
		}
	}

	private void arrayLoop(Map<String, Object> context, Writer out, Object[] items, int child_count, int increment, String loopVar, String pad) throws IOException
	{

		for (int n = 0; n < items.length; n = n + increment)
		{
			context.put(loopVar, items[n]);
			context.put("$index", n + 1);
			context.put("$length", items.length);
			for (int i = 0; i < child_count; i++)
			{
				JptNode jpt_node = getChild(i);
				jpt_node.render(context, out);
			}
			if (n < items.length - 1)
				out.write(pad);
		}

		context.remove(loopVar);
		context.remove("$index");
		context.remove("$length");
	}

	private void iteratorLoop(Map<String, Object> context, Writer out, Iterator items, int child_count, int increment, String loopVar, String pad) throws IOException
	{
		int len = -1;

		if (context.containsKey("$length"))
		{
			len = ((Integer) context.get("$length")).intValue();
		}

		int index = 0;
		for (; items.hasNext();)
		{
			Object item = items.next();
			index++;

			if (index % increment == 0)
			{
				context.put(loopVar, item);
				context.put("$index", index);
				context.put("$length", len);

				for (int i = 0; i < child_count; i++)
				{
					JptNode jpt_node = getChild(i);
					jpt_node.render(context, out);
				}
				out.write(pad);

				context.remove(loopVar);
			}
		}

		context.remove(loopVar);
		context.remove("$index");
		context.remove("$length");

	}

	private void iterableLoop(Map<String, Object> context, Writer out, Iterable items, int child_count, int increment, String loopVar, String pad) throws IOException
	{
		if (items != null)
		{
			iteratorLoop(context, out, items.iterator(), child_count, increment, loopVar, pad);
		}
	}

	private void collectionLoop(Map<String, Object> context, Writer out, Collection items, int child_count, int increment, String loopVar, String pad) throws IOException
	{
		if (items != null)
		{
			context.put("$length", items.size());
			iteratorLoop(context, out, items.iterator(), child_count, increment, loopVar, pad);
		}
	}

	boolean isLoopNode()
	{
		return true;
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}
}
