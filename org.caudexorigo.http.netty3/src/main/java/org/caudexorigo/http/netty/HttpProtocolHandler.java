package org.caudexorigo.http.netty;

import static org.jboss.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.FileNotFoundException;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.http.netty.reporting.ResponseFormatter;
import org.caudexorigo.http.netty.reporting.StandardResponseFormatter;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.jboss.netty.handler.codec.http.websocket.WebSocketFrame;
//import org.jboss.netty.handler.codec.http.websocket.WebSocketFrameDecoder;
//import org.jboss.netty.handler.codec.http.websocket.WebSocketFrameEncoder;

@Sharable
public class HttpProtocolHandler extends SimpleChannelUpstreamHandler
{
	private static Logger log = LoggerFactory.getLogger(HttpProtocolHandler.class);

	private final ResponseFormatter _rspFmt;
	private final RequestRouter _requestMapper;
	private final RequestObserver _requestObserver;

	// private boolean _hasWebSocketSupport;
	// private Map<String, WebSocketHandler> _webSocketHandlers = new ConcurrentHashMap<String, WebSocketHandler>();

	public HttpProtocolHandler(RequestRouter requestMapper)
	{
		_requestMapper = requestMapper;
		_rspFmt = new StandardResponseFormatter(false);
		_requestObserver = new DefaultObserver();
	}

	public HttpProtocolHandler(RequestRouter requestMapper, RequestObserver customObserver, ResponseFormatter customResponseFormtter)
	{
		_requestMapper = requestMapper;

		if (customResponseFormtter == null)
		{
			_rspFmt = new StandardResponseFormatter(false);
		}
		else
		{
			_rspFmt = customResponseFormtter;
		}

		if (customObserver == null)
		{
			_requestObserver = new DefaultObserver();
		}
		else
		{
			_requestObserver = customObserver;
		}
	}

