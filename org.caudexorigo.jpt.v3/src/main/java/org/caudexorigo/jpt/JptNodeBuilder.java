package org.caudexorigo.jpt;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.caudexorigo.xom.XomDocumentBuilder;
import org.caudexorigo.xom.XomUtils;

public class JptNodeBuilder extends BaseJptNodeBuilder
{
	private static final String METAL_NS = "http://xml.zope.org/namespaces/metal";

	private static final String TAL_NS = "http://xml.zope.org/namespaces/tal";

	private static final String APPEND_SLOT_NS = "http://xml.zope.org/namespaces/metal/append-to-slot";

	private URI _templateUri;

	private JptDocument _jptDocument;

	private StringBuilder _sb;

	private ArrayList<Element> _slotFillerList = new ArrayList<Element>();

	private ArrayList<Element> _slotAppenderList = new ArrayList<Element>();

	private ArrayList<Dependency> _dependecies = new ArrayList<Dependency>();

	private Deque<JptParentNode> pnodes = new ArrayDeque<JptParentNode>();

	private Map<String, Element> slotFillers = new HashMap<String, Element>();

	private Map<String, Element> slotAppenders = new HashMap<String, Element>();

	private boolean _isInSlot = false;;

	public JptNodeBuilder()
	{
		this(new StringBuilder());
	}

	private JptNodeBuilder(StringBuilder sb)
	{
		super(sb);
		_sb = sb;
	}

	public JptDocument getJptDocument()
	{
		return _jptDocument;
	}

	private void prepareElement(Element el) throws ValidityException, ParsingException, IOException
	{
		if (XomUtils.getAttribute(el, "metal:use-macro") != null)
		{
			processMetalUseMacro(el);
			return;
		}
		if (XomUtils.getAttribute(el, "metal:define-slot") != null)
		{
			processMetalDefineSlot(el);
			return;
		}
		if (XomUtils.getAttribute(el, "tal:condition") != null)
		{
			processTalCondition(el);
			return;
		}
		if (XomUtils.getAttribute(el, "tal:repeat") != null)
		{
			processTalRepeat(el);
			return;
		}
		if (XomUtils.getAttribute(el, "tal:content") != null)
		{
			processTalContent(el);
			return;
		}
		if (XomUtils.getAttribute(el, "tal:replace") != null)
		{
			processTalReplace(el);
			return;
		}
		if (XomUtils.getAttribute(el, "tal:include") != null)
		{
			processTalInclude(el);
			return;
		}
		if ("template".equals(el.getLocalName()) && TAL_NS.equals(el.getNamespaceURI()))
		{
			processTalTemplate(el);
			return;
		}
		if (XomUtils.getAttribute(el, "metal:fill-slot") != null)
			el.removeAttribute(XomUtils.getAttribute(el, "metal:fill-slot"));
		if (XomUtils.getAttribute(el, "metal:define-macro") != null)
			el.removeAttribute(XomUtils.getAttribute(el, "metal:define-macro"));
		super.process(el);
	}

	private void processTalTemplate(Element el)
	{
		JptStaticFragment sf = new JptStaticFragment(_sb.toString());
		((JptParentNode) pnodes.peek()).appendChild(sf);
		_sb.delete(0, _sb.length());

		StringBuilder template = new StringBuilder();

		for (int i = 0; i < el.getChildCount(); i++)
		{
			template.append(el.getChild(i).toXML());
		}

		el.removeChildren();

		JptTemplateNode jout;
		if (el.getBaseURI().equals(APPEND_SLOT_NS))
		{
			jout = new JptTemplateNode(template.toString(), true);
		}
		else
		{
			jout = new JptTemplateNode(template.toString(), _isInSlot);
		}
		removePNodeChildren();
		((JptParentNode) pnodes.peek()).appendChild(jout);
	}

	private void processTalInclude(Element el)
	{
		Attribute attribute = XomUtils.getAttribute(el, "tal:include");
		if (attribute != null)
		{
			el.removeAttribute(attribute);
			JptStaticFragment sf = new JptStaticFragment(_sb.toString());
			((JptParentNode) pnodes.peek()).appendChild(sf);
			_sb.delete(0, _sb.length());
			el.removeChildren();
			JptIncludeNode jinclude = new JptIncludeNode(attribute.getValue(), _isInSlot);
			removePNodeChildren();
			((JptParentNode) pnodes.peek()).appendChild(jinclude);
		}
	}

