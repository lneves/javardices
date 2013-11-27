package wstest.srv.netty;

import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.http.netty.RequestRouter;
import org.jboss.netty.handler.codec.http.HttpRequest;

public class NettyWSRouter implements RequestRouter
{
	private HttpAction wstestAction = new NettyWSTestAction();

	@Override
	public HttpAction map(HttpRequest request)
	{
		if (request.getUri().equals("/wstest"))
		{
			return wstestAction;
		}
		return null;
	}

}
