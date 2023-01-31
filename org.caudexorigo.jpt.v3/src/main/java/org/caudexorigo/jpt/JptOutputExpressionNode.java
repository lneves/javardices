package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.unbescape.xml.XmlEscape;

public class JptOutputExpressionNode extends JptNode
{
	private static final String TEXT = "text "; // DEFAULT - "angle-brackets and ampersands with their HTML entity equivalents"
	private static final String STRUCTURE = "structure "; // "passes the replacement text through unchanged";

	private boolean _isInSlot;

	private final boolean escape;
	private final JptExpression jptExpression;

//	JptOutputExpressionNode(String jptExp, boolean isInSlot)
//	{
//		this(jptExp, isInSlot, true);
//	}

	JptOutputExpressionNode(String jptExp, boolean isInSlot, boolean escapeContent)
	{
		if (StringUtils.isBlank(jptExp))
		{
			throw new IllegalArgumentException("tal expression can not be blank");
		}

		_isInSlot = isInSlot;

		String _evaluation_exp = jptExp;

		if (jptExp.startsWith(TEXT))
		{
			escape = true;
			_evaluation_exp = jptExp.substring(TEXT.length());
		}
		else if (jptExp.startsWith(STRUCTURE))
		{
			escape = false;
			_evaluation_exp = jptExp.substring(STRUCTURE.length());
		}
		else
		{
			escape = escapeContent;
		}

		jptExpression = new JptExpression(_evaluation_exp);
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
		if (context == null)
		{
			throw new IllegalArgumentException("render context can not be null");
		}

		try
		{
			String result = jptExpression.evalAsString(context);

			String sout = escape ? XmlEscape.escapeXml10(result) : result;
			out.write(sout);
		}
		catch (Throwable t)
		{
			Throwable r = findRootCause(t);
			String emsg = String.format("Error processing JptOutputExpressionNode:%nexpression: '%s';%ncontext: %s;%nmessage: '%s'", jptExpression.getExpression(), context, r.getMessage());
			throw new RuntimeException(emsg);
		}
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}
}
