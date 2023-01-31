package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.nu.xom.Attribute;
import org.unbescape.xml.XmlEscape;

public class JptConditionalAttributeNode extends JptNode
{
	private static final char QUOTE = '"';

	private boolean _isInSlot;

	private String[] new_attr_exps;

	private final char[] attributePrefix;

	private final JptExpression jptConditionExpression;

	private final JptExpression jptExpression;

	JptConditionalAttributeNode(Attribute attribute, boolean isInSlot)
	{
		_isInSlot = isInSlot;

		String attributeName = attribute.getQualifiedName();
		String attrExp = attribute.getValue().replace("\'", "\"").trim();

		new_attr_exps = StringUtils.split(attrExp, " ", 2);

		attributePrefix = (new StringBuilder())
				.append(' ')
				.append(attributeName)
				.append('=')
				.append(QUOTE).toString().toCharArray();

		if (new_attr_exps.length == 1)
		{
			jptConditionExpression = new JptExpression("true");
			jptExpression = new JptExpression(attrExp);
		}
		else
		{
			jptConditionExpression = new JptExpression(new_attr_exps[0]);
			jptExpression = new JptExpression(new_attr_exps[1]);
		}
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
		boolean condition = jptConditionExpression.evalAsBoolean(context);

		if (condition)
		{
			String sout = jptExpression.evalAsString(context);

			if (StringUtils.isNotBlank(sout))
			{
				out.write(attributePrefix, 0, attributePrefix.length);
				out.write(XmlEscape.escapeXml10Attribute(sout));
				out.write(QUOTE);
			}
		}
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}
}