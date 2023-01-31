package org.caudexorigo.jpt;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.caudexorigo.nu.xom.Attribute;
import org.caudexorigo.nu.xom.Comment;
import org.caudexorigo.nu.xom.DocType;
import org.caudexorigo.nu.xom.Document;
import org.caudexorigo.nu.xom.Element;
import org.caudexorigo.nu.xom.Node;
import org.caudexorigo.nu.xom.ParentNode;
import org.caudexorigo.nu.xom.ProcessingInstruction;
import org.caudexorigo.nu.xom.Text;
import org.caudexorigo.nu.xom.XMLException;

public class BaseJptNodeBuilder
{
	private final Set<String> allowSelfClosing = new HashSet<>();

	public BaseJptNodeBuilder(StringBuilder sb)
	{
		_buffer = sb;

		allowSelfClosing.add("area");
		allowSelfClosing.add("base");
		allowSelfClosing.add("basefont");
		allowSelfClosing.add("br");
		allowSelfClosing.add("col");
		allowSelfClosing.add("frame");
		allowSelfClosing.add("hr");
		allowSelfClosing.add("img");
		allowSelfClosing.add("input");
		allowSelfClosing.add("isindex");
		allowSelfClosing.add("link");
		allowSelfClosing.add("meta");
		allowSelfClosing.add("param");
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
			if (allowSelfClosing.contains(element.getLocalName()))
			{
				processEmptyElementTag(element);
			}
			else
			{
				processStartTag(element);
				processEndTag(element);
			}
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
		processNamespaceDeclarations(element);
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

	protected void processNamespaceDeclarations(Element element)
	{
		for (int i = 0; i < element.getNamespaceDeclarationCount(); i++)
		{
			String additionalPrefix = element.getNamespacePrefix(i);
			String uri = element.getNamespaceURI(additionalPrefix);

			if (isJptNs(uri))
			{
				continue;
			}

			ParentNode parentNode = element.getParent();

			if (parentNode != null && (parentNode instanceof Element))
			{
				Element parentElement = (Element) parentNode;
				if (uri.equals(parentElement.getNamespaceURI(additionalPrefix)))
				{
					continue;
				}
			}
			else if (uri.length() == 0)
			{
				continue; // no need to say xmlns=""
			}

			_buffer.append(" xmlns");
			if (additionalPrefix.length() > 0)
			{
				_buffer.append(':');
				_buffer.append(additionalPrefix);
			}
			_buffer.append("=\"");
			// _buffer.append(escape(uri));
			_buffer.append(uri);
			_buffer.append('"');
		}

	}

	private boolean isJptNs(String uri)
	{
		return (uri.equals("http://xml.zope.org/namespaces/tal") || uri.equals("http://xml.zope.org/namespaces/metal") || uri.equals("http://xml.zope.org/namespaces/metal/append-to-slot"));
	}

	protected static <T> T coalesce(T... values)
	{
		for (T v : values)
		{
			if (Objects.nonNull(v))
			{
				return v;
			}
		}
		return null;
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

	private void process(ProcessingInstruction pi)
	{
		// ignore
	}

	protected void processChild(Node node)
	{
		if (node instanceof Element)
			process((Element) node);
		else if (node instanceof Text)
			process((Text) node);
		else if (node instanceof Comment)
			process((Comment) node);
		else if (node instanceof ProcessingInstruction)
			process((ProcessingInstruction) node);
		else if (node instanceof DocType)
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
