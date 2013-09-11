package org.caudexorigo.twitter.search;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.caudexorigo.concurrent.CustomExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	private static final ScheduledExecutorService sched_exec = CustomExecutors.newScheduledThreadPool(2, "TwitterFetcher");

	public static void main(String[] args) throws Exception
	{

		TwitterWorker tworker = new TwitterWorker();
		sched_exec.scheduleWithFixedDelay(tworker, 10L, 5L, TimeUnit.SECONDS);

	}
}