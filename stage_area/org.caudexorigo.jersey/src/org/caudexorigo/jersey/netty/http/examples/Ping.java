package org.caudexorigo.jersey.netty.http.examples;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ping")
public class Ping
{
	private static final String MESSAGE = "<h1>Hello World!</h1>";

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String ping()
	{
		return MESSAGE;
	}
}