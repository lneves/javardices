package trade.datagen;

import org.caudexorigo.cli.Option;

/**
 * Auxiliary interface that maps command line arguments.
 * 
 */

public interface GenCliArgs
{
	@Option(shortName = "i", longName = "input-path")
	String getInputPath();

	@Option(shortName = "o", longName = "output-path")
	String getOutputPath();

	@Option(shortName = "a", longName = "accounts", defaultValue = "5000")
	int getNumberOfAccounts();

	@Option(shortName = "q", longName = "quotes", defaultValue = "1000")
	int getNumberOfQuotes();

	@Option(shortName = "h", longName = "holdings", defaultValue = "5")
	int getNumberOfHoldingsPerAccount();
}