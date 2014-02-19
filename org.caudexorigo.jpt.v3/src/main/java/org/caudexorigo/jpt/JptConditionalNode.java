package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.caudexorigo.text.StringUtils;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

public class JptConditionalNode extends JptParentNode
{
	private Serializable _compiled_exp;

	private boolean _isInSlot;

	private String _bool_exp;

	JptConditionalNode(String jpt_exp, boolean isInSlot)
	{
		_isInSlot = isInSlot;
		_bool_exp = StringUtils.isBlank(jpt_exp) ? "true" : StringUtils.replace(jpt_exp, "\'", "\"");
	}

	boolean isConditionalNode()
	{
		return true;
	}

	public void render(Map<String, Object> context, Writer out) throws IOException
	{
		try
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
				_compiled_exp = MVEL.compileExpression(_bool_exp, parser_context);
			}

			boolean condition = (Boolean) MVEL.executeExpression(_compiled_exp, context);
			// boolean condition = (Boolean) MVEL.evalToBoolean(_bool_exp, context);

			if (condition)
			{
				int child_count = getChildCount();
				for (int i = 0; i < child_count; i++)
				{
					JptNode jpt_node = getChild(i);
					jpt_node.render(context, out);
				}
			}
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}
}
