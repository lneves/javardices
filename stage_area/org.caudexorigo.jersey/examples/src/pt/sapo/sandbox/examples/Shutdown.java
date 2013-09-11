package pt.sapo.sandbox.examples;

import java.util.concurrent.atomic.AtomicBoolean;

public class Shutdown
{
	private static final AtomicBoolean is_shuttingdown = new AtomicBoolean(false);


	public static void now()
	{
		if (is_shuttingdown.getAndSet(true))
		{
			return;
		}

		System.out.println("\nExiting... ");

		while (true)
		{
			System.exit(-1);
		}
	}

	public static void now(Throwable t)
	{
		Throwable root = ErrorAnalyser.findRootCause(t);
		root.printStackTrace();
		now();
	}

	public static boolean isShutingDown()
	{
		return is_shuttingdown.get();
	}
}
