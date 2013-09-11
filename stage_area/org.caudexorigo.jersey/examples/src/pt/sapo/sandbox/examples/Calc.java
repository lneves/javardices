package pt.sapo.sandbox.examples;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/calc")
public class Calc
{
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String multPlainText(@QueryParam("a") int a, @QueryParam("b") int b)
	{
		return "" + a * b;
	}
}
