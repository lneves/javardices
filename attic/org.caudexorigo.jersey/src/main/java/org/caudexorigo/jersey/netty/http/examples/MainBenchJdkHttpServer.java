package org.caudexorigo.jersey.netty.http.examples;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.caudexorigo.Shutdown;
import org.caudexorigo.concurrent.CustomExecutors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MainBenchJdkHttpServer
{
	public static void main(String[] args)
	{
		try
		{
			HttpHandler bench = new HttpHandler()
			{
				private static final String SERVER_NAME = "jdkhttp";
				private static final String LM = "Mon, 28 Sep 1970 06:00:00 GMT";

				public void handle(HttpExchange t) throws IOException
				{
					Headers header = t.getResponseHeaders();
					header.add("Connection", "Keep-Alive");
					header.add("Server", SERVER_NAME);
					header.add("Last-Modified", LM);

					byte[] image = BenchImage.getBytes();

					t.sendResponseHeaders(200, image.length);
					OutputStream os = t.getResponseBody();

					os.write(image);
					os.close();
					t.close();
				}
			};

			InetSocketAddress inet = new InetSocketAddress(8282);

			HttpServer server = HttpServer.create(inet, 16384);
			server.createContext("/bench", bench);
			server.setExecutor(CustomExecutors.newThreadPool(6, "jdk-http-server")); // creates a default executor
			server.start();

			System.out.printf("Httpd started. Listening on port: %s%n", inet.toString());
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}
}