	public void process(Document doc, URI templateUri)
	{
		_templateUri = templateUri;
		findSlotActorsInDocument(doc);
		prepareSlotsActorsInDocument();
		String object_class_name = ContextBuilder.objectNameFromInstructions(doc);

		_jptDocument = new JptDocument(object_class_name);
		pnodes.push(_jptDocument);
		super.process(doc);
		JptStaticFragment sf = new JptStaticFragment(_sb.toString());
		((JptParentNode) pnodes.peek()).appendChild(sf);
	}

	private String extractMacroName(String uri_qs)
	{
		int pos = uri_qs.indexOf("=") + 1;
		return uri_qs.substring(pos);
	}

	private void findSlotActorsInDocument(Node node)
	{
		for (int i = 0; i < node.getChildCount(); i++)
		{
			if (node.getChild(i) instanceof Element)
			{
				Element element = (Element) node.getChild(i);
				String slot_filler_name = element.getAttributeValue("fill-slot", METAL_NS);
				if (slot_filler_name != null)
					_slotFillerList.add(element);

				String slot_to_append_name = element.getAttributeValue("append-to-slot", METAL_NS);
				if (slot_to_append_name != null)
					_slotAppenderList.add(element);

			}
			findSlotActorsInDocument(node.getChild(i));
		}
	}

	private void prepareSlotsActorsInDocument()
	{
		Element slot_filler_elements[] = (Element[]) _slotFillerList.toArray(new Element[0]);
		for (int i = 0; i < slot_filler_elements.length; i++)
		{
			Element element = slot_filler_elements[i];
			String slot_to_fill_name = element.getAttributeValue("fill-slot", METAL_NS);
			slotFillers.put(slot_to_fill_name, (Element) element.copy());
		}

		Element slot_appender_elements[] = (Element[]) _slotAppenderList.toArray(new Element[0]);
		for (int i = 0; i < slot_appender_elements.length; i++)
		{
			Element element = slot_appender_elements[i];
			String slot_to_fill_name = element.getAttributeValue("append-to-slot", METAL_NS);
			slotAppenders.put(slot_to_fill_name, (Element) element.copy());
		}
	}

	private void prepareMacro(Document macroDocument, Node useMacroNode, String macroName, String macroPath) throws ValidityException, ParsingException, IOException
	{
		Document macro_doc = macroDocument.getDocument();
		findSlotActorsInDocument(macroDocument);
		prepareSlotsActorsInDocument();
		Element subtree = (Element) XomUtils.findSpecificMacro(macro_doc, macroName);

		String object_class_name = ContextBuilder.objectNameFromInstructions(macro_doc);

		JptMacroNode jpt_macro_node = new JptMacroNode(object_class_name, _isInSlot);

		JptStaticFragment sf_pre = new JptStaticFragment(_sb.toString());
		((JptParentNode) pnodes.peek()).appendChild(sf_pre);
		_sb.delete(0, _sb.length());
		pnodes.push(jpt_macro_node);
		_isInSlot = false;

		prepareElement(subtree);

		pnodes.pop();

		((JptParentNode) pnodes.peek()).appendChild(jpt_macro_node);
	}

