package org.caudexorigo.jpt;

import nu.xom.Attribute;
import nu.xom.Comment;
import nu.xom.DocType;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ProcessingInstruction;
import nu.xom.Text;
import nu.xom.XMLException;

public class BaseJptNodeBuilder
{
	public BaseJptNodeBuilder(StringBuilder sb)
	{
		_buffer = sb;
	}

	public void process(Document doc)
	{
		int childCount = doc.getChildCount();
		for (int i = 0; i < childCount; i++)
			processChild(doc.getChild(i));
	}

	protected void process(Element element)
	{
		boolean hasRealChildren = false;
		int childCount = element.getChildCount();
		for (int i = 0; i < childCount; i++)
		{
			Node child = element.getChild(i);
			if (child instanceof Text)
			{
				Text t = (Text) child;
				if (t.getValue().length() == 0)
					continue;
			}
			hasRealChildren = true;
			break;
		}
		if (hasRealChildren)
		{
			processStartTag(element);
			for (int i = 0; i < childCount; i++)
			{
				Node child = element.getChild(i);
				processChild(child);
			}
			processEndTag(element);
		}
		else
		{
			processEmptyElementTag(element);
		}
	}

	protected void processEndTag(Element element)
	{
		_buffer.append("</");
		_buffer.append(element.getQualifiedName());
		_buffer.append('>');
	}

	protected void processStartTag(Element element)
	{
		processTagBeginning(element);
		_buffer.append('>');
	}

	protected void processEmptyElementTag(Element element)
	{
		processTagBeginning(element);
		_buffer.append("/>");
	}

	// This just extracts the commonality between processStartTag
	// and processEmptyElementTag
	private void processTagBeginning(Element element)
	{
		_buffer.append('<');
		_buffer.append(element.getQualifiedName());
		processAttributes(element);
		// processNamespaceDeclarations(element);
	}

	protected void processAttributes(Element element)
	{
		int attributeCount = element.getAttributeCount();
		for (int i = 0; i < attributeCount; i++)
		{
			Attribute attribute = element.getAttribute(i);
			_buffer.append(' ');
			process(attribute);
		}
	}

	protected void process(Attribute attribute)
	{
		_buffer.append(attribute.getQualifiedName());
		_buffer.append("=\"");
		_buffer.append(attribute.getValue());
		_buffer.append('"');
	}

	protected void process(Comment comment)
	{
		_buffer.append("<!--");
		_buffer.append(comment.getValue());
		_buffer.append("-->");
	}

	protected void process(Text text)
	{
		String value = text.getValue();
		_buffer.append(value);
	}

	protected void process(DocType doctype)
	{
		_buffer.append("<!DOCTYPE ");
		_buffer.append(doctype.getRootElementName());
		if (doctype.getPublicID() != null)
		{
			_buffer.append(" PUBLIC \"" + doctype.getPublicID() + "\" \"" + doctype.getSystemID() + "\"");
		}
		else if (doctype.getSystemID() != null)
		{
			_buffer.append(" SYSTEM \"" + doctype.getSystemID() + "\"");
		}
		_buffer.append(">\n");
	}

	protected void processChild(Node node)
	{
		if (node instanceof Element)
			process((Element) node);
		else if (node instanceof Text)
			process((Text) node);
		else if (node instanceof Comment)
			process((Comment) node);
		else if (!(node instanceof ProcessingInstruction))
			if (node instanceof DocType)
				process((DocType) node);
			else
				throw new XMLException((new StringBuilder("Cannot process a ")).append(node.getClass().getName()).append(" from the processChild() method").toString());
	}

	protected final void processAttributeValue(String value)
	{
		_buffer.append(value);
	}

	protected final void processRaw(String text)
	{
		_buffer.append(text);
	}

	public StringBuilder getBuffer()
	{
		return _buffer;
	}

	public void setBuffer(StringBuilder buf)
	{
		_buffer = buf;
	}

	private StringBuilder _buffer;
}
