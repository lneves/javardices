package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.caudexorigo.text.StringEscapeUtils;
import org.caudexorigo.text.StringUtils;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

public class JptOutputExpressionNode extends JptNode
{
	private static final String TEXT = "text "; // DEFAULT - "angle-brackets and ampersands with their HTML entity equivalents"
	private static final String STRUCTURE = "structure "; // "passes the replacement text through unchanged";

	private boolean _isInSlot;

	private Serializable _compiled_exp;

	private final boolean escape;

	private String _evaluation_exp;

	JptOutputExpressionNode(String jpt_exp, boolean isInSlot)
	{
		_isInSlot = isInSlot;

		if (StringUtils.isBlank(jpt_exp))
		{
			throw new IllegalArgumentException("tal expression can not be blank");
		}

		_evaluation_exp = jpt_exp;

		if (jpt_exp.startsWith(TEXT))
		{
			escape = true;
			_evaluation_exp = jpt_exp.substring(TEXT.length());
		}
		else if (jpt_exp.startsWith(STRUCTURE))
		{
			escape = false;
			_evaluation_exp = jpt_exp.substring(STRUCTURE.length());
		}
		else
		{
			escape = true;
		}
	}

	public int getChildCount()
	{
		return 0;
	}

	public JptNode getChild(int i)
	{
		throw new IndexOutOfBoundsException("Output Expressions do not have children");
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
			_compiled_exp = MVEL.compileExpression(_evaluation_exp, parser_context);
		}

		String result = String.valueOf(MVEL.executeExpression(_compiled_exp, context));
		String sout = escape ? StringEscapeUtils.escapeXml(result) : result;
		out.write(sout);
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}
}
