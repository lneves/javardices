package org.caudexorigo.jdbc;

public class DbRunner
{
	private final DbPool dbPool;

	public DbRunner(DbPool dbPool)
	{
		super();
		this.dbPool = dbPool;
	}

	public int executeCallableStatement(Db db, String proc)
	{
		return executeCallableStatement(db, proc, new Object[0]);
	}

	public int executeCallableStatement(Db db, String proc, Object... params)
	{
		return db.executeCallableStatement(proc, params);
	}

	public int executeCallableStatement(String proc)
	{
		return executeCallableStatement(proc, new Object[0]);
	}

	public int executeCallableStatement(String proc, Object... params)
	{
		Db db = null;

		try
		{
			db = dbPool.obtain();
			return executeCallableStatement(db, proc, params);
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			dbPool.release(db);
		}
	}

	public int executePreparedStatement(Db db, String sql, Object... params)
	{
		if ((params != null) && (params.length > 0))
		{
			return db.executePreparedStatement(sql, params);
		}
		else
		{
			return db.executeStatement(sql);
		}
	}

	public int executePreparedStatement(String sql, Object... params)
	{
		Db db = null;

		try
		{
			db = dbPool.obtain();
			return executePreparedStatement(db, sql, params);
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			dbPool.release(db);
		}
	}

	public int executeStatement(Db db, String sql)
	{
		return executePreparedStatement(db, sql, new Object[0]);
	}

	public int executeStatement(String sql)
	{
		return executePreparedStatement(sql, new Object[0]);
	}
}