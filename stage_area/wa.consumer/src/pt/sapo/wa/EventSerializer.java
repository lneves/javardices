package pt.sapo.wa;

import java.io.InputStream;
import java.io.OutputStream;

import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

public class EventSerializer
{
	public static void toXml(Event obj, OutputStream out)
	{
		try
		{
			IMarshallingContext mctx = JibxActors.getMarshallingContext();
			mctx.marshalDocument(obj, "UTF-8", null, out);
		}
		catch (JiBXException e)
		{
			JibxActors.reload();
			throw new RuntimeException(e);
		}
	}

	public static Event fromXml(InputStream in)
	{
		try
		{
			IUnmarshallingContext uctx = JibxActors.getUnmarshallingContext();
			Object o = uctx.unmarshalDocument(in, "UTF-8");
			if (o instanceof Event)
				return (Event) o;
			else
				return new Event();
		}
		catch (JiBXException e)
		{
			JibxActors.reload();
			throw new RuntimeException(e);
		}
	}
}