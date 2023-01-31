package org.caudexorigo.jpt;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;

public class JptExpression
{
	private final String plainExpression;
	private Serializable compiledExpression;

	public JptExpression(String plainExpression)
	{
		super();
		this.plainExpression = plainExpression;
	}

	protected String getExpression()
	{
		return plainExpression;
	}

	protected final Object eval(Map<String, Object> context)
	{
		doCompile(context);
		return MVEL.executeExpression(compiledExpression, context);
	}

	protected final String evalAsString(Map<String, Object> context)
	{
		doCompile(context);
		return MVEL.executeExpression(compiledExpression, context, String.class);
	}

	protected final boolean evalAsBoolean(Map<String, Object> context)
	{
		doCompile(context);
		return MVEL.executeExpression(compiledExpression, context, Boolean.class);
	}

	private void doCompile(Map<String, Object> context)
	{
		if (compiledExpression == null)
		{
			ParserContext parser_context = ParserContext.create();

			for (Entry<String, Object> entry : context.entrySet())
			{
				parser_context.addInput(entry.getKey(), entry.getValue().getClass());
			}

			// Compile the expression.
			compiledExpression = MVEL.compileExpression(plainExpression, parser_context);
		}
	}
}
