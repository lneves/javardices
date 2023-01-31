package org.caudexorigo.http.netty41;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.http.netty41.reporting.MessageBody;
import org.caudexorigo.io.UnsynchronizedByteArrayOutputStream;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;

public class CompressionAdapter extends HttpAction
{
	private static final String GZIP_ENCODING = "gzip";
	private static final String DEFLATE_ENCODING = "deflate";
	private HttpAction wrapped;

	public CompressionAdapter(HttpAction wrapped)
	{
		this.wrapped = wrapped;
	}

	@Override
	public void service(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
	{
		wrapped.service(ctx, request, response);

		int status_code = response.status().code();
		boolean allows_body = MessageBody.allow(status_code);

		if (!allows_body)
		{
			return;
		}

		String accept_enconding = request.headers().get(HttpHeaderNames.ACCEPT_ENCODING);
		boolean allows_gzip = StringUtils.containsIgnoreCase(accept_enconding, GZIP_ENCODING);
		boolean allows_deflate = StringUtils.containsIgnoreCase(accept_enconding, DEFLATE_ENCODING);

		boolean client_allows_compression = allows_gzip || allows_deflate;

		if (client_allows_compression)
		{
			try
			{
				UnsynchronizedByteArrayOutputStream bout = new UnsynchronizedByteArrayOutputStream();
				ByteBufInputStream uncompressed_response_stream = new ByteBufInputStream(response.content());

				if (allows_gzip)
				{
					response.headers().set(HttpHeaderNames.CONTENT_ENCODING, GZIP_ENCODING);
					copy(uncompressed_response_stream, new GZIPOutputStream(bout, true));
				}
				else if (allows_deflate)
				{
					response.headers().set(HttpHeaderNames.CONTENT_ENCODING, DEFLATE_ENCODING);
					copy(uncompressed_response_stream, new DeflaterOutputStream(bout, true));
				}

				response.content().clear();

				response.content().writeBytes(bout.toByteArray());
			}
			catch (Throwable t)
			{
				throw new RuntimeException(t);
			}
		}
	}

	private static void copy(InputStream input, OutputStream output) throws IOException
	{
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1)
		{
			output.write(buffer, 0, bytesRead);
		}
		input.close();
		output.close();
	}
}
