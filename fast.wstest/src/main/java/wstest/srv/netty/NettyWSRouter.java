package wstest.srv.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import org.caudexorigo.http.netty4.HttpAction;
import org.caudexorigo.http.netty4.RequestRouter;

public class NettyWSRouter implements RequestRouter
{
	private HttpAction wstestAction = new NettyWSTestAction();

	@Override
	public HttpAction map(ChannelHandlerContext ctx, FullHttpRequest request)
	{
		if (request.getUri().equals("/wstest"))
		{
			return wstestAction;
		}
		return null;
	}
}
