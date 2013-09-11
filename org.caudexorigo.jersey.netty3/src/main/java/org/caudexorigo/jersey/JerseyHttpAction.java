package org.caudexorigo.jersey;

import java.net.URI;

import javax.ws.rs.core.SecurityContext;

import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.http.netty.HttpRequestWrapper;
import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.internal.PropertiesDelegate;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.ConfigHelper;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyHttpAction extends HttpAction implements Container
{
	private static Logger log = LoggerFactory.getLogger(JerseyHttpAction.class);

	public ApplicationHandler webappHandler;
	private final ContainerLifecycleListener containerListener;
	private final ResourceConfig resConf;

	private final SecurityContext securityCtx;

	public JerseyHttpAction(ApplicationHandler webappHandler, ResourceConfig resConf, SecurityContext securityCtx)
	{
		this.webappHandler = webappHandler;
		this.resConf = resConf;
		this.securityCtx = securityCtx;

		containerListener = ConfigHelper.getContainerLifecycleListener(webappHandler);
		log.debug(String.format("new (%s, %s)", webappHandler.toString(), resConf.toString()));
	}

	@Override
	public void service(ChannelHandlerContext ctx, HttpRequest req, HttpResponse response)
	{
		try
		{
			HttpRequestWrapper request = (HttpRequestWrapper) req;
			String base = request.getUri();
			final URI baseUri = new URI(base);
			final URI requestUri = new URI(base.substring(0, base.length() - 1) + request.getUri());

			PropertiesDelegate properties = new MapPropertiesDelegate();

			ContainerRequest containerRequest = new ContainerRequest(baseUri, requestUri, request.getMethod().getName(), securityCtx, properties);
			containerRequest.setEntityStream(new ChannelBufferInputStream(request.getContent()));
			containerRequest.setWriter(new JerseyNettyResponseWriter(response));

			webappHandler.handle(containerRequest);
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	@Override
	public ResourceConfig getConfiguration()
	{
		return resConf;
	}

	@Override
	public void reload()
	{
		log.debug("reload(JerseyHttpAction)");
		reload(getConfiguration());
	}

	@Override
	public void reload(ResourceConfig resConf)
	{
		log.debug("reload(JerseyHttpAction, ResourceConfig)");
		webappHandler = new ApplicationHandler(resConf);
		containerListener.onReload(this);
	}
}