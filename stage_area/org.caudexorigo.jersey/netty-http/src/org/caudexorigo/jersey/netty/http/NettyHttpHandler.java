package org.caudexorigo.jersey.netty.http;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.ConfigHelper;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public class NettyHttpHandler extends ChannelInboundMessageHandlerAdapter<HttpRequest> implements Container
{
	private static Logger log = LoggerFactory.getLogger(NettyHttpHandler.class);
	
	public ApplicationHandler applicationHandler;

	private final ContainerLifecycleListener containerListener;

	public NettyHttpHandler(ApplicationHandler webapp, ResourceConfig rc)
	{
		super();
		this.applicationHandler = webapp;
		containerListener = ConfigHelper.getContainerLifecycleListener(webapp);

		if (log.isDebugEnabled())
		{
			log.debug(String.format("new (%s, %s)", webapp.toString(), rc.toString()));
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, HttpRequest request) throws URISyntaxException
	{
		if (log.isDebugEnabled())
		{
			log.debug(String.format("messageReceived(%s, %s)", ctx.toString(), request.toString()));
		}

		final URI requestUri = new URI(request.getUri());
		final ContainerRequest cRequest = new ContainerRequest(null, requestUri, request.getMethod().getName(), getSecurityContext(false), new MapPropertiesDelegate());
		
		cRequest.setEntityStream(new ByteBufInputStream(request.getContent()));
		
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		
		NettyHttpResponseWriter writer = new NettyHttpResponseWriter(ctx.channel(), request, response);
		cRequest.getHeaders();
		cRequest.setWriter(writer);
		applicationHandler.handle(cRequest);
	}

	private SecurityContext getSecurityContext(final boolean isSecure)
	{
		return new SecurityContext()
		{
			@Override
			public boolean isUserInRole(String role)
			{
				return false;
			}

			@Override
			public boolean isSecure()
			{
				return isSecure;
			}

			@Override
			public Principal getUserPrincipal()
			{
				return null;
			}

			@Override
			public String getAuthenticationScheme()
			{
				return null;
			}
		};
	}

	@Override
	public ResourceConfig getConfiguration()
	{
		return applicationHandler.getConfiguration();
	}

	@Override
	public void reload()
	{
		log.debug("reload()");
		reload(getConfiguration());
	}

	@Override
	public void reload(ResourceConfig configuration)
	{
		applicationHandler = new ApplicationHandler(configuration);
		containerListener.onReload(this);
	}
}