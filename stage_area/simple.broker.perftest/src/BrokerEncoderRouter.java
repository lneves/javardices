
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.codec.protobuf.ProtoBufBindingSerializer;
import pt.com.broker.codec.thrift.ThriftBindingSerializer;
import pt.com.broker.types.BindingSerializer;
import pt.com.broker.types.NetMessage;
import pt.com.broker.types.channels.ChannelAttributes;

/**
 * The network protocol has the following layout:
 * 
 * <pre>
 *  -----------
 *  |  Type   | -&gt; 16-bit signed integer in network order for protocol type
 *  -----------
 *  | Version | -&gt; 16-bit signed integer in network order for protocol version
 *  ----------- 
 *  | Length  | -&gt; 32-bit signed integer in network order for the payload length
 *  -----------
 *  | Payload | -&gt; binary message
 *  -----------
 * </pre>
 * 
 * This applies to both input and output messages.
 */

@Sharable
public class BrokerEncoderRouter extends OneToOneEncoder
{
	private static final Logger log = LoggerFactory.getLogger(BrokerEncoderRouter.class);
	private static final Map<Short, BindingSerializer> encoders = new ConcurrentHashMap<Short, BindingSerializer>();
	private static final Short ptype = new Short((short) 1);
	private static final Short pversion = new Short((short) 0);

	static
	{
		// encoders.put(new Short((short) 0), new SoapBindingSerializer());
		encoders.put(new Short((short) 1), new ProtoBufBindingSerializer());
		encoders.put(new Short((short) 2), new ThriftBindingSerializer());
	}

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception
	{
		try
		{
			Short protocol_type = (Short) ChannelAttributes.get(ChannelAttributes.getChannelId(ctx), "PROTOCOL_TYPE");
			Short protocol_version = (Short) ChannelAttributes.get(ChannelAttributes.getChannelId(ctx), "PROTOCOL_VERSION");

			if (protocol_type == null)
			{
				protocol_type = ptype;
				protocol_version = pversion;
			}

			BindingSerializer handler = encoders.get(ptype);

			if (handler == null)
			{
				throw new RuntimeException("Invalid protocol type: " + ptype);
			}

			byte[] b_msg = handler.marshal((NetMessage) msg);

			ChannelBuffer out = ChannelBuffers.buffer(b_msg.length + 8);
			out.writeShort(protocol_type);
			out.writeShort(protocol_version);
			out.writeInt(b_msg.length);
			out.writeBytes(b_msg);

			return out;
		}
		catch (Throwable t)
		{
			throw new IOException("Failed to encode message. Reason: " + t.getMessage());
		}
	}

}