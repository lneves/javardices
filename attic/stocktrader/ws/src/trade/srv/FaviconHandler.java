package trade.srv;

import java.io.InputStream;

import org.caudexorigo.Shutdown;
import org.caudexorigo.http.netty.HttpAction;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class FaviconHandler extends HttpAction
{
	private static final String content_type = "image/x-icon";

	private final ChannelBuffer image;

	public FaviconHandler()
	{
		image = ChannelBuffers.dynamicBuffer();

		try
		{
			InputStream fi = FaviconHandler.class.getResourceAsStream("favicon.ico");
			int b;
			while ((b = fi.read()) > -1)
			{
				image.writeByte((byte) b);
			}
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}

	@Override
	public void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response)
	{
		response.setStatus(HttpResponseStatus.OK);
		response.setHeader("Expires", "Fri, 31 Dec 2032 12:00:00 GMT");
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE, content_type);
		response.setContent(image.duplicate());
	}
}