	protected void process(Element element)
	{
		try
		{
			prepareElement(element);
		}
		catch (ValidityException e)
		{
			e.printStackTrace();
		}
		catch (ParsingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void processAttributes(Element element)
	{
		processTalAttributes(element);
		super.processAttributes(element);
	}

	private void processTalAttributes(Element el)
	{
		processTalConditionalAttributes(el);

		Attribute attribute = XomUtils.getAttribute(el, "tal:attributes");
		if (attribute != null)
		{
			el.removeAttribute(attribute);
			Attribute tal_attributes[] = XomUtils.processTalAttributes(attribute);
			Attribute aattribute[] = tal_attributes;
			int i = 0;
			for (int j = aattribute.length; i < j; i++)
			{
				Attribute tal_attribute = aattribute[i];
				Attribute attr_to_remove = el.getAttribute(tal_attribute.getLocalName());
				if (attr_to_remove != null)
					el.removeAttribute(attr_to_remove);
			}
			JptStaticFragment sf = new JptStaticFragment(_sb.toString());
			((JptParentNode) pnodes.peek()).appendChild(sf);
			_sb.delete(0, _sb.length());
			Attribute aattribute1[] = tal_attributes;
			int k = 0;

			for (int l = aattribute1.length; k < l; k++)
			{
				Attribute tal_attribute = aattribute1[k];

				JptAttributeNode jattr = new JptAttributeNode(tal_attribute, _isInSlot);
				((JptParentNode) pnodes.peek()).appendChild(jattr);
			}
		}
	}

	private void processTalConditionalAttributes(Element el)
	{
		Attribute attribute = XomUtils.getAttribute(el, "tal:conditionalAttributes");
		if (attribute != null)
		{
			el.removeAttribute(attribute);
			Attribute tal_attributes[] = XomUtils.processTalAttributes(attribute);
			Attribute aattribute[] = tal_attributes;
			int i = 0;
			for (int j = aattribute.length; i < j; i++)
			{
				Attribute tal_attribute = aattribute[i];
				Attribute attr_to_remove = el.getAttribute(tal_attribute.getLocalName());
				if (attr_to_remove != null)
					el.removeAttribute(attr_to_remove);
			}
			JptStaticFragment sf = new JptStaticFragment(_sb.toString());
			((JptParentNode) pnodes.peek()).appendChild(sf);
			_sb.delete(0, _sb.length());
			Attribute aattribute1[] = tal_attributes;
			int k = 0;

			for (int l = aattribute1.length; k < l; k++)
			{
				Attribute tal_attribute = aattribute1[k];

				JptConditionalAttributeNode jattr = new JptConditionalAttributeNode(tal_attribute, _isInSlot);
				((JptParentNode) pnodes.peek()).appendChild(jattr);
			}
		}
	}

	private void processTalCondition(Element el)
	{
		Attribute attribute = XomUtils.getAttribute(el, "tal:condition");
		if (attribute != null)
		{
			String current_attribute_value = attribute.getValue();
			JptStaticFragment sf_pre = new JptStaticFragment(_sb.toString());
			((JptParentNode) pnodes.peek()).appendChild(sf_pre);
			_sb.delete(0, _sb.length());
			el.removeAttribute(attribute);

			JptConditionalNode jcond = new JptConditionalNode(current_attribute_value, _isInSlot);
			pnodes.push(jcond);
			process(el);
			JptStaticFragment sf_post = new JptStaticFragment(_sb.toString());
			jcond.appendChild(sf_post);
			_sb.delete(0, _sb.length());
			processTalRepeat(el);
			processTalReplace(el);
			processTalContent(el);
			pnodes.pop();
			((JptParentNode) pnodes.peek()).appendChild(jcond);
		}
	}

	private void processTalContent(Element el)
	{
		Attribute attribute = XomUtils.getAttribute(el, "tal:content");
		if (attribute != null)
		{
			el.removeAttribute(attribute);
			removePNodeChildren();
			processStartTag(el);
			JptStaticFragment sf_pre = new JptStaticFragment(_sb.toString());
			((JptParentNode) pnodes.peek()).appendChild(sf_pre);
			_sb.delete(0, _sb.length());
			processTalAttributes(el);

			JptOutputExpressionNode jout;
			if (el.getBaseURI().equals(APPEND_SLOT_NS))
			{
				jout = new JptOutputExpressionNode(attribute.getValue(), true);
			}
			else
			{
				jout = new JptOutputExpressionNode(attribute.getValue(), _isInSlot);
			}

			// JptOutputExpressionNode jout = new
			// JptOutputExpressionNode(attribute.getValue(), _isInSlot);
			((JptParentNode) pnodes.peek()).appendChild(jout);
			processEndTag(el);
			el.removeChildren();
			JptStaticFragment sf_post = new JptStaticFragment(_sb.toString());
			((JptParentNode) pnodes.peek()).appendChild(sf_post);
			_sb.delete(0, _sb.length());
		}
	}

	private void processMetalUseMacro(Element el) throws ValidityException, ParsingException, IOException
	{
		Attribute attribute = XomUtils.getAttribute(el, "metal:use-macro");
		if (attribute != null)
		{
			el.removeAttribute(attribute);
			String use_macro_value = attribute.getValue();
			URI macroURI = _templateUri.resolve(use_macro_value);
			String macroName = extractMacroName(macroURI.getQuery());
			String macroPath = macroURI.getPath();
			URI macroUri = _templateUri.resolve(macroPath);
			Dependency d = new Dependency(macroUri);
			_dependecies.add(d);
			Document document_macro = XomDocumentBuilder.getDocument(macroUri);
			prepareMacro(document_macro, el, macroName, macroPath);
		}
	}

	private void childAppender(Element root, Node child)
	{
		if (child == null)
		{
			return;
		}
		root.setBaseURI(APPEND_SLOT_NS);

		int appendChildCount = child.getChildCount();
		for (int i = 0; i < appendChildCount; i++)
		{
			Node childNode = child.copy().getChild(i);
			if (childNode != null)
			{
				childNode.detach();
				root.appendChild(childNode);
				if (childNode instanceof Element)
				{
					Element rchild = (Element) child;
					rchild.setBaseURI(APPEND_SLOT_NS);
					int arChildCount = rchild.getChildCount();
					for (int j = 0; j < arChildCount; j++)
					{
						childAppender(rchild, rchild.getChild(j));
					}
				}
			}
		}

	}

	private void processMetalDefineSlot(Element el) throws ValidityException, ParsingException, IOException
	{

		Attribute attribute = XomUtils.getAttribute(el, "metal:define-slot");
		if (attribute != null)
		{
			el.removeAttribute(attribute);
			String current_attribute_value = attribute.getValue();

			Element slot_appender_element = (Element) slotAppenders.get(current_attribute_value);

			// - append-to-slot
			if (slot_appender_element != null)
			{
				slot_appender_element.detach();
				childAppender(el, slot_appender_element);
			}

			Element slot_filler_element = (Element) slotFillers.get(current_attribute_value);

			// - fill-slot
			if (slot_filler_element != null)
			{
				_isInSlot = true;

				prepareElement(slot_filler_element);

				_isInSlot = false;
				return;
			}

			prepareElement(el);
		}
	}

	private void processTalRepeat(Element el)
	{
		Attribute attribute = XomUtils.getAttribute(el, "tal:repeat");
		if (attribute != null)
		{
			String current_attribute_value = attribute.getValue();
			JptStaticFragment sf_pre = new JptStaticFragment(_sb.toString());
			((JptParentNode) pnodes.peek()).appendChild(sf_pre);

			int nl = _sb.lastIndexOf("\n");
			String padding = _sb.substring(nl, _sb.length());

			_sb.delete(0, _sb.length());
			el.removeAttribute(attribute);

			RepeatElements relements = new RepeatElements(current_attribute_value, padding);

			// JptLoopNode jloop = new
			// JptLoopNode(relements.getLoopSourceExpression(), _isInSlot,
			// param_clazz, param_names);
			// JptLoopNode jloop = new
			// JptLoopNode(relements.getLoopSourceExpression(), _isInSlot,
			// param_clazz, param_names, relements.getLoopIncrement());
			JptLoopNode jloop = new JptLoopNode(relements, _isInSlot);

			pnodes.push(jloop);
			process(el);
			JptStaticFragment sf_post = new JptStaticFragment(_sb.toString());
			jloop.appendChild(sf_post);
			_sb.delete(0, _sb.length());

			processTalReplace(el);
			processTalContent(el);

			pnodes.pop();

			((JptParentNode) pnodes.peek()).appendChild(jloop);
		}
	}

	private void processTalReplace(Element el)
	{
		Attribute attribute = XomUtils.getAttribute(el, "tal:replace");
		if (attribute != null)
		{
			el.removeAttribute(attribute);
			JptStaticFragment sf = new JptStaticFragment(_sb.toString());
			((JptParentNode) pnodes.peek()).appendChild(sf);
			_sb.delete(0, _sb.length());

			el.removeChildren();

			JptOutputExpressionNode jout;
			if (el.getBaseURI().equals(APPEND_SLOT_NS))
			{
				jout = new JptOutputExpressionNode(attribute.getValue(), true);
			}
			else
			{
				jout = new JptOutputExpressionNode(attribute.getValue(), _isInSlot);
			}

			// JptOutputExpressionNode jout = new
			// JptOutputExpressionNode(attribute.getValue(), _isInSlot);

			removePNodeChildren();
			((JptParentNode) pnodes.peek()).appendChild(jout);
		}
	}

	private void removePNodeChildren()
	{
	}

	public ArrayList<Dependency> getDependecies()
	{
		return _dependecies;
	}
}