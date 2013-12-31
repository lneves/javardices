package wstest.ws.xml;

import org.caudexorigo.Shutdown;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

public class JibxActors
{
	private static final JibxActors instance = new JibxActors();

	private IBindingFactory bfact;

	private JibxActors()
	{
		try
		{
			bfact = BindingDirectory.getFactory(SoapEnvelope.class);
		}
		catch (JiBXException e)
		{
			e.printStackTrace();
			Shutdown.now();
		}
	}

	private static final ThreadLocal<IMarshallingContext> mctx = new ThreadLocal<IMarshallingContext>()
	{
		@Override
		protected IMarshallingContext initialValue()
		{
			IMarshallingContext _mctx;
			try
			{
				_mctx = instance.bfact.createMarshallingContext();
				return _mctx;
			}
			catch (JiBXException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ThreadLocal<IUnmarshallingContext> uctx = new ThreadLocal<IUnmarshallingContext>()
	{
		@Override
		protected IUnmarshallingContext initialValue()
		{
			IUnmarshallingContext _uctx;
			try
			{
				_uctx = instance.bfact.createUnmarshallingContext();
				return _uctx;
			}
			catch (JiBXException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	public static IMarshallingContext getMarshallingContext()
	{
		return mctx.get();
	}

	public static IUnmarshallingContext getUnmarshallingContext()
	{
		return uctx.get();
	}
	
	public static void reload()
	{
		mctx.remove();
		uctx.remove();
	}
}
