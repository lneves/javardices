package com.gist.twitter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

public interface TwitterStreamProcessor
{
	/**
	 * Processes the twitter stream until it's interrupted or gets an
	 * IOException. This method should expect to be interrupted, and throw an
	 * InterruptedExcpetion or InterruptedIOException.
	 * 
	 * @param is
	 *            the stream to process
	 * @param credentials
	 *            the credentials used to create the stream, for logging
	 *            purposes
	 * @param ids
	 *            the twitter ids this stream is following
	 */
	void processTwitterStream(InputStream is, String credentials, HashSet<String> ids) throws InterruptedException, IOException;
}