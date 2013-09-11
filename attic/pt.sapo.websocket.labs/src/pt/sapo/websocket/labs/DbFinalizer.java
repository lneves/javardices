package pt.sapo.websocket.labs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbFinalizer
{

	public static void closeQuietly(ResultSet rs)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			}
			catch (Throwable e)
			{
				// ignore
			}
		}
	}

	public static void closeQuietly(Statement s)
	{
		if (s != null)
		{
			try
			{
				s.close();
			}
			catch (Throwable e)
			{
				// ignore
			}
		}
	}
	
	public static void closeQuietly(Connection c)
	{
		if (c != null)
		{
			try
			{
				c.close();
			}
			catch (Throwable e)
			{
				// ignore
			}
		}
	}
}
