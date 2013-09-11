package org.caudexorigo.http;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

public class NettyFilterConfig implements FilterConfig
{
	private static final List<String> empty = Collections.emptyList();

	/**
	 * The Context with which we are associated.
	 */
	private final NettyServletContext servletContext;

	/**
	 * The application Filter we are configured for.
	 */
	private final Filter filter;

	/**
	 * Filter's initParameters.
	 */
	private Map<String, String> initParameters = null;

	/**
	 * Filter name
	 */
	private String filterName;

	// ------------------------------------------------------------------ //

	public NettyFilterConfig(NettyServletContext servletContext, Filter filter)
	{
		this.servletContext = servletContext;
		this.filter = filter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInitParameter(String name)
	{
		if (initParameters == null)
		{
			return null;
		}
		return ((String) initParameters.get(name));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFilterName()
	{
		return filterName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumeration<String> getInitParameterNames()
	{
		if (initParameters == null)
		{
			return Collections.enumeration(empty);
		}
		else
		{
			return Collections.enumeration(initParameters.keySet());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ServletContext getServletContext()
	{
		return servletContext;
	}

	/**
	 * Return the application Filter we are configured for.
	 */
	public Filter getFilter()
	{
		return filter;
	}

	/**
	 * Release the Filter instance associated with this FilterConfig, if there is one.
	 */
	protected void recycle()
	{
		filter.destroy();
	}

	/**
	 * Set the {@link Filter}'s name associated with this object.
	 * 
	 * @param filter
	 */
	protected void setFilterName(String filterName)
	{
		this.filterName = filterName;
	}

	/**
	 * Set the init parameters associated with this associated {@link Filter}.
	 * 
	 * @param filter
	 */
	protected void setInitParameters(Map<String, String> initParameters)
	{
		this.initParameters = initParameters;
	}
}