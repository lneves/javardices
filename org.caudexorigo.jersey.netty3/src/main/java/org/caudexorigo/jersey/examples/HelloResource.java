package org.caudexorigo.jersey.examples;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/hello")
public class HelloResource
{
	@GET
	@Produces("text/plain")
	public String helloWorld()
	{
		return "Hello World";
	}
}