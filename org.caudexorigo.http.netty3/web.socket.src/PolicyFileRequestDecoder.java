package org.caudexorigo.http.netty;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

@ChannelPipelineCoverage("all")
public class PolicyFileRequestDecoder extends OneToOneDecoder
{
	private static final Charset ASCII = Charset.forName("US-ASCII");

	// private static final Pattern POLICE_FILE_REQUEST = Pattern.compile("<.*policy-file-request.*>");
	private static final String POLICE_FILE_REQUEST = "<policy-file-request/>";

	private final int _port;

	private static final String POLICE_FILE_FRMT = "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"%s\" /></cross-domain-policy>";

	private final byte[] police_file;

	public PolicyFileRequestDecoder(int port_number)
	{
		super();
		_port = port_number;

		police_file = String.format(POLICE_FILE_FRMT, _port).getBytes(ASCII);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel ch, Object msg) throws Exception
	{
		ChannelBuffer buf = (ChannelBuffer) msg;

		if (buf.writerIndex() == 23)
		{
			int r_ix = buf.readerIndex();

			byte[] barr = new byte[22];

			buf.readBytes(barr);

			byte b = buf.readByte();

			if (b == 0)
			{
				String req = new String(barr, ASCII);

				if (POLICE_FILE_REQUEST.equals(req))
				{
					ChannelBuffer pf = ChannelBuffers.buffer(police_file.length + 1);
					pf.writeBytes(police_file);
					pf.writeByte((byte) 0);
					ch.write(pf);
					return ChannelBuffers.buffer(0);
				}
			}
			buf.readerIndex(r_ix);
		}
		return buf;
	}
}