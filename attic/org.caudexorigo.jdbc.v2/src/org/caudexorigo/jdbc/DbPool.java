package org.caudexorigo.jdbc;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbPool
{
	private static final Logger log = LoggerFactory.getLogger(DbPool.class);

	private static final DbPool instance = new DbPool();

	private static final int DEFAULT_MAX_POOL_SIZE = 20;

	private static final int DEFAULT_TTL = 300; // 5 minutes

	private static final int DEFAULT_TIMEOUT = 5; // 5 seconds

	private final int timeout;

	private static Db dequeue(BlockingQueue<Db> pool)
	{
		try
		{
			Db d = pool.poll(instance.timeout, TimeUnit.SECONDS);

			if (d == null)
			{
				throw new RuntimeException("Could not acquire a connection from the pool in the specified timeout.");
			}

			if (!d.isActive())
			{
				d.destroy();
				DbInfo dbinfo_o = d.getDbInfo();
				DbInfo dbinfo_n = new DbInfo(dbinfo_o.getConnectionGroupName(), dbinfo_o.getDriverClass(), dbinfo_o.getDriverUrl(), dbinfo_o.getUsername(), dbinfo_o.getPassword(), dbinfo_o.getTtl());
				d = new Db(dbinfo_n);
			}

			if (log.isDebugEnabled())
			{
				log.debug("Using connection: {}", d.getDbInfo().toString());
			}

			return d;
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}

	public static void destroy(Db dbexec)
	{
		dbexec.destroy();
	}

	public static Db obtain()
	{
		return obtain(instance.defaultPool);
	}

	public static Db obtain(String poolName)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Obtain connection group: {}", poolName);
		}

		return dequeue(instance.pools.get(poolName));
	}

	public static Db pick()
	{
		return instance.doPick();
	}

	public static void renew(Db d)
	{
		if (d != null)
		{
			String poolName = d.getDbInfo().getConnectionGroupName();

			d.destroy();
			DbInfo dbinfo_o = d.getDbInfo();
			DbInfo dbinfo_n = new DbInfo(dbinfo_o.getConnectionGroupName(), dbinfo_o.getDriverClass(), dbinfo_o.getDriverUrl(), dbinfo_o.getUsername(), dbinfo_o.getPassword(), dbinfo_o.getTtl());
			instance.pools.get(poolName).offer(new Db(dbinfo_n));
		}
	}

	public static void release(Db d)
	{
		if (d != null)
		{

			if (d.isActive())
			{
				String poolName = d.getDbInfo().getConnectionGroupName();
				instance.pools.get(poolName).offer(d);
			}
			else
			{
				renew(d);
			}
		}
	}

	private final ConcurrentMap<String, BlockingQueue<Db>> pools;

	private final String defaultPool;

	private final AtomicInteger current_idx = new AtomicInteger(0);

	private DbPool()
	{
		pools = new ConcurrentHashMap<String, BlockingQueue<Db>>();

		int con_nr = tryParse(DbConfigReader.getString("connections"), 1);
		defaultPool = DbConfigReader.getString("default.connection");
		int ttl = tryParse(DbConfigReader.getString("connection.ttl"), DEFAULT_TTL);
		timeout = tryParse(DbConfigReader.getString("connection.timeout"), DEFAULT_TIMEOUT);

		log.info("Number of configured database connection groups: {}", con_nr);
		log.info("Default database connection group: '{}'", defaultPool);

		for (int i = 1; i <= con_nr; i++)
		{

			int con_poll_size = tryParse(DbConfigReader.getString(i + ".max.pool.size"), DEFAULT_MAX_POOL_SIZE);

			log.info("Connection group: '{}'; Number of connections: {}", i, con_poll_size);

			String con_name = "" + i;
			String driver_class = DbConfigReader.getString(i + ".driver.name");
			String driver_url = DbConfigReader.getString(i + ".conn.url");
			String user = DbConfigReader.getString(i + ".user.name");
			String password = DbConfigReader.getString(i + ".password");

			DbInfo dbinfo = new DbInfo(con_name, driver_class, driver_url, user, password, ttl);

			log.info("Connection: {}", dbinfo.toString());

			try
			{
				Class.forName(driver_class);
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException("Jdbc driver class not found", e);
			}

			BlockingQueue<Db> pool = new LinkedBlockingQueue<Db>(con_poll_size);

			for (int j = 0; j < con_poll_size; j++)
			{
				try
				{
					Db dbexec = new Db(dbinfo);
					pool.offer(dbexec);
				}
				catch (Throwable t)
				{
					throw new RuntimeException(t);
				}

			}
			pools.put(con_name, pool);
		}
	}

	private Db doPick()
	{
		int s = pools.size();

		if (s == 1)
		{
			return obtain();
		}

		if (s == 0)
		{
			throw new RuntimeException("No available pools from which to pick a connection.");
		}

		int n = Math.abs(current_idx.incrementAndGet() % s);

		Collection<BlockingQueue<Db>> pools = instance.pools.values();

		int idx = 0;
		for (BlockingQueue<Db> pool : pools)
		{
			idx++;
			if (idx > n)
			{
				return dequeue(pool);
			}
		}

		// we should never get here but i've been wrong before. in that case just return the default connection
		return obtain();
	}

	private int tryParse(String intValue, int defaultValue)
	{
		int r;
		try
		{
			r = Integer.parseInt(intValue);
		}
		catch (Throwable e)
		{
			log.error("Apply default value: " + e.getMessage());
			r = defaultValue;
		}
		return r;
	}
}