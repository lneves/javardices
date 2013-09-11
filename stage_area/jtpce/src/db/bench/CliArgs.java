package db.bench;

import java.util.List;

import org.caudexorigo.cli.Option;

/**
 * Auxiliary interface that maps command line arguments.
 */

public interface CliArgs
{
	/**
	 * @return Number of Clients.
	 */
	@Option(description="Number of concurrent Clients", shortName = "c", longName = "client", defaultValue = "1")
	int getClients();

	/**
	 * @return Number of iterations per Client.
	 */
	@Option(description="Number of iterations performed per Client", shortName = "i", longName = "iterations", defaultValue = "5000")
	int getIterations();
	
	/**
	 * @return Number of iterations per Client.
	 */
	@Option(description="Number of iterations performed per Client in the Warmup phase", shortName = "w", longName = "warmup-iterations", defaultValue = "0")
	int getWarmUpIterations();
	
	/**
	 * @return Number of iterations per Client.
	 */
	@Option(description="Number of iterations performed per Client in the BaseLine test", shortName = "b", longName = "baseline-iterations", defaultValue = "0")
	int getBaseLineIterations();

	/**
	 * @return Test Profile.
	 */
	@Option(description="Test Profile, default is READ_ONLY, can also be READ_WRITE", shortName = "p", longName = "profile", defaultValue = "READ_ONLY")
	Profile getProfile();

	/**
	 * @return Debug Level.
	 */
	@Option(description="Log Level, default is INFO, can also be ERROR, DEBUG", shortName = "l", longName = "log-level", defaultValue = "INFO")
	String getLogLevel();
	
	/**
	 * @return Debug Level.
	 */
	@Option(description="List of tests to exclude", shortName = "e", longName = "exclude", defaultValue = "")
	List<String> getExcludeList();

	@Option(helpRequest = true)
	boolean getHelp();
}