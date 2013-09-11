package org.caudexorigo.jersey.netty.http.examples;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/calc")

public class Calc
{

	@GET
	@Path("/get")
	@Produces(MediaType.TEXT_PLAIN)
	public String getBySecurityContext(@Context SecurityContext sc){
		if(sc.isUserInRole("users"))
			return "TRUE";
		throw new IllegalArgumentException("not in role");
	}
	
	@GET
	@Path("{x: .*}")
	//@Produces(MediaType.TEXT_PLAIN)
	public String getString(){
		return "default path getString1";
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getString2(){
		return "default path getString2";
	}
	
	@GET
	@Path("/mul")
	@Produces(MediaType.TEXT_PLAIN)
	public String multPlainText(@QueryParam("a") int a, @QueryParam("b") int b)
	{
		return "" + a * b;
	}
}

