package org.caudexorigo.http.netty;

import static org.jboss.netty.channel.Channels.pipeline;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.caudexorigo.Shutdown;
import org.caudexorigo.concurrent.CustomExecutors;
import org.caudexorigo.http.netty.reporting.ResponseFormatter;
import org.caudexorigo.http.netty.reporting.StandardResponseFormatter;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.ssl.SslHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHttpServer
{
	private static final String DEFAULT_HOST = "0.0.0.0";

	private static final int DEFAULT_PORT = 8080;

	private static Logger log = LoggerFactory.getLogger(NettyHttpServer.class);

	private Executor _bossExecutor;

	private String _host;

	private RequestRouter _mapper;
	private RequestObserver _requestObserver;
	private ResponseFormatter _rspFmt;

	private int _port;

	private Executor _workerExecutor;

	private final boolean _is_compression_enabled;

	// private Map<String, WebSocketHandler> _webSocketHandlers = new HashMap<String, WebSocketHandler>();
	// private boolean _hasWebSocketSupport;

	public NettyHttpServer()
	{
		this(DEFAULT_HOST, DEFAULT_PORT, false);
	}

	public NettyHttpServer(String host, int port)
	{

		this(host, port, false);
	}

	public NettyHttpServer(String host, int port, boolean is_compression_enabled)
	{
		_host = host;
		_port = port;
		_is_compression_enabled = is_compression_enabled;

	}

	public NettyHttpServer(String host, int port, boolean is_compression_enabled, Executor boss_executor, Executor worker_executor)
	{
		this(host, port, is_compression_enabled);
		_bossExecutor = boss_executor;
		_workerExecutor = worker_executor;
	}

	private synchronized Executor getBossThreadPool()
	{
		if (_bossExecutor == null)
		{
			_bossExecutor = CustomExecutors.newCachedThreadPool("http-boss");
		}
		return _bossExecutor;
	}

	public String getHost()
	{
		return _host;
	}

	public int getPort()
	{
		return _port;
	}

	public RequestRouter getRouter()
	{
		return _mapper;
	}

	private Executor getWorkerThreadPool()
	{
		if (_workerExecutor == null)
		{
			_workerExecutor = CustomExecutors.newCachedThreadPool("http-worker");
		}
		return _workerExecutor;
	}

	public void setHost(String host)
	{
		_host = host;
	}

	public void setPort(int port)
	{
		_port = port;
	}

	public void setRouter(RequestRouter mapper)
	{
		_mapper = mapper;
	}

	private ResponseFormatter getResponseFormtter()
	{
		if (_rspFmt != null)
		{
			return _rspFmt;
		}
		else
		{
			return new StandardResponseFormatter(false);
		}
	}

	protected RequestObserver getRequestObserver()
	{
		if (_requestObserver != null)
		{
			return _requestObserver;
		}
		else
		{
			return new DefaultObserver();
		}
	}

	public void setRequestObserver(RequestObserver requestObserver)
	{
		_requestObserver = requestObserver;
	}

	public void setResponseFormtter(ResponseFormatter rspFmt)
	{
		_rspFmt = rspFmt;
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

	public synchronized void start()
	{
		log.info("Starting Httpd");

		ChannelFactory factory = new NioServerSocketChannelFactory(getBossThreadPool(), getWorkerThreadPool());
		ServerBootstrap bootstrap = new ServerBootstrap(factory);

		ChannelPipelineFactory pf = new ChannelPipelineFactory()
		{
			@Override
			public ChannelPipeline getPipeline() throws Exception
			{
				try
				{
					// Create a default pipeline implementation.
					ChannelPipeline pipeline = pipeline();

					// if (_hasWebSocketSupport)
					// {
					// pipeline.addLast("policy-file", new PolicyFileRequestDecoder(getPort()));
					// }

					pipeline.addLast("http-decoder", new HttpRequestDecoder(4096, 8192, 256 * 1024));
					pipeline.addLast("http-encoder", new HttpResponseEncoder());

					if (_is_compression_enabled)
					{
						pipeline.addLast("http-compression", new HttpContentCompressor());
					}

					// if (_hasWebSocketSupport)
					// {
					// Set<String> ws_paths = _webSocketHandlers.keySet();
					//
					// for (String wspath : ws_paths)
					// {
					// http_handler.addWebSocketHandler(wspath, _webSocketHandlers.get(wspath));
					// }
					// }

					pipeline.addLast("http-handler", new HttpProtocolHandler(_mapper, getRequestObserver(), getResponseFormtter()));
					return pipeline;
				}
				catch (Throwable t)
				{
					Shutdown.now(t);
					return null;
				}
			}

		};

		setupBootStrap(bootstrap, pf);

		try
		{
			// Bind and start to accept incoming connections.
			InetSocketAddress inet = new InetSocketAddress(getHost(), getPort());
			bootstrap.bind(inet);
			log.info("Httpd started. Listening on port: {}", inet.toString());
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	public synchronized void startSsl(final HttpSslContext http_ssl_ctx)
	{
		log.info("Starting Httpd - SSL");

		ChannelFactory factory = new NioServerSocketChannelFactory(getBossThreadPool(), getWorkerThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(factory);

		final SSLContext sslContext;
		try
		{
			sslContext = http_ssl_ctx.getSSLContext();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		ChannelPipelineFactory pf = new ChannelPipelineFactory()
		{
			@Override
			public ChannelPipeline getPipeline() throws Exception
			{
				try
				{
					ChannelPipeline pipeline = pipeline();

					final SSLEngine sslEngine = sslContext.createSSLEngine();
					sslEngine.setUseClientMode(false);
					final SslHandler sslHandler = new SslHandler(sslEngine);

					final HttpRequestDecoder httpRequestDecoder = new HttpRequestDecoder(4096, 8192, 256 * 1024);
					final HttpResponseEncoder httpResponseEncoder = new HttpResponseEncoder();
					final HttpProtocolHandler http_handler = new HttpProtocolHandler(_mapper, getRequestObserver(), getResponseFormtter());

					pipeline.addLast("ssl", sslHandler);
					pipeline.addLast("http-decoder", httpRequestDecoder);
					pipeline.addLast("http-encoder", httpResponseEncoder);
					pipeline.addLast("http-handler", http_handler);
					return pipeline;
				}
				catch (Throwable t)
				{
					Shutdown.now(t);
					return null;
				}
			}
		};

		setupBootStrap(bootstrap, pf);

		try
		{
			// Bind and start to accept incoming connections.
			InetSocketAddress inet = new InetSocketAddress(getHost(), http_ssl_ctx.getSslPort());
			bootstrap.bind(inet);
			log.info("Httpd SSL started. Listening on port: {}", inet.toString());
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	private void setupBootStrap(ServerBootstrap bootstrap, ChannelPipelineFactory pf)
	{
		bootstrap.setPipelineFactory(pf);

		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.setOption("child.receiveBufferSize", 128 * 1024);
		bootstrap.setOption("child.sendBufferSize", 128 * 1024);
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("backlog", 1024);
	}

	public void stop()
	{
		log.warn("pt.com.http.NettyHttpServer.stop() is not implemented");
	}
}