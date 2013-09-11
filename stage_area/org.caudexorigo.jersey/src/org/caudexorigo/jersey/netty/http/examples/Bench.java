package org.caudexorigo.jersey.netty.http.examples;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/bench")
public class Bench
{
	@GET
	@Produces("image/gif")
	public Response bench()
	{
		Response resp = Response.ok(BenchImage.getBytes()).header("Last-Modified", "Mon, 28 Sep 1970 06:00:00 GMT").build();
		return resp;
	}
}