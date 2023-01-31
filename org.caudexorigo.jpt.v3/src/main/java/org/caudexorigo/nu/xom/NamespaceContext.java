package org.caudexorigo.nu.xom;

/**
 * Resolves namespace prefixes to namespace URIs.
 *
 * <p>
 * The prefixes used within an XPath expression are independent of those used within any
 * target document. When evaluating an XPath against a document, only the resolved
 * namespace URIs are compared, not their prefixes.
 * </p>
 *
 * <p>
 * A <code>NamespaceContext</code> is responsible for translating prefixes as they appear
 * in XPath expressions into URIs for comparison. A document's prefixes are resolved
 * internal to the document based upon its own namespace nodes.
 * </p>
 * 
 * <p>
 * Implementations of this interface should implement <code>Serializable</code>.
 * </p>
 *
 * @see BaseXPath
 * @see Navigator#getElementNamespaceUri
 * @see Navigator#getAttributeNamespaceUri
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public interface NamespaceContext {
  /**
   * Translate the provided namespace prefix into the matching bound namespace URI.
   *
   * <p>
   * In XPath, there is no such thing as a 'default namespace'. The empty prefix
   * <strong>always</strong> resolves to the empty namespace URI. This method should
   * return null for the empty prefix. Similarly, the prefix "xml" always resolves to the
   * URI "http://www.w3.org/XML/1998/namespace".
   * </p>
   *
   * @param prefix the namespace prefix to resolve
   *
   * @return the namespace URI bound to the prefix; or null if there is no such namespace
   */
  String translateNamespacePrefixToUri(String prefix);
}
