package org.caudexorigo.jpt;

import org.caudexorigo.nu.xom.XPathContext;

public class JptConstants
{
	private JptConstants()
	{
	}

	public static final String METAL_NS_URI = "http://xml.zope.org/namespaces/metal";

	public static final String TAL_NS_URI = "http://xml.zope.org/namespaces/tal";

	public static final XPathContext XPATH_CTX = buildXapthCtx();

	private static XPathContext buildXapthCtx()
	{
		XPathContext xctx = new XPathContext();
		xctx.addNamespace("tal", "http://xml.zope.org/namespaces/tal");
		xctx.addNamespace("metal", "http://xml.zope.org/namespaces/metal");
		return xctx;
	}
}