package jpt.test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.caudexorigo.Shutdown;

public class FieldTest
{
	public static void main(String[] args)
	{
		try
		{
			BeanInfo info = Introspector.getBeanInfo(SimpleContext.class);
			PropertyDescriptor[] pds = info.getPropertyDescriptors();
			
			
			for (PropertyDescriptor	 x : pds)
			{
				System.out.println(x.getName());
			}
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}

}
