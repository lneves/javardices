package org.caudexorigo.jpt.web;

public abstract class HttpJptController
{
	private HttpJptContext _httpContext;

	public final HttpJptContext getHttpContext()
	{
		return _httpContext;
	}

	public final void setHttpContext(HttpJptContext httpContext)
	{
		_httpContext = httpContext;
	}

	public abstract void init();

	public void redirect(String url)
	{
		_httpContext.redirect(url);
	}
}