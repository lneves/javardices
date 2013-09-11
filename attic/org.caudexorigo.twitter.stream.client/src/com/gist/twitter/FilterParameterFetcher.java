package com.gist.twitter;

import java.util.Collection;

/**
 * Fetches filter parameters to pass to twitter when creating the stream.
 * 
 */
public interface FilterParameterFetcher
{
	/**
	 * @return a collection of twitter ids to follow, or null for no id
	 *         filtering.
	 */
	Collection<String> getFollowIds();

	/**
	 * @return a collection of keywords to track, or null for no keyword
	 *         filtering.
	 */
	Collection<String> getTrackKeywords();

	/**
	 * @return the string representation of the Bounding Box to filter.
	 */
	String getLocation();
}
