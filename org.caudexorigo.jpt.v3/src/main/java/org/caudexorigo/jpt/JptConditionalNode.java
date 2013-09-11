package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

import org.caudexorigo.text.StringUtils;
import org.mvel2.MVEL;

public class JptConditionalNode extends JptParentNode
{
	private Serializable _compiled_exp;

	private boolean _isInSlot;

	JptConditionalNode(String jpt_exp, boolean isInSlot)
	{
		_isInSlot = isInSlot;
		String bool_exp = StringUtils.replace(jpt_exp, "\'", "\"");
		// Compile the expression.
		_compiled_exp = MVEL.compileExpression(bool_exp);
	}

	boolean isConditionalNode()
	{
		return true;
	}

	public void render(Map<String, Object> context, Writer out) throws IOException
	{
		boolean condition = (Boolean) MVEL.executeExpression(_compiled_exp, context);

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

	public boolean isInSlot()
	{
		return _isInSlot;
	}
}