	// public void addWebSocketHandler(String path, WebSocketHandler webSocketHandler)
	// {
	// if (webSocketHandler == null)
	// {
	// throw new IllegalArgumentException("WebSocketHandler can not be null");
	// }
	// if (StringUtils.isBlank(path))
	// {
	// throw new IllegalArgumentException("WebSocketHandler Path can not be blank");
	// }
	//
	// _webSocketHandlers.put(path, webSocketHandler);
	// _hasWebSocketSupport = true;
	// }

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
	{
		Channel ch = e.getChannel();
		ch.close();
		if (log.isDebugEnabled())
		{
			Throwable rootCause = ErrorAnalyser.findRootCause(e.getCause());
			log.debug(rootCause.getMessage(), rootCause);
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		Object msg = e.getMessage();
		Channel ch = e.getChannel();

		if (msg instanceof HttpRequest)
		{
			HttpRequest req = (HttpRequest) msg;

			if (is100ContinueExpected(req))
			{
				Channels.write(ctx, Channels.future(ch), new DefaultHttpResponse(HTTP_1_1, CONTINUE));
			}

			handleHttpRequest(ctx, req);
		}
		else
		{
			throw new IllegalArgumentException("Invalid Object type received by HttpHandler");
		}
		// else if (msg instanceof WebSocketFrame)
		// {
		// if (_hasWebSocketSupport)
		// {
		// Channel channel = ctx.getChannel();
		//
		// WebSocketHandler ws_handler = getWebsocketHandler(ctx);
		//
		// if (ws_handler != null)
		// {
		// ws_handler.handleMessage(channel, (WebSocketFrame) msg);
		// }
		// }
		// }
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest request)
	{
		// if (_hasWebSocketSupport && _webSocketHandlers.containsKey(request.getUri()) && HttpHeaders.Values.UPGRADE.equalsIgnoreCase(request.getHeader(HttpHeaders.Names.CONNECTION)) && HttpHeaders.Values.WEBSOCKET.equalsIgnoreCase(request.getHeader(HttpHeaders.Names.UPGRADE)))
		// {
		// Channel channel = ctx.getChannel();
		// String uri = request.getUri();
		// String origin = request.getHeader(HttpHeaders.Names.ORIGIN);
		// String location = getWebSocketLocation(request);
		//
		// if (log.isDebugEnabled())
		// {
		// log.debug("Received WebSocket upgrade request. Origin: '{}'; Location: '{}'", origin, location);
		// }
		//
		// _webSocketHandlers.get(uri).handleWebSocketOpened(ctx.getChannel());
		//
		// // Create the WebSocket handshake response.
		// HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(101, "Web Socket Protocol Handshake"));
		// response.addHeader(HttpHeaders.Names.UPGRADE, HttpHeaders.Values.WEBSOCKET);
		// response.addHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.UPGRADE);
		//
		// if (request.containsHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY1) && request.containsHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY2))
		// {
		// // New handshake method with a challenge:
		// response.addHeader(HttpHeaders.Names.SEC_WEBSOCKET_ORIGIN, request.getHeader(HttpHeaders.Names.ORIGIN));
		// response.addHeader(HttpHeaders.Names.SEC_WEBSOCKET_LOCATION, getWebSocketLocation(request));
		// String protocol = response.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_PROTOCOL);
		// if (protocol != null)
		// {
		// response.addHeader(HttpHeaders.Names.SEC_WEBSOCKET_PROTOCOL, protocol);
		// }
		//
		// // Calculate the answer of the challenge.
		// String key1 = request.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY1);
		// String key2 = request.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY2);
		// int a = (int) (Long.parseLong(key1.replaceAll("[^0-9]", "")) / key1.replaceAll("[^ ]", "").length());
		// int b = (int) (Long.parseLong(key2.replaceAll("[^0-9]", "")) / key2.replaceAll("[^ ]", "").length());
		// long c = request.getContent().readLong();
		// ChannelBuffer input = ChannelBuffers.buffer(16);
		// input.writeInt(a);
		// input.writeInt(b);
		// input.writeLong(c);
		//
		// try
		// {
		// ChannelBuffer output = ChannelBuffers.wrappedBuffer(MessageDigest.getInstance("MD5").digest(input.array()));
		// response.setContent(output);
		// }
		// catch (NoSuchAlgorithmException e)
		// {
		// throw new RuntimeException(e);
		// }
		// }
		// else
		// {
		// response.addHeader(HttpHeaders.Names.WEBSOCKET_ORIGIN, origin);
		// response.addHeader(HttpHeaders.Names.WEBSOCKET_LOCATION, location);
		// String protocol = request.getHeader(HttpHeaders.Names.WEBSOCKET_PROTOCOL);
		// if (protocol != null)
		// {
		// response.addHeader(HttpHeaders.Names.WEBSOCKET_PROTOCOL, protocol);
		// }
		// }
		//
		// channel.write(response);
		//
		// ChannelPipeline pipeline = channel.getPipeline();
		// pipeline.replace("http-encoder", "websocket-encoder", new WebSocket13FrameEncoder(false));
		// pipeline.replace("http-decoder", "websocket-decoder", new WebSocket13FrameDecoder(false, false));
		//
		// ctx.setAttachment("IS_WS::" + uri);
		//
		// return;
		// }

		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

		try
		{
			HttpAction action = _requestMapper.map(ctx, request);

			observeBegin(ctx, request, response);

			if (action != null)
			{
				action.process(ctx, request, response);
			}
			else
			{
				HttpAction errorAction = new ErrorAction(new WebException(new FileNotFoundException(), HttpResponseStatus.NOT_FOUND.getCode()), _rspFmt);
				errorAction.process(ctx, request, response);
			}
		}
		catch (Throwable t)
		{
			HttpAction errorAction = new ErrorAction(new WebException(t, HttpResponseStatus.INTERNAL_SERVER_ERROR.getCode()), _rspFmt);
			errorAction.process(ctx, request, response);
		}
		finally
		{
			observeEnd(ctx, request, response);
		}
	}

	private void observeBegin(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		try
		{
			_requestObserver.begin(ctx, request, response);
		}
		catch (Throwable t)
		{
			Throwable r = ErrorAnalyser.findRootCause(t);
			log.error(r.getMessage(), r);
		}
	}

	private void observeEnd(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		try
		{
			_requestObserver.end(ctx, request, response);
		}
		catch (Throwable t)
		{
			Throwable r = ErrorAnalyser.findRootCause(t);
			log.error(r.getMessage(), r);
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		// if (_hasWebSocketSupport)
		// {
		// WebSocketHandler ws_handler = getWebsocketHandler(ctx);
		//
		// if (ws_handler != null)
		// {
		// ws_handler.handleWebSocketClosed(ctx.getChannel());
		// }
		// }

		super.channelClosed(ctx, e);
	}

	// private WebSocketHandler getWebsocketHandler(ChannelHandlerContext ctx)
	// {
	// Object attach = ctx.getAttachment();
	// if (attach != null)
	// {
	// String s_attach = attach.toString();
	// if (s_attach.startsWith("IS_WS::"))
	// {
	// String uri = StringUtils.substringAfter(s_attach, "IS_WS::");
	//
	// if (StringUtils.isNotBlank(uri))
	// {
	// WebSocketHandler ws_handler = _webSocketHandlers.get(uri);
	// return ws_handler;
	// }
	// }
	// }
	//
	// return null;
	// }

	// private String getWebSocketLocation(HttpRequest req)
	// {
	// return "ws://" + req.getHeader(HttpHeaders.Names.HOST) + req.getUri();
	// }
}