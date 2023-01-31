package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class JptConditionalNode extends JptParentNode
{
	private boolean _isInSlot;
	private final JptExpression jptExpression;

	JptConditionalNode(String jpt_exp, boolean isInSlot)
	{
		_isInSlot = isInSlot;
		String boolExp = StringUtils.isBlank(jpt_exp) ? "true" : StringUtils.replace(jpt_exp, "\'", "\"");
		jptExpression = new JptExpression(boolExp);
	}

	boolean isConditionalNode()
	{
		return true;
	}

	public void render(Map<String, Object> context, Writer out) throws IOException
	{
		try
		{
			boolean condition = jptExpression.evalAsBoolean(context);
			// System.out.printf("JptConditionalNode.render -> expression: '%s'; condition: %s%n", _bool_exp, condition);
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
			Throwable r = findRootCause(t);
			String emsg = String.format("Error processing JptConditionalNode:%nexpression: '%s';%ncontext: %s;%nmessage: '%s'", jptExpression.getExpression(), context, r.getMessage());
			throw new RuntimeException(emsg);
		}
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}
}
