package org.caudexorigo.twitter.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterWorker implements Runnable
{
	private static Logger log = LoggerFactory.getLogger(TwitterWorker.class);

	private long since_id = 0;

	public TwitterWorker()
	{
		super();

	}

	@Override
	public void run()
	{
		SearchResult sr = TwitterFetcher.fetch(since_id);
	}

}