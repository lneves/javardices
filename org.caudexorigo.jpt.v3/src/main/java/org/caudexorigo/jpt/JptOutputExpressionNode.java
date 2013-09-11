package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

import org.caudexorigo.text.StringEscapeUtils;
import org.mvel2.MVEL;

public class JptOutputExpressionNode extends JptNode
{
	private static final String TEXT = "text "; // DEFAULT - "angle-brackets and ampersands with their HTML entity equivalents"
	private static final String STRUCTURE = "structure "; // "passes the replacement text through unchanged";

	private boolean _isInSlot;

	private Serializable _compiled_exp;

	private final boolean escape;

	JptOutputExpressionNode(String jpt_exp, boolean isInSlot)
	{
		_isInSlot = isInSlot;

		String evaluationExpression = jpt_exp;

		if (jpt_exp.startsWith(TEXT))
		{
			escape = true;
			evaluationExpression = jpt_exp.substring(TEXT.length());
		}
		else if (jpt_exp.startsWith(STRUCTURE))
		{
			escape = false;
			evaluationExpression = jpt_exp.substring(STRUCTURE.length());
		}
		else
		{
			escape = true;
		}
		// Compile the expression.
		_compiled_exp = MVEL.compileExpression(evaluationExpression);
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
		String result = String.valueOf(MVEL.executeExpression(_compiled_exp, context));
		String sout = escape ? StringEscapeUtils.escapeXml(result) : result;
		out.write(sout);
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}
}
