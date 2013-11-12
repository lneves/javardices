package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nu.xom.Attribute;

import org.caudexorigo.text.StringEscapeUtils;
import org.caudexorigo.text.StringUtils;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

public class JptAttributeNode extends JptNode
{
	private String _attr_exp;

	private char[] _attribute_name;

	private static final char[] EQUAL_SIGN = "=".toCharArray();

	private static final char[] QUOTE = "\"".toCharArray();

	private static final char[] SPACE = " ".toCharArray();

	private boolean _isInSlot;

	private Serializable _compiled_exp;

	JptAttributeNode(Attribute attribute, boolean isInSlot)
	{
		_isInSlot = isInSlot;

		_attribute_name = attribute.getQualifiedName().toCharArray();
		_attr_exp = attribute.getValue().replace("\'", "\"").trim();
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
			// Compile the expression.
			_compiled_exp = MVEL.compileExpression(_attr_exp, parser_context);
		}

		String v = String.valueOf(MVEL.executeExpression(_compiled_exp, context));

		if (StringUtils.isNotBlank(v))
		{
			String sout = StringEscapeUtils.escapeXml(v);
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
