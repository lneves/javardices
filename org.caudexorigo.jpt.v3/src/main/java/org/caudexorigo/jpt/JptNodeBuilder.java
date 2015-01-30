package org.caudexorigo.jpt;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
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

import org.apache.commons.lang3.StringUtils;
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

		if (XomUtils.getAttribute(el, "tal:omit-tag") != null)
		{
			processTalOmitTag(el);
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
		addStaticFragments();

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
			addStaticFragments();
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

	private Map<String, String> extractMacroParams(String uri_qs)
	{
		try
		{
			Map<String, String> query_pairs = new HashMap<String, String>();
			String[] pairs = uri_qs.split("&");
			for (String pair : pairs)
			{
				int idx = pair.indexOf("=");
				query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			}
			return query_pairs;
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
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

	private void prepareMacro(URI muri, Document macroDocument, Node useMacroNode, String macroPath, Map<String, String> macroParams) throws ValidityException, ParsingException, IOException
	{

		String macroName = macroParams.get("macro");
		// System.out.println("JptNodeBuilder.prepareMacro.macroName: " + macroName);
		// System.out.println("JptNodeBuilder.prepareMacro.macroPath: " + macroPath);

		Document macro_doc = macroDocument.getDocument();
		findSlotActorsInDocument(macroDocument);
		prepareSlotsActorsInDocument();
		Element[] defined_macro = new Element[1];
		XomUtils.findSpecificMacro(macro_doc, macroName, defined_macro);
		Element subtree = defined_macro[0];

		if (subtree == null)
		{
			throw new IllegalArgumentException(String.format("could not find macro '%s' in template '%s'", macroName, macroPath));
		}

		String object_class_name = ContextBuilder.objectNameFromInstructions(macro_doc);

		JptMacroNode jpt_macro_node = new JptMacroNode(muri, object_class_name, _isInSlot, macroParams);

		addStaticFragments();
		pnodes.push(jpt_macro_node);
		_isInSlot = false;

		prepareElement(subtree);

		pnodes.pop();

		((JptParentNode) pnodes.peek()).appendChild(jpt_macro_node);
	}

	@Override
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
			addStaticFragments();
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
			addStaticFragments();
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
			// System.out.println("JptNodeBuilder.processTalCondition.el: " + el.toXML());
			// System.out.println("JptNodeBuilder.processTalCondition.expression: " + current_attribute_value);
			// System.out.println("JptNodeBuilder.processTalCondition.sb: »»»»" + _sb.toString() + "«««");

			addStaticFragments();
			el.removeAttribute(attribute);

			JptConditionalNode jcond = new JptConditionalNode(current_attribute_value, _isInSlot);
			pnodes.push(jcond);

			process(el);

			addStaticFragments();

			// processTalRepeat(el);
			// processTalReplace(el);
			// processTalContent(el);

			pnodes.pop();
			((JptParentNode) pnodes.peek()).appendChild(jcond);
		}
	}

	private void processTalOmitTag(Element el)
	{
		Attribute omit_tag_attr = XomUtils.getAttribute(el, "tal:omit-tag");

		if (omit_tag_attr != null)
		{
			// System.out.println("JptNodeBuilder.processTalOmitTag.parent.begin: " + (((JptParentNode) pnodes.peek())).getClass().getCanonicalName());
			// System.out.println("JptNodeBuilder.processTalOmitTag.el: " + el.toXML());

			el.removeAttribute(omit_tag_attr);

			addStaticFragments();

			String omit_tag_condition = StringUtils.isBlank(omit_tag_attr.getValue()) ? "false" : String.format("!%s", omit_tag_attr.getValue());// conditional nodes output on true, revert the logic, should not output on true

			JptHolderNode holder_node = new JptHolderNode(_isInSlot);
			((JptParentNode) pnodes.peek()).appendChild(holder_node);
			pnodes.push(holder_node);

			JptConditionalNode start_cond = new JptConditionalNode(omit_tag_condition, _isInSlot);
			pnodes.push(start_cond);

			processStartTag(el);

			addStaticFragments();

			pnodes.pop(); // remove conditional node
			((JptParentNode) pnodes.peek()).appendChild(start_cond);

			int child_count = el.getChildCount();

			for (int i = 0; i < child_count; i++)
			{
				Node c = el.getChild(i);
				processChild(c);
			}

			addStaticFragments();

			JptConditionalNode end_cond = new JptConditionalNode(omit_tag_condition, _isInSlot);
			pnodes.push(end_cond);

			processEndTag(el);
			addStaticFragments();

			pnodes.pop(); // remove conditional node
			((JptParentNode) pnodes.peek()).appendChild(end_cond);

			pnodes.pop(); // remove holder node

			// System.out.println("JptNodeBuilder.processTalOmitTag.parent.end: " + (((JptParentNode) pnodes.peek())).getClass().getCanonicalName());
		}
		else
		{
			process(el);
		}
	}

	private void addStaticFragments()
	{
		JptStaticFragment sf = new JptStaticFragment(_sb.toString());
		JptParentNode parent = ((JptParentNode) pnodes.peek());
		parent.appendChild(sf);
		_sb.delete(0, _sb.length());
	}

	private void processTalContent(Element el)
	{
		Attribute attribute = XomUtils.getAttribute(el, "tal:content");
		if (attribute != null)
		{
			el.removeAttribute(attribute);

			addStaticFragments();

			Attribute omit_tag_attr = XomUtils.getAttribute(el, "tal:omit-tag");

			if (omit_tag_attr != null)
			{
				el.removeAttribute(omit_tag_attr);

				String omit_tag_condition = StringUtils.isBlank(omit_tag_attr.getValue()) ? "false" : String.format("!%s", omit_tag_attr.getValue());// conditional nodes output on true, revert the logic, should not output on true

				JptHolderNode holder_node = new JptHolderNode(_isInSlot);
				((JptParentNode) pnodes.peek()).appendChild(holder_node);
				pnodes.push(holder_node);

				JptConditionalNode start_cond = new JptConditionalNode(omit_tag_condition, _isInSlot);
				pnodes.push(start_cond);

				processStartTag(el);

				addStaticFragments();

				pnodes.pop();
				((JptParentNode) pnodes.peek()).appendChild(start_cond);
				pnodes.pop();
				// processStartTag(el);
			}
			else
			{
				removePNodeChildren();
				processStartTag(el);
			}

			addStaticFragments();
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

			((JptParentNode) pnodes.peek()).appendChild(jout);

			if (omit_tag_attr != null)
			{
				String omit_tag_condition = StringUtils.isBlank(omit_tag_attr.getValue()) ? "false" : String.format("!%s", omit_tag_attr.getValue());// conditional nodes output on true, revert the logic, should not output on true
				JptConditionalNode end_cond = new JptConditionalNode(omit_tag_condition, _isInSlot);
				pnodes.push(end_cond);

				processEndTag(el);
				addStaticFragments();

				pnodes.pop(); // remove conditional node
				((JptParentNode) pnodes.peek()).appendChild(end_cond);
			}
			else
			{
				processEndTag(el);
			}

			el.removeChildren();
			addStaticFragments();
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
			// String macroName = extractMacroName(macroURI.getQuery());
			String macroPath = macroURI.getPath();
			URI macroUri = _templateUri.resolve(macroPath);
			Dependency d = new Dependency(macroUri);
			_dependecies.add(d);

			Map<String, String> macroParams = extractMacroParams(macroURI.getRawQuery());
			Document document_macro = XomDocumentBuilder.getDocument(macroUri);
			prepareMacro(macroURI, document_macro, el, macroPath, macroParams);
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
			// System.out.println("JptNodeBuilder.processTalRepeat.el: »»»" + el.toXML() + "«««");
			el.removeAttribute(attribute);
			String current_attribute_value = attribute.getValue();

			addStaticFragments();

			String padding = "";

			RepeatElements relements = new RepeatElements(current_attribute_value, padding);

			JptLoopNode jloop = new JptLoopNode(relements, _isInSlot);

			pnodes.push(jloop);
			process(el);

			addStaticFragments();

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
			addStaticFragments();

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