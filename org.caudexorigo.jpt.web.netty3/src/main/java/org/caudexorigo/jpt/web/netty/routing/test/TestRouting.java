package org.caudexorigo.jpt.web.netty.routing.test;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.http.netty.HttpRequestWrapper;
import org.caudexorigo.jpt.web.netty.NettyWebJptAction;
import org.caudexorigo.jpt.web.netty.routing.RoutingManager;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

/**
 * Please read
 * 
 */

public class TestRouting
{
	private static final String routingFile =
		"#This is a comment\n" +
		"\n" +
		"#method	uri pattern				template\n" +
		"\n" + "DENY	/private/.*\n" +
		"GET	/<name>					<name>.jpt\n" +
		"GET	/detalhes/<event>			detalhes.jpt\n" +
		"GET	/subscricao/<action>/<event>		private/event-<event>.jpt\n" + "\n" +
		"POST	/login					login.jpt\n" +
		"\n" + 
		"*	/endpoint				endpoint.jpt";

	public static void main(String[] args)
	{
		StringReader reader = new StringReader(routingFile);

		RoutingManager routingManager = new RoutingManager(reader, "/server/http/wwwroot/", "http://www.example.com");

		HttpRequestWrapper requestWrapper = null;// getRequestWrapper();

		System.out.println("Testing Routing manager");
		System.out.println();
		System.out.println("Configuration file");
		System.out.println(routingFile);
		System.out.println();
		System.out.println();
		HttpAction action = null;
		String location = null;

		String locations[] = { "/private/goldpot.doc", "/file", "/subscricao/criar/editar" };
		for (int i = 0; i != locations.length; ++i)
		{
			try
			{
				location = locations[i];
				System.out.println(String.format("- Acessing '%s'", location));
				action = routingManager.map(HttpMethod.GET, location, requestWrapper);
				if (action instanceof NettyWebJptAction)
				{
					System.out.println("File: " + ((NettyWebJptAction) action).getTemplateURI().toString());
				}
				else
				{
					String clazz = (action == null) ? "null" : action.getClass().getCanonicalName();
					System.out.println("Something went wrong... Expecting an instance of NettyWebJptAction and got " + clazz);
				}
			}
			catch (Exception e)
			{
				System.out.println("Failed to access." + e.getMessage());
			}
		}

		String reverse = "file.jpt";
		System.out.print("Reverse for " + reverse);
		String reversed = routingManager.reverse(reverse);
		System.out.println(" - " + reversed);
		
		String reverse2 = "private/event-tempo.jpt";
		System.out.print("Reverse for " + reverse2);
		String reversed2 = routingManager.reverse(reverse2);
		System.out.println(" - " + reversed2);
		
		
		String reverse3 = "private/event-tempo.jpt";
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("action", "editar");
		System.out.print("Reverse for " + reverse3);
		String reversed3 = routingManager.reverse(reverse3, values);
		System.out.println(" (with default values) -  " + reversed3);
	}

	public static HttpRequestWrapper getRequestWrapper()
	{
		HttpRequest request = new HttpRequest()
		{
			@Override
			public void setProtocolVersion(HttpVersion arg0)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.setProtocolVersion()");
			}

			@Override
			public void setHeader(String arg0, Iterable<?> arg1)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.setHeader()");
			}

			@Override
			public void setHeader(String arg0, Object arg1)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.setHeader()");
			}

			@Override
			public void setContent(ChannelBuffer arg0)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.setContent()");
			}

			@Override
			public void setChunked(boolean arg0)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.setChunked()");
			}

			@Override
			public void removeHeader(String arg0)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.removeHeader()");
			}

			public boolean isKeepAlive()
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.isKeepAlive()");
				return false;
			}

			@Override
			public boolean isChunked()
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.isChunked()");
				return false;
			}

			@Override
			public HttpVersion getProtocolVersion()
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.getProtocolVersion()");
				return null;
			}

			@Override
			public List<String> getHeaders(String arg0)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.getHeaders()");
				return null;
			}

			@Override
			public List<Entry<String, String>> getHeaders()
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.getHeaders()");
				return null;
			}

			@Override
			public Set<String> getHeaderNames()
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.getHeaderNames()");
				return null;
			}

			@Override
			public String getHeader(String arg0)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.getHeader()");
				return null;
			}


			@Override
			public ChannelBuffer getContent()
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.getContent()");
				return null;
			}

			@Override
			public boolean containsHeader(String arg0)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.containsHeader()");
				return false;
			}

			@Override
			public void clearHeaders()
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.clearHeaders()");
			}

			@Override
			public void addHeader(String arg0, Object arg1)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.addHeader()");
			}

			@Override
			public void setUri(String arg0)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.setUri()");
			}

			@Override
			public void setMethod(HttpMethod arg0)
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.setMethod()");
			}

			@Override
			public String getUri()
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.getUri()");
				return null;
			}

			@Override
			public HttpMethod getMethod()
			{
				System.out.println("TestRouting.getRequestWrapper().new HttpRequest() {...}.getMethod()");
				return null;
			}

			@Override
			public HttpHeaders headers()
			{
				// TODO Auto-generated method stub
				return null;
			}
		};

		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
		return requestWrapper;
	}
}
