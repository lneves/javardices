package org.caudexorigo.http.netty4;

import org.caudexorigo.cli.Option;

/**
 * Auxiliary interface that maps command line arguments.
 * 
 */

public interface CliArgs
{

	/**
	 * 
	 * @return Listening Port.
	 */
	@Option(shortName = "p", longName = "port", defaultValue = "8080")
	int getPort();

	/**
	 * 
	 * @return Host.
	 */
	@Option(shortName = "h", longName = "host", defaultValue = "0.0.0.0")
	String getHost();

	/**
	 * 
	 * @return Root Directory.
	 */
	@Option(shortName = "r", longName = "root-directory", defaultValue = ".")
	String getRootDirectory();

}