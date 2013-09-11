package trade.srv;

import java.util.HashMap;
import java.util.Map;

import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.http.netty.RequestRouter;
import org.caudexorigo.text.StringUtils;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpSrvRouter implements RequestRouter
{
	private static final Logger log = LoggerFactory.getLogger(HttpSrvRouter.class);

	private final Map<String, HttpAction> routes = new HashMap<String, HttpAction>();

	public HttpSrvRouter()
	{
		routes.put("/favicon.ico", new FaviconHandler());
		routes.put("/trade", new SoapHandler());
	}

	@Override
	public HttpAction map(HttpRequest request)
	{
		String path = StringUtils.substringBefore(request.getUri(), "?");

		return routes.get(path);
	}
}