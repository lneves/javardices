package org.caudexorigo.wstest.ws.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

public class SoapJibxSerializer
{
	public static void toXml(SoapEnvelope soapEnv, OutputStream out)
	{
		try
		{
			IMarshallingContext mctx = JibxActors.getMarshallingContext();
			mctx.marshalDocument(soapEnv, "UTF-8", null, out);
			try
			{
				out.flush();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		catch (JiBXException e)
		{
			JibxActors.reload();
			throw new RuntimeException(e);
		}
	}

	public static SoapEnvelope fromXml(InputStream in)
	{
		try
		{
			IUnmarshallingContext uctx = JibxActors.getUnmarshallingContext();
			Object o = uctx.unmarshalDocument(in, "UTF-8");
			if (o instanceof SoapEnvelope)
				return (SoapEnvelope) o;
			else
				throw new IllegalArgumentException("Not a valid Soap Message");
		}
		catch (JiBXException e)
		{
			JibxActors.reload();
			throw new RuntimeException(e);
		}
	}
}