package org.caudexorigo;

public class ErrorAnalyser
{
	public static Throwable findRootCause(Throwable ex)
	{
		Throwable error_ex = new Exception(ex);
		while (error_ex.getCause() != null)
		{
			error_ex = error_ex.getCause();
		}
		return error_ex;
	}

	public static void exitIfOOM(Throwable t)
	{
		if (t instanceof OutOfMemoryError)
		{
			System.err.println("Giving up, reason: " + t.getMessage());
			Shutdown.now();
		}
	}
}
