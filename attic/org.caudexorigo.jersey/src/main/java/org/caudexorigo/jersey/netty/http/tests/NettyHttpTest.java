package org.caudexorigo.jersey.netty.http.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.caudexorigo.jersey.netty.http.NettyHttpServer;
import org.caudexorigo.jersey.netty.http.NettyHttpServerFactory;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.server.ResourceConfig;

import org.junit.*;

public class NettyHttpTest
{

	private static final int PORT = 8383;
	private static final String HOSTNAME = "localhost";
	private static String BASE_URI = String.format("https://%s:%s/", HOSTNAME, PORT);
	private static final String RESOURCES_PACKAGE = "org.caudexorigo.jersey.netty.http.examples";

	static Client client;
	static NettyHttpServer server;

	@BeforeClass
	public static void init() throws InterruptedException
	{
		InetSocketAddress inet = new InetSocketAddress("0.0.0.0", 8383);
		ResourceConfig rc = getResourceConfiguration(BASE_URI);
		server = NettyHttpServerFactory.create(rc, inet);
		server.startServer();
		client = ClientFactory.newClient();
	}

	private static ResourceConfig getResourceConfiguration(final String baseUri)
	{
		final ResourceConfig rc = new ResourceConfig();
		rc.packages(RESOURCES_PACKAGE);
		return rc;
	}

	@BeforeClass
	public static void SSLHandshake() throws KeyStoreException
	{

		TrustManager mytm[] = null;
		KeyManager mykm[] = null;

		try
		{
			mytm = new TrustManager[] { new MyX509TrustManager("ssl/client.jks", "123456789".toCharArray()) };
			mykm = new KeyManager[] { new MyX509KeyManager("ssl/client.jks", "123456789".toCharArray()) };
		}
		catch (Exception ex)
		{

		}

		SSLContext context = null;

		try
		{
			context = SSLContext.getInstance("SSL");
			context.init(mykm, mytm, null);
		}
		catch (NoSuchAlgorithmException nae)
		{
			System.out.println("NoSuchAlgorithmException: " + nae.getMessage());
		}
		catch (KeyManagementException kme)
		{
			System.out.println("KeyManagementException: " + kme.getMessage());
		}
		assertNotNull(context);

		try
		{
			client.configuration().setProperty(ClientProperties.SSL_CONTEXT, context);
		}
		catch (Exception e)
		{

		}
	}

	@Test
	public void testGetQueryResponse()
	{
		String uri = BASE_URI + "calc/mul";

		WebTarget target = client.target(uri + "?a=45&b=617");

		final Response response = target.path("/").request().get(Response.class);

		assertEquals(200, response.getStatus());
		String xpto = response.readEntity(String.class);
		assertEquals("27765", xpto);
	}

	@Test
	public void testGetDefaultPathSendingWrongURI()
	{
		String uri = BASE_URI + "calc/mul";

		WebTarget target = client.target(uri + "a=45&b=617");

		final Response response = target.path("/").request().get(Response.class);

		assertEquals(200, response.getStatus());
		String xpto = response.readEntity(String.class);
		assertEquals("default path getString1", xpto);
	}

	@Test
	public void testGetDefaultPath()
	{
		String uri = BASE_URI + "calc";

		WebTarget target = client.target(uri);

		final Response response = target.path("/").request().get(Response.class);

		assertEquals(200, response.getStatus());
		String xpto = response.readEntity(String.class);
		assertEquals("default path getString2", xpto);
	}

	@Test
	public void testGetImage()
	{
		String uri = BASE_URI + "calc";

		WebTarget target = client.target(uri);

		final Response response = target.path("/").request().get(Response.class);

		assertEquals(200, response.getStatus());
		String xpto = response.readEntity(String.class);
		assertEquals("default path getString2", xpto);
	}

	@Test
	public void testPostEcho()
	{
		String uri = BASE_URI + "echo";

		WebTarget target = client.target(uri);
		String toSend = "qwertyuiopasdfghjklzxcvbnm";

		final Response response = target.path("/").request().post(Entity.text(toSend));

		assertEquals(200, response.getStatus());
		String xpto = response.readEntity(String.class);
		assertEquals(toSend, xpto);
	}

	@Test
	public void testGetWithRoleHeader()
	{
		String uri = BASE_URI + "calc/get";

		WebTarget target = client.target(uri);

		final Response response = target.path("/").request().header("UserID", "1003").get(Response.class);

		assertEquals(200, response.getStatus());
		String xpto = response.readEntity(String.class);
		assertEquals("TRUE", xpto);
	}

	@Test
	public void testGetWithNotInRoleHeader()
	{
		String uri = BASE_URI + "calc/get";

		WebTarget target = client.target(uri);

		final Response response = target.path("/").request().header("UserID", "1203").get(Response.class);

		assertEquals(403, response.getStatus());
	}

	@AfterClass
	public static void stop()
	{
		client.close();
		server.stopServer();
	}

}
