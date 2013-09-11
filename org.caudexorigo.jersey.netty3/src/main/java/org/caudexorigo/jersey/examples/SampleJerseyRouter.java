package org.caudexorigo.jersey.examples;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.http.netty.RequestRouter;
import org.caudexorigo.jersey.JerseyHttpAction;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.jboss.netty.handler.codec.http.HttpRequest;

public class SampleJerseyRouter implements RequestRouter
{
	private static final String RESOURCES_PACKAGE = "org.caudexorigo.jersey.examples";
	private final HttpAction jerseyAction;

	public SampleJerseyRouter()
	{
		super();

		final ResourceConfig res_conf = new ResourceConfig();
		res_conf.packages(RESOURCES_PACKAGE);
		ApplicationHandler application = new ApplicationHandler(res_conf);

		jerseyAction = new JerseyHttpAction(application, res_conf, getSecurityContext(false));
	}
	
	private SecurityContext getSecurityContext(final boolean isSecure)
	{
		return new SecurityContext()
		{
			@Override
			public boolean isUserInRole(String role)
			{
				return true;
			}

			@Override
			public boolean isSecure()
			{
				return isSecure;
			}

			@Override
			public Principal getUserPrincipal()
			{
				return new Principal()
				{
					@Override
					public String getName()
					{
						return "<anonymous>";
					}
				};
			}

			@Override
			public String getAuthenticationScheme()
			{
				return "<void-auth-scheme>";
			}
		};
	}

	@Override
	public HttpAction map(HttpRequest req)
	{
		return jerseyAction;
	}
}