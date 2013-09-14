package org.caudexorigo;

import java.util.concurrent.atomic.AtomicBoolean;

public class Shutdown
{
	private static final AtomicBoolean is_shuttingdown = new AtomicBoolean(false);

	public static void main(String[] args)
	{
		// NOP: ServiceWrapper artifact
	}

	public static void now()
	{
		System.out.println("\nExiting... ");
		exit();
	}

	public static void now(Throwable t)
	{
		Throwable root = ErrorAnalyser.findRootCause(t);
		root.printStackTrace();
		System.err.println("\nExiting... ");
		exit();
	}

	public static boolean isShutingDown()
	{
		return is_shuttingdown.get();
	}

	private static void exit()
	{
		if (is_shuttingdown.getAndSet(true))
		{
			return;
		}

		while (true)
		{
			System.exit(-1);
		}
	}
}
