package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nu.xom.Attribute;

import org.caudexorigo.text.StringUtils;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

public class JptConditionalAttributeNode extends JptNode
{
	private String _attr_exp;

	private char[] _attribute_name;

	private static final char[] EQUAL_SIGN = "=".toCharArray();

	private static final char[] QUOTE = "\"".toCharArray();

	private static final char[] SPACE = " ".toCharArray();

	private boolean _isInSlot;

	private Serializable _compiled_exp;

	private Serializable _condition_compiled_exp;

	private String[] new_attr_exps;

	JptConditionalAttributeNode(Attribute attribute, boolean isInSlot)
	{
		_isInSlot = isInSlot;

		_attribute_name = attribute.getQualifiedName().toCharArray();
		_attr_exp = attribute.getValue().replace("\'", "\"").trim();

		new_attr_exps = StringUtils.split(_attr_exp, " ", 2);
	}

	public int getChildCount()
	{
		return 0;
	}

	public JptNode getChild(int i)
	{
		throw new IndexOutOfBoundsException("Attributes do not have children");
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

			if (new_attr_exps.length == 1)
			{
				_condition_compiled_exp = MVEL.compileExpression("true");
				_compiled_exp = MVEL.compileExpression(_attr_exp, parser_context);
			}
			else
			{
				_condition_compiled_exp = MVEL.compileExpression(new_attr_exps[0]);
				_compiled_exp = MVEL.compileExpression(new_attr_exps[1], parser_context);
			}
		}

		boolean condition = (Boolean) MVEL.executeExpression(_condition_compiled_exp, context);

		if (condition)
		{
			String sout = String.valueOf(MVEL.executeExpression(_compiled_exp, context));
			out.write(SPACE, 0, 1);
			out.write(_attribute_name, 0, _attribute_name.length);
			out.write(EQUAL_SIGN, 0, 1);
			out.write(QUOTE, 0, 1);
			out.write(sout);
			out.write(QUOTE, 0, 1);
		}
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}
}