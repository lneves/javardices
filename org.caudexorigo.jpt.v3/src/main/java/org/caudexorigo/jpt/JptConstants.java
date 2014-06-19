package org.caudexorigo.jpt;

import nu.xom.XPathContext;

public class JptConstants
{
	private JptConstants()
	{
	}

	public static final String METAL_NS_URI = "http://xml.zope.org/namespaces/metal";

	public static final String TAL_NS_URI = "http://xml.zope.org/namespaces/tal";

	public static final XPathContext XPATH_CTX;
	
	static
	{
		XPATH_CTX = new XPathContext();
		XPATH_CTX.addNamespace("tal", "http://xml.zope.org/namespaces/tal");
		XPATH_CTX.addNamespace("metal", "http://xml.zope.org/namespaces/metal");
	}
}