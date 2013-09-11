package org.caudexorigo.jersey.netty.http.examples;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/echo")
public class Echo
{	
	@POST	
	@Consumes("text/plain")
	@Produces(MediaType.TEXT_PLAIN)
	public String echo(String name)
	{
		return name;
	}
}
