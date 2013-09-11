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

	private final int query_timeout;

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
				DbInfo dbinfo_n = new DbInfo(dbinfo_o.getConnectionGroupName(), dbinfo_o.getDriverClass(), dbinfo_o.getDriverUrl(), dbinfo_o.getUsername(), dbinfo_o.getPassword(), dbinfo_o.getTtl(), dbinfo_o.getQueryTimeout());
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

	public static void reset()
	{
		instance.doReset();
	}

	public static void close()
	{
		instance.doClose();
	}

	public static Db obtain(String poolName)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Obtain connection group: {}", poolName);
		}

		return dequeue(instance.pools.get(poolName));
	}

	public static DbType getDbType()
	{
		return instance.dbType;
	}

	public static Db pick()
	{
		return instance.doPick();
	}

	public static void release(Db d)
	{
		if (d != null)
		{
			String poolName = d.getDbInfo().getConnectionGroupName();

			if (d.isActive())
			{
				instance.pools.get(poolName).offer(d);
			}
			else
			{
				d.destroy();
				DbInfo dbinfo_o = d.getDbInfo();
				DbInfo dbinfo_n = new DbInfo(dbinfo_o.getConnectionGroupName(), dbinfo_o.getDriverClass(), dbinfo_o.getDriverUrl(), dbinfo_o.getUsername(), dbinfo_o.getPassword(), dbinfo_o.getTtl(), dbinfo_o.getQueryTimeout());
				instance.pools.get(poolName).offer(new Db(dbinfo_n));
			}
		}
	}

	private final ConcurrentMap<String, BlockingQueue<Db>> pools;

	private final String defaultPool;

	private DbType dbType;

	private final AtomicInteger current_idx = new AtomicInteger(0);

	private final int con_nr;

	private final int ttl;

	private DbPool()
	{
		pools = new ConcurrentHashMap<String, BlockingQueue<Db>>();

		con_nr = tryParse(DbConfigReader.getString("connections"), 1);
		defaultPool = DbConfigReader.getString("default.connection");
		ttl = tryParse(DbConfigReader.getString("connection.ttl"), DEFAULT_TTL);
		timeout = tryParse(DbConfigReader.getString("connection.timeout"), DEFAULT_TIMEOUT);
		query_timeout = tryParse(DbConfigReader.getString("query.timeout"), DEFAULT_TIMEOUT);
		log.info("Number of configured database connection groups: {}", con_nr);
		log.info("Default database connection group: '{}'", defaultPool);

		init();
	}

	private void init()
	{
		for (int i = 1; i <= con_nr; i++)
		{
			int con_poll_size = tryParse(DbConfigReader.getString(i + ".max.pool.size"), DEFAULT_MAX_POOL_SIZE);

			log.info("Connection group: '{}'; Number of connections: {}", i, con_poll_size);

			String con_name = "" + i;
			String driver_class = DbConfigReader.getString(i + ".driver.name");
			String driver_url = DbConfigReader.getString(i + ".conn.url");
			String user = DbConfigReader.getString(i + ".user.name");
			String password = DbConfigReader.getString(i + ".password");

			DbInfo dbinfo = new DbInfo(con_name, driver_class, driver_url, user, password, ttl, query_timeout);

			log.info("Connection: {}", dbinfo.toString());

			try
			{
				Class.forName(driver_class);
				setDbType(con_name, driver_class);
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

	private void doReset()
	{
		doClose();
		init();
	}

	private void doClose()
	{
		Collection<BlockingQueue<Db>> inner_pools = pools.values();

		for (BlockingQueue<Db> bq : inner_pools)
		{
			for (Db db : bq)
			{
				db.destroy();
			}

			bq.clear();
		}

		pools.clear();
	}

	private void setDbType(String con_name, String driver_class)
	{
		if (con_name.equals(defaultPool))
		{
			if ("org.postgresql.Driver".equals(driver_class))
			{
				dbType = DbType.PGSQL;
			}
			else if ("com.mysql.jdbc.Driver".equals(driver_class))
			{
				dbType = DbType.MYSQL;
			}
			else if ("net.sourceforge.jtds.jdbc.Driver".equals(driver_class) || "com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(driver_class))
			{
				dbType = DbType.MSSQL;
			}
			else if ("oracle.jdbc.OracleDriver".equals(driver_class))
			{
				dbType = DbType.ORACLE;
			}
			else if ("org.h2.Driver".equals(driver_class))
			{
				dbType = DbType.H2;
			}
			else
			{
				dbType = DbType.OTHER;
			}
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

		// we should never get here but i've been wrong before. in that case
		// just return the default connection
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
			log.error(String.format("Apply default value for %s: %s", defaultValue, e.getMessage()));
			r = defaultValue;
		}
		return r;
	}
}