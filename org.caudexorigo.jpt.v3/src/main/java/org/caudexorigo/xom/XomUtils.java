package org.caudexorigo.xom;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;

import org.caudexorigo.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XomUtils
{
	private static final Logger log = LoggerFactory.getLogger(XomUtils.class);

	public XomUtils()
	{
	}

	public static Node findSpecificMacro(Node node, String macro_name)
	{
		int child_count = node.getChildCount();
		for (int i = 0; i < child_count; i++)
		{
			Node childNode = node.getChild(i);
			if (childNode instanceof Element)
			{
				Element childElement = (Element) childNode;
				Attribute attr = childElement.getAttribute("define-macro", "http://xml.zope.org/namespaces/metal");
				if (attr != null)
					return childElement;
			}
			findSpecificMacro(childNode, macro_name);
		}
		return null;
	}

	public static String textValueFromPath(Document doc, String path)
	{
		try
		{
			return ((Text) doc.query(path).get(0)).getValue();
		}
		catch (Exception e)
		{
			log.warn("Xpath:\"" + path + "\" not found in document!");
			return "";
		}
	}

	public static Attribute getAttribute(Element el, String attr_name_to_find)
	{
		int numAttr = el.getAttributeCount();
		for (int i = 0; i < numAttr; i++)
		{
			Attribute curr_attr = el.getAttribute(i);
			String curr_attr_name = curr_attr.getQualifiedName();
			if (attr_name_to_find.equals(curr_attr_name))
				return curr_attr;
		}
		return null;
	}

	public static Attribute[] getAttributes(Element el)
	{
		int numAttr = el.getAttributeCount();
		Attribute attrs[] = new Attribute[numAttr];
		for (int i = 0; i < numAttr; i++)
			attrs[i] = el.getAttribute(i);
		return attrs;
	}

	public static Attribute[] getJptAttributes(Element el)
	{
		int numAttr = el.getAttributeCount();
		Attribute attrs[] = new Attribute[numAttr];
		int j = 0;
		for (int i = 0; i < numAttr; i++)
			if (el.getAttribute(i).getNamespacePrefix().equals("tal") || el.getAttribute(i).getNamespacePrefix().equals("metal"))
			{
				attrs[j] = el.getAttribute(i);
				j++;
			}
		Attribute jpt_attrs[] = new Attribute[j];
		System.arraycopy(attrs, 0, jpt_attrs, 0, j);
		return jpt_attrs;
	}

	public static boolean hasAttribute(Element el, String attr_name_to_find)
	{
		int numAttr = el.getAttributeCount();
		for (int i = 0; i < numAttr; i++)
		{
			Attribute curr_attr = el.getAttribute(i);
			String curr_attr_name = curr_attr.getQualifiedName();
			if (attr_name_to_find.equals(curr_attr_name))
				return true;
		}
		return false;
	}

	public static Attribute[] processTalAttributes(Attribute tal_attr)
	{
		String tal_attr_exp = StringUtils.trimToNull(tal_attr.getValue());
		Attribute attributes[];
		if (tal_attr_exp != null)
		{
			String tal_attr_sub_exp[] = StringUtils.split(tal_attr_exp, ";");
			int len = tal_attr_sub_exp.length;
			attributes = new Attribute[len];
			for (int i = 0; i < len; i++)
			{
				String[] new_attr_exp = StringUtils.split(tal_attr_sub_exp[i], " ", 2);
				String new_attr_qname = new_attr_exp[0].trim();
				String new_attr_value = new_attr_exp[1].trim();
				String[] new_attr = StringUtils.split(new_attr_qname, ":", 2);
				if (new_attr.length == 1)
				{
					attributes[i] = new Attribute(new_attr_qname, new_attr_value);
				}
				else
				{
					String new_attr_prefix = new_attr[0];
					String new_attr_lname = new_attr[1];
					attributes[i] = new Attribute(new_attr_lname, new_attr_value);
					if (new_attr[0].equals("xml"))
					{
						attributes[i].setNamespace(new_attr_prefix, "http://www.w3.org/XML/1998/namespace");
					}
					else
					{
						attributes[i].setNamespace(new_attr_prefix, "http://localhost/");
					}

				}
			}
		}
		else
		{
			return new Attribute[0];
		}
		return attributes;
	}
}