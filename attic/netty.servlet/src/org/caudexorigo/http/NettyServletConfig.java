package org.caudexorigo.http;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class NettyServletConfig implements ServletConfig
{
	private String name;
	private final ConcurrentHashMap<String, String> initParameters = new ConcurrentHashMap<String, String>(16, 0.75f, 64);
	private NettyServletContext servletContextImpl;

	protected NettyServletConfig(NettyServletContext servletContextImpl, Map<String, String> initParameters)
	{
		this.servletContextImpl = servletContextImpl;
		this.setInitParameters(initParameters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getServletName()
	{
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ServletContext getServletContext()
	{
		return servletContextImpl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInitParameter(String name)
	{
		return initParameters.get(name);
	}

	protected void setInitParameters(Map<String, String> parameters)
	{
		this.initParameters.clear();
		this.initParameters.putAll(parameters);
	}

	/**
	 * Set the name of this servlet.
	 * 
	 * @param name
	 *            The new name of this servlet
	 */
	public void setServletName(String name)
	{
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumeration<String> getInitParameterNames()
	{
		return Collections.enumeration(initParameters.keySet());
	}
}