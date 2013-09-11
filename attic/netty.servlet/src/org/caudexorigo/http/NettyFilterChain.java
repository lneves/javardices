package org.caudexorigo.http;

import java.io.IOException;
import java.util.EventListener;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletResponse;

import org.caudexorigo.ErrorAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyFilterChain implements FilterChain
{
	private static Logger log = LoggerFactory.getLogger(NettyFilterChain.class);

	public static final int INCREMENT = 8;

	public NettyFilterChain()
	{
	}

	/**
	 * Filters.
	 */
	private NettyFilterConfig[] filters = new NettyFilterConfig[8];

	private final static Object[] lock = new Object[0];

	/**
	 * The int which is used to maintain the current position in the filter chain.
	 */
	private int pos = 0;
	/**
	 * The int which gives the current number of filters in the chain.
	 */
	private int n = 0;
	/**
	 * The servlet instance to be executed by this chain.
	 */
	private Servlet servlet = null;
	private NettyServletConfig servletConfigImpl;

	/**
	 * Initialize the {@link Filter}
	 * 
	 * @throws javax.servlet.ServletException
	 */
	protected void init() throws ServletException
	{
		for (NettyFilterConfig f : filters)
		{
			if (f != null)
			{
				f.getFilter().init(f);
			}
		}
	}

	// ---------------------------------------------------- FilterChain Methods
	protected void invokeFilterChain(ServletRequest request, ServletResponse response) throws IOException, ServletException
	{

		ServletRequestEvent event = new ServletRequestEvent(servletConfigImpl.getServletContext(), request);
		try
		{
			for (EventListener l : ((NettyServletContext) servletConfigImpl.getServletContext()).getListeners())
			{
				try
				{
					if (l instanceof ServletRequestListener)
					{
						((ServletRequestListener) l).requestInitialized(event);
					}
				}
				catch (Throwable t)
				{
					Throwable r = ErrorAnalyser.findRootCause(t);
					log.warn(r.getMessage(), r);
				}
			}
			pos = 0;
			doFilter(request, response);
		}
		finally
		{
			for (EventListener l : ((NettyServletContext) servletConfigImpl.getServletContext()).getListeners())
			{
				try
				{
					if (l instanceof ServletRequestListener)
					{
						((ServletRequestListener) l).requestDestroyed(event);
					}
				}
				catch (Throwable t)
				{
					Throwable r = ErrorAnalyser.findRootCause(t);
					log.warn(r.getMessage(), r);
				}
			}
		}
	}

	/**
	 * Invoke the next filter in this chain, passing the specified request and response. If there are no more filters in this chain, invoke the <code>service()</code> method of the
	 * servlet itself.
	 * 
	 * @param request
	 *            The servlet request we are processing
	 * @param response
	 *            The servlet response we are creating
	 * 
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet exception occurs
	 */
	public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException
	{
		// Call the next filter if there is one
		if (pos < n)
		{
			NettyFilterConfig filterConfig = null;

			synchronized (lock)
			{
				filterConfig = filters[pos++];
			}

			Filter filter = null;
			try
			{
				filter = filterConfig.getFilter();
				filter.doFilter(request, response, this);
			}
			catch (IOException e)
			{
				throw e;
			}
			catch (ServletException e)
			{
				throw e;
			}
			catch (RuntimeException e)
			{
				throw e;
			}
			catch (Throwable e)
			{
				throw new ServletException("Throwable", e);
			}

			return;
		}

		try
		{
			if (servlet != null)
			{
				servlet.service(request, response);
			}
		}
		catch (IOException e)
		{
			throw e;
		}
		catch (ServletException e)
		{
			throw e;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new ServletException("Throwable", e);
		}
	}

	// -------------------------------------------------------- Package Methods
	/**
	 * Add a filter to the set of filters that will be executed in this chain.
	 * 
	 * @param filterConfig
	 *            The FilterConfig for the servlet to be executed
	 */
	protected void addFilter(NettyFilterConfig filterConfig)
	{
		synchronized (lock)
		{
			if (n == filters.length)
			{
				NettyFilterConfig[] newFilters = new NettyFilterConfig[n + INCREMENT];
				System.arraycopy(filters, 0, newFilters, 0, n);
				filters = newFilters;
			}

			filters[n++] = filterConfig;
		}
	}

	/**
	 * Release references to the filters and servletConfigImpl executed by this chain.
	 */
	protected void recycle()
	{
		pos = 0;
	}

	/**
	 * Set the servlet that will be executed at the end of this chain. Set by the mapper filter
	 */
	protected void setServlet(NettyServletConfig configImpl, Servlet servlet)
	{
		this.servletConfigImpl = configImpl;
		this.servlet = servlet;
	}

	protected NettyFilterConfig getFilter(int i)
	{
		return filters[i];
	}

	protected Servlet getServlet()
	{
		return servlet;
	}

	protected NettyServletConfig getServletConfig()
	{
		return servletConfigImpl;
	}

	public void destroy()
	{
		for (NettyFilterConfig filter : filters)
		{
			if (filter != null)
				filter.recycle();
		}
		if (servlet != null)
		{
			servlet.destroy();
			servlet = null;
		}
		filters = null;
	}
}