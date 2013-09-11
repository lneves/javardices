package org.caudexorigo.jersey.netty.http;

import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpChunk;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
public class NettyHttpHandler extends ChannelInboundMessageHandlerAdapter<Object> implements Container
{

	private static Logger log = LoggerFactory.getLogger(NettyHttpHandler.class);

	public ApplicationHandler applicationHandler;
	private final ContainerLifecycleListener containerListener;
	private HttpRequest request;
	private boolean readingChunks;

	// O currentUserID sera um id enviado como header
	// pelo client
	private String currentUserID;
	protected static List<String> role = new ArrayList<String>();

	public NettyHttpHandler(ApplicationHandler webapp, ResourceConfig rc)
	{

		super();
		//
		role.add("1001");
		role.add("1002");
		role.add("1003");
		//
		this.applicationHandler = webapp;
		containerListener = ConfigHelper.getContainerLifecycleListener(webapp);

		if (log.isDebugEnabled())
		{
			log.debug(String.format("new (%s, %s)", webapp.toString(), rc.toString()));
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception
	{

		if (log.isDebugEnabled())
		{
			log.debug(String.format("messageReceived(%s, %s)", ctx.toString(), msg.toString()));
		}

		if (!readingChunks)
		{

			HttpRequest request = this.request = (HttpRequest) msg;

			currentUserID = request.getHeader("UserID");
			if (request.isChunked())
			{
				readingChunks = true;
				request.setChunked(false);
			}
			else
			{
				handleRequest(ctx, request);
			}

			if (is100ContinueExpected(request))
			{
				send100Continue(ctx);
			}

		}
		else
		{
			HttpChunk chunk = (HttpChunk) msg;
			if (chunk.isLast())
			{
				readingChunks = false;
				request.getContent().writeBytes(chunk.getContent());
				handleRequest(ctx, request);
			}
			else
			{
				request.setContent(chunk.getContent());
			}
		}

	}

	private void handleRequest(ChannelHandlerContext ctx, HttpRequest request) throws URISyntaxException
	{

		final URI requestUri = new URI(request.getUri());
		final ContainerRequest cRequest = new ContainerRequest(null, requestUri, request.getMethod().getName(), getSecurityContext(true), new MapPropertiesDelegate());

		cRequest.setEntityStream(new ByteBufInputStream(request.getContent()));

		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

		NettyHttpResponseWriter writer = new NettyHttpResponseWriter(ctx.channel(), request, response);
		for (Entry<String, String> e : request.getHeaders())
		{
			cRequest.headers(e.getKey(), e.getValue());
		}
		cRequest.setWriter(writer);
		applicationHandler.handle(cRequest);
	}

	private void send100Continue(ChannelHandlerContext ctx)
	{
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, CONTINUE);
		ctx.write(response);
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

	private SecurityContext getSecurityContext(final boolean isSecure)
	{
		return new SecurityContext()
		{

			@Override
			public boolean isUserInRole(String role)
			{
				for (String s : NettyHttpHandler.role)
					if (s.equals(getUserPrincipal().getName()))
						return true;
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
				return new Principal()
				{

					@Override
					public String getName()
					{
						return currentUserID;
					}
				};
			}

			@Override
			public String getAuthenticationScheme()
			{
				return null;
			}
		};
	}

}
