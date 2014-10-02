package org.caudexorigo.jpt.sample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.http.netty4.HttpAction;
import org.caudexorigo.http.netty4.RequestRouter;
import org.caudexorigo.http.netty4.StaticFileAction;
import org.caudexorigo.jpt.web.netty.NettyWebJptAction;

public class AppMapper implements RequestRouter
{
	private final Map<String, HttpAction> routes;
	private final HttpAction default_action;

	public AppMapper(URI root_dir)
	{
		default_action = new StaticFileAction(root_dir);
		routes = new HashMap<>();
		routes.put("/CustomerList", new NettyWebJptAction(root_dir.resolve("CustomerList.jpt")));

	}

	@Override
	public HttpAction map(ChannelHandlerContext ctx, FullHttpRequest req)
	{
		String uri = req.getUri();
		String path = StringUtils.substringBefore(uri, "?");

		HttpAction action = routes.get(path);

		if (action != null)
		{
			return action;
		}
		else
		{
			return default_action;
		}
	}
}