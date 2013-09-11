package org.caudexorigo.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.http.netty.MimeTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServletContext implements ServletContext
{
	private static Logger log = LoggerFactory.getLogger(NettyServletContext.class);

	private final transient List<EventListener> eventListeners = new ArrayList<EventListener>();

	private static final List<String> empty = Collections.emptyList();

	/**
	 * The merged context initialization parameters for this Context.
	 */
	private final ConcurrentHashMap<String, String> parameters = new ConcurrentHashMap<String, String>(16, 0.75f, 64);

	/**
	 * The context attributes for this context.
	 */
	private final ConcurrentHashMap<String, Object> attributes = new ConcurrentHashMap<String, Object>(16, 0.75f, 64);

	private String contextPath = "";

	private String basePath = "";

	// display-name
	private String contextName = "";

	// ----------------------------------------------------------------- //

	/**
	 * Stop {@link ServletContextListener}
	 */
	protected void destroyListeners()
	{
		ServletContextEvent event = null;
		for (int i = 0; i < eventListeners.size(); i++)
		{
			if (!(eventListeners.get(i) instanceof ServletContextListener))
				continue;
			ServletContextListener listener = (ServletContextListener) eventListeners.get(i);
			if (event == null)
			{
				event = new ServletContextEvent(this);
			}
			try
			{
				listener.contextDestroyed(event);
			}
			catch (Throwable t)
			{
				logWarn(t);
			}
		}
	}

	/**
	 * Return the value of the specified context attribute, if any; otherwise return <code>null</code>.
	 * 
	 * @param name
	 *            Name of the context attribute to return
	 */
	@Override
	public Object getAttribute(String name)
	{
		return attributes.get(name);
	}

	/**
	 * Return an enumeration of the names of the context attributes associated with this context.
	 */
	@Override
	public Enumeration<String> getAttributeNames()
	{
		return Collections.enumeration(attributes.keySet());
	}

	/**
	 * Return the current based path.
	 * 
	 * @return basePath
	 */
	protected String getBasePath()
	{
		return basePath;
	}

	/**
	 * Return a <code>ServletContext</code> object that corresponds to a specified URI on the server. This method allows servlets to gain access to the context for various parts of
	 * the server, and as needed obtain <code>RequestDispatcher</code> objects or resources from the context. The given path must be absolute (beginning with a "/"), and is
	 * interpreted based on our virtual host's document root.
	 * 
	 * @param uri
	 *            Absolute URI of a resource on the server
	 */
	@Override
	public ServletContext getContext(String uri)
	{
		return this;
	}

	public String getContextPath()
	{
		return contextPath;
	}

	/**
	 * Return the value of the specified initialization parameter, or <code>null</code> if this parameter does not exist.
	 * 
	 * @param name
	 *            Name of the initialization parameter to retrieve
	 */
	@Override
	public String getInitParameter(final String name)
	{
		return parameters.get(name);
	}

	/**
	 * Return the names of the context's initialization parameters, or an empty enumeration if the context has no initialization parameters.
	 */
	public Enumeration<String> getInitParameterNames()
	{
		return Collections.enumeration(parameters.keySet());
	}

	/**
	 * Return the Servlet Listener.
	 * 
	 * @return return the Servlet Listener.
	 */
	protected List<EventListener> getListeners()
	{
		return eventListeners;
	}

	/**
	 * Return the major version of the Java Servlet API that we implement.
	 */
	@Override
	public int getMajorVersion()
	{
		return 2;
	}

	/**
	 * Return the MIME type of the specified file, or <code>null</code> if the MIME type cannot be determined.
	 * 
	 * @param file
	 *            Filename for which to identify a MIME type
	 */
	@Override
	public String getMimeType(String file)
	{
		return MimeTable.getContentType(file);
	}

	/**
	 * Return the minor version of the Java Servlet API that we implement.
	 */
	@Override
	public int getMinorVersion()
	{
		return 5;
	}

	/**
	 * {@inheritDoc}
	 */
	// TODO.
	@Override
	public RequestDispatcher getNamedDispatcher(String name)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Return the real path for a given virtual path, if possible; otherwise return <code>null</code>.
	 * 
	 * @param path
	 *            The path to the desired resource
	 */
	@Override
	public String getRealPath(String path)
	{
		if (path == null)
		{
			return null;
		}

		return new File(basePath, path).getAbsolutePath();
	}

	/**
	 * {@inheritDoc}
	 */
	// TODO.
	@Override
	public RequestDispatcher getRequestDispatcher(String name)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Return the URL to the resource that is mapped to a specified path. The path must begin with a "/" and is interpreted as relative to the current context root.
	 * 
	 * @param path
	 *            The path to the desired resource
	 * 
	 * @exception MalformedURLException
	 *                if the path is not given in the correct form
	 */
	@Override
	public URL getResource(String path) throws MalformedURLException
	{

		if (path == null || !path.startsWith("/"))
		{
			throw new MalformedURLException(path);
		}

		path = normalize(path);
		if (path == null)
			return (null);

		// Help the UrlClassLoader, which is not able to load resources
		// that contains '//'
		if (path.length() > 1)
		{
			path = path.substring(1);
		}

		URL url = Thread.currentThread().getContextClassLoader().getResource(path);
		return url;
	}

	/**
	 * Return the requested resource as an {@link InputStream}. The path must be specified according to the rules described under <code>getResource</code>. If no such resource can
	 * be identified, return <code>null</code>.
	 * 
	 * @param path
	 *            The path to the desired resource.
	 */
	@Override
	public InputStream getResourceAsStream(String path)
	{

		path = normalize(path);
		if (path == null)
			return (null);

		// Help the UrlClassLoader, which is not able to load resources
		// that contains '//'
		if (path.length() > 1)
		{
			path = path.substring(1);
		}

		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}

	/**
	 * Return a Set containing the resource paths of resources member of the specified collection. Each path will be a String starting with a "/" character. The returned set is
	 * immutable.
	 * 
	 * @param path
	 *            Collection path
	 */
	@Override
	public Set<String> getResourcePaths(String path)
	{
		// Validate the path argument
		if (path == null)
		{
			return null;
		}
		if (!path.startsWith("/"))
		{
			throw new IllegalArgumentException(path);
		}

		path = normalize(path);
		if (path == null)
			return (null);

		File[] files = new File(basePath, path).listFiles();
		Set<String> list = new HashSet<String>();
		if (files != null)
		{
			for (File f : files)
			{
				try
				{
					String canonicalPath = f.getCanonicalPath();

					// add a trailing "/" if a folder
					if (f.isDirectory())
					{
						canonicalPath = canonicalPath + "/";
					}

					canonicalPath = canonicalPath.substring(canonicalPath.indexOf(basePath) + basePath.length());
					list.add(canonicalPath.replace("\\", "/"));
				}
				catch (IOException ex)
				{
					throw new RuntimeException(ex);
				}
			}
		}
		return list;
	}

	@Override
	public String getServerInfo()
	{
		return "Netty based servlet engine";
	}

	/**
	 * @deprecated As of Java Servlet API 2.1, with no direct replacement.
	 */
	@Override
	public Servlet getServlet(String name)
	{
		return (null);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getServletContextName()
	{
		return contextName;
	}

	/**
	 * @deprecated As of Java Servlet API 2.1, with no direct replacement.
	 */
	@Override
	public Enumeration<String> getServletNames()
	{
		return Collections.enumeration(empty);
	}

	/**
	 * @deprecated As of Java Servlet API 2.1, with no direct replacement.
	 */
	@Override
	public Enumeration<String> getServlets()
	{
		return Collections.enumeration(empty);
	}

	/**
	 * Notify the {@link ServletContextListener} that we are starting.
	 */
	protected void initListeners(List<String> listeners)
	{

		for (String listenerClass : listeners)
		{
			EventListener el = null;
			try
			{
				el = (EventListener) Thread.currentThread().getContextClassLoader().loadClass(listenerClass).newInstance();
				eventListeners.add(el);
			}
			catch (Throwable t)
			{
				logWarn(t);
			}
		}

		ServletContextEvent event = null;
		for (int i = 0; i < eventListeners.size(); i++)
		{
			if (!(eventListeners.get(i) instanceof ServletContextListener))
				continue;
			ServletContextListener listener = (ServletContextListener) eventListeners.get(i);
			if (event == null)
			{
				event = new ServletContextEvent(this);
			}
			try
			{
				listener.contextInitialized(event);
			}
			catch (Throwable t)
			{
				logWarn(t);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(Exception e, String msg)
	{
		log.error(msg, e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(String msg)
	{
		log.info(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(String msg, Throwable t)
	{
		log.info(msg, t);
	}

	private void logWarn(Throwable t)
	{
		Throwable r = ErrorAnalyser.findRootCause(t);
		log.warn(r.getMessage(), r);
	}

	/**
	 * Return a context-relative path, beginning with a "/", that represents the canonical version of the specified path after ".." and "." elements are resolved out. If the
	 * specified path attempts to go outside the boundaries of the current context (i.e. too many ".." path elements are present), return <code>null</code> instead.
	 * 
	 * @param path
	 *            Path to be normalized
	 */
	protected String normalize(String path)
	{

		if (path == null)
		{
			return null;
		}

		String normalized = path;

		// Normalize the slashes and add leading slash if necessary
		if (normalized.indexOf('\\') >= 0)
			normalized = normalized.replace('\\', '/');

		// Resolve occurrences of "/../" in the normalized path
		while (true)
		{
			int index = normalized.indexOf("/../");
			if (index < 0)
				break;
			if (index == 0)
				return (null); // Trying to go outside our context
			int index2 = normalized.lastIndexOf('/', index - 1);
			normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
		}

		// Return the normalized path that we have completed
		return (normalized);

	}

	/**
	 * Remove the context attribute with the specified name, if any.
	 * 
	 * @param name
	 *            Name of the context attribute to be removed
	 */
	public void removeAttribute(String name)
	{
		Object value = attributes.remove(name);
		if (value == null)
			return;

		ServletContextAttributeEvent event = null;
		for (int i = 0; i < eventListeners.size(); i++)
		{
			if (!(eventListeners.get(i) instanceof ServletContextAttributeListener))
				continue;
			ServletContextAttributeListener listener = (ServletContextAttributeListener) eventListeners.get(i);
			try
			{
				if (event == null)
				{
					event = new ServletContextAttributeEvent(this, name, value);

				}
				listener.attributeRemoved(event);
			}
			catch (Throwable t)
			{
				logWarn(t);
			}
		}
	}

	/**
	 * Bind the specified value with the specified context attribute name, replacing any existing value for that name.
	 * 
	 * @param name
	 *            Attribute name to be bound
	 * @param value
	 *            New attribute value to be bound
	 */
	@Override
	public void setAttribute(String name, Object value)
	{

		// Name cannot be null
		if (name == null)
			throw new IllegalArgumentException("Cannot be null");

		// Null value is the same as removeAttribute()
		if (value == null)
		{
			removeAttribute(name);
			return;
		}

		Object oldValue = attributes.put(name, value);

		ServletContextAttributeEvent event = null;
		for (int i = 0; i < eventListeners.size(); i++)
		{
			if (!(eventListeners.get(i) instanceof ServletContextAttributeListener))
				continue;
			ServletContextAttributeListener listener = (ServletContextAttributeListener) eventListeners.get(i);
			try
			{
				if (event == null)
				{
					if (oldValue != null)
						event = new ServletContextAttributeEvent(this, name, oldValue);
					else
						event = new ServletContextAttributeEvent(this, name, value);

				}
				if (oldValue != null)
				{
					listener.attributeReplaced(event);
				}
				else
				{
					listener.attributeAdded(event);
				}
			}
			catch (Throwable t)
			{
				logWarn(t);
			}
		}
	}

	/**
	 * Set the basePath used by the {@link #getRealPath}.
	 * 
	 * @param basePath
	 */
	protected void setBasePath(String basePath)
	{
		this.basePath = basePath;
	}

	/**
	 * Programmatically set the context path of the Servlet.
	 * 
	 * @param contextPath
	 */
	protected void setContextPath(String contextPath)
	{
		this.contextPath = contextPath;
	}

	/**
	 * Set the Servlet context name (display-name)
	 * 
	 * @param contextName
	 */
	public void setDisplayName(String contextName)
	{
		this.contextName = contextName;
	}

	// ----------------------------------- Listener implementation ----------//

	protected void setInitParameter(Map<String, String> parameters)
	{
		this.parameters.clear();
		this.parameters.putAll(parameters);
	}
}