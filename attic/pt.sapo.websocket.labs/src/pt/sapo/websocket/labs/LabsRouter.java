package pt.sapo.websocket.labs;

import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.http.netty.RequestRouter;
import org.jboss.netty.handler.codec.http.HttpRequest;

public class LabsRouter implements RequestRouter
{

	private static final RedirectAction redirect = new RedirectAction("/index.html");

	public LabsRouter()
	{
	}

	@Override
	public HttpAction map(HttpRequest request)
	{
		String path = request.getUri();

		if (path.equals("/"))
		{
			return redirect;
		}

		return null;
	}
}