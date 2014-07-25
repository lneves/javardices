package org.caudexorigo.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

public class CustomExecutors
{
	public static ThreadPoolExecutor newThreadPool(int maxThreads, String threadPrefix)
	{
		ThreadFactory tf = new DefaultThreadFactory(threadPrefix);
		ThreadPoolExecutor exec_srv = new ThreadPoolExecutor(maxThreads, maxThreads, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(maxThreads * 50), tf, new ThreadPoolExecutor.CallerRunsPolicy());
		return exec_srv;
	}

	public static ThreadPoolExecutor newCachedThreadPool(String threadPrefix)
	{
		ThreadFactory tf = new DefaultThreadFactory(threadPrefix);
		ThreadPoolExecutor exec_srv = new ThreadPoolExecutor(0, 1024, 30L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), tf, new ThreadPoolExecutor.AbortPolicy());
		return exec_srv;
	}

	public static ScheduledThreadPoolExecutor newScheduledThreadPool(int maxThreads, String threadPrefix)
	{
		ThreadFactory tf = new DefaultThreadFactory(threadPrefix);
		ScheduledThreadPoolExecutor shed_exec_srv = new ScheduledThreadPoolExecutor(maxThreads, tf);
		shed_exec_srv.prestartAllCoreThreads();
		return shed_exec_srv;
	}

	/**
	 * The default thread factory
	 */
	static class DefaultThreadFactory implements ThreadFactory
	{
		static final AtomicInteger poolNumber = new AtomicInteger(1);
		final ThreadGroup group;
		final AtomicInteger threadNumber = new AtomicInteger(1);
		final String namePrefix;

		DefaultThreadFactory(String prefix)
		{
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			if (StringUtils.isNotBlank(prefix))
			{
				namePrefix = prefix + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
			}
			else
			{
				namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
			}
		}

		public Thread newThread(Runnable r)
		{
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}
}
