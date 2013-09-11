package org.caudexorigo.jersey.netty.http.examples;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<IllegalArgumentException>
{

	@Override
	public Response toResponse(IllegalArgumentException e)
	{
		return Response.status(403).entity(e.getMessage()).type("text/plain").build();
	}

}
