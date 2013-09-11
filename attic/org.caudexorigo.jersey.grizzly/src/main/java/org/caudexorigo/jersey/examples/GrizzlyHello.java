package org.caudexorigo.jersey.examples;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.Header;

public class GrizzlyHello
{
	private static final byte[] hello = "Hello World".getBytes();

	public static void main(String[] args)
	{
		HttpServer server = HttpServer.createSimpleServer(".", 8083);
		server.getServerConfiguration().addHttpHandler(new HttpHandler()
		{
			public void service(Request request, Response response) throws Exception
			{

				response.setHeader(Header.Date, HttpDateFormat.getCurrentHttpDate());
				response.setContentType("text/plain");
				response.setContentLength(11);
				response.getOutputStream().write(hello);
				// response.getOutputStream().flush();
			}
		}, "/hello");
		try
		{
			server.start();
			System.out.println("Press any key to stop the server...");
			System.in.read();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
}